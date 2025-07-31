package com.taco.TinyRealm.module.storageModule.controller;

import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/save")
    public ResponseEntity<?> saveGame(@PathVariable String playerId, @RequestBody GameState gameState) {
        try {
            storageService.saveGameState(playerId, gameState,false);
            return ResponseEntity.ok("Game state saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save game state: " + e.getMessage());
        }
    }

    @GetMapping("/load")
    public ResponseEntity<?> loadGame(@RequestBody Map<String, Object> body ) {
        String playerId = (String) body.get("playerId");
        try {
            GameState gameState = storageService.loadGameState(playerId,false);
            if (gameState == null) return ResponseEntity.status(404).body(null);
            return ResponseEntity.ok(gameState);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
