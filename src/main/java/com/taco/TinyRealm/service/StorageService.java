package com.taco.TinyRealm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.model.Alliance;
import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.MarketListing;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class StorageService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String storagePath = "game_data/";

    private String getFilePrefix(boolean isTest) {
        return isTest ? "test_" : "";
    }

    public void saveGameState(String playerId, GameState gameState, boolean isTest) throws IOException {
        File dir = new File(storagePath);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(storagePath + getFilePrefix(isTest) + playerId + ".json"), gameState);
    }

    public GameState loadGameState(String playerId, boolean isTest) throws IOException {
        File file = new File(storagePath + getFilePrefix(isTest) + playerId + ".json");
        if (!file.exists()) {
            // 自動初始化空 GameState
            GameState gameState = new GameState();
            // 預設 player
            com.taco.TinyRealm.model.Player player = new com.taco.TinyRealm.model.Player();
            player.setId(playerId);
            player.setName(playerId);
            player.setLevel(1);
            gameState.setPlayer(player);
            // 預設資源
            com.taco.TinyRealm.model.Resource resource = new com.taco.TinyRealm.model.Resource();
            resource.setGold(0);
            resource.setWood(0);
            gameState.setResources(resource);
            // 其他欄位預設
            gameState.setBuildings(new java.util.ArrayList<>());
            gameState.setUnits(new java.util.ArrayList<>());
            gameState.setTasks(new java.util.ArrayList<>());
            gameState.setInventory(new java.util.ArrayList<>());
            gameState.setTrades(new java.util.ArrayList<>());
            gameState.setTechnologies(new java.util.ArrayList<>());
            gameState.setBattles(new java.util.ArrayList<>());
            gameState.setEvents(new java.util.ArrayList<>());
            gameState.setTerrains(new java.util.ArrayList<>());
            saveGameState(playerId, gameState, isTest);
            return gameState;
        }
        return objectMapper.readValue(file, GameState.class);
    }

    public void saveAlliance(String allianceId, Alliance alliance, boolean isTest) throws IOException {
        File dir = new File(storagePath);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(storagePath + getFilePrefix(isTest) + "alliance_" + allianceId + ".json"), alliance);
    }

    public Alliance loadAlliance(String allianceId, boolean isTest) throws IOException {
        File file = new File(storagePath + getFilePrefix(isTest) + "alliance_" + allianceId + ".json");
        if (!file.exists()) return null;
        return objectMapper.readValue(file, Alliance.class);
    }

    public void saveMarket(List<MarketListing> market, boolean isTest) throws IOException {
        File dir = new File(storagePath);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(storagePath + getFilePrefix(isTest) + "market.json"), market);
    }

    public List<MarketListing> loadMarket(boolean isTest) throws IOException {
        File file = new File(storagePath + getFilePrefix(isTest) + "market.json");
        if (!file.exists()) return new java.util.ArrayList<>();
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, MarketListing.class));
    }

    // 保留原本方法，預設 isTest=false
    public void saveGameState(String playerId, GameState gameState) throws IOException {
        saveGameState(playerId, gameState, false);
    }
    public GameState loadGameState(String playerId) throws IOException {
        return loadGameState(playerId, false);
    }
    public void saveAlliance(String allianceId, Alliance alliance) throws IOException {
        saveAlliance(allianceId, alliance, false);
    }
    public Alliance loadAlliance(String allianceId) throws IOException {
        return loadAlliance(allianceId, false);
    }
    public void saveMarket(List<MarketListing> market) throws IOException {
        saveMarket(market, false);
    }
    public List<MarketListing> loadMarket() throws IOException {
        return loadMarket(false);
    }

    // 清除所有 test_ 檔案
    public void clearTestData() {
        File dir = new File(storagePath);
        if (dir.exists()) {
            File[] files = dir.listFiles((d, name) -> name.startsWith("test_"));
            if (files != null) {
                for (File f : files) f.delete();
            }
        }
    }
}
