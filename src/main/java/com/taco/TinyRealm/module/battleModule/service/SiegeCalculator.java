package com.taco.TinyRealm.module.battleModule.service;

import com.taco.TinyRealm.module.soldierModule.model.PlayerSoldier;
import com.taco.TinyRealm.module.soldierModule.model.UnitType;
import com.taco.TinyRealm.module.terrainMapModule.model.Terrain;
import com.taco.TinyRealm.module.terrainMapModule.model.TerrainType;
import com.taco.TinyRealm.module.battleModule.model.SiegeResult;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

/**
 * 攻城計算器
 * 實現主城攻擊/防禦計算邏輯
 */
public class SiegeCalculator {
    
    private static final double WALL_DEFENSE_BONUS = 2.0; // 城牆防禦加成
    private static final double CITY_DEFENSE_BONUS = 1.5; // 城市防禦加成
    private static final double SIEGE_DAMAGE_MULTIPLIER = 3.0; // 攻城器對城牆傷害倍數
    
    /**
     * 執行攻城戰鬥
     * @param attackers 進攻方部隊
     * @param cityTerrain 城市地形
     * @return 攻城結果
     */
    public SiegeResult siegeAttack(List<PlayerSoldier> attackers, Terrain cityTerrain) {
        SiegeResult result = new SiegeResult();
        result.setStartTime(System.currentTimeMillis());
        
        // 檢查是否為城市地形
        if (!isCityTerrain(cityTerrain)) {
            result.setResult("NOT_CITY");
            result.setDescription("目標不是城市，無法進行攻城戰鬥");
            return result;
        }
        
        // 初始化攻城狀態
        SiegeState siegeState = initializeSiegeState(attackers, cityTerrain);
        
        // 執行攻城回合
        int round = 0;
        while (round < 20 && !isSiegeEnded(siegeState)) { // 最多20回合
            round++;
            executeSiegeRound(siegeState, round);
        }
        
        // 計算最終結果
        calculateSiegeResult(siegeState, result);
        result.setEndTime(System.currentTimeMillis());
        result.setTotalRounds(round);
        
        return result;
    }
    
    /**
     * 檢查是否為城市地形
     */
    private boolean isCityTerrain(Terrain terrain) {
        return terrain != null && (terrain.getTerrainType() == TerrainType.CITY || 
                                 terrain.getTerrainType() == TerrainType.WALL);
    }
    
    /**
     * 初始化攻城狀態
     */
    private SiegeState initializeSiegeState(List<PlayerSoldier> attackers, Terrain cityTerrain) {
        SiegeState state = new SiegeState();
        state.setCityTerrain(cityTerrain);
        state.setAttackers(attackers);
        
        // 設置城牆耐久度
        if (cityTerrain.getTerrainType() == TerrainType.WALL) {
            state.setWallDurability(cityTerrain.getDurability());
            state.setMaxWallDurability(cityTerrain.getMaxDurability());
        }
        
        // 設置城市防禦部隊（模擬）
        state.setDefenderCount(calculateDefenderCount(cityTerrain));
        
        return state;
    }
    
    /**
     * 計算防守部隊數量
     */
    private int calculateDefenderCount(Terrain cityTerrain) {
        // 根據城市等級和地形類型計算防守部隊數量
        int baseDefenders = 100;
        int levelMultiplier = cityTerrain.getLevel();
        
        if (cityTerrain.getTerrainType() == TerrainType.WALL) {
            baseDefenders = 200; // 城牆有更多防守部隊
        }
        
        return baseDefenders * levelMultiplier;
    }
    
    /**
     * 執行攻城回合
     */
    private void executeSiegeRound(SiegeState state, int round) {
        // 1. 攻城器攻擊城牆
        if (state.getWallDurability() > 0) {
            executeWallAttack(state, round);
        }
        
        // 2. 其他部隊攻擊防守部隊
        if (state.getDefenderCount() > 0) {
            executeDefenderAttack(state, round);
        }
        
        // 3. 防守部隊反擊
        if (state.getDefenderCount() > 0) {
            executeDefenderCounterAttack(state, round);
        }
        
        // 4. 更新狀態
        updateSiegeState(state);
    }
    
    /**
     * 執行城牆攻擊
     */
    private void executeWallAttack(SiegeState state, int round) {
        List<PlayerSoldier> siegeUnits = state.getAttackers().stream()
            .filter(soldier -> soldier.isSiege())
            .toList();
        
        for (PlayerSoldier siegeUnit : siegeUnits) {
            if (siegeUnit.isAlive()) {
                // 攻城器對城牆造成額外傷害
                int damage = (int)(siegeUnit.getEffectiveAttack() * SIEGE_DAMAGE_MULTIPLIER);
                state.setWallDurability(Math.max(0, state.getWallDurability() - damage));
                
                // 記錄攻擊日誌
                state.addSiegeLog(String.format("回合 %d: 攻城器 %s 攻擊城牆，造成 %d 點傷害", 
                                              round, siegeUnit.getName(), damage));
                
                if (state.getWallDurability() <= 0) {
                    state.addSiegeLog("回合 " + round + ": 城牆被攻破！");
                    break;
                }
            }
        }
    }
    
    /**
     * 執行防守部隊攻擊
     */
    private void executeDefenderAttack(SiegeState state, int round) {
        List<PlayerSoldier> nonSiegeUnits = state.getAttackers().stream()
            .filter(soldier -> !soldier.isSiege())
            .toList();
        
        for (PlayerSoldier attacker : nonSiegeUnits) {
            if (attacker.isAlive() && state.getDefenderCount() > 0) {
                // 計算對防守部隊的傷害
                int damage = calculateDefenderDamage(attacker, state.getCityTerrain());
                int defendersKilled = Math.min(damage / 10, state.getDefenderCount()); // 每10點傷害殺死1個防守部隊
                
                state.setDefenderCount(Math.max(0, state.getDefenderCount() - defendersKilled));
                
                // 記錄攻擊日誌
                state.addSiegeLog(String.format("回合 %d: %s 攻擊防守部隊，殺死 %d 個防守部隊", 
                                              round, attacker.getName(), defendersKilled));
                
                if (state.getDefenderCount() <= 0) {
                    state.addSiegeLog("回合 " + round + ": 所有防守部隊被消滅！");
                    break;
                }
            }
        }
    }
    
    /**
     * 執行防守部隊反擊
     */
    private void executeDefenderCounterAttack(SiegeState state, int round) {
        if (state.getDefenderCount() <= 0) return;
        
        // 防守部隊反擊進攻方
        int totalDefenderDamage = state.getDefenderCount() * 5; // 每個防守部隊造成5點傷害
        
        // 隨機選擇進攻方單位進行攻擊
        List<PlayerSoldier> aliveAttackers = state.getAttackers().stream()
            .filter(PlayerSoldier::isAlive)
            .toList();
        
        if (!aliveAttackers.isEmpty()) {
            PlayerSoldier target = aliveAttackers.get(ThreadLocalRandom.current().nextInt(aliveAttackers.size()));
            int damage = Math.min(totalDefenderDamage, target.getHp());
            
            target.takeDamage(damage);
            
            state.addSiegeLog(String.format("回合 %d: 防守部隊反擊，對 %s 造成 %d 點傷害", 
                                          round, target.getName(), damage));
        }
    }
    
    /**
     * 計算對防守部隊的傷害
     */
    private int calculateDefenderDamage(PlayerSoldier attacker, Terrain cityTerrain) {
        int baseDamage = attacker.getEffectiveAttack();
        
        // 考慮地形防禦修正
        double defenseModifier = cityTerrain.getDefenseModifier();
        int finalDamage = (int)(baseDamage / defenseModifier);
        
        // 添加隨機因素
        double randomFactor = 0.8 + ThreadLocalRandom.current().nextDouble() * 0.4; // 0.8-1.2
        
        return Math.max(1, (int)(finalDamage * randomFactor));
    }
    
    /**
     * 更新攻城狀態
     */
    private void updateSiegeState(SiegeState state) {
        // 清理死亡單位
        state.getAttackers().removeIf(soldier -> !soldier.isAlive());
        
        // 更新城牆狀態
        if (state.getWallDurability() <= 0 && state.getMaxWallDurability() > 0) {
            state.setWallDestroyed(true);
        }
    }
    
    /**
     * 檢查攻城是否結束
     */
    private boolean isSiegeEnded(SiegeState state) {
        // 進攻方全滅
        if (state.getAttackers().stream().noneMatch(PlayerSoldier::isAlive)) {
            return true;
        }
        
        // 防守方全滅且城牆被攻破
        if (state.getDefenderCount() <= 0 && state.isWallDestroyed()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 計算攻城結果
     */
    private void calculateSiegeResult(SiegeState state, SiegeResult result) {
        boolean attackersDefeated = state.getAttackers().stream().noneMatch(PlayerSoldier::isAlive);
        boolean defendersDefeated = state.getDefenderCount() <= 0 && state.isWallDestroyed();
        
        if (attackersDefeated) {
            result.setResult("DEFENDER_WIN");
            result.setDescription("進攻方被擊敗，防守方勝利");
        } else if (defendersDefeated) {
            result.setResult("ATTACKER_WIN");
            result.setDescription("防守方被擊敗，進攻方勝利");
        } else {
            result.setResult("DRAW");
            result.setDescription("攻城戰鬥平手");
        }
        
        // 設置統計信息
        result.setWallDurabilityRemaining(state.getWallDurability());
        result.setDefendersRemaining(state.getDefenderCount());
        result.setAttackerSurvivors(state.getAttackers().stream()
            .filter(PlayerSoldier::isAlive)
            .mapToInt(PlayerSoldier::getCount)
            .sum());
    }
    
    /**
     * 攻城狀態內部類
     */
    private static class SiegeState {
        private Terrain cityTerrain;
        private List<PlayerSoldier> attackers;
        private int wallDurability;
        private int maxWallDurability;
        private int defenderCount;
        private boolean wallDestroyed = false;
        private List<String> siegeLog = new ArrayList<>();
        
        // Getters and Setters
        public Terrain getCityTerrain() { return cityTerrain; }
        public void setCityTerrain(Terrain cityTerrain) { this.cityTerrain = cityTerrain; }
        
        public List<PlayerSoldier> getAttackers() { return attackers; }
        public void setAttackers(List<PlayerSoldier> attackers) { this.attackers = attackers; }
        
        public int getWallDurability() { return wallDurability; }
        public void setWallDurability(int wallDurability) { this.wallDurability = wallDurability; }
        
        public int getMaxWallDurability() { return maxWallDurability; }
        public void setMaxWallDurability(int maxWallDurability) { this.maxWallDurability = maxWallDurability; }
        
        public int getDefenderCount() { return defenderCount; }
        public void setDefenderCount(int defenderCount) { this.defenderCount = defenderCount; }
        
        public boolean isWallDestroyed() { return wallDestroyed; }
        public void setWallDestroyed(boolean wallDestroyed) { this.wallDestroyed = wallDestroyed; }
        
        public List<String> getSiegeLog() { return siegeLog; }
        public void addSiegeLog(String log) { siegeLog.add(log); }
    }
}
