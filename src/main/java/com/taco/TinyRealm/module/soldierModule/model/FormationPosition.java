package com.taco.TinyRealm.module.soldierModule.model;

/**
 * 部隊站位枚舉
 * 定義部隊在戰場上的站位
 */
public enum FormationPosition {
    FRONT("前排", "最前線，優先受到攻擊"),
    MIDDLE("中排", "中間位置，前排陣亡後受到攻擊"),
    BACK("後排", "後方位置，最後受到攻擊");

    private final String displayName;
    private final String description;

    FormationPosition(String displayName, String description) {
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
     * 獲取攻擊優先級（數字越小優先級越高）
     */
    public int getAttackPriority() {
        switch (this) {
            case FRONT: return 1;
            case MIDDLE: return 2;
            case BACK: return 3;
            default: return 999;
        }
    }

    /**
     * 獲取受擊優先級（數字越小優先級越高）
     */
    public int getDefensePriority() {
        return getAttackPriority(); // 受擊優先級與攻擊優先級相同
    }
}
