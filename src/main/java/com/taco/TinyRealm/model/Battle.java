package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.taco.TinyRealm.module.ResourceModule.model.Resource;

import java.util.List;

/**
 * 戰鬥紀錄物件，紀錄每場戰鬥的詳細資訊。
 */
public class Battle {
    /** 戰鬥唯一識別碼 */
    @JsonProperty("id")
    private String id;
    /** 玩家 ID */
    @JsonProperty("playerId")
    private String playerId;
    /** 敵人類型（如 bandit） */
    @JsonProperty("enemyType")
    private String enemyType;
    /** 玩家參戰單位列表 */
    @JsonProperty("playerUnits")
    private List<Unit> playerUnits;
    /** 敵方單位列表 */
    @JsonProperty("enemyUnits")
    private List<Unit> enemyUnits;
    /** 戰鬥結果（WIN/LOSE） */
    @JsonProperty("result")
    private String result;
    /** 戰鬥獎勵 */
    //@JsonProperty("rewards")
    //private Resource rewards;
    /** 戰鬥發生時間戳（毫秒） */
    @JsonProperty("timestamp")
    private long timestamp;

    /** 取得戰鬥 ID */
    public String getId() { return id; }
    /** 設定戰鬥 ID */
    public void setId(String id) { this.id = id; }
    /** 取得玩家 ID */
    public String getPlayerId() { return playerId; }
    /** 設定玩家 ID */
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    /** 取得敵人類型 */
    public String getEnemyType() { return enemyType; }
    /** 設定敵人類型 */
    public void setEnemyType(String enemyType) { this.enemyType = enemyType; }
    /** 取得玩家單位列表 */
    public List<Unit> getPlayerUnits() { return playerUnits; }
    /** 設定玩家單位列表 */
    public void setPlayerUnits(List<Unit> playerUnits) { this.playerUnits = playerUnits; }
    /** 取得敵方單位列表 */
    public List<Unit> getEnemyUnits() { return enemyUnits; }
    /** 設定敵方單位列表 */
    public void setEnemyUnits(List<Unit> enemyUnits) { this.enemyUnits = enemyUnits; }
    /** 取得戰鬥結果 */
    public String getResult() { return result; }
    /** 設定戰鬥結果 */
    public void setResult(String result) { this.result = result; }
    /** 取得戰鬥獎勵 */
    //public Resource getRewards() { return rewards; }
    /** 設定戰鬥獎勵 */
    //public void setRewards(Resource rewards) { this.rewards = rewards; }
    /** 取得戰鬥時間戳 */
    public long getTimestamp() { return timestamp; }
    /** 設定戰鬥時間戳 */
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
