package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 戰鬥統計模型
 * 記錄戰鬥的詳細統計信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleStatistics {
    
    /** 總傷害輸出 */
    @JsonProperty("totalDamageDealt")
    private int totalDamageDealt;
    
    /** 總傷害承受 */
    @JsonProperty("totalDamageTaken")
    private int totalDamageTaken;
    
    /** 暴擊次數 */
    @JsonProperty("criticalHits")
    private int criticalHits;
    
    /** 總攻擊次數 */
    @JsonProperty("totalAttacks")
    private int totalAttacks;
    
    /** 暴擊率 */
    @JsonProperty("criticalHitRate")
    private double criticalHitRate;
    
    /** 平均傷害 */
    @JsonProperty("averageDamage")
    private double averageDamage;
    
    /** 最大單次傷害 */
    @JsonProperty("maxDamage")
    private int maxDamage;
    
    /** 最小單次傷害 */
    @JsonProperty("minDamage")
    private int minDamage;
    
    /** 遠程攻擊次數 */
    @JsonProperty("rangedAttacks")
    private int rangedAttacks;
    
    /** 近戰攻擊次數 */
    @JsonProperty("meleeAttacks")
    private int meleeAttacks;
    
    /** 移動總距離 */
    @JsonProperty("totalMovement")
    private int totalMovement;
    
    /** 地形修正統計 */
    @JsonProperty("terrainModifiers")
    private Map<String, Integer> terrainModifiers = new HashMap<>();
    
    /** 單位類型統計 */
    @JsonProperty("unitTypeStats")
    private Map<String, UnitTypeStats> unitTypeStats = new HashMap<>();

    /**
     * 添加傷害統計
     */
    public void addDamage(int damage, boolean isCritical) {
        totalDamageDealt += damage;
        totalAttacks++;
        
        if (isCritical) {
            criticalHits++;
        }
        
        if (damage > maxDamage) {
            maxDamage = damage;
        }
        
        if (minDamage == 0 || damage < minDamage) {
            minDamage = damage;
        }
        
        updateStatistics();
    }

    /**
     * 添加攻擊類型統計
     */
    public void addAttackType(boolean isRanged) {
        if (isRanged) {
            rangedAttacks++;
        } else {
            meleeAttacks++;
        }
    }

    /**
     * 添加移動距離
     */
    public void addMovement(int distance) {
        totalMovement += distance;
    }

    /**
     * 添加地形修正統計
     */
    public void addTerrainModifier(String terrainType) {
        terrainModifiers.merge(terrainType, 1, Integer::sum);
    }

    /**
     * 添加單位類型統計
     */
    public void addUnitTypeStats(String unitType, int kills, int damage) {
        unitTypeStats.computeIfAbsent(unitType, k -> new UnitTypeStats())
                    .addStats(kills, damage);
    }

    /**
     * 更新統計數據
     */
    private void updateStatistics() {
        if (totalAttacks > 0) {
            criticalHitRate = (double) criticalHits / totalAttacks;
            averageDamage = (double) totalDamageDealt / totalAttacks;
        }
    }

    /**
     * 獲取戰鬥效率評分
     */
    public double getBattleEfficiency() {
        if (totalAttacks == 0) return 0.0;
        
        double damageEfficiency = (double) totalDamageDealt / totalAttacks;
        double criticalEfficiency = criticalHitRate * 1.5;
        double movementEfficiency = Math.min(totalMovement / 100.0, 1.0);
        
        return (damageEfficiency + criticalEfficiency + movementEfficiency) / 3.0;
    }

    /**
     * 單位類型統計內部類
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitTypeStats {
        private int kills;
        private int damage;
        private int attacks;
        
        public void addStats(int kills, int damage) {
            this.kills += kills;
            this.damage += damage;
            this.attacks++;
        }
        
        public double getAverageDamage() {
            return attacks > 0 ? (double) damage / attacks : 0.0;
        }
        
        public double getKillRate() {
            return attacks > 0 ? (double) kills / attacks : 0.0;
        }
    }
}
