package com.taco.TinyRealm.module.terrainModule.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.taco.TinyRealm.module.terrainModule.service.TerrainService;
@RestController
@RequestMapping("/api/terrain")
public class TerrainController {
    @Autowired
    private TerrainService terrainService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllerrain() {
        return ResponseEntity.ok(terrainService.getAllerrain());
    }
    @PostMapping("/typeById")
    public ResponseEntity<?> getResourceTypeById(@RequestBody Map<String, Object> body) {
        String terrainID = (String) body.get("terrainID");
        return ResponseEntity.ok(terrainService.getTerrainTypeById(terrainID ));
    }
}
