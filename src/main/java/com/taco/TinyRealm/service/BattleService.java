package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Battle;
import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Resource;
import com.taco.TinyRealm.model.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BattleService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TerrainService terrainService;
    @Autowired
    private TaskService taskService; // 新增：任務服務

    public Battle startBattle(String playerId, List<String> unitIds, String enemyType,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        List<Unit> playerUnits = new ArrayList<>();
        int totalPlayerStrength = 0;
        for (String unitId : unitIds) {
            Unit unit = gameState.getUnits().stream()
                    .filter(u -> u.getId().equals(unitId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unit not found"));
            playerUnits.add(unit);
            totalPlayerStrength += unit.getCount();
        }

        List<Unit> enemyUnits = new ArrayList<>();
        Unit enemyUnit = new Unit();
        enemyUnit.setId(UUID.randomUUID().toString());
        enemyUnit.setType("bandit");
        enemyUnit.setCount(10);
        enemyUnits.add(enemyUnit);
        int totalEnemyStrength = 10;

        boolean playerWins = totalPlayerStrength > totalEnemyStrength;

        for (Unit unit : playerUnits) {
            unit.setCount(unit.getCount() / 2);
            if (unit.getCount() == 0) {
                gameState.getUnits().remove(unit);
                terrainService.releasePosition(playerId, unit.getX(), unit.getY());
            }
        }

        Resource rewards = new Resource();
        if (playerWins) {
            rewards.setGold(50);
            rewards.setWood(20);
            resourceService.addResources(playerId, rewards.getGold(), rewards.getWood(), isTest);

            // 更新任務進度
            if (enemyType.equals("bandit")) {
                if (gameState.getTasks() != null) {
                    gameState.getTasks().stream()
                        .filter(t -> t.getType().equals("defeat_bandit") && "ACTIVE".equals(t.getStatus()))
                        .forEach(t -> {
                            try {
                                taskService.updateTaskProgress(playerId, t.getId(), 1,isTest);
                            } catch (IOException e) {
                                // ignore
                            }
                        });
                }
            }
        }

        Battle battle = new Battle();
        battle.setId(UUID.randomUUID().toString());
        battle.setPlayerId(playerId);
        battle.setEnemyType(enemyType);
        battle.setPlayerUnits(playerUnits);
        battle.setEnemyUnits(enemyUnits);
        battle.setResult(playerWins ? "WIN" : "LOSE");
        battle.setRewards(rewards);
        battle.setTimestamp(System.currentTimeMillis());

        if (gameState.getBattles() == null) gameState.setBattles(new java.util.ArrayList<>());
        gameState.getBattles().add(battle);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "battle_completed", "Battle against " + enemyType + ": " + battle.getResult(), isTest);
        return battle;
    }

    public List<Battle> getBattles(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getBattles();
    }
}
