package com.taco.TinyRealm.module.battleModule.service;

import com.taco.TinyRealm.module.soldierModule.model.PlayerSoldier;
import com.taco.TinyRealm.module.soldierModule.model.FormationPosition;
import com.taco.TinyRealm.module.terrainMapModule.model.Terrain;
import com.taco.TinyRealm.module.battleModule.model.BattleResult;
import com.taco.TinyRealm.module.battleModule.model.RoundResult;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 戰鬥計算器
 * 實現 Kingdom Clash Battle System 的混合制戰鬥邏輯
 */
public class BattleCalculator {
    
    private static final int MAX_ROUNDS = 50; // 最大回合數
    private static final double CRITICAL_HIT_CHANCE = 0.1; // 暴擊機率 10%
    private static final double CRITICAL_HIT_MULTIPLIER = 1.5; // 暴擊倍數
    
    /**
     * 執行戰鬥
     * @param armyA 進攻方部隊
     * @param armyB 防守方部隊
     * @param terrain 地形
     * @return 戰鬥結果
     */
    public BattleResult executeBattle(List<PlayerSoldier> armyA, List<PlayerSoldier> armyB, 
                                    Terrain terrain, boolean isSiege) {
        BattleResult result = new BattleResult();
        result.setStartTime(System.currentTimeMillis());
        
        // 初始化戰場
        BattleField battleField = initializeBattleField(armyA, armyB, terrain);
        
        int round = 0;
        while (round < MAX_ROUNDS && !isBattleEnded(battleField)) {
            round++;
            executeRound(battleField, round);
            
            // 記錄回合結果
            result.addRoundResult(createRoundResult(battleField, round));
        }
        
        // 計算最終結果
        calculateFinalResult(battleField, result);
        result.setEndTime(System.currentTimeMillis());
        result.setTotalRounds(round);
        
        return result;
    }
    
    /**
     * 初始化戰場
     */
    private BattleField initializeBattleField(List<PlayerSoldier> armyA, List<PlayerSoldier> armyB, Terrain terrain) {
        BattleField field = new BattleField();
        field.setTerrain(terrain);
        
        // 設置部隊位置
        setupArmyPositions(armyA, 0, 0, field.getArmyA());
        setupArmyPositions(armyB, 10, 0, field.getArmyB());
        
        // 套用地形修正
        applyTerrainModifiers(field);
        
        return field;
    }
    
    /**
     * 設置部隊位置
     */
    private void setupArmyPositions(List<PlayerSoldier> soldiers, int startX, int startY, 
                                   Map<FormationPosition, List<PlayerSoldier>> army) {
        // 按站位分組
        Map<FormationPosition, List<PlayerSoldier>> formationGroups = new HashMap<>();
        for (PlayerSoldier soldier : soldiers) {
            formationGroups.computeIfAbsent(soldier.getFormationPosition(), k -> new ArrayList<>()).add(soldier);
        }
        
        // 設置位置
        int x = startX;
        for (FormationPosition pos : Arrays.asList(FormationPosition.FRONT, FormationPosition.MIDDLE, FormationPosition.BACK)) {
            List<PlayerSoldier> group = formationGroups.get(pos);
            if (group != null) {
                for (PlayerSoldier soldier : group) {
                    soldier.setPositionX(x);
                    soldier.setPositionY(startY);
                    x += 2; // 每個單位間隔2格
                }
            }
        }
        
        // 保存到戰場
        army.putAll(formationGroups);
    }
    
    /**
     * 套用地形修正
     */
    private void applyTerrainModifiers(BattleField field) {
        Terrain terrain = field.getTerrain();
        if (terrain == null) return;
        
        // 套用到所有部隊
        applyTerrainToArmy(field.getArmyA(), terrain);
        applyTerrainToArmy(field.getArmyB(), terrain);
    }
    
    /**
     * 套用地形修正到部隊
     */
    private void applyTerrainToArmy(Map<FormationPosition, List<PlayerSoldier>> army, Terrain terrain) {
        for (List<PlayerSoldier> soldiers : army.values()) {
            for (PlayerSoldier soldier : soldiers) {
                // 地形已經在 Terrain 類中處理，這裡主要是記錄
                soldier.setPositionX(soldier.getPositionX());
                soldier.setPositionY(soldier.getPositionY());
            }
        }
    }
    
    /**
     * 執行一個回合
     */
    private void executeRound(BattleField field, int round) {
        // 1. 遠程單位射擊
        executeRangedAttack(field, round);
        
        // 2. 近戰單位移動和攻擊
        executeMeleeMovementAndAttack(field, round);
        
        // 3. 清理死亡單位
        cleanupDeadUnits(field);
    }
    
    /**
     * 執行遠程攻擊
     */
    private void executeRangedAttack(BattleField field, int round) {
        // 進攻方遠程攻擊
        executeRangedAttackForArmy(field.getArmyA(), field.getArmyB(), field.getTerrain(), round);
        
        // 防守方遠程攻擊
        executeRangedAttackForArmy(field.getArmyB(), field.getArmyA(), field.getTerrain(), round);
    }
    
    /**
     * 執行部隊的遠程攻擊
     */
    private void executeRangedAttackForArmy(Map<FormationPosition, List<PlayerSoldier>> attackers,
                                          Map<FormationPosition, List<PlayerSoldier>> defenders,
                                          Terrain terrain, int round) {
        // 按站位順序攻擊（後排優先）
        for (FormationPosition pos : Arrays.asList(FormationPosition.BACK, FormationPosition.MIDDLE, FormationPosition.FRONT)) {
            List<PlayerSoldier> rangedUnits = attackers.get(pos);
            if (rangedUnits != null) {
                for (PlayerSoldier attacker : rangedUnits) {
                    if (attacker.isAlive() && attacker.isRanged()) {
                        PlayerSoldier target = findRangedTarget(attacker, defenders, terrain);
                        if (target != null) {
                            executeAttack(attacker, target, terrain, true);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 尋找遠程攻擊目標
     */
    private PlayerSoldier findRangedTarget(PlayerSoldier attacker, 
                                         Map<FormationPosition, List<PlayerSoldier>> defenders,
                                         Terrain terrain) {
        PlayerSoldier bestTarget = null;
        double bestPriority = Double.MAX_VALUE;
        
        // 按站位優先級尋找目標
        for (FormationPosition pos : Arrays.asList(FormationPosition.FRONT, FormationPosition.MIDDLE, FormationPosition.BACK)) {
            List<PlayerSoldier> defendersInPosition = defenders.get(pos);
            if (defendersInPosition != null) {
                for (PlayerSoldier defender : defendersInPosition) {
                    if (defender.isAlive() && attacker.isInRange(defender.getPositionX(), defender.getPositionY())) {
                        double priority = pos.getDefensePriority();
                        if (priority < bestPriority) {
                            bestPriority = priority;
                            bestTarget = defender;
                        }
                    }
                }
            }
        }
        
        return bestTarget;
    }
    
    /**
     * 執行近戰移動和攻擊
     */
    private void executeMeleeMovementAndAttack(BattleField field, int round) {
        // 進攻方移動和攻擊
        executeMeleeForArmy(field.getArmyA(), field.getArmyB(), field.getTerrain(), round);
        
        // 防守方移動和攻擊
        executeMeleeForArmy(field.getArmyB(), field.getArmyA(), field.getTerrain(), round);
    }
    
    /**
     * 執行部隊的近戰邏輯
     */
    private void executeMeleeForArmy(Map<FormationPosition, List<PlayerSoldier>> attackers,
                                   Map<FormationPosition, List<PlayerSoldier>> defenders,
                                   Terrain terrain, int round) {
        // 按站位順序移動和攻擊（前排優先）
        for (FormationPosition pos : Arrays.asList(FormationPosition.FRONT, FormationPosition.MIDDLE, FormationPosition.BACK)) {
            List<PlayerSoldier> meleeUnits = attackers.get(pos);
            if (meleeUnits != null) {
                for (PlayerSoldier attacker : meleeUnits) {
                    if (attacker.isAlive() && attacker.isMelee()) {
                        // 移動
                        moveTowardsEnemy(attacker, defenders, terrain);
                        
                        // 攻擊
                        PlayerSoldier target = findMeleeTarget(attacker, defenders, terrain);
                        if (target != null) {
                            executeAttack(attacker, target, terrain, false);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 移動向敵人
     */
    private void moveTowardsEnemy(PlayerSoldier attacker, 
                                Map<FormationPosition, List<PlayerSoldier>> defenders,
                                Terrain terrain) {
        PlayerSoldier nearestEnemy = findNearestEnemy(attacker, defenders);
        if (nearestEnemy != null) {
            double distance = attacker.getDistanceTo(nearestEnemy.getPositionX(), nearestEnemy.getPositionY());
            if (distance > 1.0) { // 如果不在攻擊範圍內
                int effectiveSpeed = (int)(attacker.getEffectiveSpeed() * terrain.getSpeedModifier());
                if (effectiveSpeed > 0) {
                    // 簡單的移動邏輯：向敵人方向移動
                    int deltaX = Integer.compare(nearestEnemy.getPositionX(), attacker.getPositionX());
                    int deltaY = Integer.compare(nearestEnemy.getPositionY(), attacker.getPositionY());
                    
                    attacker.setPositionX(attacker.getPositionX() + deltaX * Math.min(effectiveSpeed, 1));
                    attacker.setPositionY(attacker.getPositionY() + deltaY * Math.min(effectiveSpeed, 1));
                }
            }
        }
    }
    
    /**
     * 尋找最近的敵人
     */
    private PlayerSoldier findNearestEnemy(PlayerSoldier attacker, 
                                         Map<FormationPosition, List<PlayerSoldier>> defenders) {
        PlayerSoldier nearest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (List<PlayerSoldier> defendersInPosition : defenders.values()) {
            for (PlayerSoldier defender : defendersInPosition) {
                if (defender.isAlive()) {
                    double distance = attacker.getDistanceTo(defender.getPositionX(), defender.getPositionY());
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = defender;
                    }
                }
            }
        }
        
        return nearest;
    }
    
    /**
     * 尋找近戰攻擊目標
     */
    private PlayerSoldier findMeleeTarget(PlayerSoldier attacker, 
                                        Map<FormationPosition, List<PlayerSoldier>> defenders,
                                        Terrain terrain) {
        // 檢查是否在攻擊範圍內
        for (FormationPosition pos : Arrays.asList(FormationPosition.FRONT, FormationPosition.MIDDLE, FormationPosition.BACK)) {
            List<PlayerSoldier> defendersInPosition = defenders.get(pos);
            if (defendersInPosition != null) {
                for (PlayerSoldier defender : defendersInPosition) {
                    if (defender.isAlive() && attacker.getDistanceTo(defender.getPositionX(), defender.getPositionY()) <= 1.0) {
                        return defender;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * 執行攻擊
     */
    private void executeAttack(PlayerSoldier attacker, PlayerSoldier defender, 
                             Terrain terrain, boolean isRanged) {
        // 計算攻擊力
        int attackPower = attacker.getEffectiveAttack();
        
        // 計算防禦力（考慮地形修正）
        int defensePower = (int)(defender.getEffectiveDefense() * terrain.getDefenseModifier());
        
        // 計算傷害
        int damage = Math.max(1, attackPower - defensePower);
        
        // 暴擊判定
        if (ThreadLocalRandom.current().nextDouble() < CRITICAL_HIT_CHANCE) {
            damage = (int)(damage * CRITICAL_HIT_MULTIPLIER);
        }
        
        // 應用傷害
        defender.takeDamage(damage);
        
        // 記錄攻擊日誌
        // 這裡可以添加到戰鬥日誌中
    }
    
    /**
     * 清理死亡單位
     */
    private void cleanupDeadUnits(BattleField field) {
        cleanupDeadUnitsFromArmy(field.getArmyA());
        cleanupDeadUnitsFromArmy(field.getArmyB());
    }
    
    /**
     * 清理部隊中的死亡單位
     */
    private void cleanupDeadUnitsFromArmy(Map<FormationPosition, List<PlayerSoldier>> army) {
        for (List<PlayerSoldier> soldiers : army.values()) {
            soldiers.removeIf(soldier -> !soldier.isAlive());
        }
    }
    
    /**
     * 檢查戰鬥是否結束
     */
    private boolean isBattleEnded(BattleField field) {
        return isArmyDefeated(field.getArmyA()) || isArmyDefeated(field.getArmyB());
    }
    
    /**
     * 檢查部隊是否被擊敗
     */
    private boolean isArmyDefeated(Map<FormationPosition, List<PlayerSoldier>> army) {
        for (List<PlayerSoldier> soldiers : army.values()) {
            for (PlayerSoldier soldier : soldiers) {
                if (soldier.isAlive()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 計算最終結果
     */
    private void calculateFinalResult(BattleField field, BattleResult result) {
        boolean armyADefeated = isArmyDefeated(field.getArmyA());
        boolean armyBDefeated = isArmyDefeated(field.getArmyB());
        
        if (armyADefeated && armyBDefeated) {
            result.setResult("DRAW");
        } else if (armyADefeated) {
            result.setResult("DEFENDER_WIN");
        } else if (armyBDefeated) {
            result.setResult("ATTACKER_WIN");
        } else {
            result.setResult("UNKNOWN");
        }
        
        // 計算存活單位統計
        result.setAttackerSurvivors(countSurvivors(field.getArmyA()));
        result.setDefenderSurvivors(countSurvivors(field.getArmyB()));
    }
    
    /**
     * 計算存活單位數量
     */
    private int countSurvivors(Map<FormationPosition, List<PlayerSoldier>> army) {
        int count = 0;
        for (List<PlayerSoldier> soldiers : army.values()) {
            for (PlayerSoldier soldier : soldiers) {
                if (soldier.isAlive()) {
                    count += soldier.getCount();
                }
            }
        }
        return count;
    }
    
    /**
     * 創建回合結果
     */
    private RoundResult createRoundResult(BattleField field, int round) {
        RoundResult roundResult = new RoundResult();
        roundResult.setRound(round);
        roundResult.setAttackerUnits(countTotalUnits(field.getArmyA()));
        roundResult.setDefenderUnits(countTotalUnits(field.getArmyB()));
        return roundResult;
    }
    
    /**
     * 計算總單位數量
     */
    private int countTotalUnits(Map<FormationPosition, List<PlayerSoldier>> army) {
        int count = 0;
        for (List<PlayerSoldier> soldiers : army.values()) {
            for (PlayerSoldier soldier : soldiers) {
                count += soldier.getCount();
            }
        }
        return count;
    }
    
    // 內部類別
    private static class BattleField {
        private Terrain terrain;
        private Map<FormationPosition, List<PlayerSoldier>> armyA = new HashMap<>();
        private Map<FormationPosition, List<PlayerSoldier>> armyB = new HashMap<>();
        
        // Getters and Setters
        public Terrain getTerrain() { return terrain; }
        public void setTerrain(Terrain terrain) { this.terrain = terrain; }
        public Map<FormationPosition, List<PlayerSoldier>> getArmyA() { return armyA; }
        public Map<FormationPosition, List<PlayerSoldier>> getArmyB() { return armyB; }
    }
}
