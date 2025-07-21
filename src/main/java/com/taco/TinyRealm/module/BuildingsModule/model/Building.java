package com.taco.TinyRealm.module.BuildingsModule.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap; // For initializing storedResources
import java.util.UUID;

/**
 * 代表玩家在遊戲中建造的單一建築物實例。
 * 其屬性是動態的，會隨著遊戲的進行（例如升級、施工完成、生產）而變化。
 * 這些實例數據會被持久化（例如儲存到 JSON 檔案）。
 */
public class Building {
    private String id;               // 建築物實例的唯一 ID (UUID)
    private String ownerPlayerId;    // 擁有這個建築的玩家 ID
    private String terrainTileId;    // 建築物所在的地形格子 ID

    private String buildingTypeId;   // 引用建築類型配置的 ID (例如："FARM", "BARRACKS")
    private int level;               // 建築物當前等級
    private BuildingState state;     // 建築物當前狀態

    private long constructionStartTime; // 施工或升級開始的時間戳 (毫秒)
    private long constructionEndTime;   // 施工或升級預計完成的時間戳 (毫秒)

    private List<ProductionTask> productionQueue; // 該建築的生產隊列

    // --- 潛在擴展屬性 ---
    private int currentHealth;       // 建築物當前生命值
    private Map<String, Integer> storedResources; // 建築物內部儲存的資源

    /**
     * 無參數構造函數：Jackson 反序列化時需要，用於創建對象實例。
     * 自動生成唯一 ID 並初始化集合屬性。
     */
    public Building() {
        this.id = UUID.randomUUID().toString();
        this.productionQueue = new ArrayList<>();
        this.storedResources = new HashMap<>(); // 初始化儲存資源 Map
    }

    /**
     * 常用構造函數：用於手動創建 Building 實例時提供關鍵屬性。
     */
    public Building(String id, String ownerPlayerId, String terrainTileId,
                    String buildingTypeId, int level, BuildingState state,
                    long constructionStartTime, long constructionEndTime) {
        this(); // 調用無參構造函數以初始化 id 和 productionQueue
        this.id = id;
        this.ownerPlayerId = ownerPlayerId;
        this.terrainTileId = terrainTileId;
        this.buildingTypeId = buildingTypeId;
        this.level = level;
        this.state = state;
        this.constructionStartTime = constructionStartTime;
        this.constructionEndTime = constructionEndTime;
        // currentHealth 和 storedResources 會在 BuildingService 中根據配置初始化
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOwnerPlayerId() { return ownerPlayerId; }
    public void setOwnerPlayerId(String ownerPlayerId) { this.ownerPlayerId = ownerPlayerId; }
    public String getTerrainTileId() { return terrainTileId; }
    public void setTerrainTileId(String terrainTileId) { this.terrainTileId = terrainTileId; }
    public String getBuildingTypeId() { return buildingTypeId; }
    public void setBuildingTypeId(String buildingTypeId) { this.buildingTypeId = buildingTypeId; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public BuildingState getState() { return state; }
    public void setState(BuildingState state) { this.state = state; }
    public long getConstructionStartTime() { return constructionStartTime; }
    public void setConstructionStartTime(long constructionStartTime) { this.constructionStartTime = constructionStartTime; }
    public long getConstructionEndTime() { return constructionEndTime; }
    public void setConstructionEndTime(long constructionEndTime) { this.constructionEndTime = constructionEndTime; }
    public List<ProductionTask> getProductionQueue() { return productionQueue; }
    public void setProductionQueue(List<ProductionTask> productionQueue) { this.productionQueue = productionQueue; }
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
    public Map<String, Integer> getStoredResources() { return storedResources; }
    public void setStoredResources(Map<String, Integer> storedResources) { this.storedResources = storedResources; }

    // --- 實用方法 ---
    public boolean isConstructionComplete() {
        return this.state == BuildingState.ACTIVE || System.currentTimeMillis() >= this.constructionEndTime;
    }
    public boolean addToProductionQueue(ProductionTask task, int capacity) {
        if (this.productionQueue.size() >= capacity) {
            return false;
        }
        return productionQueue.add(task);
    }
    public ProductionTask getNextProductionTask() {
        return productionQueue.isEmpty() ? null : productionQueue.get(0);
    }
    public void completeNextProductionTask() {
        if (!productionQueue.isEmpty()) {
            productionQueue.remove(0);
        }
    }
}