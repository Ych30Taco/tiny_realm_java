package com.taco.TinyRealm.module.SaveSystemModule.model;

import com.taco.TinyRealm.module.playerModule.model.Player; 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // 用於生成新玩家ID

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    private Player player; // 包含玩家模組的所有數據
    private List<Map<String,String>> resources ;
    private List<Map<String,String>> buildings ;

    /* 
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
    }*/
}