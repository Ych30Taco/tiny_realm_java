package com.taco.TinyRealm.module.BuildingsModule.model;

import java.util.UUID;

/**
 * 代表建築物生產隊列中的一個單一任務。
 * 例如：兵營訓練一個士兵，或者工廠生產一個工具。
 */
public class ProductionTask {
    private String id;          // 任務本身的唯一 ID
    private String unitType;    // 要生產的單位/物品類型 ID
    private long startTime;     // 任務實際開始生產的時間戳 (毫秒)
    private long endTime;       // 任務預計完成生產的時間戳 (毫秒)

    public ProductionTask() {
        this.id = UUID.randomUUID().toString();
    }

    public ProductionTask(String unitType, long durationSeconds) {
        this(); // 調用無參構造函數以初始化 id
        this.unitType = unitType;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + (durationSeconds * 1000L);
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUnitType() { return unitType; }
    public void setUnitType(String unitType) { this.unitType = unitType; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    // --- 實用方法 ---
    public boolean isComplete() {
        return System.currentTimeMillis() >= endTime;
    }
}