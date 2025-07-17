package com.taco.TinyRealm.module.playerModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String playerId;
    private String playerName;
    private int level; // 暫時設置
    private Location currentLocation; // 假設 Location 已定義
    private Map<String, Integer> resources; // 暫時設置

    // 必須提供這個建構子，以便 GameState.initializeNewGame() 可以初始化 Player
    public Player(String playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.level = 1; // 初始等級
        this.currentLocation = new Location(0, 0); // 初始位置
        this.resources = new HashMap<>(); // 初始化資源Map
    }
}