package com.taco.TinyRealm.module.buildingModule.controller;

import com.taco.TinyRealm.module.buildingModule.service.BuildingService;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/building")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllResourceTypes() {
        return ResponseEntity.ok(buildingService.getAllbuilding());
    }

    @PostMapping("/typeById")
    public ResponseEntity<?> getResourceTypeById(@RequestBody Map<String, Object> body) {
        String buildingID = (String) body.get("buildingID");
        return ResponseEntity.ok(buildingService.getBuildingById(buildingID ));
    }
     
    @PostMapping("/create")
    public ResponseEntity<?> createBuilding(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        String buildingId = (String) body.get("buildingId");
        int x = (int) body.get("x");
        int y = (int) body.get("y");
        try {
            GameState playerBuliding = buildingService.createBuilding(playerId, buildingId, x, y,false);
            return ResponseEntity.ok(playerBuliding);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    @PutMapping("/upgrade")
    public ResponseEntity<?> upgradeBuilding(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        String buildingId = (String) body.get("buildingId");
        try {
            GameState building = buildingService.upgradeBuilding(playerId, buildingId,false);
            return ResponseEntity.ok(building);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    //@PostMapping("/accelerate")

    /*
    @GetMapping("/{playerId}")
    public ResponseEntity<?> getBuildings(@PathVariable String playerId) {
        try {
            List<Building> buildings = buildingService.getBuildings(playerId ,false);
            return ResponseEntity.ok(buildings);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }*/
}
 