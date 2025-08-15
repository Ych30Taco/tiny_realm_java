package com.taco.TinyRealm.module.soldierModule.model;

/**
 * 兵種類型枚舉
 * 定義遊戲中的各種兵種
 */
public enum UnitType {
    INFANTRY("步兵", "近戰單位，防禦力高，適合前排"),
    ARCHER("弓箭手", "遠程單位，攻擊力高，射程遠"),
    CAVALRY("騎兵", "機動單位，移動速度快，攻擊力中等"),
    SIEGE("攻城器", "攻城單位，對城牆傷害高"),
    MAGE("法師", "魔法單位，特殊攻擊效果"),
    DEFENDER("守軍", "城防單位，防禦力極高");

    private final String displayName;
    private final String description;

    UnitType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 判斷是否為遠程單位
     */
    public boolean isRanged() {
        return this == ARCHER || this == MAGE || this == SIEGE;
    }

    /**
     * 判斷是否為近戰單位
     */
    public boolean isMelee() {
        return this == INFANTRY || this == CAVALRY || this == DEFENDER;
    }

    /**
     * 判斷是否為攻城單位
     */
    public boolean isSiege() {
        return this == SIEGE;
    }
}
