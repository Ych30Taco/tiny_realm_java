package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 戰鬥結果模型
 * 記錄戰鬥的詳細結果和統計信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleResult {
    
    /** 戰鬥ID */
    @JsonProperty("id")
    private String id;
    
    /** 戰鬥結果 */
    @JsonProperty("result")
    private String result; // ATTACKER_WIN, DEFENDER_WIN, DRAW
    
    /** 總回合數 */
    @JsonProperty("totalRounds")
    private int totalRounds;
    
    /** 開始時間 */
    @JsonProperty("startTime")
    private long startTime;
    
    /** 結束時間 */
    @JsonProperty("endTime")
    private long endTime;
    
    /** 進攻方存活單位數 */
    @JsonProperty("attackerSurvivors")
    private int attackerSurvivors;
    
    /** 防守方存活單位數 */
    @JsonProperty("defenderSurvivors")
    private int defenderSurvivors;
    
    /** 回合結果列表 */
    @JsonProperty("roundResults")
    private List<RoundResult> roundResults = new ArrayList<>();
    
    /** 戰鬥統計 */
    @JsonProperty("statistics")
    private BattleStatistics statistics = new BattleStatistics();
    
    /** 戰鬥日誌 */
    @JsonProperty("battleLog")
    private List<BattleLogEntry> battleLog = new ArrayList<>();

    /**
     * 添加回合結果
     */
    public void addRoundResult(RoundResult roundResult) {
        roundResults.add(roundResult);
    }

    /**
     * 添加戰鬥日誌
     */
    public void addBattleLog(BattleLogEntry logEntry) {
        battleLog.add(logEntry);
    }

    /**
     * 獲取戰鬥持續時間（毫秒）
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
     * 獲取勝利方
     */
    public String getWinner() {
        if (isAttackerWin()) return "ATTACKER";
        if (isDefenderWin()) return "DEFENDER";
        return "NONE";
    }
}
