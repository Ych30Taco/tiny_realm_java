package com.taco.TinyRealm.module.soldierModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.soldierModule.model.PlayerSoldier;
import com.taco.TinyRealm.module.soldierModule.model.SoldierType;
import com.taco.TinyRealm.module.buildingModule.model.Building;
import com.taco.TinyRealm.module.buildingModule.model.PlayerBuliding;
import com.taco.TinyRealm.module.buildingModule.service.BuildingService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 士兵服務類別
 * 
 * 負責士兵的創建、管理、升級、移動等核心業務邏輯
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@Service
public class SoldierService {
    
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private ResourceService resourceService;
    @Autowired
    TerrainMapService terrainMapService;
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("${app.data.soldier-path}")
    private org.springframework.core.io.Resource soldierPath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /** 士兵類型配置 */
    private  List<SoldierType> soldierTypeList = Collections.emptyList();
    
    /**
     * 初始化士兵類型配置
     */
    @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入士兵模組 ----");
        try {
            loadSoldierTypes(soldierPath);
            String soldierNames = getSoldierName();
            System.out.println("---- 應用程式啟動中，已載入" + soldierNames + " ----");
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入士兵模組失敗 ----");
            e.printStackTrace();
            throw new RuntimeException("Failed to load soldier.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入士兵模組完成 ----");
    }

    private void loadSoldierTypes(org.springframework.core.io.Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            soldierTypeList = objectMapper.readValue(is, new TypeReference<List<SoldierType>>() {});
        }
    }

    public void reloadSoldierTypes(String overridePath) throws IOException {
        org.springframework.core.io.Resource target = soldierPath;
        if (overridePath != null && !overridePath.isBlank()) {
            target = resourceLoader.getResource(overridePath);
        }
        loadSoldierTypes(target);
    }

    /**
     * 獲取所有士兵類型
     * 
     * @return 士兵類型列表
     */
    public List<SoldierType> getAllsoldierType() {
        return soldierTypeList;
    }
    public String getSoldierName() {
        String names = "";
        for (SoldierType type : soldierTypeList) {
            names += type.getName() + ", ";
        }
        names+= "共"+soldierTypeList.size()+"種兵種";
        return names.length() > 0 ? names : "";
    }

    public SoldierType getSoldierTypeById(String soldierID) {
        return soldierTypeList.stream()
                .filter(r -> r.getId().equals(soldierID))
                .findFirst()
                .orElse(null);
    }
    /**
     * 創建士兵
     */
    public GameState createSoldier(String playerId, String soldierID, int count, boolean isTest) throws IOException {
        
        // 驗證參數
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("玩家ID不能為空");
        }
        if (soldierID == null || soldierID.trim().isEmpty()) {
            throw new IllegalArgumentException("士兵ID不能為空");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("士兵數量必須大於0");
        }
        
        // 載入遊戲狀態
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        // 獲取士兵配置
        SoldierType soldier = getSoldierTypeById(soldierID);
        if (soldier == null) {
            throw new IllegalArgumentException("士兵ID不存在: " + soldierID);
        }
        
        // 檢查建築要求
        if (soldier.getRequirements() != null && !soldier.getRequirements().isEmpty()) {
            Map<String, PlayerBuliding> playerBuildings = gameState.getBuildings();
            for (Map.Entry<String, Integer> req : soldier.getRequirements().entrySet()) {
                String buildingId = req.getKey();
                int requiredLevel = req.getValue();
                if (playerBuildings == null || !playerBuildings.containsKey(buildingId)) {
                    throw new IllegalArgumentException("需要建築物: " + buildingId);
                }
                if (playerBuildings.get(buildingId).getLevel() < requiredLevel) {
                    throw new IllegalArgumentException("建築物 " + buildingId + " 等級不足，需等級: " + requiredLevel);
                }
            }
        }
        
        // 檢查科技要求
        /*if (soldier.getRequiredTech() != null) {
            boolean hasTech = gameState.getTechnologies() != null &&
                    gameState.getTechnologies().stream()
                            .anyMatch(tech -> tech.getType().equals(soldier.getRequiredTech()));
            if (!hasTech) {
                throw new IllegalArgumentException("需要科技: " + soldier.getRequiredTech());
            }
        }*/
        
        // 檢查資源是否足夠
        Map<String, Integer> cost = soldier.getStats().getCost();
        if (cost != null) {
            if (!resourceService.hasEnoughResources(playerId, cost)) {
                throw new IllegalArgumentException("資源不足，無法創建士兵");
            }
            resourceService.dedResources(playerId, cost, isTest); // 扣除資源
        }
        
        // 創建士兵
        PlayerSoldier playerSoldier = new PlayerSoldier();
        playerSoldier.setId(soldier.getId());
        playerSoldier.setType(soldier.getType());
        playerSoldier.setName(soldier.getName());
        playerSoldier.setCount(
            gameState.getSoldiers().containsKey(soldier.getId())
                ? (gameState.getSoldiers().get(soldier.getId()).getCount() + count)
                : count
        );
        playerSoldier.setLevel(1);
        playerSoldier.setAttack(soldier.getStats().getAttack());
        playerSoldier.setDefense(soldier.getStats().getDefense());
        playerSoldier.setSpeed(soldier.getStats().getSpeed());
        playerSoldier.setHp(soldier.getStats().getHp());
        playerSoldier.setStatus("ACTIVE");
        playerSoldier.setFormationPosition(soldier.getFormationPosition());        
        // 添加到遊戲狀態
        gameState.getSoldiers().put(soldier.getId(), playerSoldier);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState,"創建士兵", isTest);
        
        System.out.println("---- 玩家 " + playerId + " 創建了 " + count + " 個 " + soldier.getType() + " 士兵 ----"); // Update to use soldier.getType()
        return gameState;
    }
    /**
     * 解散士兵
     */
    public GameState disbandSoldier(String playerId, String soldierID, int count,boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        SoldierType soldier = getSoldierTypeById(soldierID);
        if (soldier == null) {
            throw new IllegalArgumentException("士兵ID不存在: " + soldierID);
        }
        
        // 移除士兵
        gameState.getSoldiers().get(soldierID).setCount(
            gameState.getSoldiers().get(soldierID).getCount() - count
        );
        // 如果數量小於等於0，則從列表中移除
        if (gameState.getSoldiers().get(soldierID).getCount() <= 0) {
            gameState.getSoldiers().remove(soldierID);
        }
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState,"解散士兵" ,isTest);
        
        System.out.println("---- 玩家 " + playerId + " 解散了士兵 " + soldierID + " ----");
        return gameState;
    }
}
