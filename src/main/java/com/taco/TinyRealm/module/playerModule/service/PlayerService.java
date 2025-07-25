package com.taco.TinyRealm.module.playerModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.EventPublisher;
import com.taco.TinyRealm.module.EventSystemModule.model.EventType;
import com.taco.TinyRealm.module.playerModule.model.Location;
import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.playerModule.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID; // 用於生成唯一 ID

@Service // 標記為 Spring Bean
public class PlayerService {
    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final EventPublisher eventPublisher; // 注入事件發佈器

    public PlayerService(PlayerRepository playerRepository, EventPublisher eventPublisher) {
        this.playerRepository = playerRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 創建一個新玩家。
     * 在此方法中會確保ID唯一性。
     * @param playerName 玩家名稱。
     * @return 新創建並儲存的玩家物件。
     */
    public Player createPlayer(String playerName) {
        // 檢查是否有現有玩家，如果已經有，通常不允許直接創建新的主玩家
        // 在單人模式下，通常只有一個主要玩家。這裡簡化處理。
        // 更嚴謹的邏輯應該是：如果已經有存檔玩家，則不創建，或者提供覆蓋選項。
        // 為了測試方便，我們暫時允許每次都嘗試創建新的（會覆蓋舊的game_state.json）

        String newPlayerId = "player_" + UUID.randomUUID().toString(); // 生成唯一 ID
        Player newPlayer = new Player(newPlayerId, playerName);
        logger.info("新玩家已創建: {} (ID: {})", playerName, newPlayerId);
        return playerRepository.save(newPlayer); // 儲存新玩家 (透過 savesystem)
    }

    /**
     * 根據玩家ID獲取玩家資訊。
     * @param playerId 玩家ID。
     * @return 玩家物件 (Optional 避免 NullPointerException)。
     */
    public Optional<Player> getPlayer(String playerId) {
        return playerRepository.findById(playerId);
    }

    /**
     * 更新玩家的位置。
     * @param playerId 玩家ID。
     * @param newLocation 新的位置。
     * @return 更新後的玩家物件 (Optional 避免 NullPointerException)。
     */
    public Optional<Player> updatePlayerLocation(String playerId, Location newLocation) {
        Optional<Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            player.setCurrentLocation(newLocation);
            playerRepository.save(player); // 儲存更新後的玩家數據
            return Optional.of(player);
        }
        return Optional.empty(); // 找不到玩家
    }

    /**
     * 增加玩家經驗值並檢查是否升級。
     * @param playerId 玩家ID。
     * @param amount 增加的經驗值數量。
     * @return 更新後的玩家物件 (Optional 避免 NullPointerException)。
     */
    public Optional<Player> addExperience(String playerId, long amount) {
        Optional<Player> playerOpt = playerRepository.findById(playerId);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            player.setExperience(player.getExperience() + amount);
            logger.info("玩家 {} 增加經驗值 {}，目前總經驗 {}。", player.getPlayerName(), amount, player.getExperience());
            // 這裡可以加入升級邏輯
            boolean leveledUp = false;
            if (player.getExperience() >= calculateExpToNextLevel(player.getLevel())) {
                player.setLevel(player.getLevel() + 1);
                logger.info("玩家 {} 升級到 Lv.{}！", player.getPlayerName(), player.getLevel());
                // 經驗值可以歸零或扣除升級所需
                player.setExperience(player.getExperience() - calculateExpToNextLevel(player.getLevel() -1));
                leveledUp = true;
            }
            playerRepository.save(player);
            // 發佈經驗值變動事件
            eventPublisher.publish(EventType.RESOURCE_CHANGED, "exp+" + amount, this);
            // 若升級則發佈升級事件
            if (leveledUp) {
                eventPublisher.publish(EventType.PLAYER_LEVEL_UP, player, this);
            }
            return Optional.of(player);
        }
        logger.error("找不到玩家 ID: {}，無法增加經驗。", playerId);
        return Optional.empty();
    }

    // 計算升級所需經驗值 (簡化版)
    private long calculateExpToNextLevel(int currentLevel) {
        return currentLevel * 100L; // 簡化：每級所需經驗為當前等級的100倍
    }

    // ... 其他玩家相關的業務邏輯，例如消耗資源、解鎖科技等
}
