package com.taco.TinyRealm.module.terrainMapModule.model;

/**
 * 地形類型枚舉
 * 定義不同地形對戰鬥的影響
 */
public enum TerrainType {
    PLAIN("平原", "無修正", 1.0, 0),
    FOREST("森林", "移動速度 -20%", 0.8, 0),
    MOUNTAIN("山地", "移動速度 -40%，遠程射程 +1", 0.6, 1),
    WATER("水域", "移動速度 -60%", 0.4, 0),
    WALL("城牆", "防禦 +50%，遠程射程 +2", 0.0, 2),
    CITY("城市", "防禦 +30%", 0.7, 0),
    ROAD("道路", "移動速度 +20%", 1.2, 0);

    private final String displayName;
    private final String description;
    private final double speedModifier; // 移動速度修正倍數
    private final int rangeBonus; // 射程加成

    TerrainType(String displayName, String description, double speedModifier, int rangeBonus) {
        this.displayName = displayName;
        this.description = description;
        this.speedModifier = speedModifier;
        this.rangeBonus = rangeBonus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public double getSpeedModifier() {
        return speedModifier;
    }

    public int getRangeBonus() {
        return rangeBonus;
    }

    /**
     * 獲取防禦修正倍數
     */
    public double getDefenseModifier() {
        switch (this) {
            case WALL: return 1.5; // 防禦 +50%
            case CITY: return 1.3; // 防禦 +30%
            default: return 1.0;   // 無修正
        }
    }

    /**
     * 檢查是否為城防地形
     */
    public boolean isDefensive() {
        return this == WALL || this == CITY;
    }

    /**
     * 檢查是否為障礙地形
     */
    public boolean isObstacle() {
        return this == MOUNTAIN || this == WATER;
    }

    /**
     * 檢查是否為移動友好地形
     */
    public boolean isMovementFriendly() {
        return this == ROAD || this == PLAIN;
    }
}
