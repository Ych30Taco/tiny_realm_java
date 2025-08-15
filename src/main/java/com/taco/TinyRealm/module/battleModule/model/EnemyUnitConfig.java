package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 敵方單位配置
 */
public class EnemyUnitConfig {
    /** 單位類型 */
    @JsonProperty("type")
    private String type;
    
    /** 單位數量 */
    @JsonProperty("count")
    private int count;
    
    /** 單位等級 */
    @JsonProperty("level")
    private int level;
    
    /** 攻擊力 */
    @JsonProperty("attack")
    private int attack;
    
    /** 防禦力 */
    @JsonProperty("defense")
    private int defense;
    
    /** 生命值 */
    @JsonProperty("health")
    private int health;

    /**
     * 預設建構函數
     */
    public EnemyUnitConfig() {
        this.level = 1;
    }

    /**
     * 取得單位類型
     * @return 單位類型
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * 設定單位類型
     * @param type 單位類型
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * 取得單位數量
     * @return 單位數量
     */
    public int getCount() { 
        return count; 
    }
    
    /**
     * 設定單位數量
     * @param count 單位數量
     */
    public void setCount(int count) { 
        this.count = count; 
    }
    
    /**
     * 取得單位等級
     * @return 單位等級
     */
    public int getLevel() { 
        return level; 
    }
    
    /**
     * 設定單位等級
     * @param level 單位等級
     */
    public void setLevel(int level) { 
        this.level = level; 
    }
    
    /**
     * 取得攻擊力
     * @return 攻擊力
     */
    public int getAttack() { 
        return attack; 
    }
    
    /**
     * 設定攻擊力
     * @param attack 攻擊力
     */
    public void setAttack(int attack) { 
        this.attack = attack; 
    }
    
    /**
     * 取得防禦力
     * @return 防禦力
     */
    public int getDefense() { 
        return defense; 
    }
    
    /**
     * 設定防禦力
     * @param defense 防禦力
     */
    public void setDefense(int defense) { 
        this.defense = defense; 
    }
    
    /**
     * 取得生命值
     * @return 生命值
     */
    public int getHealth() { 
        return health; 
    }
    
    /**
     * 設定生命值
     * @param health 生命值
     */
    public void setHealth(int health) { 
        this.health = health; 
    }
}
