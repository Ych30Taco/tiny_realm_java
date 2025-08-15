package com.taco.TinyRealm.module.storageModule.controller;

import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.List;

/**
 * 存儲模組控制器
 * 
 * 負責處理遊戲狀態的保存、載入、查詢等操作
 * 提供統一的 RESTful API 介面
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {
    
    @Autowired
    private StorageService storageService;

    /**
     * 保存遊戲狀態
     * 
     * @param playerId 玩家ID
     * @param gameState 遊戲狀態
     * @return 保存結果
     */
   /*@PostMapping("/save")
    public ResponseEntity<?> saveGame(@RequestParam String playerId, @RequestBody GameState gameState) {
        try {
            storageService.saveGameState(playerId, gameState,"保存遊戲狀態", false);
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "遊戲狀態保存成功", 
                "data", null
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "保存遊戲狀態失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }*/

    /**
     * 載入遊戲狀態
     * 
     * @param body 包含 playerId 的請求體
     * @return 遊戲狀態
     */
    /*@PostMapping("/load")
    public ResponseEntity<?> loadGame(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        if (playerId == null || playerId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, 
                "message", "玩家ID不能為空", 
                "data", null
            ));
        }
        
        try {
            GameState gameState = storageService.loadGameState(playerId, false);
            if (gameState == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false, 
                    "message", "遊戲狀態不存在", 
                    "data", null
                ));
            }
            System.out.println("---- 已載入玩家 " + playerId + "----");
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "遊戲狀態載入成功", 
                "data", gameState
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "載入遊戲狀態失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }*/

    /**
     * 獲取所有玩家列表
     * 
     * @return 玩家ID列表
     */
    @GetMapping("/playerList")
    public ResponseEntity<?> listAllFiles() {
        try {
            List<String> files = storageService.listAllFiles();
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "獲取玩家列表成功", 
                "data", files
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "獲取玩家列表失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }
    
    /**
     * 獲取在線玩家列表
     * 
     * @return 在線玩家ID列表
     */
    @GetMapping("/onlinePlayers")
    public ResponseEntity<?> getOnlineGameStateIdList() {
        try {
            List<String> onlinePlayers = storageService.getOnlineGameStateIdList();
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "獲取在線玩家列表成功", 
                "data", onlinePlayers
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "獲取在線玩家列表失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }
    
    /**
     * 獲取離線玩家列表
     * 
     * @return 離線玩家ID列表
     */
    @GetMapping("/offlinePlayers")
    public ResponseEntity<?> getOfflineGameStateIdList() {
        try {
            List<String> offlinePlayers = storageService.getOfflineGameStateIdList();
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "獲取離線玩家列表成功", 
                "data", offlinePlayers
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "獲取離線玩家列表失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }
    
    /**
     * 獲取所有遊戲狀態
     * 
     * @return 所有遊戲狀態映射
     */
    @GetMapping("/allGameStateList")
    public ResponseEntity<?> getAllGameStateList() {
        try {
            Map<String, GameState> allGameStates = storageService.getAllGameStateList();
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "獲取所有遊戲狀態成功", 
                "data", allGameStates
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "獲取所有遊戲狀態失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }

    /**
     * 根據玩家ID獲取遊戲狀態
     * 
     * @param playerId 玩家ID
     * @return 遊戲狀態
     */
    @GetMapping("/gameState/{playerId}")
    public ResponseEntity<?> getGameStateById(@PathVariable String playerId) {
        try {
            GameState gameState = storageService.getGameStateListById(playerId);
            if (gameState == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false, 
                    "message", "玩家遊戲狀態不存在", 
                    "data", null
                ));
            }
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "獲取玩家遊戲狀態成功", 
                "data", gameState
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "獲取玩家遊戲狀態失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }

    /**
     * 清除測試資料
     * 
     * @return 清除結果
     */
    @DeleteMapping("/clearTestData")
    public ResponseEntity<?> clearTestData() {
        try {
            storageService.clearTestData();
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "測試資料清除成功", 
                "data", null
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "success", false, 
                "message", "清除測試資料失敗: " + e.getMessage(), 
                "data", null
            ));
        }
    }
}
