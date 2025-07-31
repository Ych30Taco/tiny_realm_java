package com.taco.TinyRealm.module.storageModule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.model.Alliance;
import com.taco.TinyRealm.model.MarketListing;
import com.taco.TinyRealm.module.resourceModule.model.ResourceType;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class StorageService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String storagePath = "game_data/";

    private Map<String,GameState> gameStateList = new ConcurrentHashMap<>();
    
    public GameState getGameStateList(String playerId) {
        return  Collections.unmodifiableMap(gameStateList).get(playerId);
    }

    private String getFilePrefix(boolean isTest) {
        return isTest ? "test_" : "";
    }

    public void saveGameState(String playerId, GameState gameState, boolean isTest) throws IOException {
        File dir = new File(storagePath);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(storagePath + getFilePrefix(isTest) + playerId + ".json"), gameState);
        gameStateList.put(playerId, gameState);
    }

    public GameState loadGameState(String playerId, boolean isTest) throws IOException {
        File file = new File(storagePath + getFilePrefix(isTest) + playerId + ".json");
        if (!file.exists()) {
            System.out.println("Warning: Game state file for player " + playerId + " does not exist.");
            return null;
        }
        gameStateList.put(playerId, objectMapper.readValue(file, GameState.class));
        return objectMapper.readValue(file, GameState.class);
    }

    /*
    //聯盟
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
    //交易市場
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
*/
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
