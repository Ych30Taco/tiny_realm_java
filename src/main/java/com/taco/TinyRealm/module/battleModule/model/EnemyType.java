package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taco.TinyRealm.module.resourceModule.model.Resource;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 敵人類型配置
 * 定義不同敵人類型的屬性和行為
 */
public class EnemyType {
    /** 敵人類型識別碼 */
    @JsonProperty("type")
    private String type;
    
    /** 敵人類型名稱 */
    @JsonProperty("name")
    private String name;
    
    /** 敵人類型描述 */
    @JsonProperty("description")
    private String description;
    
    /** 敵人類型等級 */
    @JsonProperty("level")
    private int level;
    
    /** 敵方單位配置 */
    @JsonProperty("units")
    private List<EnemyUnitConfig> units;
    
    /** 戰鬥獎勵 */
    @JsonProperty("rewards")
    private Resource rewards;
    
    /** 戰鬥難度 */
    @JsonProperty("difficulty")
    private String difficulty;
    
    /** 最小玩家等級要求 */
    @JsonProperty("minPlayerLevel")
    private int minPlayerLevel;
    
    /** 戰鬥持續時間（秒） */
    @JsonProperty("battleDuration")
    private int battleDuration;
    
    /** 特殊技能 */
    @JsonProperty("skills")
    private Map<String, Object> skills;
    
    /** 出現機率 */
    @JsonProperty("spawnChance")
    private double spawnChance;
    
    /** 是否為Boss */
    @JsonProperty("isBoss")
    private boolean isBoss;
    
    /** 戰鬥背景描述 */
    @JsonProperty("battleDescription")
    private String battleDescription;

    /**
     * 預設建構函數
     */
    public EnemyType() {
        this.skills = new HashMap<>();
        this.isBoss = false;
        this.spawnChance = 1.0;
    }

    /**
     * 取得敵人類型識別碼
     * @return 敵人類型識別碼
     */
    public String getType() { 
        return type; 
    }
    
    /**
     * 設定敵人類型識別碼
     * @param type 敵人類型識別碼
     */
    public void setType(String type) { 
        this.type = type; 
    }
    
    /**
     * 取得敵人類型名稱
     * @return 敵人類型名稱
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * 設定敵人類型名稱
     * @param name 敵人類型名稱
     */
    public void setName(String name) { 
        this.name = name; 
    }
    
    /**
     * 取得敵人類型描述
     * @return 敵人類型描述
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * 設定敵人類型描述
     * @param description 敵人類型描述
     */
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    /**
     * 取得敵人類型等級
     * @return 敵人類型等級
     */
    public int getLevel() { 
        return level; 
    }
    
    /**
     * 設定敵人類型等級
     * @param level 敵人類型等級
     */
    public void setLevel(int level) { 
        this.level = level; 
    }
    
    /**
     * 取得敵方單位配置
     * @return 敵方單位配置列表
     */
    public List<EnemyUnitConfig> getUnits() { 
        return units; 
    }
    
    /**
     * 設定敵方單位配置
     * @param units 敵方單位配置列表
     */
    public void setUnits(List<EnemyUnitConfig> units) { 
        this.units = units; 
    }
    
    /**
     * 取得戰鬥獎勵
     * @return 戰鬥獎勵資源
     */
    public Resource getRewards() { 
        return rewards; 
    }
    
    /**
     * 設定戰鬥獎勵
     * @param rewards 戰鬥獎勵資源
     */
    public void setRewards(Resource rewards) { 
        this.rewards = rewards; 
    }
    
    /**
     * 取得戰鬥難度
     * @return 戰鬥難度
     */
    public String getDifficulty() { 
        return difficulty; 
    }
    
    /**
     * 設定戰鬥難度
     * @param difficulty 戰鬥難度
     */
    public void setDifficulty(String difficulty) { 
        this.difficulty = difficulty; 
    }
    
    /**
     * 取得最小玩家等級要求
     * @return 最小玩家等級要求
     */
    public int getMinPlayerLevel() { 
        return minPlayerLevel; 
    }
    
    /**
     * 設定最小玩家等級要求
     * @param minPlayerLevel 最小玩家等級要求
     */
    public void setMinPlayerLevel(int minPlayerLevel) { 
        this.minPlayerLevel = minPlayerLevel; 
    }
    
    /**
     * 取得戰鬥持續時間
     * @return 戰鬥持續時間（秒）
     */
    public int getBattleDuration() { 
        return battleDuration; 
    }
    
    /**
     * 設定戰鬥持續時間
     * @param battleDuration 戰鬥持續時間（秒）
     */
    public void setBattleDuration(int battleDuration) { 
        this.battleDuration = battleDuration; 
    }
    
    /**
     * 取得特殊技能
     * @return 特殊技能映射
     */
    public Map<String, Object> getSkills() { 
        return skills; 
    }
    
    /**
     * 設定特殊技能
     * @param skills 特殊技能映射
     */
    public void setSkills(Map<String, Object> skills) { 
        this.skills = skills; 
    }
    
    /**
     * 取得出現機率
     * @return 出現機率
     */
    public double getSpawnChance() { 
        return spawnChance; 
    }
    
    /**
     * 設定出現機率
     * @param spawnChance 出現機率
     */
    public void setSpawnChance(double spawnChance) { 
        this.spawnChance = spawnChance; 
    }
    
    /**
     * 檢查是否為Boss
     * @return 是否為Boss
     */
    public boolean isBoss() { 
        return isBoss; 
    }
    
    /**
     * 設定是否為Boss
     * @param boss 是否為Boss
     */
    public void setBoss(boolean boss) { 
        isBoss = boss; 
    }
    
    /**
     * 取得戰鬥背景描述
     * @return 戰鬥背景描述
     */
    public String getBattleDescription() { 
        return battleDescription; 
    }
    
    /**
     * 設定戰鬥背景描述
     * @param battleDescription 戰鬥背景描述
     */
    public void setBattleDescription(String battleDescription) { 
        this.battleDescription = battleDescription; 
    }

    /**
     * 檢查是否有特定技能
     * @param skillName 技能名稱
     * @return 是否有該技能
     */
    public boolean hasSkill(String skillName) {
        return skills != null && skills.containsKey(skillName);
    }
    
    /**
     * 取得技能值
     * @param skillName 技能名稱
     * @return 技能值
     */
    public Object getSkillValue(String skillName) {
        return skills != null ? skills.get(skillName) : null;
    }
    
    /**
     * 計算敵方總戰力
     * @return 敵方總戰力
     */
    public int getTotalStrength() {
        if (units == null) return 0;
        return units.stream()
                .mapToInt(unit -> unit.getAttack() * unit.getCount())
                .sum();
    }
    
    /**
     * 檢查玩家等級是否滿足要求
     * @param playerLevel 玩家等級
     * @return 是否滿足等級要求
     */
    public boolean isPlayerLevelSufficient(int playerLevel) {
        return playerLevel >= minPlayerLevel;
    }
    
    /**
     * 添加技能
     * @param skillName 技能名稱
     * @param skillValue 技能值
     */
    public void addSkill(String skillName, Object skillValue) {
        if (skills == null) {
            skills = new HashMap<>();
        }
        skills.put(skillName, skillValue);
    }
}
