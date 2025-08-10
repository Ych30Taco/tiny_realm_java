package com.taco.TinyRealm.module.unitModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * 部隊模型類別
 * 
 * 代表遊戲中的一個部隊單位，包含部隊的基本資訊、屬性、位置等
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
public class Unit {
    
    /** 部隊唯一ID */
    @JsonProperty("id")
    private String id;
    
    /** 部隊類型 */
    @JsonProperty("type")
    private String type;
    
    /** 部隊名稱 */
    @JsonProperty("name")
    private String name;
    
    /** 部隊數量 */
    @JsonProperty("count")
    private int count;
    
    /** 部隊等級 */
    @JsonProperty("level")
    private int level = 1;
    
    /** 部隊經驗值 */
    @JsonProperty("experience")
    private int experience = 0;
    
    /** 部隊位置 X 座標 */
    @JsonProperty("x")
    private int x;
    
    /** 部隊位置 Y 座標 */
    @JsonProperty("y")
    private int y;
    
    /** 部隊狀態 (0: 訓練中, 1: 待命, 2: 戰鬥中, 3: 移動中) */
    @JsonProperty("status")
    private int status = 1;
    
    /** 部隊攻擊力 */
    @JsonProperty("attack")
    private int attack;
    
    /** 部隊防禦力 */
    @JsonProperty("defense")
    private int defense;
    
    /** 部隊生命值 */
    @JsonProperty("health")
    private int health;
    
    /** 部隊最大生命值 */
    @JsonProperty("maxHealth")
    private int maxHealth;
    
    /** 部隊移動速度 */
    @JsonProperty("speed")
    private int speed;
    
    /** 部隊攻擊範圍 */
    @JsonProperty("range")
    private int range;
    
    /** 部隊特殊技能 */
    @JsonProperty("skills")
    private Map<String, Object> skills;
    
    /** 部隊創建時間 */
    @JsonProperty("createdTime")
    private long createdTime;
    
    /** 部隊最後更新時間 */
    @JsonProperty("lastUpdatedTime")
    private long lastUpdatedTime;
    
    /** 部隊所屬玩家ID */
    @JsonProperty("playerId")
    private String playerId;

    // 建構函數
    public Unit() {
        this.createdTime = System.currentTimeMillis();
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    // Getter 和 Setter 方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    
    public int getRange() { return range; }
    public void setRange(int range) { this.range = range; }
    
    public Map<String, Object> getSkills() { return skills; }
    public void setSkills(Map<String, Object> skills) { this.skills = skills; }
    
    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
    
    public long getLastUpdatedTime() { return lastUpdatedTime; }
    public void setLastUpdatedTime(long lastUpdatedTime) { this.lastUpdatedTime = lastUpdatedTime; }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    /**
     * 檢查部隊是否存活
     * 
     * @return 是否存活
     */
    public boolean isAlive() {
        return health > 0 && count > 0;
    }

    /**
     * 檢查部隊是否可以移動
     * 
     * @return 是否可以移動
     */
    public boolean canMove() {
        return status == 1 && isAlive();
    }

    /**
     * 檢查部隊是否可以戰鬥
     * 
     * @return 是否可以戰鬥
     */
    public boolean canFight() {
        return status != 0 && isAlive();
    }

    /**
     * 增加經驗值
     * 
     * @param exp 增加的經驗值
     */
    public void addExperience(int exp) {
        this.experience += exp;
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    /**
     * 升級部隊
     */
    public void levelUp() {
        this.level++;
        this.attack += 2;
        this.defense += 1;
        this.maxHealth += 5;
        this.health = this.maxHealth;
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    /**
     * 治療部隊
     * 
     * @param healAmount 治療量
     */
    public void heal(int healAmount) {
        this.health = Math.min(this.maxHealth, this.health + healAmount);
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    /**
     * 部隊受到傷害
     * 
     * @param damage 傷害量
     */
    public void takeDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return String.format("Unit{id='%s', type='%s', name='%s', count=%d, level=%d, status=%d, position=(%d,%d)}",
                id, type, name, count, level, status, x, y);
    }
}
