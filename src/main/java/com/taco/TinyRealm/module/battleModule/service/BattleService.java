package com.taco.TinyRealm.module.battleModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.battleModule.model.Battle;
import com.taco.TinyRealm.module.battleModule.model.EnemyType;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
//import com.taco.TinyRealm.module.soldierModule.model.Unit;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;
import com.taco.TinyRealm.service.EventService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 戰鬥服務
 * 處理戰鬥相關的業務邏輯，包括戰鬥開始、結果計算、獎勵發放等
 */
@Service
public class BattleService {
    
    @Autowired
    private StorageService storageService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private TerrainMapService terrainMapService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("${app.data.enemies-path:classpath:config/enemies.json}")
    private org.springframework.core.io.Resource enemiesPath;
    
    /** 敵人類型配置 */
    private Map<String, EnemyType> enemyTypes = new HashMap<>();
    
    /** 戰鬥統計 */
    private Map<String, Object> battleStatistics = new HashMap<>();

    /**
     * 初始化服務，載入敵人類型配置
     */
    @PostConstruct
    public void init() {
        try {
            loadEnemyTypes(enemiesPath);
            System.out.println("BattleService initialized with " + enemyTypes.size() + " enemy types");
        } catch (Exception e) {
            System.err.println("Failed to initialize BattleService: " + e.getMessage());
        }
    }

    /**
     * 載入敵人類型配置
     * @param resource 配置文件的資源
     * @throws IOException 載入失敗時拋出異常
     */
    private void loadEnemyTypes(org.springframework.core.io.Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, EnemyType> loaded = mapper.readValue(is, new TypeReference<Map<String, EnemyType>>() {});
            enemyTypes.clear();
            enemyTypes.putAll(loaded);
        }
    }

    /**
     * 重新載入敵人類型配置
     * @param overridePath 覆蓋預設路徑的配置文件路徑
     * @throws IOException 載入失敗時拋出異常
     */
    public void reloadEnemies(String overridePath) throws IOException {
        org.springframework.core.io.Resource target = enemiesPath;
        if (overridePath != null && !overridePath.isBlank()) {
            target = resourceLoader.getResource(overridePath);
        }
        loadEnemyTypes(target);
    }

    /**
     * 開始戰鬥
     * @param playerId 玩家ID
     * @param unitIds 參戰單位ID列表
     * @param enemyType 敵人類型
     * @param locationX 戰鬥位置X座標
     * @param locationY 戰鬥位置Y座標
     * @param isTest 是否為測試模式
     * @return 戰鬥結果
     * @throws IOException 操作失敗時拋出異常
     */
    /*public Battle startBattle(String playerId, List<String> unitIds, String enemyType, 
                             int locationX, int locationY, boolean isTest) throws IOException {
        // 驗證參數
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (unitIds == null || unitIds.isEmpty()) {
            throw new IllegalArgumentException("Unit IDs cannot be null or empty");
        }
        if (enemyType == null || enemyType.trim().isEmpty()) {
            throw new IllegalArgumentException("Enemy type cannot be null or empty");
        }

        // 載入遊戲狀態
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        // 驗證敵人類型
        EnemyType enemyConfig = enemyTypes.get(enemyType);
        if (enemyConfig == null) {
            throw new IllegalArgumentException("Unknown enemy type: " + enemyType);
        }

        // 驗證玩家等級
        if (!enemyConfig.isPlayerLevelSufficient(gameState.getPlayer().getLevel())) {
            throw new IllegalArgumentException("Player level too low for this enemy type");
        }

        // 獲取玩家單位
        List<Unit> playerUnits = getPlayerUnits(gameState, unitIds);
        if (playerUnits.isEmpty()) {
            throw new IllegalArgumentException("No valid units found for battle");
        }

        // 創建敵方單位
        List<Unit> enemyUnits = createEnemyUnits(enemyConfig);

        // 執行戰鬥邏輯
        Battle battle = executeBattle(playerId, playerUnits, enemyUnits, enemyConfig, 
                                    locationX, locationY, isTest);

        // 更新遊戲狀態
        updateGameStateAfterBattle(gameState, playerUnits, battle, isTest);

        // 發放獎勵
        if (battle.isVictory()) {
            distributeRewards(playerId, battle.getRewards(), isTest);
            updateTaskProgress(playerId, enemyType, gameState, isTest);
        }

        // 記錄事件
        recordBattleEvent(playerId, battle, isTest);

        return battle;
    }*/

    /**
     * 獲取玩家單位
     * @param gameState 遊戲狀態
     * @param unitIds 單位ID列表
     * @return 玩家單位列表
     */
    /*private List<Unit> getPlayerUnits(GameState gameState, List<String> unitIds) {
        List<Unit> playerUnits = new ArrayList<>();
        for (String unitId : unitIds) {
            Unit unit = gameState.getUnits().stream()
                    .filter(u -> u.getId().equals(unitId) && u.isAlive())
                    .findFirst()
                    .orElse(null);
            if (unit != null) {
                playerUnits.add(unit);
            }
        }
        return playerUnits;
    }*/

    /**
     * 創建敵方單位
     * @param enemyConfig 敵方配置
     * @return 敵方單位列表
     */
    /* 
    private List<Unit> createEnemyUnits(EnemyType enemyConfig) {
        List<Unit> enemyUnits = new ArrayList<>();
        for (var unitConfig : enemyConfig.getUnits()) {
            Unit enemyUnit = new Unit();
            enemyUnit.setId(UUID.randomUUID().toString());
            enemyUnit.setType(unitConfig.getType());
            enemyUnit.setName(enemyConfig.getName() + " " + unitConfig.getType());
            enemyUnit.setLevel(unitConfig.getLevel());
            enemyUnit.setAttack(unitConfig.getAttack());
            enemyUnit.setDefense(unitConfig.getDefense());
            enemyUnit.setHealth(unitConfig.getHealth());
            enemyUnit.setMaxHealth(unitConfig.getHealth());
            enemyUnit.setStatus("ACTIVE");
            enemyUnit.setCreatedTime(System.currentTimeMillis());
            enemyUnit.setLastUpdatedTime(System.currentTimeMillis());
            enemyUnits.add(enemyUnit);
        }
        return enemyUnits;
    }*/

    /**
     * 執行戰鬥邏輯
     * @param playerId 玩家ID
     * @param playerUnits 玩家單位
     * @param enemyUnits 敵方單位
     * @param enemyConfig 敵方配置
     * @param locationX 戰鬥位置X
     * @param locationY 戰鬥位置Y
     * @param isTest 是否測試模式
     * @return 戰鬥結果
     */
   /* private Battle executeBattle(String playerId, List<Unit> playerUnits, List<Unit> enemyUnits,
                                EnemyType enemyConfig, int locationX, int locationY, boolean isTest) {
        Battle battle = new Battle();
        battle.setId(UUID.randomUUID().toString());
        battle.setPlayerId(playerId);
        battle.setEnemyType(enemyConfig.getType());
        battle.setPlayerUnits(new ArrayList<>(playerUnits));
        battle.setEnemyUnits(new ArrayList<>(enemyUnits));
        battle.setLocationX(locationX);
        battle.setLocationY(locationY);
        battle.setDifficulty(enemyConfig.getDifficulty());
        battle.setDuration(enemyConfig.getBattleDuration());
        battle.setDescription(enemyConfig.getBattleDescription());
        battle.setStatus("IN_PROGRESS");

        // 計算戰鬥結果
        int playerStrength = calculateTotalStrength(playerUnits);
        int enemyStrength = calculateTotalStrength(enemyUnits);
        
        // 添加隨機因素
        double playerRandomFactor = 0.8 + ThreadLocalRandom.current().nextDouble() * 0.4; // 0.8-1.2
        double enemyRandomFactor = 0.8 + ThreadLocalRandom.current().nextDouble() * 0.4; // 0.8-1.2
        
        double finalPlayerStrength = playerStrength * playerRandomFactor;
        double finalEnemyStrength = enemyStrength * enemyRandomFactor;

        // 決定勝負
        String result;
        if (finalPlayerStrength > finalEnemyStrength * 1.1) {
            result = "WIN";
        } else if (finalEnemyStrength > finalPlayerStrength * 1.1) {
            result = "LOSE";
        } else {
            result = "DRAW";
        }

        battle.setResult(result);
        battle.setStatus("COMPLETED");

        // 計算單位損失
        calculateUnitLosses(playerUnits, enemyUnits, result);

        // 設定獎勵
        if ("WIN".equals(result)) {
            battle.setRewards(enemyConfig.getRewards());
        } else {
            battle.setRewards(new Resource()); // 空獎勵
        }

        // 添加統計數據
        battle.addStatistic("playerStrength", playerStrength);
        battle.addStatistic("enemyStrength", enemyStrength);
        battle.addStatistic("playerRandomFactor", playerRandomFactor);
        battle.addStatistic("enemyRandomFactor", enemyRandomFactor);
        battle.addStatistic("finalPlayerStrength", finalPlayerStrength);
        battle.addStatistic("finalEnemyStrength", finalEnemyStrength);

        return battle;
    }*/

    /**
     * 計算總戰力
     * @param units 單位列表
     * @return 總戰力
     */
    /*private int calculateTotalStrength(List<Unit> units) {
        return units.stream()
                .mapToInt(unit -> unit.getAttack() * unit.getLevel() * unit.getHealth() / unit.getMaxHealth())
                .sum();
    }*/

    /**
     * 計算單位損失
     * @param playerUnits 玩家單位
     * @param enemyUnits 敵方單位
     * @param result 戰鬥結果
     */
    /*private void calculateUnitLosses(List<Unit> playerUnits, List<Unit> enemyUnits, String result) {
        if ("WIN".equals(result)) {
            // 玩家勝利，單位損失較少
            for (Unit unit : playerUnits) {
                int damage = ThreadLocalRandom.current().nextInt(10, 30); // 10-30% 傷害
                unit.takeDamage(damage);
            }
        } else if ("LOSE".equals(result)) {
            // 玩家失敗，單位損失較大
            for (Unit unit : playerUnits) {
                int damage = ThreadLocalRandom.current().nextInt(40, 70); // 40-70% 傷害
                unit.takeDamage(damage);
            }
        } else {
            // 平手，中等損失
            for (Unit unit : playerUnits) {
                int damage = ThreadLocalRandom.current().nextInt(20, 40); // 20-40% 傷害
                unit.takeDamage(damage);
            }
        }
    }*/

    /**
     * 更新戰鬥後的遊戲狀態
     * @param gameState 遊戲狀態
     * @param playerUnits 玩家單位
     * @param battle 戰鬥結果
     * @param isTest 是否測試模式
     * @throws IOException 操作失敗時拋出異常
     */
    /*private void updateGameStateAfterBattle(GameState gameState, List<Unit> playerUnits, 
                                          Battle battle, boolean isTest) throws IOException {
        // 移除已死亡的單位並釋放地形
        Iterator<Unit> iterator = gameState.getUnits().iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            if (!unit.isAlive()) {
                //terrainMapService.releasePosition(battle.getPlayerId(), unit.getX(), unit.getY());
                iterator.remove();
            }
        }

        // 添加戰鬥記錄
        if (gameState.getBattles() == null) {
            gameState.setBattles(new ArrayList<>());
        }
        gameState.getBattles().add(battle);

        // 保存遊戲狀態
        storageService.saveGameState(battle.getPlayerId(), gameState, isTest);
    }*/

    /**
     * 發放獎勵
     * @param playerId 玩家ID
     * @param rewards 獎勵資源
     * @param isTest 是否測試模式
     * @throws IOException 操作失敗時拋出異常
     */
    /*private void distributeRewards(String playerId, Resource rewards, boolean isTest) throws IOException {
        if (rewards != null) {
            resourceService.addResources(playerId, rewards.getGold(), rewards.getWood(), 
                                       rewards.getStone(), rewards.getIron(), rewards.getFood(), isTest);
        }
    }*/

    /**
     * 更新任務進度
     * @param playerId 玩家ID
     * @param enemyType 敵人類型
     * @param gameState 遊戲狀態
     * @param isTest 是否測試模式
     */
    /*
    private void updateTaskProgress(String playerId, String enemyType, GameState gameState, boolean isTest) {
        if (gameState.getTasks() != null) {
            gameState.getTasks().stream()
                .filter(task -> task.getType().equals("defeat_" + enemyType) && "ACTIVE".equals(task.getStatus()))
                .forEach(task -> {
                    try {
                        taskService.updateTaskProgress(playerId, task.getId(), 1, isTest);
                    } catch (IOException e) {
                        // 忽略任務更新失敗
                    }
                });
        }
    }*/

    /**
     * 記錄戰鬥事件
     * @param playerId 玩家ID
     * @param battle 戰鬥結果
     * @param isTest 是否測試模式
     */
    private void recordBattleEvent(String playerId, Battle battle, boolean isTest) {
        String eventMessage = String.format("Battle against %s: %s", 
                                          battle.getEnemyType(), battle.getResult());
        try {
            eventService.addEvent(playerId, "battle_completed", eventMessage, isTest);
        } catch (IOException e) {
            // 忽略事件記錄失敗
        }
    }

    /**
     * 獲取玩家戰鬥記錄
     * @param playerId 玩家ID
     * @param isTest 是否測試模式
     * @return 戰鬥記錄列表
     * @throws IOException 操作失敗時拋出異常
     */
    public List<Battle> getBattles(String playerId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }

        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        return gameState.getBattles() != null ? gameState.getBattles() : new ArrayList<>();
    }

    /**
     * 獲取特定戰鬥記錄
     * @param playerId 玩家ID
     * @param battleId 戰鬥ID
     * @param isTest 是否測試模式
     * @return 戰鬥記錄
     * @throws IOException 操作失敗時拋出異常
     */
    public Battle getBattleById(String playerId, String battleId, boolean isTest) throws IOException {
        List<Battle> battles = getBattles(playerId, isTest);
        return battles.stream()
                .filter(battle -> battle.getId().equals(battleId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 獲取所有敵人類型
     * @return 敵人類型映射
     */
    public Map<String, EnemyType> getAllEnemyTypes() {
        return new HashMap<>(enemyTypes);
    }

    /**
     * 獲取特定敵人類型
     * @param enemyType 敵人類型識別碼
     * @return 敵人類型配置
     */
    public EnemyType getEnemyType(String enemyType) {
        return enemyTypes.get(enemyType);
    }

    /**
     * 獲取玩家戰鬥統計
     * @param playerId 玩家ID
     * @param isTest 是否測試模式
     * @return 戰鬥統計
     * @throws IOException 操作失敗時拋出異常
     */
    public Map<String, Object> getBattleStatistics(String playerId, boolean isTest) throws IOException {
        List<Battle> battles = getBattles(playerId, isTest);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBattles", battles.size());
        stats.put("victories", (int) battles.stream().filter(Battle::isVictory).count());
        stats.put("defeats", (int) battles.stream().filter(Battle::isDefeat).count());
        stats.put("draws", (int) battles.stream().filter(Battle::isDraw).count());
        
        if (!battles.isEmpty()) {
            stats.put("winRate", (double) stats.get("victories") / battles.size());
            stats.put("lastBattle", battles.get(battles.size() - 1));
        }
        
        return stats;
    }

    /**
     * 清理測試數據
     * @param playerId 玩家ID
     * @param isTest 是否測試模式
     * @throws IOException 操作失敗時拋出異常
     */
    public void clearTestBattles(String playerId, boolean isTest) throws IOException {
        if (!isTest) {
            throw new IllegalArgumentException("Can only clear test battles");
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState != null) {
            gameState.setBattles(new ArrayList<>());
            storageService.saveGameState(playerId, gameState,"清理測試數據", isTest);
        }
    }
}
