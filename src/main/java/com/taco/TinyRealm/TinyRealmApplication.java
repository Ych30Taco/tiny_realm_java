package com.taco.TinyRealm;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;
import com.taco.TinyRealm.module.SaveSystemModule.model.GameState;
import com.taco.TinyRealm.module.SaveSystemModule.service.GameSaveLoadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling; // 導入 @EnableScheduling

/**
 * TinyRealm 遊戲後端應用的主啟動類。
 * 負責啟動 Spring Boot 應用程式，並啟用定時任務功能。
 */
@SpringBootApplication // 標記為 Spring Boot 應用程式，自動配置並掃描組件
@EnableScheduling      // 啟用 Spring 的定時任務功能，允許使用 @Scheduled 註解
public class TinyRealmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyRealmApplication.class, args);
    }

    /**
     * CommandLineRunner 是一個函數式接口，用於在 Spring 應用程式啟動完成後執行特定的程式碼。
     * 在這裡，我們用它來初始化遊戲狀態 (載入存檔或創建新遊戲)。
     * @param saveLoadService 遊戲存檔/載入服務的實例 (由 Spring 自動注入)。
     * @param playerService 玩家服務的實例 (由 Spring 自動注入)。
     * @return 一個 CommandLineRunner 實例。
     */
    @Bean
    public CommandLineRunner run(GameSaveLoadService saveLoadService, PlayerService playerService) {
        return args -> {
            System.out.println("---- 應用程式啟動中，初始化遊戲狀態 ----");

            // 嘗試載入遊戲存檔
            GameState gameState = saveLoadService.loadGame();

            if (gameState == null || gameState.getPlayer() == null) {
                // 如果沒有找到有效存檔或存檔中沒有玩家數據，則創建一個新遊戲和初始玩家。
                System.out.println("未找到有效存檔或玩家數據，正在創建一個新遊戲...");
                // 調用 PlayerService 創建一個新玩家，PlayerService 內部會負責將其保存到 GameState。
                Player initialPlayer = playerService.createPlayer("DefaultHero"); // 為新玩家設定一個預設名稱
                if (initialPlayer != null) {
                    System.out.println("新遊戲已創建，初始玩家: " + initialPlayer.getPlayerName() + " (ID: " + initialPlayer.getPlayerId() + ")");
                } else {
                    System.err.println("錯誤: 無法創建初始玩家。");
                }
            } else {
                // 如果成功載入存檔，顯示當前玩家資訊。
                System.out.println("遊戲已載入。當前玩家: " + gameState.getPlayer().getPlayerName() + ", 等級: " + gameState.getPlayer().getLevel());
                // 這裡可以做一些啟動後的數據更新或日誌記錄。
                // 為了測試目的，我們可以簡單地增加遊戲時間並重新保存。
                gameState.setGameTime(gameState.getGameTime() + 1);
                // 確保 gameEvents 列表不為 null，以防止 NullPointerException
                if(gameState.getGameEvents() != null) {
                    gameState.getGameEvents().add("遊戲啟動，時間增加1單位。");
                }
                saveLoadService.saveGame(gameState); // 保存更新後的遊戲狀態
                System.out.println("遊戲時間已更新並重新儲存。");
            }

            System.out.println("---- 應用程式啟動完成 ----");
        };
    }
}
