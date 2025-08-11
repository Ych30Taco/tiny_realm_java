package com.taco.TinyRealm.module.soldierModule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taco.TinyRealm.module.soldierModule.model.SoldierType;
import com.taco.TinyRealm.module.soldierModule.service.SoldierService;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 部隊控制器
 * 
 * 提供部隊管理的 RESTful API 介面
 * 包含部隊創建、移動、升級、治療等功能
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/soldier")
public class SoldierController {
    
    @Autowired
    private SoldierService soldierService;

    /**
     * 獲取所有部隊類型
     * 
     * @return 部隊類型列表
     */
    @GetMapping("/types")
    public ResponseEntity<?> getAllsoldierTypes() {
        try {
            List<SoldierType> soldierTypes = soldierService.getAllsoldierType();
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "獲取部隊類型成功",
                "data", soldierTypes
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "獲取部隊類型失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }
    @PostMapping("/typeById")
    public ResponseEntity<?> getAllsoldierTypeById(@RequestBody Map<String, Object> body) {
        String soldierID = (String) body.get("soldierID");
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取部隊類型成功", "data", soldierService.getSoldierTypeById(soldierID)));
    }

    /**
     * 創建部隊
     */
    @PostMapping("/create")
    public ResponseEntity<?> createSoldier(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            String soldierID = (String) body.get("soldierID");
            int count = (int) body.get("count");
            
            GameState gameState = soldierService.createSoldier(playerId, soldierID, count,false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "部隊創建成功",
                "data", gameState
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "部隊創建失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }

    /**
     * 移動部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @param newX 新X座標
     * @param newY 新Y座標
     * @return 移動後的部隊
     */
    /*@PutMapping("/move")
    public ResponseEntity<?> moveUnit(@RequestParam String playerId,
                                     @RequestParam String unitId,
                                     @RequestParam int newX,
                                     @RequestParam int newY) {
        try {
            Unit unit = unitService.moveUnit(playerId, unitId, newX, newY, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "部隊移動成功",
                "data", unit
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "部隊移動失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/

    /**
     * 獲取玩家的所有部隊
     * 
     * @param playerId 玩家ID
     * @return 部隊列表
     */
    /*@GetMapping("/player/{playerId}")
    public ResponseEntity<?> getPlayerUnits(@PathVariable String playerId) {
        try {
            List<Unit> units = unitService.getPlayerUnits(playerId, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "獲取玩家部隊成功",
                "data", units
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "獲取玩家部隊失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/

    /**
     * 升級部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @return 升級後的部隊
     */
    /*@PutMapping("/upgrade")
    public ResponseEntity<?> upgradeUnit(@RequestParam String playerId,
                                        @RequestParam String unitId) {
        try {
            Unit unit = unitService.upgradeUnit(playerId, unitId, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "部隊升級成功",
                "data", unit
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "部隊升級失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/

    /**
     * 治療部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @param healAmount 治療量
     * @return 治療後的部隊
     */
    /*@PutMapping("/heal")
    public ResponseEntity<?> healUnit(@RequestParam String playerId,
                                     @RequestParam String unitId,
                                     @RequestParam int healAmount) {
        try {
            Unit unit = unitService.healUnit(playerId, unitId, healAmount, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "部隊治療成功",
                "data", unit
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "部隊治療失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/

    /**
     * 解散部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @return 解散結果
     */
    /*@DeleteMapping("/disband")
    public ResponseEntity<?> disbandUnit(@RequestParam String playerId,
                                        @RequestParam String unitId) {
        try {
            boolean result = unitService.disbandUnit(playerId, unitId, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "部隊解散成功",
                "data", result
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "部隊解散失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/

    /**
     * 獲取部隊統計資訊
     * 
     * @param playerId 玩家ID
     * @return 統計資訊
     */
   /*@GetMapping("/stats/{playerId}")
    public ResponseEntity<?> getUnitStats(@PathVariable String playerId) {
        try {
            Map<String, Object> stats = unitService.getUnitStats(playerId, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "獲取部隊統計成功",
                "data", stats
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "獲取部隊統計失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/

    /**
     * 根據ID獲取部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @return 部隊資訊
     */
    /*@GetMapping("/{playerId}/{unitId}")
    public ResponseEntity<?> getUnitById(@PathVariable String playerId,
                                        @PathVariable String unitId) {
        try {
            List<Unit> units = unitService.getPlayerUnits(playerId, false);
            Unit targetUnit = units.stream()
                    .filter(unit -> unit.getId().equals(unitId))
                    .findFirst()
                    .orElse(null);
            
            if (targetUnit == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "部隊不存在",
                    "data", null
                ));
            }
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "獲取部隊資訊成功",
                "data", targetUnit
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage(),
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "獲取部隊資訊失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }*/
}
