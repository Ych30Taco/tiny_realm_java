package com.taco.TinyRealm.module.unitModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.unitModule.model.Unit;
import com.taco.TinyRealm.module.unitModule.model.UnitType;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.buildingModule.service.BuildingService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 部隊服務類別
 * 
 * 負責部隊的創建、管理、升級、移動等核心業務邏輯
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@Service
public class UnitService {
    
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private BuildingService buildingService;
    
    @Autowired
    private TerrainMapService terrainMapService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("${app.data.config-resource-path}")
    private String configResourcePath;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /** 部隊類型配置快取 */
    private final Map<String, UnitType> unitTypes = new ConcurrentHashMap<>();
    
    /**
     * 初始化部隊類型配置
     */
    @PostConstruct
    public void init() {
        try {
            loadUnitTypes();
            System.out.println("---- 部隊模組初始化完成，已載入 " + unitTypes.size() + " 種部隊類型 ----");
        } catch (Exception e) {
            System.err.println("---- 部隊模組初始化失敗: " + e.getMessage() + " ----");
        }
    }
    
    /**
     * 載入部隊類型配置
     */
    private void loadUnitTypes() throws IOException {
        Resource resource = resourceLoader.getResource(configResourcePath + "units.json");
        List<UnitType> types = objectMapper.readValue(resource.getInputStream(), 
                new TypeReference<List<UnitType>>() {});
        
        for (UnitType type : types) {
            unitTypes.put(type.getType(), type);
        }
    }
    
    /**
     * 獲取所有部隊類型
     * 
     * @return 部隊類型列表
     */
    public List<UnitType> getAllUnitTypes() {
        return new ArrayList<>(unitTypes.values());
    }
    
    /**
     * 根據類型獲取部隊配置
     * 
     * @param type 部隊類型
     * @return 部隊配置
     */
    public UnitType getUnitType(String type) {
        return unitTypes.get(type);
    }
    
    /**
     * 創建部隊
     * 
     * @param playerId 玩家ID
     * @param type 部隊類型
     * @param count 部隊數量
     * @param x X座標
     * @param y Y座標
     * @param isTest 是否為測試模式
     * @return 創建的部隊
     * @throws IOException 檔案操作異常
     */
    public Unit createUnit(String playerId, String type, int count, int x, int y, boolean isTest) throws IOException {
        // 驗證參數
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("玩家ID不能為空");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("部隊類型不能為空");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("部隊數量必須大於0");
        }
        
        // 載入遊戲狀態
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        // 獲取部隊配置
        UnitType unitType = getUnitType(type);
        if (unitType == null) {
            throw new IllegalArgumentException("部隊類型不存在: " + type);
        }
        
        // 檢查建築要求
        if (unitType.getRequiredBuilding() != null) {
            boolean hasBuilding = gameState.getBuildings() != null &&
                    gameState.getBuildings().containsKey(unitType.getRequiredBuilding());
            if (!hasBuilding) {
                throw new IllegalArgumentException("需要建築物: " + unitType.getRequiredBuilding());
            }
        }
        
        // 檢查科技要求
        if (unitType.getRequiredTech() != null) {
            boolean hasTech = gameState.getTechnologies() != null &&
                    gameState.getTechnologies().stream()
                            .anyMatch(tech -> tech.getType().equals(unitType.getRequiredTech()));
            if (!hasTech) {
                throw new IllegalArgumentException("需要科技: " + unitType.getRequiredTech());
            }
        }
        
        // 檢查位置有效性
        if (!terrainMapService.isPositionValid(playerId, x, y)) {
            throw new IllegalArgumentException("無效或已被佔領的位置");
        }
        
        // 檢查資源是否足夠
        Map<String, Integer> cost = unitType.getCost();
        if (cost != null) {
            for (Map.Entry<String, Integer> entry : cost.entrySet()) {
                String resourceType = entry.getKey();
                int requiredAmount = entry.getValue() * count;
                int currentAmount = resourceService.getResourceAmount(playerId, resourceType);
                if (currentAmount < requiredAmount) {
                    throw new IllegalArgumentException("資源不足: " + resourceType + 
                            " (需要: " + requiredAmount + ", 擁有: " + currentAmount + ")");
                }
            }
        }
        
        // 扣除資源
        if (cost != null) {
            for (Map.Entry<String, Integer> entry : cost.entrySet()) {
                String resourceType = entry.getKey();
                int amount = entry.getValue() * count;
                resourceService.deductResource(playerId, resourceType, amount, isTest);
            }
        }
        
        // 佔領位置
        terrainMapService.occupyPosition(playerId, x, y, isTest);
        
        // 創建部隊
        Unit unit = new Unit();
        unit.setId(UUID.randomUUID().toString());
        unit.setType(type);
        unit.setName(unitType.getName());
        unit.setCount(count);
        unit.setLevel(1);
        unit.setX(x);
        unit.setY(y);
        unit.setStatus(0); // 訓練中
        unit.setAttack(unitType.getAttack());
        unit.setDefense(unitType.getDefense());
        unit.setHealth(unitType.getHealth());
        unit.setMaxHealth(unitType.getHealth());
        unit.setSpeed(unitType.getSpeed());
        unit.setRange(unitType.getRange());
        unit.setSkills(unitType.getSkills());
        unit.setPlayerId(playerId);
        
        // 添加到遊戲狀態
        if (gameState.getUnits() == null) {
            gameState.setUnits(new ArrayList<>());
        }
        gameState.getUnits().add(unit);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 創建了 " + count + " 個 " + type + " 部隊 ----");
        return unit;
    }
    
    /**
     * 移動部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @param newX 新X座標
     * @param newY 新Y座標
     * @param isTest 是否為測試模式
     * @return 移動後的部隊
     * @throws IOException 檔案操作異常
     */
    public Unit moveUnit(String playerId, String unitId, int newX, int newY, boolean isTest) throws IOException {
        // 載入遊戲狀態
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        // 查找部隊
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("部隊不存在");
        }
        
        // 檢查部隊是否可以移動
        if (!unit.canMove()) {
            throw new IllegalArgumentException("部隊無法移動");
        }
        
        // 檢查新位置有效性
        if (!terrainMapService.isPositionValid(playerId, newX, newY)) {
            throw new IllegalArgumentException("無效或已被佔領的位置");
        }
        
        // 釋放原位置
        terrainMapService.releasePosition(playerId, unit.getX(), unit.getY(), isTest);
        
        // 佔領新位置
        terrainMapService.occupyPosition(playerId, newX, newY, isTest);
        
        // 更新部隊位置
        unit.setX(newX);
        unit.setY(newY);
        unit.setStatus(3); // 移動中
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 的部隊 " + unitId + " 移動到 (" + newX + "," + newY + ") ----");
        return unit;
    }
    
    /**
     * 獲取玩家的所有部隊
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式
     * @return 部隊列表
     * @throws IOException 檔案操作異常
     */
    public List<Unit> getPlayerUnits(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        return gameState.getUnits() != null ? gameState.getUnits() : new ArrayList<>();
    }
    
    /**
     * 根據ID查找部隊
     * 
     * @param units 部隊列表
     * @param unitId 部隊ID
     * @return 部隊物件
     */
    private Unit findUnitById(List<Unit> units, String unitId) {
        if (units == null) return null;
        return units.stream()
                .filter(unit -> unit.getId().equals(unitId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 升級部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @param isTest 是否為測試模式
     * @return 升級後的部隊
     * @throws IOException 檔案操作異常
     */
    public Unit upgradeUnit(String playerId, String unitId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("部隊不存在");
        }
        
        UnitType unitType = getUnitType(unit.getType());
        if (unitType == null) {
            throw new IllegalArgumentException("部隊類型配置不存在");
        }
        
        // 檢查是否達到等級上限
        if (unit.getLevel() >= unitType.getMaxLevel()) {
            throw new IllegalArgumentException("部隊已達到最高等級");
        }
        
        // 檢查經驗值是否足夠
        if (unit.getExperience() < unitType.getExpToLevel()) {
            throw new IllegalArgumentException("經驗值不足，無法升級");
        }
        
        // 升級部隊
        unit.levelUp();
        unit.setExperience(unit.getExperience() - unitType.getExpToLevel());
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 的部隊 " + unitId + " 升級到 " + unit.getLevel() + " 級 ----");
        return unit;
    }
    
    /**
     * 治療部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @param healAmount 治療量
     * @param isTest 是否為測試模式
     * @return 治療後的部隊
     * @throws IOException 檔案操作異常
     */
    public Unit healUnit(String playerId, String unitId, int healAmount, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("部隊不存在");
        }
        
        // 治療部隊
        unit.heal(healAmount);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 的部隊 " + unitId + " 治療了 " + healAmount + " 點生命值 ----");
        return unit;
    }
    
    /**
     * 解散部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @param isTest 是否為測試模式
     * @return 是否成功解散
     * @throws IOException 檔案操作異常
     */
    public boolean disbandUnit(String playerId, String unitId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("玩家不存在");
        }
        
        Unit unit = findUnitById(gameState.getUnits(), unitId);
        if (unit == null) {
            throw new IllegalArgumentException("部隊不存在");
        }
        
        // 釋放位置
        terrainMapService.releasePosition(playerId, unit.getX(), unit.getY(), isTest);
        
        // 移除部隊
        gameState.getUnits().remove(unit);
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState, isTest);
        
        System.out.println("---- 玩家 " + playerId + " 解散了部隊 " + unitId + " ----");
        return true;
    }
    
    /**
     * 獲取部隊統計資訊
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式
     * @return 統計資訊
     * @throws IOException 檔案操作異常
     */
    public Map<String, Object> getUnitStats(String playerId, boolean isTest) throws IOException {
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
    }
}
