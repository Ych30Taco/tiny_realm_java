package com.taco.TinyRealm.module.storageModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Service
public class StorageService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${app.data.storagePath}")
    private String storagePath;

    private Map<String,GameState> gameStateList = new ConcurrentHashMap<>();
    
    public Map<String, GameState> getAllGameStateList() {
        return Collections.unmodifiableMap(gameStateList);
    }

    public GameState getGameStateListById(String playerId) {
        return  Collections.unmodifiableMap(gameStateList).get(playerId);
    }
    // 回傳目前上線的玩家ID清單
    public List<String> getOnlineGameStateIdList() {
        return new ArrayList<>(gameStateList.keySet().stream()
                .filter(id -> gameStateList.get(id).getPlayer().getStatus() == 1)
                .toList());
    }
    // 回傳目前下線的玩家ID清單
    public List<String> getOfflineGameStateIdList() {
        return new ArrayList<>(gameStateList.keySet().stream()
                .filter(id -> gameStateList.get(id).getPlayer().getStatus() == 0)
                .toList());
    }

    // 回傳所有玩家ID清單
    public List<String> getAllPlayerIdList() {
        return new ArrayList<>(gameStateList.keySet());
    }

    private String getFilePrefix(boolean isTest) {
        return isTest ? "test_" : "";
    }
     @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入遊戲資料 ----");
        try {
            List<String> fileIds = listAllFiles();
            for (String id : fileIds) {
                try {
                    GameState gameState = objectMapper.readValue(
                        new File(storagePath + id + ".json"), GameState.class);
                    gameStateList.put(id, gameState);
                } catch (Exception e) {
                    System.out.println("載入玩家 " + id + " 的遊戲資料失敗: " + e.getMessage());
                }
            }
            System.out.println("---- 已載入 " + gameStateList.size() + " 位玩家遊戲資料 ----");
        } catch (Exception e) {
            System.out.println("---- 載入遊戲資料失敗 ----");
            e.printStackTrace();
        }
        System.out.println("---- 應用程式啟動中，載入遊戲資料完成 ----");
    }

    public void saveGameState(String playerId, GameState gameState, boolean isTest) throws IOException {
        gameState.getPlayer().setLastUpdatedTime(System.currentTimeMillis());
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
        GameState gameState = objectMapper.readValue(file, GameState.class);
        gameState.getPlayer().setLastLoginTime(System.currentTimeMillis());
        gameState.getPlayer().setLastUpdatedTime(System.currentTimeMillis());

        gameStateList.put(playerId, gameState);
        return gameState;
    }

    public void logOutGameState(String playerId,boolean isTest) throws IOException {
        if(gameStateList.remove(playerId)!= null)
            System.out.println("Player " + playerId + " 退出並保存遊戲.");
        else
            System.out.println("Player " + playerId + " 未在遊戲狀態清單中找到");
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

    // 抓取 storagePath 內所有檔案名稱（去除副檔名 .json）
    public List<String> listAllFiles() {
        File dir = new File(storagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return Collections.emptyList();
        }
        String[] files = dir.list();
        if (files == null) {
            return Collections.emptyList();
        }
        List<String> fileList = new ArrayList<>();
        for (String file : files) {
            if (file.endsWith(".json")) {
                fileList.add(file.substring(0, file.length() - 5));
            }
        }
        return fileList;
    }
}
