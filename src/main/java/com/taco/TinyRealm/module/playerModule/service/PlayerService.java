package com.taco.TinyRealm.module.playerModule.service;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private StorageService storageService;

    public Player createPlayer(String name, boolean isTest) throws IOException {
        Player player = new Player();
        
        player.setId(UUID.randomUUID().toString());
        player.setName(name);
        player.setLevel(1);

        GameState gameState = new GameState();
        gameState.setPlayer(player);
        gameState.setResources(new java.util.ArrayList<>());
        gameState.setBuildings(new java.util.ArrayList<>());
        gameState.setUnits(new java.util.ArrayList<>());
        gameState.setTasks(new java.util.ArrayList<>());
        gameState.setInventory(new java.util.ArrayList<>());
        gameState.setTrades(new java.util.ArrayList<>());   
        gameState.setTechnologies(new java.util.ArrayList<>());
        gameState.setBattles(new java.util.ArrayList<>());
        gameState.setEvents(new java.util.ArrayList<>());
        gameState.setTerrains(new java.util.ArrayList<>());
        storageService.saveGameState(player.getId(), gameState, isTest);
        return player;
    }

    public GameState getPlayer(String id, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(id, isTest);
        return gameState != null ? gameState : null;
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
