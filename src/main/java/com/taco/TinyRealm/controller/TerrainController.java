package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Terrain;
import com.taco.TinyRealm.service.TerrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/terrain")
public class TerrainController {
    @Autowired
    private TerrainService terrainService;

    @PostMapping("/{playerId}/initialize")
    public ResponseEntity<?> initializeMap(@PathVariable String playerId) {
        try {
            terrainService.initializeMap(playerId);
            return ResponseEntity.ok("Map initialized successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to initialize map: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getMap(@PathVariable String playerId) {
        try {
            List<Terrain> map = terrainService.getMap(playerId);
            return ResponseEntity.ok(map);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
