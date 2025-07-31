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
            Player player = playerService.createPlayer(name,false);
            return ResponseEntity.ok(player);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/userdata")
    public ResponseEntity<?> getPlayer(@RequestBody Map<String, Object> body) {
        try {
            String playid = (String) body.get("playid");
            GameState playdata = playerService.getPlayer(playid,false);
            if (playdata == null) return ResponseEntity.status(404).body(null);
            return ResponseEntity.ok(playdata);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
