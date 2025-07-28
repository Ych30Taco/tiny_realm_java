package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @PostMapping("/save/{playerId}")
    public ResponseEntity<?> saveGame(@PathVariable String playerId, @RequestBody GameState gameState) {
        try {
            storageService.saveGameState(playerId, gameState);
            return ResponseEntity.ok("Game state saved successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to save game state: " + e.getMessage());
        }
    }

    @GetMapping("/load/{playerId}")
    public ResponseEntity<?> loadGame(@PathVariable String playerId) {
        try {
            GameState gameState = storageService.loadGameState(playerId);
            if (gameState == null) return ResponseEntity.status(404).body(null);
            return ResponseEntity.ok(gameState);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
