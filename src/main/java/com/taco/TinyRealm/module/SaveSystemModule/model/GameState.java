package com.taco.TinyRealm.module.SaveSystemModule.model;

// 未來會引入其他模組的數據模型
import com.taco.TinyRealm.module.playerModule.model.Player; // 即使 Player 模組還沒開始寫，先假設它會存在

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // 用於生成新玩家ID

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    private Player player; // 包含玩家模組的所有數據
    // private World world; // 未來世界模組的數據，暫時註釋掉
    // private Map<String, Integer> globalResources; // 未來如果遊戲有全局資源儲存點
    private long gameTime; // 遊戲內時間，例如回合數、遊戲日數
    private List<String> gameEvents = new ArrayList<>(); // 確保預設不為 null

    // 提供一個靜態方法用於初始化一個新的遊戲狀態
    public static GameState initializeNewGame(String initialPlayerName) {
        GameState newState = new GameState();
        // 在這裡初始化 Player 物件，即使 Player 模組的詳細邏輯還沒寫，但需要一個 Player 實例
        // 注意：這裡假設 Player 類別已經有一個帶有 id 和 name 的建構子
        newState.setPlayer(new Player("player_" + UUID.randomUUID().toString(), initialPlayerName));
        newState.setGameTime(0);
        newState.setGameEvents(new ArrayList<>()); // 初始化 gameEvents
        System.out.println("DEBUG: 新遊戲狀態已初始化，玩家ID: " + newState.getPlayer().getPlayerId());
        return newState;
    }
}