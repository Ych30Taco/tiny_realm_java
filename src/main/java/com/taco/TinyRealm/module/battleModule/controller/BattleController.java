package com.taco.TinyRealm.module.battleModule.controller;

import com.taco.TinyRealm.module.battleModule.model.Battle;
import com.taco.TinyRealm.module.battleModule.model.EnemyType;
import com.taco.TinyRealm.module.battleModule.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 戰鬥控制器
 * 提供戰鬥管理的RESTful API端點
 */
@RestController
@RequestMapping("/api/battle")
public class BattleController {

    @Autowired
    private BattleService battleService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllEnemyTypes() {
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取戰鬥類型成功", "data", battleService.getAllEnemyTypes()));
    }

    @PostMapping("/typeById")
    public ResponseEntity<?> getEnemyType(@RequestBody Map<String, Object> body) {
        String enemyType = (String) body.get("enemyType");
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取戰鬥類型成功", "data", battleService.getEnemyType(enemyType)));
    }
    /**
     * 開始戰鬥
     * POST /api/battle/start
     * 
     * @param body 請求體，包含戰鬥參數
     * @return 戰鬥結果
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startBattle(@RequestBody Map<String, Object> body) {
        
        try {
            String playerId = (String) body.get("playerId");
            Map<String,Integer> soldierIds = (Map<String,Integer>) body.get("soldierIds");
            String enemyType = (String) body.get("enemyType");
            Integer locationX = (Integer) body.getOrDefault("locationX", 0);
            Integer locationY = (Integer) body.getOrDefault("locationY", 0);
            Boolean isTest = (Boolean) body.getOrDefault("isTest", false);
            
            // 驗證必要參數
            if (playerId == null || playerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Player ID is required");
            }
            if (soldierIds == null || soldierIds.isEmpty()) {
                throw new IllegalArgumentException("Soldier IDs are required");
            }
            if (enemyType == null || enemyType.trim().isEmpty()) {
                throw new IllegalArgumentException("Enemy type is required");
            }
            
            Battle battle = battleService.startBattle(playerId, soldierIds, enemyType, 
                                                    locationX, locationY, isTest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Battle started successfully");
            response.put("data", battle);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid battle parameters: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
            
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to start battle: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 獲取玩家戰鬥記錄
     * GET /api/battle/player/{playerId}
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式（可選，預設false）
     * @return 戰鬥記錄列表
     */
    /*@GetMapping("/player/{playerId}")
    public ResponseEntity<Map<String, Object>> getBattles(
            @PathVariable String playerId,
            @RequestParam(defaultValue = "false") boolean isTest) {
        
        try {
            List<Battle> battles = battleService.getBattles(playerId, isTest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved battle history");
            response.put("data", battles);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid player ID: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
            
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve battle history: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    /**
     * 獲取特定戰鬥記錄
     * GET /api/battle/{playerId}/{battleId}
     * 
     * @param playerId 玩家ID
     * @param battleId 戰鬥ID
     * @param isTest 是否為測試模式（可選，預設false）
     * @return 戰鬥記錄
     */
    /*@GetMapping("/{playerId}/{battleId}")
    public ResponseEntity<Map<String, Object>> getBattleById(
            @PathVariable String playerId,
            @PathVariable String battleId,
            @RequestParam(defaultValue = "false") boolean isTest) {
        
        try {
            Battle battle = battleService.getBattleById(playerId, battleId, isTest);
            
            Map<String, Object> response = new HashMap<>();
            if (battle != null) {
                response.put("success", true);
                response.put("message", "Successfully retrieved battle");
                response.put("data", battle);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Battle not found");
                response.put("data", null);
                return ResponseEntity.notFound().build();
            }
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid parameters: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
            
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve battle: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    /**
     * 獲取所有敵人類型
     * GET /api/battle/enemies
     * 
     * @return 敵人類型配置
     */
    /*@GetMapping("/enemies")
    public ResponseEntity<Map<String, Object>> getAllEnemyTypes() {
        try {
            Map<String, EnemyType> enemyTypes = battleService.getAllEnemyTypes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved all enemy types");
            response.put("data", enemyTypes);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve enemy types: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    /**
     * 獲取特定敵人類型
     * GET /api/battle/enemies/{enemyType}
     * 
     * @param enemyType 敵人類型識別碼
     * @return 敵人類型配置
     */
   /*@GetMapping("/enemies/{enemyType}")
    public ResponseEntity<Map<String, Object>> getEnemyType(@PathVariable String enemyType) {
        try {
            EnemyType enemy = battleService.getEnemyType(enemyType);
            
            Map<String, Object> response = new HashMap<>();
            if (enemy != null) {
                response.put("success", true);
                response.put("message", "Successfully retrieved enemy type");
                response.put("data", enemy);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Enemy type not found: " + enemyType);
                response.put("data", null);
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve enemy type: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    /**
     * 獲取玩家戰鬥統計
     * GET /api/battle/stats/{playerId}
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式（可選，預設false）
     * @return 戰鬥統計
     */
    /*@GetMapping("/stats/{playerId}")
    public ResponseEntity<Map<String, Object>> getBattleStatistics(
            @PathVariable String playerId,
            @RequestParam(defaultValue = "false") boolean isTest) {
        
        try {
            Map<String, Object> stats = battleService.getBattleStatistics(playerId, isTest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved battle statistics");
            response.put("data", stats);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid player ID: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
            
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve battle statistics: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    /**
     * 清理測試戰鬥數據
     * DELETE /api/battle/clearTest/{playerId}
     * 
     * @param playerId 玩家ID
     * @param isTest 是否為測試模式（必須為true）
     * @return 操作結果
     */
    /*@DeleteMapping("/clearTest/{playerId}")
    public ResponseEntity<Map<String, Object>> clearTestBattles(
            @PathVariable String playerId,
            @RequestParam(defaultValue = "false") boolean isTest) {
        
        try {
            if (!isTest) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Can only clear test battles. Set isTest=true");
                response.put("data", null);
                return ResponseEntity.badRequest().body(response);
            }
            
            battleService.clearTestBattles(playerId, isTest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully cleared test battle data");
            response.put("data", null);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid parameters: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
            
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to clear test battle data: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/

    /**
     * 獲取戰鬥系統狀態
     * GET /api/battle/status
     * 
     * @return 系統狀態
     */
    /*@GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getBattleSystemStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("enemyTypesCount", battleService.getAllEnemyTypes().size());
            status.put("systemStatus", "ACTIVE");
            status.put("version", "1.0.0");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Battle system is operational");
            response.put("data", status);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to get system status: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/
}
