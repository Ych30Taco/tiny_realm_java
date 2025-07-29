package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Battle {
    @JsonProperty("id")
    private String id;
    @JsonProperty("playerId")
    private String playerId;
    @JsonProperty("enemyType")
    private String enemyType;
    @JsonProperty("playerUnits")
    private List<Unit> playerUnits;
    @JsonProperty("enemyUnits")
    private List<Unit> enemyUnits;
    @JsonProperty("result")
    private String result;
    @JsonProperty("rewards")
    private Resource rewards;
    @JsonProperty("timestamp")
    private long timestamp;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public String getEnemyType() { return enemyType; }
    public void setEnemyType(String enemyType) { this.enemyType = enemyType; }
    public List<Unit> getPlayerUnits() { return playerUnits; }
    public void setPlayerUnits(List<Unit> playerUnits) { this.playerUnits = playerUnits; }
    public List<Unit> getEnemyUnits() { return enemyUnits; }
    public void setEnemyUnits(List<Unit> enemyUnits) { this.enemyUnits = enemyUnits; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Resource getRewards() { return rewards; }
    public void setRewards(Resource rewards) { this.rewards = rewards; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
