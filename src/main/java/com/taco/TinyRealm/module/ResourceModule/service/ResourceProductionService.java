package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
import com.taco.TinyRealm.module.buildingModule.model.Building;
import com.taco.TinyRealm.module.buildingModule.model.LevelData;
import com.taco.TinyRealm.module.buildingModule.model.PlayerBuliding;
import com.taco.TinyRealm.module.buildingModule.service.BuildingService;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceProductionService {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private StorageService storageService;

    /**
     * 更新玩家資源（計算並添加自動生產的資源）
     */
    public void updatePlayerResources(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateList(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        PlayerResource playerResource = gameState.getResources();
        if (playerResource == null) {
            throw new IllegalArgumentException("Player resources not found: " + playerId);
        }

        // 計算時間間隔（小時）
        long currentTime = System.currentTimeMillis();
        long lastUpdatedTime = playerResource.getLastUpdatedTime();
        double hoursPassed = (currentTime - lastUpdatedTime) / (1000.0 * 3600.0);

        // 如果時間間隔太短，跳過更新
        if (hoursPassed < 0.001) {
            return;
        }

        // 計算每種資源的生產速率
        Map<String, Integer> productionRates = calculateProductionRates(playerId, isTest);
        playerResource.setProductionRates(productionRates);

        // 計算並添加生產的資源
        Map<String, Integer> resources = playerResource.getNowAmount();
        Map<String, Integer> maxAmounts = playerResource.getMaxAmount();

        for (Map.Entry<String, Integer> entry : productionRates.entrySet()) {
            String resourceId = entry.getKey();
            Integer rate = entry.getValue();
            
            if (rate != null && rate > 0) {
                // 計算累積資源
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
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("已更新玩家 " + playerId + " 的資源，時間間隔: " + hoursPassed + " 小時");
    }

    /**
     * 計算玩家的資源生產速率
     */
    public Map<String, Integer> calculateProductionRates(String playerId, boolean isTest) throws IOException {
        Map<String, Integer> productionRates = new HashMap<>();
        
        // 獲取玩家建築
        GameState gameState = storageService.getGameStateList(playerId);
        if (gameState == null || gameState.getBuildings() == null) {
            return productionRates;
        }

        Map<String, PlayerBuliding> playerBuildings = gameState.getBuildings();
        
        // 遍歷玩家建築，計算總生產速率
        for (Map.Entry<String, PlayerBuliding> entry : playerBuildings.entrySet()) {
            String buildingId = entry.getKey();
            PlayerBuliding playerBuilding = entry.getValue();
            
            // 獲取建築類型定義
            Building buildingType = buildingService.getBuildingById(buildingId);
            if (buildingType == null) {
                continue;
            }
            
            // 檢查建築是否生產資源
            String resourceType = buildingType.getResourceType();
            if (resourceType != null && !resourceType.isEmpty()) {
                // 根據建築等級獲取生產速率
                int level = playerBuilding.getLevel();
                int rate = getBuildingProductionRate(buildingType, level);
                
                // 累加生產速率
                productionRates.merge(resourceType, rate, Integer::sum);
            }
        }

        // 確保所有資源類型都有生產速率（即使為 0）
        List<Resource> allResourceTypes = resourceService.getAllResourceTypes();
        for (Resource resourceType : allResourceTypes) {
            productionRates.putIfAbsent(resourceType.getId(), 0);
        }

        return productionRates;
    }

    /**
     * 根據建築類型和等級獲取生產速率
     */
    private int getBuildingProductionRate(Building buildingType, int level) {
        // 根據建築等級獲取生產速率
        if (buildingType.getLevels() != null) {
            for (LevelData levelData : buildingType.getLevels()) {
                if (levelData.getLevel() == level) {
                    return levelData.getProductionRate();
                }
            }
        }
        return 0;
    }

    /**
     * 初始化玩家資源生產速率
     */
    public void initializePlayerProductionRates(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateList(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        PlayerResource playerResource = gameState.getResources();
        if (playerResource == null) {
            throw new IllegalArgumentException("Player resources not found: " + playerId);
        }

        // 計算初始生產速率
        Map<String, Integer> productionRates = calculateProductionRates(playerId, isTest);
        playerResource.setProductionRates(productionRates);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("已初始化玩家 " + playerId + " 的資源生產速率");
    }
}
