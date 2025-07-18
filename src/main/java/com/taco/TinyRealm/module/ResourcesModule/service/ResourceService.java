package com.taco.TinyRealm.module.ResourcesModule.service;

import com.fasterxml.jackson.core.type.TypeReference; // 用於 Jackson 反序列化 List
import com.fasterxml.jackson.databind.ObjectMapper;   // Jackson 核心物件，用於 JSON 處理
import com.taco.TinyRealm.module.playerModule.model.Player;         // 引入玩家模型，因為資源操作會影響玩家
import com.taco.TinyRealm.module.playerModule.repository.PlayerRepository; // 引入玩家儲存庫，用於直接保存玩家狀態
import com.taco.TinyRealm.module.playerModule.service.PlayerService; // 引入玩家服務，用於增減資源等高層操作
import com.taco.TinyRealm.module.ResourcesModule.model.Resource;     // 引入資源類型模型
import com.taco.TinyRealm.module.SaveSystemModule.model.GameState;
import com.taco.TinyRealm.module.SaveSystemModule.service.GameSaveLoadService; // 新增 import
import org.springframework.beans.factory.annotation.Value; // 用於從配置檔讀取屬性
import org.springframework.core.io.ResourceLoader;       // 用於載入 Spring 資源 (如 classpath 中的檔案)
import org.springframework.scheduling.annotation.Scheduled; // 用於建立定時任務
import org.springframework.stereotype.Service;           // 標記為 Spring 服務組件

import jakarta.annotation.PostConstruct; // 用於標記初始化方法
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 資源模組的服務層。
 * 負責管理資源的靜態定義，以及處理玩家資源的增加和消耗。
 * 也包含了每秒自動產出資源的定時任務。
 */
@Service
public class ResourceService {

    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private final ResourceLoader resourceLoader;  // Spring 資源載入器
    private final PlayerService playerService;    // 玩家服務，用於委託玩家相關操作
    private final PlayerRepository playerRepository; // 玩家儲存庫，用於直接更新玩家資料
    private final GameSaveLoadService gameSaveLoadService; // 新增 GameSaveLoadService 欄位

    // 用於儲存所有資源類型的 Map，以資源 ID 為鍵，方便快速查找其定義
    private Map<String, Resource> resourceDefinitions;

    // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.resource-types-path}")
    private String resourceTypesPath;

    /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     * @param resourceLoader Spring ResourceLoader 實例
     * @param playerService PlayerService 實例
     * @param playerRepository PlayerRepository 實例
     * @param gameSaveLoadService GameSaveLoadService 實例
     */
    public ResourceService(ObjectMapper objectMapper, ResourceLoader resourceLoader, PlayerService playerService, PlayerRepository playerRepository, GameSaveLoadService gameSaveLoadService) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.playerService = playerService;
        this.playerRepository = playerRepository;
        this.gameSaveLoadService = gameSaveLoadService; // 注入 GameSaveLoadService
    }

    /**
     * 初始化方法：在 ResourceService 實例化並注入所有依賴後自動執行。
     * 負責從配置的 JSON 檔案中載入所有資源類型的定義。
     */
    @PostConstruct
    public void init() {
        try {
            // 從 classpath 載入 resource_types.json 檔案
            org.springframework.core.io.Resource resourceFile = resourceLoader.getResource(resourceTypesPath);
            try (InputStream inputStream = resourceFile.getInputStream()) {
                // 使用 ObjectMapper 將 JSON 檔案內容反序列化為 List<Resource>
                List<Resource> resources = objectMapper.readValue(inputStream, new TypeReference<List<Resource>>() {});
                // 將 List 轉換為 Map (以資源 ID 為鍵，Resource 物件為值)，方便快速查找
                resourceDefinitions = resources.stream()
                                                .collect(Collectors.toMap(Resource::getId, Function.identity()));
                System.out.println("DEBUG: 資源類型已成功載入 (" + resourceDefinitions.size() + " 種)。");
            }
        } catch (IOException e) {
            // 如果載入失敗，列印錯誤訊息並堆疊追蹤。
            // 在實際應用中，可以考慮拋出 RuntimeException 讓應用程式啟動失敗，
            // 因為靜態數據載入失敗通常是不可接受的錯誤。
            System.err.println("錯誤：載入資源類型失敗！請檢查 " + resourceTypesPath + " 檔案。" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 獲取所有已定義的資源類型列表。
     * @return 包含所有資源類型定義的列表。
     */
    public List<Resource> getAllResourceDefinitions() {
        // 返回一個新的 ArrayList，避免外部直接修改內部 Map
        return new java.util.ArrayList<>(resourceDefinitions.values());
    }

    /**
     * 根據資源 ID 獲取單個資源類型的定義。
     * @param resourceId 要查詢的資源 ID。
     * @return 如果找到，則返回包含 Resource 物件的 Optional；否則返回 Optional.empty()。
     */
    public Optional<Resource> getResourceDefinitionById(String resourceId) {
        return Optional.ofNullable(resourceDefinitions.get(resourceId));
    }

    /**
     * 玩家獲取資源 (增加資源數量)。
     * 此方法會委託給 PlayerService 來實際增加玩家的資源數量，並觸發數據保存。
     * @param playerId 玩家的唯一 ID。
     * @param resourceId 資源類型 ID (例如 "wood", "gold")。
     * @param amount 獲取的資源數量 (必須大於 0)。
     * @return 如果操作成功，返回包含更新後 Player 物件的 Optional；否則返回 Optional.empty()。
     */
    public Optional<Player> addPlayerResource(String playerId, String resourceId, int amount) {
        // 驗證資源 ID 是否有效
        if (!resourceDefinitions.containsKey(resourceId)) {
            System.err.println("錯誤：無效的資源類型ID: " + resourceId);
            return Optional.empty();
        }
        // 驗證增加數量是否合法
        if (amount <= 0) {
            System.err.println("錯誤：資源增加數量必須大於0。");
            return Optional.empty();
        }
        // 委託 PlayerService 執行實際的資源增加操作，並由 PlayerService 負責保存
        return playerService.addPlayerResource(playerId, resourceId, amount);
    }

    /**
     * 玩家消耗資源 (減少資源數量)。
     * 此方法會檢查玩家資源是否充足，然後更新玩家資源數量並保存。
     * @param playerId 玩家的唯一 ID。
     * @param resourceId 資源類型 ID。
     * @param amount 消耗的資源數量 (必須大於 0)。
     * @return 如果操作成功且資源充足，返回包含更新後 Player 物件的 Optional；否則返回 Optional.empty()。
     */
    public Optional<Player> consumePlayerResource(String playerId, String resourceId, int amount) {
        // 驗證資源 ID 是否有效
        if (!resourceDefinitions.containsKey(resourceId)) {
            System.err.println("錯誤：無效的資源類型ID: " + resourceId);
            return Optional.empty();
        }
        // 驗證消耗數量是否合法
        if (amount <= 0) {
            System.err.println("錯誤：資源消耗數量必須大於0。");
            return Optional.empty();
        }

        // 從 PlayerService 獲取當前玩家數據
        Optional<Player> playerOpt = playerService.getPlayer(playerId);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            Map<String, Integer> resources = player.getResources(); // 獲取玩家當前資源 Map
            int currentAmount = resources.getOrDefault(resourceId, 0); // 獲取當前資源數量，如果沒有則為 0

            // 檢查資源是否充足
            if (currentAmount < amount) {
                System.out.println("玩家 " + player.getPlayerName() + " (ID: " + playerId + ") 資源 " + resourceId + " 不足，需要 " + amount + " 但只有 " + currentAmount + "。");
                return Optional.empty(); // 資源不足，操作失敗
            }

            // 更新玩家資源數量
            resources.put(resourceId, currentAmount - amount);
            // 直接透過 PlayerRepository 保存更新後的 Player 物件，這會觸發 GameState 的保存
            playerRepository.save(player);
            return Optional.of(player); // 返回更新後的玩家物件
        }
        System.err.println("錯誤：找不到玩家 ID: " + playerId + "，無法消耗資源。");
        return Optional.empty(); // 找不到玩家
    }

    /**
     * 定時任務：每秒鐘為遊戲中的「主玩家」增加資源。
     * 此任務在應用程式啟動後立即執行 (initialDelay = 0)，然後每隔 1000 毫秒 (1 秒) 重複執行。
     * <p>
     * 注意：這個實作是針對單人遊戲模式。在多人遊戲中，您需要遍歷所有活躍玩家或從資料庫中獲取所有玩家，
     * 並為每個玩家計算和增加資源。
     * </p>
     */
    @Scheduled(initialDelay = 0, fixedRate = 1000) // 應用程式啟動後立即執行，然後每 1 秒執行一次
    public void produceResourcesPerSecond() {
        // 首先，嘗試從遊戲狀態中獲取當前玩家。
        // 在單人遊戲中，GameState 只有一個 Player 物件。
        // 透過 PlayerRepositoryImpl 的 public final saveLoadService 屬性來獲取當前 GameState。
        // 這是一種簡化的方法，因為 PlayerRepositoryImpl 已經注入了 GameSaveLoadService。
        // 更嚴謹的做法可以是 PlayerService 提供一個 getActivePlayer() 或 getSinglePlayer()。
        GameState gameState = gameSaveLoadService.loadGame();
        Player currentPlayer = (gameState != null) ? gameState.getPlayer() : null;

        if (currentPlayer == null) {
            // 如果遊戲未初始化或沒有玩家，則跳過資源生產
            // 這通常發生在應用程式剛啟動，還沒有通過 /api/player/create 創建玩家時
            System.out.println("DEBUG: 遊戲未初始化或無玩家，跳過資源生產定時任務。");
            return;
        }

        // 檢查玩家是否有設定資源生產率
        if (currentPlayer.getResourceProductionRates() != null && !currentPlayer.getResourceProductionRates().isEmpty()) {
            boolean resourcesActuallyAdded = false; // 標記是否實際增加了資源

            // 遍歷玩家設定的每個資源生產率
            for (Map.Entry<String, Integer> entry : currentPlayer.getResourceProductionRates().entrySet()) {
                String resourceId = entry.getKey();   // 資源類型 ID (如 "wood")
                Integer amountPerSecond = entry.getValue(); // 每秒生產的數量

                if (amountPerSecond != null && amountPerSecond > 0) { // 只處理有效且正數的生產率
                    // 檢查這個資源 ID 是否是已定義的有效資源類型
                    if (resourceDefinitions.containsKey(resourceId)) {
                        Map<String, Integer> playerResources = currentPlayer.getResources(); // 獲取玩家當前資源 Map
                        // 增加資源數量：原數量 + 生產數量
                        playerResources.put(resourceId, playerResources.getOrDefault(resourceId, 0) + amountPerSecond);
                        System.out.println("DEBUG: 玩家 [" + currentPlayer.getPlayerName() + "] 獲得 " + amountPerSecond + " " + resourceDefinitions.get(resourceId).getName() + "。");
                        resourcesActuallyAdded = true;
                    } else {
                        System.err.println("警告：玩家設定中存在未定義的資源類型ID: " + resourceId + "，已忽略。");
                    }
                }
            }

            // 如果實際有資源被增加，才需要保存玩家數據
            if (resourcesActuallyAdded) {
                playerRepository.save(currentPlayer); // 保存更新後的玩家數據 (這會觸發 GameState 保存到 JSON)
                System.out.println("DEBUG: 玩家 [" + currentPlayer.getPlayerName() + "] 的資源已更新並儲存到檔案。");
            }
        } else {
            System.out.println("DEBUG: 玩家 [" + currentPlayer.getPlayerName() + "] 沒有設定任何資源生產率。");
        }
    }
}