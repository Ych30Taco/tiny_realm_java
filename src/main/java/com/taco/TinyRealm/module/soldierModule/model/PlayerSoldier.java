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
    /** 移動速度 */
    private int speed;
    /** 當前生命值 */
    private int health;
    /** 最大生命值 */
    private int maxHealth;
    /** 士兵狀態 (ACTIVE, INJURED, DEAD) */
    private String status;
}
