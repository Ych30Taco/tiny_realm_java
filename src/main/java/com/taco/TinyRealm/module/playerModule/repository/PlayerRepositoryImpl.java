package com.taco.TinyRealm.module.playerModule.repository;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.SaveSystemModule.model.GameState;
import com.taco.TinyRealm.module.SaveSystemModule.service.GameSaveLoadService;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // 標記為 Spring Bean
public class PlayerRepositoryImpl implements PlayerRepository {

    public final GameSaveLoadService saveLoadService;

    public PlayerRepositoryImpl(GameSaveLoadService saveLoadService) {
        this.saveLoadService = saveLoadService;
    }

    @Override
    public Optional<Player> findById(String playerId) {
        // 每次讀取都載入最新狀態
        GameState gameState = saveLoadService.loadGame();
        // 檢查 GameState 是否存在且包含玩家數據，並且 ID 匹配
        if (gameState != null && gameState.getPlayer() != null && gameState.getPlayer().getPlayerId().equals(playerId)) {
            return Optional.of(gameState.getPlayer());
        }
        return Optional.empty(); // 找不到玩家
    }

    @Override
    public Player save(Player player) {
        // 載入當前遊戲狀態
        GameState gameState = saveLoadService.loadGame();
        if (gameState == null) {
            // 如果沒有現有遊戲狀態（例如第一次創建玩家），則創建一個新的 GameState
            // 並將此玩家設置為其核心玩家
            gameState = GameState.initializeNewGame(player.getPlayerName());
            // 重要：確保這裡設置的 player 是傳入的 player 物件，而不是 initializeNewGame 內部新創建的
            gameState.setPlayer(player);
        } else {
            // 如果有現有遊戲狀態，更新其玩家數據
            gameState.setPlayer(player);
        }
        // 儲存整個遊戲狀態回檔案
        saveLoadService.saveGame(gameState);
        return player; // 返回儲存後的玩家物件
    }

    @Override
    public boolean existsById(String playerId) {
        return findById(playerId).isPresent();
    }
}