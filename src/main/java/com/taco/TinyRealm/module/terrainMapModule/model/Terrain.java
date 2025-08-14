package com.taco.TinyRealm.module.terrainMapModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地形模型
 * 按照 Kingdom Clash Battle System 規格設計
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terrain {
    
    /** 地形唯一識別碼 */
    @JsonProperty("id")
    private String id;
    
    /** 地形名稱 */
    @JsonProperty("name")
    private String name;
    
    /** 地形類型 */
    @JsonProperty("terrainType")
    private TerrainType terrainType;
    
    /** X座標 */
    @JsonProperty("positionX")
    private int positionX;
    
    /** Y座標 */
    @JsonProperty("positionY")
    private int positionY;
    
    /** 地形等級 */
    @JsonProperty("level")
    private int level;
    
    /** 地形耐久度（用於城牆等） */
    @JsonProperty("durability")
    private int durability;
    
    /** 最大耐久度 */
    @JsonProperty("maxDurability")
    private int maxDurability;
    
    /** 地形描述 */
    @JsonProperty("description")
    private String description;
    
    /** 是否可通行 */
    @JsonProperty("passable")
    private boolean passable;
    
    /** 是否可建造 */
    @JsonProperty("buildable")
    private boolean buildable;
    
    /** 地形效果持續時間（回合數） */
    @JsonProperty("effectDuration")
    private int effectDuration;

    /**
     * 檢查地形是否完整
     */
    public boolean isIntact() {
        return durability >= maxDurability;
    }

    /**
     * 檢查地形是否損壞
     */
    public boolean isDamaged() {
        return durability < maxDurability;
    }

    /**
     * 檢查地形是否完全破壞
     */
    public boolean isDestroyed() {
        return durability <= 0;
    }

    /**
     * 獲取地形耐久度百分比
     */
    public double getDurabilityPercentage() {
        if (maxDurability <= 0) return 0.0;
        return (double) durability / maxDurability;
    }

    /**
     * 受到傷害
     */
    public void takeDamage(int damage) {
        durability = Math.max(0, durability - damage);
    }

    /**
     * 修復地形
     */
    public void repair(int repairAmount) {
        durability = Math.min(maxDurability, durability + repairAmount);
    }

    /**
     * 獲取移動速度修正倍數
     */
    public double getSpeedModifier() {
        if (terrainType == null) return 1.0;
        return terrainType.getSpeedModifier();
    }

    /**
     * 獲取射程加成
     */
    public int getRangeBonus() {
        if (terrainType == null) return 0;
        return terrainType.getRangeBonus();
    }

    /**
     * 獲取防禦修正倍數
     */
    public double getDefenseModifier() {
        if (terrainType == null) return 1.0;
        return terrainType.getDefenseModifier();
    }

    /**
     * 檢查是否為城防地形
     */
    public boolean isDefensive() {
        return terrainType != null && terrainType.isDefensive();
    }

    /**
     * 檢查是否為障礙地形
     */
    public boolean isObstacle() {
        return terrainType != null && terrainType.isObstacle();
    }

    /**
     * 檢查是否為移動友好地形
     */
    public boolean isMovementFriendly() {
        return terrainType != null && terrainType.isMovementFriendly();
    }

    /**
     * 計算與目標的距離
     */
    public double getDistanceTo(int targetX, int targetY) {
        int deltaX = positionX - targetX;
        int deltaY = positionY - targetY;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * 檢查是否在指定範圍內
     */
    public boolean isInRange(int targetX, int targetY, int range) {
        return getDistanceTo(targetX, targetY) <= range;
    }

    /**
     * 獲取地形戰鬥影響描述
     */
    public String getCombatEffectDescription() {
        if (terrainType == null) return "無特殊效果";
        
        StringBuilder description = new StringBuilder();
        
        if (getSpeedModifier() != 1.0) {
            if (getSpeedModifier() < 1.0) {
                description.append("移動速度 ").append((int)((1.0 - getSpeedModifier()) * 100)).append("%");
            } else {
                description.append("移動速度 +").append((int)((getSpeedModifier() - 1.0) * 100)).append("%");
            }
        }
        
        if (getRangeBonus() > 0) {
            if (description.length() > 0) description.append("，");
            description.append("遠程射程 +").append(getRangeBonus());
        }
        
        if (getDefenseModifier() > 1.0) {
            if (description.length() > 0) description.append("，");
            description.append("防禦 +").append((int)((getDefenseModifier() - 1.0) * 100)).append("%");
        }
        
        return description.length() > 0 ? description.toString() : "無特殊效果";
    }
}


