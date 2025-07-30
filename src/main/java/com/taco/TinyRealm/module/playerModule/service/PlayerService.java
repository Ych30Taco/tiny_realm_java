package com.taco.TinyRealm.module.playerModule.service;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.SaveSystemModule.model.GameState;
import com.taco.TinyRealm.module.SaveSystemModule.service.SaveLoadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;


@Service // 標記為 Spring Bean
public class PlayerService {
    @Autowired
    SaveLoadService saveLoadService;


    public Player createPlayer() {
        String newPlayerId = "player_" + UUID.randomUUID().toString(); // 生成唯一 ID
        String playerName ="player_" + UUID.randomUUID().toString(); // 生成唯一 ID
        Player newPlayer = new Player();
        newPlayer.setId(newPlayerId);
        newPlayer.setName(playerName);
        newPlayer.setLevel(1);

        GameState newState = new GameState();
        newState.setPlayer(newPlayer);
        newState.setResources(new ArrayList<>()); // 初始化資源列表
        newState.setBuildings(new ArrayList<>()); // 初始化建築列表

        saveLoadService.saveGame(newState);

        return newPlayer; // 儲存新玩家 (透過 savesystem)
    }
    /**
    * 根據玩家ID獲取玩家資訊。
    * @param playerId 玩家ID。
    * @return 玩家物件 (Optional 避免 NullPointerException)。
    */
    public Optional<Player> getPlayer(String playerId) {
        GameState gameState = saveLoadService.loadGame();
        // 檢查 GameState 是否存在且包含玩家數據，並且 ID 匹配
        if (gameState != null && gameState.getPlayer() != null && gameState.getPlayer().getId().equals(playerId)) {
            return Optional.of(gameState.getPlayer());
        }
        return Optional.empty(); // 找不到玩家
    }
}