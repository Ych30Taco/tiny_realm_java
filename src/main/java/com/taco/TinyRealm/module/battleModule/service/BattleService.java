package com.taco.TinyRealm.module.battleModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.battleModule.model.Battle;
import com.taco.TinyRealm.module.battleModule.model.EnemyType;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.soldierModule.model.PlayerSoldier;
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
    
    @Value("${app.data.enemies-path}")
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
        System.out.println("---- 應用程式啟動中，載入戰鬥模組 ----");
        try {
            loadEnemyTypes(enemiesPath);
            String enemyNames = getEnemyNames();
            System.out.println("---- 應用程式啟動中，已載入" + enemyNames + " ----");
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入戰鬥模組失敗 ----");
            e.printStackTrace();
            throw new RuntimeException("Failed to load enemies.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入戰鬥模組完成 ----");
    }

    /**
     * 獲取敵人名稱列表
     */
    public String getEnemyNames() {
        String names = "";
        for (EnemyType type : enemyTypes.values()) {
            names += type.getName() + ", ";
        }
        names += "共" + enemyTypes.size() + "種敵人";
        return names.length() > 0 ? names : "";
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
     * 開始戰鬥
     * @param playerId 玩家ID
     * @param soldierIds 參戰士兵ID列表
     * @param enemyType 敵人類型
     * @param locationX 戰鬥位置X座標
     * @param locationY 戰鬥位置Y座標
     * @param isTest 是否為測試模式
     * @return 戰鬥結果
     * @throws IOException 操作失敗時拋出異常
     */
    public Battle startBattle(String playerId, Map<String,Integer> soldierIds, String enemyType, 
                             int locationX, int locationY, boolean isTest) throws IOException {
        // 驗證參數
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (soldierIds == null || soldierIds.isEmpty()) {
            throw new IllegalArgumentException("Soldier IDs cannot be null or empty");
        }
        if (enemyType == null || enemyType.trim().isEmpty()) {
            throw new IllegalArgumentException("Enemy type cannot be null or empty");
        }

        // 載入遊戲狀態
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        // 驗證敵人類型
        EnemyType enemyConfig = enemyTypes.get(enemyType);
        if (enemyConfig == null) {
            throw new IllegalArgumentException("Unknown enemy type: " + enemyType);
        }

        // 驗證玩家等級
        if (gameState.getPlayer() != null && !enemyConfig.isPlayerLevelSufficient(gameState.getPlayer().getLevel())) {
            throw new IllegalArgumentException("Player level too low for this enemy type");
        }
        
        // 獲取玩家士兵
        List<PlayerSoldier> playerSoldiers = getPlayerSoldiers(gameState, soldierIds);
        if (playerSoldiers.isEmpty()) {
            throw new IllegalArgumentException("No valid soldiers found for battle");
        }
        System.out.println(playerSoldiers);

        // 創建敵方單位
        List<PlayerSoldier> enemySoldiers = createEnemySoldiers(enemyConfig);
        System.out.println(enemyType);
        // 執行戰鬥邏輯
        Battle battle = executeBattle(playerId, playerSoldiers, enemySoldiers, enemyConfig, 
                                    locationX, locationY, isTest);
         
        // 更新遊戲狀態
        updateGameStateAfterBattle(gameState, playerSoldiers, battle, isTest);

        // 發放獎勵
        if (battle.isVictory()) {
            distributeRewards(playerId, battle.getRewards(), isTest);
        }

        // 記錄事件
        //recordBattleEvent(playerId, battle, isTest);
        
        return battle;
    }

    /**
     * 獲取玩家士兵
     * @param gameState 遊戲狀態
     * @param soldierIds 士兵ID列表
     * @return 玩家士兵列表
     */
    private List<PlayerSoldier> getPlayerSoldiers(GameState gameState, Map<String,Integer> soldierIds) {
        List<PlayerSoldier> playerSoldiers = new ArrayList<>();
        for (String soldierId : soldierIds.keySet()) {
            PlayerSoldier soldier = gameState.getSoldiers().get(soldierId);
            soldier.setCount(soldierIds.get(soldierId));
            if (soldier != null /*&& "ACTIVE".equals(soldier.getStatus())*/) {
                playerSoldiers.add(soldier);
            }
        }
        return playerSoldiers;
    }

    /**
     * 創建敵方士兵
     * @param enemyConfig 敵方配置
     * @return 敵方士兵列表
     */
    private List<PlayerSoldier> createEnemySoldiers(EnemyType enemyConfig) {
        List<PlayerSoldier> enemySoldiers = new ArrayList<>();
        for (var unitConfig : enemyConfig.getUnits()) {
            PlayerSoldier enemySoldier = new PlayerSoldier();
            enemySoldier.setId(UUID.randomUUID().toString());
            enemySoldier.setType(unitConfig.getType());
            enemySoldier.setName(enemyConfig.getName() + " " + unitConfig.getType());
            enemySoldier.setLevel(unitConfig.getLevel());
            enemySoldier.setAttack(unitConfig.getAttack());
            enemySoldier.setDefense(unitConfig.getDefense());
            enemySoldier.setHealth(unitConfig.getHealth());
            enemySoldier.setMaxHealth(unitConfig.getHealth());
            enemySoldier.setStatus("ACTIVE");
            enemySoldier.setCount(unitConfig.getCount());
            enemySoldiers.add(enemySoldier);
        }
        return enemySoldiers;
    }

    /**
     * 執行戰鬥邏輯
     * @param playerId 玩家ID
     * @param playerSoldiers 玩家士兵
     * @param enemySoldiers 敵方士兵
     * @param enemyConfig 敵方配置
     * @param locationX 戰鬥位置X
     * @param locationY 戰鬥位置Y
     * @param isTest 是否測試模式
     * @return 戰鬥結果
     */
    private Battle executeBattle(String playerId, List<PlayerSoldier> playerSoldiers, List<PlayerSoldier> enemySoldiers,
                                EnemyType enemyConfig, int locationX, int locationY, boolean isTest) {
        Battle battle = new Battle();
        battle.setId(UUID.randomUUID().toString());
        battle.setPlayerId(playerId);
        battle.setEnemyType(enemyConfig.getType());
        battle.setLocationX(locationX);
        battle.setLocationY(locationY);
        battle.setDifficulty(enemyConfig.getDifficulty());
        battle.setDuration(enemyConfig.getBattleDuration());
        battle.setDescription(enemyConfig.getBattleDescription());
        battle.setStatus("IN_PROGRESS");

        // 計算戰鬥結果
        int playerStrength = calculateTotalStrength(playerSoldiers);
        int enemyStrength = calculateTotalStrength(enemySoldiers);
        System.out.println("playerStrength = "+playerStrength+", enemyStrength = "+enemyStrength);
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

        // 計算士兵損失
        calculateSoldierLosses(playerSoldiers, enemySoldiers, result);

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
    }

    /**
     * 計算總戰力
     * @param soldiers 士兵列表
     * @return 總戰力
     */
    private int calculateTotalStrength(List<PlayerSoldier> soldiers) {
        return soldiers.stream()
                .mapToInt(soldier -> soldier.getAttack() * soldier.getLevel() * soldier.getHealth() / soldier.getMaxHealth()*soldier.getCount())
                .sum();
    }

    /**
     * 計算士兵損失
     * @param playerSoldiers 玩家士兵
     * @param enemySoldiers 敵方士兵
     * @param result 戰鬥結果
     */
    private void calculateSoldierLosses(List<PlayerSoldier> playerSoldiers, List<PlayerSoldier> enemySoldiers, String result) {
        if ("WIN".equals(result)) {
            // 玩家勝利，士兵損失較少
            for (PlayerSoldier soldier : playerSoldiers) {
                int damage = ThreadLocalRandom.current().nextInt(10, 30); // 10-30% 傷害
                soldier.setHealth(Math.max(1, soldier.getHealth() - damage));
            }
        } else if ("LOSE".equals(result)) {
            // 玩家失敗，士兵損失較大
            for (PlayerSoldier soldier : playerSoldiers) {
                int damage = ThreadLocalRandom.current().nextInt(40, 70); // 40-70% 傷害
                soldier.setHealth(Math.max(1, soldier.getHealth() - damage));
            }
        } else {
            // 平手，中等損失
            for (PlayerSoldier soldier : playerSoldiers) {
                int damage = ThreadLocalRandom.current().nextInt(20, 40); // 20-40% 傷害
                soldier.setHealth(Math.max(1, soldier.getHealth() - damage));
            }
        }
    }

    /**
     * 更新戰鬥後的遊戲狀態
     * @param gameState 遊戲狀態
     * @param playerSoldiers 玩家士兵
     * @param battle 戰鬥結果
     * @param isTest 是否測試模式
     * @throws IOException 操作失敗時拋出異常
     */
    private void updateGameStateAfterBattle(GameState gameState, List<PlayerSoldier> playerSoldiers, 
                                          Battle battle, boolean isTest) throws IOException {
        // 更新士兵狀態
        for (PlayerSoldier soldier : playerSoldiers) {
            if (soldier.getHealth() <= 0) {
                soldier.setStatus("INJURED");
            }
        }

        // 創建一個簡化的戰鬥記錄，避免序列化問題
        Battle battleRecord = new Battle();
        battleRecord.setId(battle.getId());
        battleRecord.setPlayerId(battle.getPlayerId());
        battleRecord.setEnemyType(battle.getEnemyType());
        battleRecord.setResult(battle.getResult());
        battleRecord.setStatus(battle.getStatus());
        battleRecord.setTimestamp(battle.getTimestamp());
        battleRecord.setDuration(battle.getDuration());
        battleRecord.setDifficulty(battle.getDifficulty());
        battleRecord.setLocationX(battle.getLocationX());
        battleRecord.setLocationY(battle.getLocationY());
        battleRecord.setDescription(battle.getDescription());
        
        // 簡化獎勵數據，創建一個新的 Resource 對象，只包含必要信息
        if (battle.getRewards() != null) {
            Resource simplifiedRewards = new Resource();
            simplifiedRewards.setId("battle_rewards");
            // 不設置複雜的獎勵數據，避免序列化問題
            simplifiedRewards.setNowAmount(0);
            simplifiedRewards.setMaxAmount(0);
            battleRecord.setRewards(simplifiedRewards);
        }
        
        // 簡化統計數據，只保留基本數值，避免複雜對象序列化問題
        if (battle.getStatistics() != null && !battle.getStatistics().isEmpty()) {
            Map<String, Object> simplifiedStats = new HashMap<>();
            for (Map.Entry<String, Object> entry : battle.getStatistics().entrySet()) {
                Object value = entry.getValue();
                // 只保留基本數據類型，避免複雜對象
                if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                    simplifiedStats.put(entry.getKey(), value);
                }
            }
            battleRecord.setStatistics(simplifiedStats);
        }
        
        // 添加戰鬥記錄
        if (gameState.getBattles() == null) {
            gameState.setBattles(new ArrayList<>());
        }
        gameState.getBattles().add(battleRecord);

        // 保存遊戲狀態
        storageService.saveGameState(battle.getPlayerId(), gameState, "戰鬥完成", isTest);
    }

    /**
     * 發放獎勵
     * @param playerId 玩家ID
     * @param rewards 獎勵資源
     * @param isTest 是否測試模式
     * @throws IOException 操作失敗時拋出異常
     */
    private void distributeRewards(String playerId, Resource rewards, boolean isTest) throws IOException {
        if (rewards != null) {
            // 創建資源映射
            Map<String, Integer> resourceMap = new HashMap<>();
            // 這裡需要根據實際的Resource模型來映射資源
            // 暫時使用預設值，實際實現時需要根據Resource模型的具體結構來調整
            resourceMap.put("gold", 1000);
            resourceMap.put("food", 1000);
            resourceMap.put("wood", 1000);
            resourceMap.put("stone", 1000);
            resourceMap.put("iron", 1000);
            
            resourceService.addResources(playerId, resourceMap, isTest);
        }
    }

    /**
     * 記錄戰鬥事件
     * @param playerId 玩家ID
     * @param battle 戰鬥結果
     * @param isTest 是否測試模式
     */
    /*private void recordBattleEvent(String playerId, Battle battle, boolean isTest) {
        String eventMessage = String.format("Battle against %s: %s", 
                                          battle.getEnemyType(), battle.getResult());
        try {
            eventService.addEvent(playerId, "battle_completed", eventMessage, isTest);
        } catch (IOException e) {
            // 忽略事件記錄失敗
        }
    }*/

    /**
     * 獲取玩家戰鬥記錄
     * @param playerId 玩家ID
     * @param isTest 是否測試模式
     * @return 戰鬥記錄列表
     * @throws IOException 操作失敗時拋出異常
     */
    /*public List<Battle> getBattles(String playerId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }

        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        return gameState.getBattles() != null ? gameState.getBattles() : new ArrayList<>();
    }*/

    /**
     * 獲取特定戰鬥記錄
     * @param playerId 玩家ID
     * @param battleId 戰鬥ID
     * @param isTest 是否測試模式
     * @return 戰鬥記錄
     * @throws IOException 操作失敗時拋出異常
     */
    /*public Battle getBattleById(String playerId, String battleId, boolean isTest) throws IOException {
        List<Battle> battles = getBattles(playerId, isTest);
        return battles.stream()
                .filter(battle -> battle.getId().equals(battleId))
                .findFirst()
                .orElse(null);
    }*/



    /**
     * 獲取玩家戰鬥統計
     * @param playerId 玩家ID
     * @param isTest 是否測試模式
     * @return 戰鬥統計
     * @throws IOException 操作失敗時拋出異常
     */
    /*public Map<String, Object> getBattleStatistics(String playerId, boolean isTest) throws IOException {
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
    }*/

    /**
     * 清理測試數據
     * @param playerId 玩家ID
     * @param isTest 是否測試模式
     * @throws IOException 操作失敗時拋出異常
     */
    /*public void clearTestBattles(String playerId, boolean isTest) throws IOException {
        if (!isTest) {
            throw new IllegalArgumentException("Can only clear test battles");
        }
        
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState != null) {
            gameState.setBattles(new ArrayList<>());
            storageService.saveGameState(playerId, gameState, "清理測試數據", isTest);
        }
    }*/
}


