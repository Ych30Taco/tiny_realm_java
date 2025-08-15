package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 攻城結果模型
 * 記錄攻城戰鬥的結果和統計信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiegeResult {
    
    /** 攻城結果 */
    @JsonProperty("result")
    private String result; // ATTACKER_WIN, DEFENDER_WIN, DRAW, NOT_CITY
    
    /** 結果描述 */
    @JsonProperty("description")
    private String description;
    
    /** 總回合數 */
    @JsonProperty("totalRounds")
    private int totalRounds;
    
    /** 開始時間 */
    @JsonProperty("startTime")
    private long startTime;
    
    /** 結束時間 */
    @JsonProperty("endTime")
    private long endTime;
    
    /** 城牆剩餘耐久度 */
    @JsonProperty("wallDurabilityRemaining")
    private int wallDurabilityRemaining;
    
    /** 防守部隊剩餘數量 */
    @JsonProperty("defendersRemaining")
    private int defendersRemaining;
    
    /** 進攻方存活單位數 */
    @JsonProperty("attackerSurvivors")
    private int attackerSurvivors;
    
    /** 攻城日誌 */
    @JsonProperty("siegeLog")
    private List<String> siegeLog = new ArrayList<>();
    
    /** 攻城統計 */
    @JsonProperty("siegeStatistics")
    private SiegeStatistics siegeStatistics = new SiegeStatistics();

    /**
     * 獲取攻城持續時間（毫秒）
     */
    public long getDuration() {
        return endTime - startTime;
    }

    /**
     * 檢查是否為進攻方勝利
     */
    public boolean isAttackerWin() {
        return "ATTACKER_WIN".equals(result);
    }

    /**
     * 檢查是否為防守方勝利
     */
    public boolean isDefenderWin() {
        return "DEFENDER_WIN".equals(result);
    }

    /**
     * 檢查是否為平手
     */
    public boolean isDraw() {
        return "DRAW".equals(result);
    }

    /**
     * 檢查是否為城市地形
     */
    public boolean isCityTerrain() {
        return !"NOT_CITY".equals(result);
    }

    /**
     * 獲取勝利方
     */
    public String getWinner() {
        if (isAttackerWin()) return "ATTACKER";
        if (isDefenderWin()) return "DEFENDER";
        if (isDraw()) return "NONE";
        return "INVALID";
    }

    /**
     * 獲取城牆狀態描述
     */
    public String getWallStatusDescription() {
        if (wallDurabilityRemaining <= 0) {
            return "城牆已被攻破";
        } else if (wallDurabilityRemaining < 100) {
            return "城牆嚴重損壞";
        } else if (wallDurabilityRemaining < 500) {
            return "城牆中度損壞";
        } else {
            return "城牆輕微損壞";
        }
    }

    /**
     * 獲取防守部隊狀態描述
     */
    public String getDefenderStatusDescription() {
        if (defendersRemaining <= 0) {
            return "防守部隊全滅";
        } else if (defendersRemaining < 50) {
            return "防守部隊嚴重不足";
        } else if (defendersRemaining < 200) {
            return "防守部隊不足";
        } else {
            return "防守部隊充足";
        }
    }

    /**
     * 計算攻城效率評分
     */
    public double getSiegeEfficiency() {
        if (totalRounds <= 0) return 0.0;
        
        double roundEfficiency = Math.max(0, 1.0 - (totalRounds / 20.0)); // 回合數越少越好
        double damageEfficiency = wallDurabilityRemaining == 0 ? 1.0 : 
                                 Math.max(0, 1.0 - (wallDurabilityRemaining / 1000.0));
        double defenderEfficiency = defendersRemaining == 0 ? 1.0 : 
                                   Math.max(0, 1.0 - (defendersRemaining / 1000.0));
        
        return (roundEfficiency + damageEfficiency + defenderEfficiency) / 3.0;
    }
}
