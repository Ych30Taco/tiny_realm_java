package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.Map;

/**
 * 資源生產服務類別
 * 
 * 負責處理遊戲中的自動資源生產邏輯，包括：
 * - 根據時間間隔計算資源生產量
 * - 根據建築等級計算生產速率
 * - 更新玩家資源數據
 * - 確保資源不超過儲存上限
 * 
 * 生產邏輯：
 * 1. 計算從上次更新到現在的時間間隔
 * 2. 根據玩家建築計算每種資源的生產速率
 * 3. 計算累積的資源量
 * 4. 更新玩家資源並保存到儲存
 * 
 * @author TinyRealm Team
 * @version 1.0
 */

@Service
public class ResourceProductionService {
    
    /** 資源服務，用於獲取資源類型定義 */
    @Autowired
    private ResourceService resourceService;
    
    /** 建築服務，用於獲取建築信息和生產速率 */
    //@Autowired
   // private BuildingService buildingService;
    
    /** 儲存服務，用於持久化遊戲狀態 */
    @Autowired
    private StorageService storageService;

    /**
     * 更新玩家資源（計算並添加自動生產的資源）
     * 
     * 這是資源生產的核心方法，會：
     * 1. 計算從上次更新到現在的時間間隔
     * 2. 根據玩家建築計算生產速率
     * 3. 計算累積的資源量並添加到玩家資源中
     * 4. 確保資源不超過儲存上限
     * 5. 更新最後更新時間並保存狀態
     * 
     * @param playerId 玩家唯一識別碼
     * @param isTest 是否為測試模式
     * @throws IOException 當儲存操作失敗時拋出
     * @throws IllegalArgumentException 當玩家不存在或資源數據缺失時拋出
     */
    public void updatePlayerResources(String playerId, boolean isTest) throws IOException {
        // 獲取玩家遊戲狀態
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        // 獲取玩家資源數據
        PlayerResource playerResource = gameState.getResources();
        if (playerResource == null) {
            throw new IllegalArgumentException("Player resources not found: " + playerId);
        }

        // 計算時間間隔（小時）
        long currentTime = System.currentTimeMillis();
        long lastUpdatedTime = playerResource.getLastUpdatedTime();
        double hoursPassed = (currentTime - lastUpdatedTime) / (1000.0 * 3600.0);

        // 如果時間間隔太短（小於0.001小時），跳過更新以避免頻繁計算
        if (hoursPassed < 0.001) {
            return;
        }

        // 計算每種資源的生產速率
        Map<String, Integer> productionRates = gameState.getResources().getProductionRates();
        playerResource.setProductionRates(productionRates);

        // 計算並添加生產的資源
        Map<String, Integer> resources = playerResource.getNowAmount();
        Map<String, Integer> maxAmounts = playerResource.getMaxAmount();

        // 遍歷每種資源的生產速率
        for (Map.Entry<String, Integer> entry : productionRates.entrySet()) {
            String resourceId = entry.getKey();
            Integer rate = entry.getValue();
            
            // 只處理有生產速率的資源
            if (rate != null && rate > 0) {
                // 計算累積資源量（生產速率 × 時間間隔）
                int produced = (int) (rate * hoursPassed);
                int currentAmount = resources.getOrDefault(resourceId, 0);
                int maxAmount = maxAmounts.getOrDefault(resourceId, Integer.MAX_VALUE);
                
                // 確保不超過儲存上限
                int newAmount = Math.min(currentAmount + produced, maxAmount);
                resources.put(resourceId, newAmount);
            }
        }

        // 更新上次更新時間
        playerResource.setLastUpdatedTime(currentTime);
        
        // 保存遊戲狀態到持久化儲存
        storageService.saveGameState(playerId, gameState,"更新玩家資源", isTest);
        
        System.out.println("已更新玩家 " + playerId + " 的資源，時間間隔: " + hoursPassed + " 小時");
    }
}
