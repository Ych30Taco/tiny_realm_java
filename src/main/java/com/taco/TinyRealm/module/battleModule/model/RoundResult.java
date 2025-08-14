package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 回合結果模型
 * 記錄每個回合的狀態和統計信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundResult {
    
    /** 回合數 */
    @JsonProperty("round")
    private int round;
    
    /** 進攻方單位數量 */
    @JsonProperty("attackerUnits")
    private int attackerUnits;
    
    /** 防守方單位數量 */
    @JsonProperty("defenderUnits")
    private int defenderUnits;
    
    /** 進攻方損失 */
    @JsonProperty("attackerLosses")
    private int attackerLosses;
    
    /** 防守方損失 */
    @JsonProperty("defenderLosses")
    private int defenderLosses;
    
    /** 回合持續時間（毫秒） */
    @JsonProperty("duration")
    private long duration;
    
    /** 回合描述 */
    @JsonProperty("description")
    private String description;
    
    /** 特殊事件 */
    @JsonProperty("specialEvents")
    private List<String> specialEvents = new ArrayList<>();

    /**
     * 添加特殊事件
     */
    public void addSpecialEvent(String event) {
        specialEvents.add(event);
    }

    /**
     * 計算進攻方存活率
     */
    public double getAttackerSurvivalRate() {
        if (attackerUnits <= 0) return 0.0;
        return (double) (attackerUnits - attackerLosses) / attackerUnits;
    }

    /**
     * 計算防守方存活率
     */
    public double getDefenderSurvivalRate() {
        if (defenderUnits <= 0) return 0.0;
        return (double) (defenderUnits - defenderLosses) / defenderUnits;
    }

    /**
     * 檢查是否為關鍵回合
     */
    public boolean isCriticalRound() {
        return attackerLosses > attackerUnits * 0.3 || defenderLosses > defenderUnits * 0.3;
    }
}
