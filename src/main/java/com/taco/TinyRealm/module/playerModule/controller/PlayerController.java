package com.taco.TinyRealm.module.playerModule.controller;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/create")
    public ResponseEntity<?> createPlayer(@RequestBody Map<String, Object> body) {
        try {
            String name = (String) body.get("name");
            Player player = playerService.createPlayer(name, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "玩家創建成功", "data", player));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }

    @PostMapping("/userdata")
    public ResponseEntity<?> getPlayer(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            GameState playdata = playerService.getPlayer(playerId, false);
            if (playdata == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "玩家不存在", "data", null));
            }
            return ResponseEntity.ok(Map.of("success", true, "message", "獲取玩家數據成功", "data", playdata));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }
    
    @PostMapping("/logOut")
    public ResponseEntity<?> logOutPlayer(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            GameState playdata = playerService.logOutPlayer(playerId, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "玩家登出成功", "data", playdata));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> logInPlayer(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            GameState playdata = playerService.logInPlayer(playerId, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "玩家登入成功", "data", playdata));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }   
}
