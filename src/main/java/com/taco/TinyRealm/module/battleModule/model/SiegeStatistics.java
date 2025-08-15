package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 攻城統計模型
 * 記錄攻城戰鬥的詳細統計信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiegeStatistics {
    
    /** 對城牆總傷害 */
    @JsonProperty("totalWallDamage")
    private int totalWallDamage;
    
    /** 對防守部隊總傷害 */
    @JsonProperty("totalDefenderDamage")
    private int totalDefenderDamage;
    
    /** 進攻方承受總傷害 */
    @JsonProperty("totalAttackerDamage")
    private int totalAttackerDamage;
    
    /** 攻城器攻擊次數 */
    @JsonProperty("siegeAttacks")
    private int siegeAttacks;
    
    /** 其他部隊攻擊次數 */
    @JsonProperty("otherAttacks")
    private int otherAttacks;
    
    /** 防守部隊反擊次數 */
    @JsonProperty("defenderCounterAttacks")
    private int defenderCounterAttacks;
    
    /** 城牆破壞進度百分比 */
    @JsonProperty("wallDestructionProgress")
    private double wallDestructionProgress;
    
    /** 防守部隊消滅進度百分比 */
    @JsonProperty("defenderEliminationProgress")
    private double defenderEliminationProgress;
    
    /** 單位類型統計 */
    @JsonProperty("unitTypeStats")
    private Map<String, SiegeUnitStats> unitTypeStats = new HashMap<>();
    
    /** 回合效率統計 */
    @JsonProperty("roundEfficiency")
    private Map<Integer, RoundEfficiency> roundEfficiency = new HashMap<>();

    /**
     * 添加城牆傷害
     */
    public void addWallDamage(int damage) {
        totalWallDamage += damage;
        siegeAttacks++;
    }

    /**
     * 添加防守部隊傷害
     */
    public void addDefenderDamage(int damage) {
        totalDefenderDamage += damage;
        otherAttacks++;
    }

    /**
     * 添加進攻方傷害
     */
    public void addAttackerDamage(int damage) {
        totalAttackerDamage += damage;
        defenderCounterAttacks++;
    }

    /**
     * 更新城牆破壞進度
     */
    public void updateWallDestructionProgress(int currentDurability, int maxDurability) {
        if (maxDurability > 0) {
            wallDestructionProgress = (double) (maxDurability - currentDurability) / maxDurability * 100.0;
        }
    }

    /**
     * 更新防守部隊消滅進度
     */
    public void updateDefenderEliminationProgress(int currentDefenders, int initialDefenders) {
        if (initialDefenders > 0) {
            defenderEliminationProgress = (double) (initialDefenders - currentDefenders) / initialDefenders * 100.0;
        }
    }

    /**
     * 添加單位類型統計
     */
    public void addUnitTypeStats(String unitType, int wallDamage, int defenderDamage, int attacks) {
        unitTypeStats.computeIfAbsent(unitType, k -> new SiegeUnitStats())
                    .addStats(wallDamage, defenderDamage, attacks);
    }

    /**
     * 添加回合效率統計
     */
    public void addRoundEfficiency(int round, int wallDamage, int defenderDamage, int attackerDamage) {
        RoundEfficiency efficiency = new RoundEfficiency();
        efficiency.setRound(round);
        efficiency.setWallDamage(wallDamage);
        efficiency.setDefenderDamage(defenderDamage);
        efficiency.setAttackerDamage(attackerDamage);
        efficiency.setTotalDamage(wallDamage + defenderDamage + attackerDamage);
        
        roundEfficiency.put(round, efficiency);
    }

    /**
     * 獲取總攻擊次數
     */
    public int getTotalAttacks() {
        return siegeAttacks + otherAttacks;
    }

    /**
     * 獲取平均每回合傷害
     */
    public double getAverageDamagePerRound() {
        int totalRounds = roundEfficiency.size();
        if (totalRounds == 0) return 0.0;
        
        return (double) (totalWallDamage + totalDefenderDamage) / totalRounds;
    }

    /**
     * 獲取攻城效率評分
     */
    public double getSiegeEfficiency() {
        if (getTotalAttacks() == 0) return 0.0;
        
        double damageEfficiency = (double) (totalWallDamage + totalDefenderDamage) / getTotalAttacks();
        double progressEfficiency = (wallDestructionProgress + defenderEliminationProgress) / 200.0; // 0-100%
        double survivalEfficiency = Math.max(0, 1.0 - (totalAttackerDamage / 1000.0)); // 進攻方存活率
        
        return (damageEfficiency + progressEfficiency + survivalEfficiency) / 3.0;
    }

    /**
     * 攻城單位統計內部類
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SiegeUnitStats {
        private int wallDamage;
        private int defenderDamage;
        private int attacks;
        
        public void addStats(int wallDamage, int defenderDamage, int attacks) {
            this.wallDamage += wallDamage;
            this.defenderDamage += defenderDamage;
            this.attacks += attacks;
        }
        
        public double getAverageDamage() {
            return attacks > 0 ? (double) (wallDamage + defenderDamage) / attacks : 0.0;
        }
        
        public double getWallDamageRatio() {
            int totalDamage = wallDamage + defenderDamage;
            return totalDamage > 0 ? (double) wallDamage / totalDamage : 0.0;
        }
    }

    /**
     * 回合效率內部類
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoundEfficiency {
        private int round;
        private int wallDamage;
        private int defenderDamage;
        private int attackerDamage;
        private int totalDamage;
        
        public double getEfficiency() {
            return totalDamage > 0 ? (double) (wallDamage + defenderDamage) / totalDamage : 0.0;
        }
        
        public boolean isHighEfficiency() {
            return getEfficiency() > 0.7; // 70%以上效率為高效率
        }
    }
}
