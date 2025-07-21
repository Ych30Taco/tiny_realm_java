package com.taco.TinyRealm.module.BuildingsModule.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代表遊戲中一種建築物的靜態配置（藍圖），例如總部、農場、兵營等。
 * 這些數據通常從配置文件（如 JSON）載入，並在遊戲啟動時讀取一次，不隨遊戲進行而改變。
 */
public class BuildingTypeConfig {
    private String typeId;               // 建築物類型唯一ID (例如："HEADQUARTERS", "FARM", "BARRACKS")
    private String name;                 // 建築物的顯示名稱，用於UI
    private String description;          // 建築物的簡短描述

    private int maxLevel;                // 該類型建築物的最大可升級等級
    private int baseBuildTimeSeconds;    // 建造該建築物（1級）所需基礎時間 (秒)
    private Map<String, Integer> baseConstructionCost; // 建造該建築物（1級）所需基礎資源及數量

    // 升級成本定義：外層Map的Key是目標等級(String，Jackson會將JSON中的數字key讀成String)，內層Map是資源類型和數量
    private Map<String, Map<String, Integer>> upgradeCosts;
    // 升級時間定義：Key是當前等級(String)，Value是升級到下一級所需時間(秒)
    private Map<String, Integer> upgradeTimesSeconds;

    private int requiredPlayerLevel;     // 建造/解鎖該建築所需玩家的最低等級
    private String requiredTechnologyId; // 建造/解鎖該建築所需科技的ID (如果遊戲有科技樹系統)
    private List<String> requiredBuildingTypeIds; // 建造該建築所需的前置建築類型ID列表

    private boolean hasProductionQueue;  // 該建築類型是否支持生產隊列 (e.g., 兵營為true, 農場為false)
    private int productionQueueCapacity; // 如果支持生產，其生產隊列的最大容量
    private List<String> producibleUnitTypes; // 該建築可以生產的單位/物品類型ID列表 (e.g., "SOLDIER", "ARCHER")

    // --- 潛在擴展屬性 (可選，根據遊戲需求決定是否加入) ---
    private int baseHealth; // 建築物基礎生命值 (1級時的生命值)
    private int maxStorageCapacity; // 最大資源儲存容量 (如果建築是倉庫類)
    private Map<String, Double> productionRates; // 該建築每週期（例如每秒或每分鐘）生產的資源量及類型
    private List<String> terrainRestrictions; // 只能建造在哪些地形類型上 (例如："GRASS", "FOREST")


    // 無參數構造函數：Jackson 反序列化時需要
    public BuildingTypeConfig() {
        this.baseConstructionCost = new HashMap<>();
        this.upgradeCosts = new HashMap<>();
        this.upgradeTimesSeconds = new HashMap<>();
        this.requiredBuildingTypeIds = new ArrayList<>();
        this.producibleUnitTypes = new ArrayList<>();
        this.productionRates = new HashMap<>();
        this.terrainRestrictions = new ArrayList<>();
    }

    // --- Getters and Setters ---
    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getMaxLevel() { return maxLevel; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
    public int getBaseBuildTimeSeconds() { return baseBuildTimeSeconds; }
    public void setBaseBuildTimeSeconds(int baseBuildTimeSeconds) { this.baseBuildTimeSeconds = baseBuildTimeSeconds; }
    public Map<String, Integer> getBaseConstructionCost() { return Collections.unmodifiableMap(baseConstructionCost); }
    public void setBaseConstructionCost(Map<String, Integer> baseConstructionCost) { this.baseConstructionCost = new HashMap<>(baseConstructionCost); }
    public Map<String, Map<String, Integer>> getUpgradeCosts() { return Collections.unmodifiableMap(upgradeCosts); }
    public void setUpgradeCosts(Map<String, Map<String, Integer>> upgradeCosts) {
        this.upgradeCosts = new HashMap<>();
        upgradeCosts.forEach((level, costs) -> this.upgradeCosts.put(level, new HashMap<>(costs)));
    }
    public Map<String, Integer> getUpgradeTimesSeconds() { return Collections.unmodifiableMap(upgradeTimesSeconds); }
    public void setUpgradeTimesSeconds(Map<String, Integer> upgradeTimesSeconds) { this.upgradeTimesSeconds = new HashMap<>(upgradeTimesSeconds); }
    public int getRequiredPlayerLevel() { return requiredPlayerLevel; }
    public void setRequiredPlayerLevel(int requiredPlayerLevel) { this.requiredPlayerLevel = requiredPlayerLevel; }
    public String getRequiredTechnologyId() { return requiredTechnologyId; }
    public void setRequiredTechnologyId(String requiredTechnologyId) { this.requiredTechnologyId = requiredTechnologyId; }
    public List<String> getRequiredBuildingTypeIds() { return Collections.unmodifiableList(requiredBuildingTypeIds); }
    public void setRequiredBuildingTypeIds(List<String> requiredBuildingTypeIds) { this.requiredBuildingTypeIds = new ArrayList<>(requiredBuildingTypeIds); }
    public boolean isHasProductionQueue() { return hasProductionQueue; }
    public void setHasProductionQueue(boolean hasProductionQueue) { this.hasProductionQueue = hasProductionQueue; }
    public int getProductionQueueCapacity() { return productionQueueCapacity; }
    public void setProductionQueueCapacity(int productionQueueCapacity) { this.productionQueueCapacity = productionQueueCapacity; }
    public List<String> getProducibleUnitTypes() { return Collections.unmodifiableList(producibleUnitTypes); }
    public void setProducibleUnitTypes(List<String> producibleUnitTypes) { this.producibleUnitTypes = new ArrayList<>(producibleUnitTypes); }
    public int getBaseHealth() { return baseHealth; }
    public void setBaseHealth(int baseHealth) { this.baseHealth = baseHealth; }
    public int getMaxStorageCapacity() { return maxStorageCapacity; }
    public void setMaxStorageCapacity(int maxStorageCapacity) { this.maxStorageCapacity = maxStorageCapacity; }
    public Map<String, Double> getProductionRates() { return Collections.unmodifiableMap(productionRates); }
    public void setProductionRates(Map<String, Double> productionRates) { this.productionRates = new HashMap<>(productionRates); }
    public List<String> getTerrainRestrictions() { return Collections.unmodifiableList(terrainRestrictions); }
    public void setTerrainRestrictions(List<String> terrainRestrictions) { this.terrainRestrictions = new ArrayList<>(terrainRestrictions); }

    // --- 實用方法 ---
    public Map<String, Integer> getUpgradeCost(int targetLevel) {
        return upgradeCosts.getOrDefault(String.valueOf(targetLevel), Collections.emptyMap());
    }
    public int getUpgradeTimeSeconds(int currentLevel) {
        return upgradeTimesSeconds.getOrDefault(String.valueOf(currentLevel), 0);
    }
}
