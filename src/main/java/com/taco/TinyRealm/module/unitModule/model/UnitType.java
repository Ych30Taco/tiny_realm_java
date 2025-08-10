package com.taco.TinyRealm.module.unitModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * 部隊類型配置模型
 * 
 * 定義不同部隊類型的基本屬性、成本、要求等配置資訊
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
public class UnitType {
    
    /** 部隊類型 */
    @JsonProperty("type")
    private String type;
    
    /** 部隊名稱 */
    @JsonProperty("name")
    private String name;
    
    /** 部隊描述 */
    @JsonProperty("description")
    private String description;
    
    /** 部隊成本 */
    @JsonProperty("cost")
    private Map<String, Integer> cost;
    
    /** 部隊基礎攻擊力 */
    @JsonProperty("attack")
    private int attack;
    
    /** 部隊基礎防禦力 */
    @JsonProperty("defense")
    private int defense;
    
    /** 部隊基礎生命值 */
    @JsonProperty("health")
    private int health;
    
    /** 部隊移動速度 */
    @JsonProperty("speed")
    private int speed;
    
    /** 部隊攻擊範圍 */
    @JsonProperty("range")
    private int range;
    
    /** 需要的建築物 */
    @JsonProperty("requiredBuilding")
    private String requiredBuilding;
    
    /** 需要的科技 */
    @JsonProperty("requiredTech")
    private String requiredTech;
    
    /** 訓練時間（秒） */
    @JsonProperty("trainingTime")
    private int trainingTime;
    
    /** 部隊等級上限 */
    @JsonProperty("maxLevel")
    private int maxLevel = 10;
    
    /** 升級所需經驗值 */
    @JsonProperty("expToLevel")
    private int expToLevel = 100;
    
    /** 部隊特殊技能 */
    @JsonProperty("skills")
    private Map<String, Object> skills;

    // Getter 和 Setter 方法
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Map<String, Integer> getCost() { return cost; }
    public void setCost(Map<String, Integer> cost) { this.cost = cost; }
    
    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }
    
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = defense; }
    
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    
    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }
    
    public int getRange() { return range; }
    public void setRange(int range) { this.range = range; }
    
    public String getRequiredBuilding() { return requiredBuilding; }
    public void setRequiredBuilding(String requiredBuilding) { this.requiredBuilding = requiredBuilding; }
    
    public String getRequiredTech() { return requiredTech; }
    public void setRequiredTech(String requiredTech) { this.requiredTech = requiredTech; }
    
    public int getTrainingTime() { return trainingTime; }
    public void setTrainingTime(int trainingTime) { this.trainingTime = trainingTime; }
    
    public int getMaxLevel() { return maxLevel; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
    
    public int getExpToLevel() { return expToLevel; }
    public void setExpToLevel(int expToLevel) { this.expToLevel = expToLevel; }
    
    public Map<String, Object> getSkills() { return skills; }
    public void setSkills(Map<String, Object> skills) { this.skills = skills; }

    /**
     * 獲取特定資源的成本
     * 
     * @param resourceType 資源類型
     * @return 成本數量
     */
    public int getCost(String resourceType) {
        return cost != null ? cost.getOrDefault(resourceType, 0) : 0;
    }

    /**
     * 計算總成本
     * 
     * @return 總成本
     */
    public int getTotalCost() {
        if (cost == null) return 0;
        return cost.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * 檢查是否有特定技能
     * 
     * @param skillName 技能名稱
     * @return 是否有該技能
     */
    public boolean hasSkill(String skillName) {
        return skills != null && skills.containsKey(skillName);
    }

    /**
     * 獲取技能值
     * 
     * @param skillName 技能名稱
     * @return 技能值
     */
    public Object getSkillValue(String skillName) {
        return skills != null ? skills.get(skillName) : null;
    }

    @Override
    public String toString() {
        return String.format("UnitType{type='%s', name='%s', attack=%d, defense=%d, health=%d}",
                type, name, attack, defense, health);
    }
}
