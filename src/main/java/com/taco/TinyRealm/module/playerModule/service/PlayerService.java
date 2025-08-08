package com.taco.TinyRealm.module.playerModule.service;


import com.taco.TinyRealm.module.buildingModule.model.BuildingStatus;
import com.taco.TinyRealm.module.buildingModule.model.PlayerBuliding;
import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
        
        player.setId(UUID.randomUUID().toString());
        //player.setId("9f917eb2-de3a-420b-ae3b-0a493264072a");
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
        gameState.setBuildings(initializePlayerBuilding(player.getId()));
        /*gameState.setUnits(new java.util.ArrayList<>());
        gameState.setTasks(new java.util.ArrayList<>());
        gameState.setInventory(new java.util.ArrayList<>());
        gameState.setTrades(new java.util.ArrayList<>());   
        gameState.setTechnologies(new java.util.ArrayList<>());
        gameState.setBattles(new java.util.ArrayList<>());
        gameState.setEvents(new java.util.ArrayList<>());
        gameState.setTerrains(new java.util.ArrayList<>());*/
        System.out.println("---- 應用程式啟動中，已建立玩家 " + player.getId().toString() + " ----");
        storageService.saveGameState(player.getId(), gameState, isTest);
        return player;
    }

    public GameState getPlayer(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        return gameState != null ? gameState : null;
    }
/* 
    public Player updatePlayer(String id, String name, int level, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(id, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Player player = gameState.getPlayer();
        player.setName(name);
        player.setLevel(level);
        storageService.saveGameState(id, gameState, isTest);
        return player;
    }*/

    public PlayerResource initializePlayerResources() {
        PlayerResource playerResource = new PlayerResource();
        Map<String, Integer> nowAmount = new HashMap<>();
        Map<String, Integer> maxAmount = new HashMap<>();
        Map<String, Integer> productionRates = new HashMap<>();

        for (Resource type : resourceService.getAllResourceTypes()) {
           nowAmount.put(type.getId(),  type.getNowAmount());
           maxAmount.put(type.getId(), type.getMaxAmount());
           productionRates.put(type.getId(), type.getBaseProductionRate()); // 初始生產速率
        }
        playerResource.setNowAmount(nowAmount);
        playerResource.setMaxAmount(maxAmount);
        playerResource.setProductionRates(productionRates);
        playerResource.setLastUpdatedTime(System.currentTimeMillis());
         return playerResource;
    }
    public Map<String ,PlayerBuliding> initializePlayerBuilding(String playerId) {
        Map<String ,PlayerBuliding> playerBuildings = new HashMap<>();
        PlayerBuliding playerBuilding = new PlayerBuliding();
        playerBuilding.setOwnerId(playerId);
        playerBuilding.setBuildingId("mainHall");
        playerBuilding.setInstanceId("mainHall_" + playerId);
        playerBuilding.setLevel(1);
        playerBuilding.setStatus(BuildingStatus.IDLE);
        playerBuilding.setBuildStartTime(System.currentTimeMillis());
        playerBuilding.setPositionX(0);
        playerBuilding.setPositionY(0);
        playerBuildings.put ("mainHall",playerBuilding);
        return playerBuildings;  
    }


    public PlayerResource getPlayerResources(String playerId) {
        return playerResources.get(playerId);
    }
    public GameState logOutPlayer(String playerId, boolean isTest) throws IOException{
        GameState gameState = storageService.getGameStateListById(playerId);
        Player player = gameState.getPlayer();
        player.setStatus(0); // 設置玩家狀態為離線
        player.setLastLogoutTime(System.currentTimeMillis());
        gameState.setPlayer(player);
        storageService.saveGameState(playerId, gameState, isTest);
        storageService.logOutGameState(playerId, isTest);
        return gameState;
    }
    public GameState logInPlayer(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) return null;
        Player player = gameState.getPlayer();
        player.setStatus(1); // 設置玩家狀態為上線
        player.setLastLoginTime(System.currentTimeMillis());
        gameState.setPlayer(player);
        storageService.saveGameState(playerId, gameState, isTest);
        return gameState;
    }


    private Map<String, Object> createResponse(String code, String stringCode, String message, String messageEN , List<Map<String, Object>> dataList) {
        //logger.warn("創建回應: code={}, message={}, messageEN={}", code, message, messageEN);
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, Object> status = new HashMap<>();
        status.put("version", 1);
        status.put("status", true);
        status.put("code", code);
        status.put("stringCode", stringCode);
        status.put("message", message);
        status.put("messageEN", messageEN);
        errorResponse.put("status", status);
        errorResponse.put("dataList", dataList);
        return errorResponse;
    }
}
