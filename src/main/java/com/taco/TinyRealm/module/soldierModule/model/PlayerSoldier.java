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
<<<<<<< HEAD
=======
    
    /** 兵種類型 */
    @JsonProperty("unitType")
    private UnitType type;
    
    /** 站位位置 */
    @JsonProperty("formationPosition")
    private FormationPosition formationPosition;
    
    /** 生命值 */
    @JsonProperty("hp")
    private int hp;
    
    /** 最大生命值 */
    @JsonProperty("maxHp")
    private int maxHp;
    
    /** 攻擊力 */
    @JsonProperty("attack")
    private int attack;
    
    /** 防禦力 */
    @JsonProperty("defense")
    private int defense;
    
    /** 射程（單位：格數） */
    @JsonProperty("range")
    private int range;
    
    /** 移動速度（每回合格數） */
    @JsonProperty("speed")
    private int speed;
    
    /** 當前X座標 */
    @JsonProperty("positionX")
    private int positionX;
    
    /** 當前Y座標 */
    @JsonProperty("positionY")
    private int positionY;
    
>>>>>>> 戰鬥模組修正
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
<<<<<<< HEAD
=======
    
    /** 經驗值 */
    @JsonProperty("experience")
    private int experience;
    
    /** 升級所需經驗值 */
    @JsonProperty("experienceToNext")
    private int experienceToNext;
    
    /** 存活狀態 */
    @JsonProperty("alive")
    private boolean alive;

    @JsonProperty("ranged")
    private boolean ranged;

    @JsonProperty("melee")
    private boolean melee;

    @JsonProperty("siege")
    private boolean siege;

    /** 實際防禦力（考慮等級加成） */
    @JsonProperty("effectiveDefense")
    private int effectiveDefense;


    /** 當前生命值百分比 */
    @JsonProperty("healthPercentage")
    private double healthPercentage;

    /** 實際射程（考慮等級加成） */
    @JsonProperty("effectiveRange")
    private int effectiveRange;

    /** 實際攻擊力（考慮等級加成） */
    @JsonProperty("effectiveAttack")
    private int effectiveAttack;

    @JsonProperty("combatPower")
    private int combatPower;

    @JsonProperty("effectiveSpeed")
    private int effectiveSpeed;

    /**
     * 檢查是否為近戰單位
     */
    public boolean isMelee() {
        return type != null && type.isMelee();
    }

    /**
     * 檢查是否為攻城單位
     */
    public boolean isSiege() {
        return type != null && type.isSiege();
    }

    /**
     * 獲取當前生命值百分比
     */
    public double getHealthPercentage() {
        if (maxHp <= 0) return 0.0;
        return (double) hp / maxHp;
    }

    /**
     * 獲取實際攻擊力（考慮等級加成）
     */
    public int getEffectiveAttack() {
        return attack + (level - 1) * 2; // 每級增加2點攻擊力
    }

    /**
     * 獲取實際防禦力（考慮等級加成）
     */
    public int getEffectiveDefense() {
        return defense + (level - 1) * 1; // 每級增加1點防禦力
    }

    /**
     * 獲取實際射程（考慮等級加成）
     */
    public int getEffectiveRange() {
        return range + (level - 1) / 5; // 每5級增加1格射程
    }

    /**
     * 獲取實際移動速度（考慮等級加成）
     */
    public int getEffectiveSpeed() {
        return speed + (level - 1) / 3; // 每3級增加1格移動速度
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
     * 檢查是否在射程內
     */
    public boolean isInRange(int targetX, int targetY) {
        return getDistanceTo(targetX, targetY) <= getEffectiveRange();
    }

    /**
     * 受到傷害
     */
    public void takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        if (hp <= 0) {
            count = Math.max(0, count - 1);
            if (count > 0) {
                hp = maxHp; // 如果還有數量，恢復滿血
            }
        }
    }

    /**
     * 治療
     */
    public void heal(int healAmount) {
        hp = Math.min(maxHp, hp + healAmount);
    }

    /**
     * 升級
     */
    public boolean levelUp() {
        if (experience >= experienceToNext) {
            level++;
            experience -= experienceToNext;
            experienceToNext = calculateNextLevelExperience();
            
            // 升級後屬性提升
            maxHp += 10;
            hp = maxHp; // 升級後恢復滿血
            attack += 3;
            defense += 2;
            
            return true;
        }
        return false;
    }

    /**
     * 計算下一級所需經驗值
     */
    private int calculateNextLevelExperience() {
        return level * 100; // 簡單的經驗值計算公式
    }

    /**
     * 獲取戰鬥力評分
     */
    public int getCombatPower() {
        return getEffectiveAttack() * 2 + getEffectiveDefense() + 
               getEffectiveRange() + getEffectiveSpeed() + 
               (hp * 100 / maxHp); // 生命值百分比影響戰鬥力
    }
>>>>>>> 戰鬥模組修正
}
