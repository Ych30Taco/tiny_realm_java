package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
//import com.taco.TinyRealm.module.soldierModule.model.Unit;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 戰鬥紀錄物件，紀錄每場戰鬥的詳細資訊
 * 包含戰鬥結果、參與單位、獎勵等完整資訊
 */
public class Battle {
    /** 戰鬥唯一識別碼 */
    @JsonProperty("id")
    private String id;
    
    /** 玩家 ID */
    @JsonProperty("playerId")
    private String playerId;
    
    /** 敵人類型（如 bandit, monster, boss） */
    @JsonProperty("enemyType")
    private String enemyType;
    
    /** 玩家參戰單位列表 */
    /*@JsonProperty("playerUnits")
    private List<Unit> playerUnits;*/
    
    /** 敵方單位列表 */
    /*@JsonProperty("enemyUnits")
    private List<Unit> enemyUnits;*/
    
    /** 戰鬥結果（WIN/LOSE/DRAW） */
    @JsonProperty("result")
    private String result;
    
    /** 戰鬥獎勵資源 */
    @JsonProperty("rewards")
    private Resource rewards;
    
    /** 戰鬥發生時間戳（毫秒） */
    @JsonProperty("timestamp")
    private long timestamp;
    
    /** 戰鬥持續時間（秒） */
    @JsonProperty("duration")
    private int duration;
    
    /** 戰鬥難度等級 */
    @JsonProperty("difficulty")
    private String difficulty;
    
    /** 戰鬥位置（X座標） */
    @JsonProperty("locationX")
    private int locationX;
    
    /** 戰鬥位置（Y座標） */
    @JsonProperty("locationY")
    private int locationY;
    
    /** 戰鬥詳細統計 */
    @JsonProperty("statistics")
    private Map<String, Object> statistics;
    
    /** 戰鬥狀態（PENDING/IN_PROGRESS/COMPLETED） */
    @JsonProperty("status")
    private String status;
    
    /** 戰鬥描述 */
    @JsonProperty("description")
    private String description;

    /**
     * 預設建構函數
     */
    public Battle() {
        this.statistics = new HashMap<>();
        this.status = "PENDING";
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 取得戰鬥 ID
     * @return 戰鬥唯一識別碼
     */
    public String getId() { 
        return id; 
    }
    
    /**
     * 設定戰鬥 ID
     * @param id 戰鬥唯一識別碼
     */
    public void setId(String id) { 
        this.id = id; 
    }
    
    /**
     * 取得玩家 ID
     * @return 玩家識別碼
     */
    public String getPlayerId() { 
        return playerId; 
    }
    
    /**
     * 設定玩家 ID
     * @param playerId 玩家識別碼
     */
    public void setPlayerId(String playerId) { 
        this.playerId = playerId; 
    }
    
    /**
     * 取得敵人類型
     * @return 敵人類型
     */
    public String getEnemyType() { 
        return enemyType; 
    }
    
    /**
     * 設定敵人類型
     * @param enemyType 敵人類型
     */
    public void setEnemyType(String enemyType) { 
        this.enemyType = enemyType; 
    }
    
    /**
     * 取得玩家單位列表
     * @return 玩家參戰單位列表
     */
    /*public List<Unit> getPlayerUnits() { 
        return playerUnits; 
    }*/
    
    /**
     * 設定玩家單位列表
     * @param playerUnits 玩家參戰單位列表
     */
   /*  public void setPlayerUnits(List<Unit> playerUnits) { 
        this.playerUnits = playerUnits; 
    }
    */
    /**
     * 取得敵方單位列表
     * @return 敵方單位列表
     */
    /*public List<Unit> getEnemyUnits() { 
        return enemyUnits; 
    }*/
    
    /**
     * 設定敵方單位列表
     * @param enemyUnits 敵方單位列表
     */
   /* public void setEnemyUnits(List<Unit> enemyUnits) { 
        this.enemyUnits = enemyUnits; 
    }*/
    
    /**
     * 取得戰鬥結果
     * @return 戰鬥結果（WIN/LOSE/DRAW）
     */
    public String getResult() { 
        return result; 
    }
    
    /**
     * 設定戰鬥結果
     * @param result 戰鬥結果
     */
    public void setResult(String result) { 
        this.result = result; 
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
     * 取得戰鬥時間戳
     * @return 戰鬥發生時間戳（毫秒）
     */
    public long getTimestamp() { 
        return timestamp; 
    }
    
    /**
     * 設定戰鬥時間戳
     * @param timestamp 戰鬥發生時間戳（毫秒）
     */
    public void setTimestamp(long timestamp) { 
        this.timestamp = timestamp; 
    }
    
    /**
     * 取得戰鬥持續時間
     * @return 戰鬥持續時間（秒）
     */
    public int getDuration() { 
        return duration; 
    }
    
    /**
     * 設定戰鬥持續時間
     * @param duration 戰鬥持續時間（秒）
     */
    public void setDuration(int duration) { 
        this.duration = duration; 
    }
    
    /**
     * 取得戰鬥難度
     * @return 戰鬥難度等級
     */
    public String getDifficulty() { 
        return difficulty; 
    }
    
    /**
     * 設定戰鬥難度
     * @param difficulty 戰鬥難度等級
     */
    public void setDifficulty(String difficulty) { 
        this.difficulty = difficulty; 
    }
    
    /**
     * 取得戰鬥位置X座標
     * @return 戰鬥位置X座標
     */
    public int getLocationX() { 
        return locationX; 
    }
    
    /**
     * 設定戰鬥位置X座標
     * @param locationX 戰鬥位置X座標
     */
    public void setLocationX(int locationX) { 
        this.locationX = locationX; 
    }
    
    /**
     * 取得戰鬥位置Y座標
     * @return 戰鬥位置Y座標
     */
    public int getLocationY() { 
        return locationY; 
    }
    
    /**
     * 設定戰鬥位置Y座標
     * @param locationY 戰鬥位置Y座標
     */
    public void setLocationY(int locationY) { 
        this.locationY = locationY; 
    }
    
    /**
     * 取得戰鬥統計
     * @return 戰鬥詳細統計
     */
    public Map<String, Object> getStatistics() { 
        return statistics; 
    }
    
    /**
     * 設定戰鬥統計
     * @param statistics 戰鬥詳細統計
     */
    public void setStatistics(Map<String, Object> statistics) { 
        this.statistics = statistics; 
    }
    
    /**
     * 取得戰鬥狀態
     * @return 戰鬥狀態
     */
    public String getStatus() { 
        return status; 
    }
    
    /**
     * 設定戰鬥狀態
     * @param status 戰鬥狀態
     */
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    /**
     * 取得戰鬥描述
     * @return 戰鬥描述
     */
    public String getDescription() { 
        return description; 
    }
    
    /**
     * 設定戰鬥描述
     * @param description 戰鬥描述
     */
    public void setDescription(String description) { 
        this.description = description; 
    }

    /**
     * 檢查是否為勝利
     * @return 是否勝利
     */
    public boolean isVictory() {
        return "WIN".equals(result);
    }
    
    /**
     * 檢查是否為失敗
     * @return 是否失敗
     */
    public boolean isDefeat() {
        return "LOSE".equals(result);
    }
    
    /**
     * 檢查是否為平手
     * @return 是否平手
     */
    public boolean isDraw() {
        return "DRAW".equals(result);
    }
    
    /**
     * 檢查戰鬥是否已完成
     * @return 是否已完成
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    /**
     * 計算玩家總戰力
     * @return 玩家總戰力
     */
    /*public int getPlayerTotalStrength() {
        if (playerUnits == null) return 0;
        return playerUnits.stream()
                .mapToInt(unit -> unit.getAttack() * unit.getLevel())
                .sum();
    }*/
    
    /**
     * 計算敵方總戰力
     * @return 敵方總戰力
     */
    /*public int getEnemyTotalStrength() {
        if (enemyUnits == null) return 0;
        return enemyUnits.stream()
                .mapToInt(unit -> unit.getAttack() * unit.getLevel())
                .sum();
    }*/
    
    /**
     * 添加統計數據
     * @param key 統計鍵
     * @param value 統計值
     */
    public void addStatistic(String key, Object value) {
        if (statistics == null) {
            statistics = new HashMap<>();
        }
        statistics.put(key, value);
    }
    
    /**
     * 取得統計數據
     * @param key 統計鍵
     * @return 統計值
     */
    public Object getStatistic(String key) {
        return statistics != null ? statistics.get(key) : null;
    }

    // 讓 JSON 直接映射 completed/victory/draw/defeat
    @JsonProperty("completed")
    public boolean getCompleted() {
        return isCompleted();
    }

    @JsonProperty("victory")
    public boolean getVictory() {
        return isVictory();
    }

    @JsonProperty("draw")
    public boolean getDraw() {
        return isDraw();
    }

    @JsonProperty("defeat")
    public boolean getDefeat() {
        return isDefeat();
    }
}
