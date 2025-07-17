package com.taco.TinyRealm;

import com.taco.TinyRealm.module.playerModule.model.Player; // 需要這個導入，即使 Player 類別現在是空的，但 GameState 引用了它
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
    public CommandLineRunner run(GameSaveLoadService saveLoadService) {
        return args -> {
            System.out.println("---- 應用程式啟動中，測試儲存模組 ----");

            // 1. 嘗試載入遊戲
            GameState gameState = saveLoadService.loadGame();

            if (gameState == null || gameState.getPlayer() == null) {
                // 2. 如果載入失敗 (無存檔或存檔損壞)，則初始化一個新遊戲狀態
                System.out.println("未找到有效存檔，正在創建一個新遊戲...");
                // 注意：這裡假設 Player 的建構子能被調用。Player 類別需要在 player.model 包中定義。
                gameState = GameState.initializeNewGame("DefaultPlayer");
                saveLoadService.saveGame(gameState); // 儲存初始遊戲狀態
                System.out.println("新遊戲已創建，預設玩家: " + gameState.getPlayer().getPlayerName());
            } else {
                // 3. 如果成功載入，顯示玩家信息
                System.out.println("遊戲已載入。當前玩家: " + gameState.getPlayer().getPlayerName() + ", 等級: " + gameState.getPlayer().getLevel());
                // 這裡可以做一個簡單的修改並儲存，以測試寫入功能
                gameState.setGameTime(gameState.getGameTime() + 1); // 增加遊戲時間
                gameState.getGameEvents().add("遊戲載入後，時間增加1單位。");
                saveLoadService.saveGame(gameState);
                System.out.println("遊戲時間已更新並重新儲存。");
            }

            System.out.println("---- 儲存模組測試完成 ----");
        };
    }

}
