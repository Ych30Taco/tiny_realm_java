package com.taco.TinyRealm.module.playerModule.service;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.resourceModule.model.ResourceType;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;

    private Map<String, PlayerResource> playerResources;
    
    public Player createPlayer(String name, boolean isTest) throws IOException {
        Player player = new Player();
        
        //player.setId(UUID.randomUUID().toString());
        player.setId("9f917eb2-de3a-420b-ae3b-0a493264072a");
        player.setName(name);
        player.setLevel(1);
        player.setExperience(0);
        player.setStatus(1);
        player.setFoundingTime(System.currentTimeMillis());
        player.setLastLoginTime(System.currentTimeMillis());
        player.setLastUpdatedTime(0);
        player.setLastLogoutTime(0); // 初始登出時間為 0
        

        GameState gameState = new GameState();
        gameState.setPlayer(player);
        gameState.setResources(initializePlayerResources());
        gameState.setBuildings(new java.util.ArrayList<>());
        /*gameState.setUnits(new java.util.ArrayList<>());
        gameState.setTasks(new java.util.ArrayList<>());
        gameState.setInventory(new java.util.ArrayList<>());
        gameState.setTrades(new java.util.ArrayList<>());   
        gameState.setTechnologies(new java.util.ArrayList<>());
        gameState.setBattles(new java.util.ArrayList<>());
        gameState.setEvents(new java.util.ArrayList<>());
        gameState.setTerrains(new java.util.ArrayList<>());*/
        storageService.saveGameState(player.getId(), gameState, isTest);
        return player;
    }

    public GameState getPlayer(String playerId, boolean isTest) throws IOException {
        //GameState gameState = storageService.loadGameState(id, isTest);
        GameState gameState = storageService.getGameStateList(playerId);
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

    public PlayerResource initializePlayerResources() {
        PlayerResource playerResource = new PlayerResource();
        Map<String, Integer> nowAmount = new HashMap<>();
        Map<String, Integer> maxAmount = new HashMap<>();

        for (ResourceType type : resourceService.getAllResourceTypes()) {
           nowAmount.put(type.getResourceID(),  type.getNowAmount());
           maxAmount.put(type.getResourceID(), type.getMaxAmount());
        }
        playerResource.setNowAmount(nowAmount);
        playerResource.setMaxAmount(maxAmount);
        playerResource.setLastUpdatedTime(System.currentTimeMillis());
         return playerResource;
     }
    public PlayerResource getPlayerResources(String playerId) {
        return playerResources.get(playerId);
    }
    public void logOutPlayer(String playerId, boolean isTest) throws IOException{
        GameState gameState = storageService.getGameStateList(playerId);
        Player player = gameState.getPlayer();
        player.setStatus(0); // 設置玩家狀態為離線
        player.setLastLogoutTime(System.currentTimeMillis());
        gameState.setPlayer(player);
        storageService.saveGameState(playerId, gameState, isTest);
        storageService.logOutGameState(playerId, isTest);
    }
}
