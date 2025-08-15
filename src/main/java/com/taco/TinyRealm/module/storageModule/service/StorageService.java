package com.taco.TinyRealm.module.storageModule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * 存儲服務類別
 * 
 * 負責遊戲狀態的持久化存儲、載入和管理
 * 提供記憶體快取和檔案系統雙重存儲機制
 * 
 * 主要功能：
 * - 遊戲狀態的保存和載入
 * - 玩家狀態的記憶體管理
 * - 檔案系統操作
 * - 測試資料管理
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@Service
public class StorageService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Value("${app.data.storagePath}")
    private String storagePath;

    /** 記憶體中的遊戲狀態快取 */
    private final Map<String, GameState> gameStateList = new ConcurrentHashMap<>();
    
    /**
     * 獲取所有遊戲狀態的不可修改映射
     * 
     * @return 所有遊戲狀態映射
     */
    public Map<String, GameState> getAllGameStateList() {
        return Collections.unmodifiableMap(gameStateList);
    }

    /**
     * 根據玩家ID獲取遊戲狀態
     * 
     * @param playerId 玩家ID
     * @return 遊戲狀態，如果不存在則返回null
     */
    public GameState getGameStateListById(String playerId) {
        return gameStateList.get(playerId);
    }
    
    /**
     * 獲取在線玩家ID列表
     * 
     * @return 在線玩家ID列表
     */
    public List<String> getOnlineGameStateIdList() {
        return gameStateList.entrySet().stream()
                .filter(entry -> entry.getValue().getPlayer().getStatus() == 1)
                .map(Map.Entry::getKey)
                .toList();
    }
    
    /**
     * 獲取離線玩家ID列表
     * 
     * @return 離線玩家ID列表
     */
    public List<String> getOfflineGameStateIdList() {
        return gameStateList.entrySet().stream()
                .filter(entry -> entry.getValue().getPlayer().getStatus() == 0)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * 獲取所有玩家ID列表
     * 
     * @return 所有玩家ID列表
     */
    public List<String> getAllPlayerIdList() {
        return new ArrayList<>(gameStateList.keySet());
    }

    /**
     * 獲取檔案前綴
     * 
     * @param isTest 是否為測試模式
     * @return 檔案前綴
     */
    private String getFilePrefix(boolean isTest) {
        return isTest ? "test_" : "";
    }
    
    /**
     * 應用程式啟動時的初始化方法
     * 載入所有已存在的遊戲狀態檔案
     */
    @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入遊戲資料 ----");
        try {
            // 確保存儲目錄存在
            File storageDir = new File(storagePath);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
                System.out.println("---- 創建存儲目錄: " + storagePath + " ----");
            }
            
            // 載入所有遊戲狀態檔案
            List<String> fileIds = listAllFiles();
            int loadedCount = 0;
            
            for (String id : fileIds) {
                try {
                    GameState gameState = objectMapper.readValue(
                        new File(storagePath + id + ".json"), GameState.class);
                    gameStateList.put(id, gameState);
                    loadedCount++;
                } catch (Exception e) {
                    System.out.println("載入玩家 " + id + " 的遊戲資料失敗: " + e.getMessage());
                }
            }
            
            System.out.println("---- 已載入 " + loadedCount + " 位玩家遊戲資料 ----");
        } catch (Exception e) {
            System.out.println("---- 載入遊戲資料失敗 ----");
            e.printStackTrace();
        }
        System.out.println("---- 應用程式啟動中，載入遊戲資料完成 ----");
    }

    /**
     * 保存遊戲狀態
     * 
     * @param playerId 玩家ID
     * @param gameState 遊戲狀態
     * @param isTest 是否為測試模式
     * @throws IOException 檔案操作異常
     */
    public void saveGameState(String playerId, GameState gameState,String message, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("玩家ID不能為空");
        }
        
        if (gameState == null) {
            throw new IllegalArgumentException("遊戲狀態不能為空");
        }
        
        // 更新最後修改時間
        gameState.getPlayer().setLastUpdatedTime(System.currentTimeMillis());
        
        // 確保存儲目錄存在
        File dir = new File(storagePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 保存到檔案系統
        String fileName = getFilePrefix(isTest) + playerId + ".json";
        File file = new File(storagePath + fileName);
        objectMapper.writeValue(file, gameState);
        
        // 更新記憶體快取
        gameStateList.put(playerId, gameState);
        
        System.out.println("---- 已保存玩家 " + playerId + " 的遊戲狀態, 來自" + message + " ----");
    }

    /**
     * 載入遊戲狀態
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式
     * @return 遊戲狀態，如果檔案不存在則返回null
     * @throws IOException 檔案操作異常
     */
    public GameState loadGameState(String playerId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("玩家ID不能為空");
        }
        
        String fileName = getFilePrefix(isTest) + playerId + ".json";
        File file = new File(storagePath + fileName);
        
        if (!file.exists()) {
            System.out.println("Warning: Game state file for player " + playerId + " does not exist.");
            return null;
        }
        
        // 從檔案載入遊戲狀態
        GameState gameState = objectMapper.readValue(file, GameState.class);
        
        // 更新登入和修改時間
        gameState.getPlayer().setLastLoginTime(System.currentTimeMillis());
        gameState.getPlayer().setLastUpdatedTime(System.currentTimeMillis());

        // 更新記憶體快取
        gameStateList.put(playerId, gameState);
        
        System.out.println("---- 已載入玩家 " + playerId + " 的遊戲狀態 ----");
        return gameState;
    }

    /**
     * 玩家登出，從記憶體中移除遊戲狀態
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式
     * @throws IOException 檔案操作異常
     */
    /*public void logOutGameState(String playerId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("玩家ID不能為空");
        }
        
        GameState removedState = gameStateList.remove(playerId);
        if (removedState != null) {
            // 保存最終狀態到檔案
            removedState.getPlayer().setLastUpdatedTime(System.currentTimeMillis());
            saveGameState(playerId, removedState, isTest);
            System.out.println("Player " + playerId + " 退出並保存遊戲.");
        } else {
            System.out.println("Player " + playerId + " 未在遊戲狀態清單中找到");
       }
    }*/

    /**
     * 清除所有測試資料檔案
     */
    public void clearTestData() {
        File dir = new File(storagePath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> name.startsWith("test_"));
            if (files != null) {
                int deletedCount = 0;
                for (File f : files) {
                    if (f.delete()) {
                        deletedCount++;
                    }
                }
                System.out.println("---- 已清除 " + deletedCount + " 個測試檔案 ----");
            }
        }
    }

    /**
     * 獲取存儲目錄中所有JSON檔案的名稱（去除副檔名）
     * 
     * @return 檔案名稱列表
     */
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
    
    /**
     * 檢查玩家是否存在
     * 
     * @param playerId 玩家ID
     * @return 是否存在
     */
    public boolean playerExists(String playerId) {
        return gameStateList.containsKey(playerId);
    }
    
    /**
     * 獲取存儲統計資訊
     * 
     * @return 統計資訊映射
     */
    public Map<String, Object> getStorageStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("totalPlayers", gameStateList.size());
        stats.put("onlinePlayers", getOnlineGameStateIdList().size());
        stats.put("offlinePlayers", getOfflineGameStateIdList().size());
        stats.put("storagePath", storagePath);
        stats.put("totalFiles", listAllFiles().size());
        return stats;
    }
}
