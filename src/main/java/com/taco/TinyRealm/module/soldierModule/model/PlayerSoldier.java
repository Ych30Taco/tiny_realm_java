package com.taco.TinyRealm.module.soldierModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 玩家士兵模型
 * 代表玩家擁有的個別士兵單位
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSoldier {
    /** 士兵ID */
    private String id;
    /** 士兵類型 */
    private String type;
    /** 士兵名稱 */
    private String name;
    /** 士兵數量 */
    private int count;
    /** 士兵等級 */
    private int level;
    /** 攻擊力 */
    private int attack;
    /** 防禦力 */
    private int defense;
    /** 當前生命值 */
    private int health;
    /** 最大生命值 */
    private int maxHealth;
    /** 士兵狀態 (ACTIVE, INJURED, DEAD) */
    private String status;
    /** 創建時間 */
    private long createdTime;
    /** 最後更新時間 */
    private long lastUpdatedTime;
    
    /**
     * 檢查士兵是否存活
     * @return 是否存活
     */
    public boolean isAlive() {
        return health > 0 && "ACTIVE".equals(status);
    }
    
    /**
     * 檢查士兵是否受傷
     * @return 是否受傷
     */
    public boolean isInjured() {
        return health < maxHealth || "INJURED".equals(status);
    }
    
    /**
     * 計算戰力
     * @return 戰力值
     */
    public int getCombatPower() {
        return attack * level * health / Math.max(1, maxHealth);
    }
}
