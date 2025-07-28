package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Player;
import com.taco.TinyRealm.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/create")
    public ResponseEntity<?> createPlayer(@RequestParam String id, @RequestParam String name) {
        try {
            Player player = playerService.createPlayer(id, name);
            return ResponseEntity.ok(player);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable String id) {
        try {
            Player player = playerService.getPlayer(id);
            if (player == null) return ResponseEntity.status(404).body(null);
            return ResponseEntity.ok(player);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable String id, @RequestParam String name, @RequestParam int level) {
        try {
            Player player = playerService.updatePlayer(id, name, level);
            return ResponseEntity.ok(player);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
