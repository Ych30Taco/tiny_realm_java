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
            try (InputStream is = soldierPath.getInputStream()) {
                soldierTypeList = objectMapper.readValue(is, new TypeReference<List<SoldierType>>() {});
                String soldierNames = getSoldierName();
                System.out.println("---- 應用程式啟動中，已載入" + soldierNames + " ----");
            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入士兵模組失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load soldier.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入士兵模組完成 ----");
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
     * 
     * @param playerId 玩家ID
     * @param type 士兵類型
     * @param count 士兵數量
     * @param x X座標
     * @param y Y座標
     * @param isTest 是否為測試模式
     * @return 創建的士兵
     * @throws IOException 檔案操作異常
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
        System.out.println(soldier);
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
        playerSoldier.setCount(gameState.getSoldiers().keySet().contains(soldier.getId()) ? (gameState.getSoldiers().get(soldier.getId()).getCount() + count) : count);

        
        
        
        // 添加到遊戲狀態
        gameState.getSoldiers().put(soldier.getId(), playerSoldier);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState,"創建士兵", isTest);
        
        System.out.println("---- 玩家 " + playerId + " 創建了 " + count + " 個 " + soldier.getType() + " 士兵 ----"); // Update to use soldier.getType()
        return gameState;
    }
    
    /**
     * 移動士兵
     * 
     * @param playerId 玩家ID
     * @param unitId 士兵ID
     * @param newX 新X座標
     * @param newY 新Y座標
     * @param isTest 是否為測試模式
     * @return 移動後的士兵
     * @throws IOException 檔案操作異常
     */
    /*public Unit moveUnit(String playerId, String unitId, int newX, int newY, boolean isTest) throws IOException {
        // 載入遊戲狀態
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        // 查找士兵
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("士兵不存在");
        }
        
        // 檢查士兵是否可以移動
        if (!unit.canMove()) {
            throw new IllegalArgumentException("士兵無法移動");
        }
        
        // 檢查新位置有效性
        if (!terrainMapService.isPositionValid(playerId, newX, newY)) {
            throw new IllegalArgumentException("無效或已被佔領的位置");
        }
        
        // 釋放原位置
        terrainMapService.releasePosition(playerId, unit.getX(), unit.getY(), isTest);
        
        // 佔領新位置
        terrainMapService.occupyPosition(playerId, newX, newY, isTest);
        
        // 更新士兵位置
        unit.setX(newX);
        unit.setY(newY);
        unit.setStatus(3); // 移動中
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 的士兵 " + unitId + " 移動到 (" + newX + "," + newY + ") ----");
        return unit;
    }*/
    
    /**
     * 獲取玩家的所有士兵
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式
     * @return 士兵列表
     * @throws IOException 檔案操作異常
     */
   /*public List<Unit> getPlayerUnits(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        return gameState.getUnits() != null ? gameState.getUnits() : new ArrayList<>();
    }*/
    
    /**
     * 根據ID查找士兵
     * 
     * @param units 士兵列表
     * @param unitId 士兵ID
     * @return 士兵物件
     */
    /*private Unit findUnitById(List<Unit> units, String unitId) {
        if (units == null) return null;
        return units.stream()
                .filter(unit -> unit.getId().equals(unitId))
                .findFirst()
                .orElse(null);
    }*/
    
    /**
     * 升級士兵
     * 
     * @param playerId 玩家ID
     * @param unitId 士兵ID
     * @param isTest 是否為測試模式
     * @return 升級後的士兵
     * @throws IOException 檔案操作異常
     */
   /* public Unit upgradeUnit(String playerId, String unitId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("士兵不存在");
        }
        
        UnitType unitType = getUnitType(unit.getType());
        if (unitType == null) {
            throw new IllegalArgumentException("士兵類型配置不存在");
        }
        
        // 檢查是否達到等級上限
        if (unit.getLevel() >= unitType.getMaxLevel()) {
            throw new IllegalArgumentException("士兵已達到最高等級");
        }
        
        // 檢查經驗值是否足夠
        if (unit.getExperience() < unitType.getExpToLevel()) {
            throw new IllegalArgumentException("經驗值不足，無法升級");
        }
        
        // 升級士兵
        unit.levelUp();
        unit.setExperience(unit.getExperience() - unitType.getExpToLevel());
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 的士兵 " + unitId + " 升級到 " + unit.getLevel() + " 級 ----");
        return unit;
    }*/
    
    /**
     * 治療士兵
     * 
     * @param playerId 玩家ID
     * @param unitId 士兵ID
     * @param healAmount 治療量
     * @param isTest 是否為測試模式
     * @return 治療後的士兵
     * @throws IOException 檔案操作異常
     */
    /*public Unit healUnit(String playerId, String unitId, int healAmount, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("士兵不存在");
        }
        
        // 治療士兵
        unit.heal(healAmount);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 的士兵 " + unitId + " 治療了 " + healAmount + " 點生命值 ----");
        return unit;
    }*/
    
    /**
     * 解散士兵
     * 
     * @param playerId 玩家ID
     * @param unitId 士兵ID
     * @param isTest 是否為測試模式
     * @return 是否成功解散
     * @throws IOException 檔案操作異常
     */
    /*public boolean disbandUnit(String playerId, String unitId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("士兵不存在");
        }
        
        // 釋放位置
        terrainMapService.releasePosition(playerId, unit.getX(), unit.getY(), isTest);
        
        // 移除士兵
        gameState.getUnits().remove(unit);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 解散了士兵 " + unitId + " ----");
        return true;
    }*/
    
    /**
     * 獲取士兵統計資訊
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式
     * @return 統計資訊
     * @throws IOException 檔案操作異常
     */
    /*public Map<String, Object> getUnitStats(String playerId, boolean isTest) throws IOException {
        List<Unit> units = getPlayerUnits(playerId, isTest);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUnits", units.size());
        stats.put("totalCount", units.stream().mapToInt(Unit::getCount).sum());
        stats.put("aliveUnits", units.stream().filter(Unit::isAlive).count());
        stats.put("averageLevel", units.isEmpty() ? 0 : 
                units.stream().mapToInt(Unit::getLevel).average().orElse(0));
        
        // 按類型統計
        Map<String, Integer> typeStats = new HashMap<>();
        for (Unit unit : units) {
            typeStats.merge(unit.getType(), unit.getCount(), Integer::sum);
        }
        stats.put("typeStats", typeStats);
        
        return stats;
    }*/
}
