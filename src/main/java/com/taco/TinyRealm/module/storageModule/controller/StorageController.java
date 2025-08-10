package com.taco.TinyRealm.module.storageModule.controller;

import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/save")
    public ResponseEntity<?> saveGame(@PathVariable String playerId, @RequestBody GameState gameState) {
        try {
            storageService.saveGameState(playerId, gameState, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "遊戲狀態保存成功", "data", null));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "保存遊戲狀態失敗: " + e.getMessage(), "data", null));
        }
    }

    @GetMapping("/load")
    public ResponseEntity<?> loadGame(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        try {
            GameState gameState = storageService.loadGameState(playerId, false);
            if (gameState == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "遊戲狀態不存在", "data", null));
            }
            System.out.println("---- 已載入玩家 " + playerId + "----");
            return ResponseEntity.ok(Map.of("success", true, "message", "遊戲狀態載入成功", "data", gameState));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }

    @GetMapping("/playerList")
    public ResponseEntity<?> listAllFiles() {
        List<String> files = storageService.listAllFiles();
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取玩家列表成功", "data", files));
    }
    
    @GetMapping("/onlinePlayers")
    public ResponseEntity<?> getOnlineGameStateIdList() {
        List<String> onlinePlayers = storageService.getOnlineGameStateIdList();
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取在線玩家列表成功", "data", onlinePlayers));
    }
    
    @GetMapping("/offlinePlayers")
    public ResponseEntity<?> getOfflineGameStateIdList() {
        List<String> offlinePlayers = storageService.getOfflineGameStateIdList();
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取離線玩家列表成功", "data", offlinePlayers));
    }
    
    @GetMapping("/allGameStateList")
    public ResponseEntity<?> getAllGameStateList() {
        Map<String, GameState> allGameStates = storageService.getAllGameStateList();
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取所有遊戲狀態成功", "data", allGameStates));
    }
}
