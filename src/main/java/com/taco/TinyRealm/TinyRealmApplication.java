package com.taco.TinyRealm;

import com.taco.TinyRealm.module.playerModule.model.Player; // 需要這個導入，即使 Player 類別現在是空的，但 GameState 引用了它
import com.taco.TinyRealm.module.playerModule.service.PlayerService;
import com.taco.TinyRealm.module.SaveSystemModule.model.GameState;
import com.taco.TinyRealm.module.SaveSystemModule.service.GameSaveLoadService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TinyRealmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyRealmApplication.class, args);
		System.out.println("System startup successful");
	}

	// 當 Spring 應用程式啟動完成後，會自動執行這個 Bean 中的 run 方法
    @Bean
    public CommandLineRunner run(GameSaveLoadService saveLoadService, PlayerService playerService) {
        return args -> {
            System.out.println("---- 應用程式啟動中，初始化遊戲狀態 ----");

            GameState gameState = saveLoadService.loadGame();

            Player currentPlayer = null;
            if (gameState != null && gameState.getPlayer() != null) {
                currentPlayer = gameState.getPlayer();
                System.out.println("遊戲已載入。當前玩家: " + currentPlayer.getPlayerName() + ", 等級: " + currentPlayer.getLevel());
                // 為了測試，可以模擬玩家進行一些操作並重新儲存
                gameState.setGameTime(gameState.getGameTime() + 1); // 假設 GameState 中有 setGameTime 方法
                saveLoadService.saveGame(gameState); // 儲存更新後的狀態
                System.out.println("遊戲時間已更新並重新儲存。");
            } else {
                System.out.println("未找到有效存檔或玩家數據，正在創建一個新遊戲...");
                // 這裡直接使用 PlayerService 來創建新的玩家，並會自動保存到 GameState
                Player initialPlayer = playerService.createPlayer("NewHero");
                if (initialPlayer != null) {
                    System.out.println("新遊戲已創建，初始玩家: " + initialPlayer.getPlayerName() + " (ID: " + initialPlayer.getPlayerId() + ")");
                } else {
                    System.err.println("錯誤: 無法創建初始玩家。");
                }
            }

            System.out.println("---- 應用程式啟動完成 ----");
        };
    }

}
