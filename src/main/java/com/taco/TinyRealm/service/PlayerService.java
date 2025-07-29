package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PlayerService {
    @Autowired
    private StorageService storageService;

    public Player createPlayer(String id, String name, boolean isTest) throws IOException {
        Player player = new Player();
        player.setId(id);
        player.setName(name);
        player.setLevel(1);

        GameState gameState = new GameState();
        gameState.setPlayer(player);
        storageService.saveGameState(id, gameState, isTest);
        return player;
    }

    public Player getPlayer(String id, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(id, isTest);
        return gameState != null ? gameState.getPlayer() : null;
    }

    public Player updatePlayer(String id, String name, int level, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(id, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Player player = gameState.getPlayer();
        player.setName(name);
        player.setLevel(level);
        storageService.saveGameState(id, gameState, isTest);
        return player;
    }
}
