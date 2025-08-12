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
     * 解散部隊
     * 
     * @param playerId 玩家ID
     * @param unitId 部隊ID
     * @return 解散結果
     */
    @DeleteMapping("/disband")
    public ResponseEntity<?> disbandSoldier(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            String soldierID = (String) body.get("soldierID");
            int count = (int) body.get("count");
            GameState gameState = soldierService.disbandSoldier(playerId, soldierID,count, false);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "部隊解散成功",
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
                "message", "部隊解散失敗: " + e.getMessage(),
                "data", null
            ));
        }
    }
}
