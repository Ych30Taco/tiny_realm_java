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
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取建築類型成功", "data", buildingService.getAllbuilding()));
    }

    @PostMapping("/typeById")
    public ResponseEntity<?> getResourceTypeById(@RequestBody Map<String, Object> body) {
        String buildingID = (String) body.get("buildingID");
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取建築類型成功", "data", buildingService.getBuildingById(buildingID)));
    }
     
    @PostMapping("/create")
    public ResponseEntity<?> createBuilding(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        String buildingId = (String) body.get("buildingId");
        int x = (int) body.get("x");
        int y = (int) body.get("y");
        try {
            GameState playerBuliding = buildingService.createBuilding(playerId, buildingId, x, y, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "建築建造成功", "data", playerBuliding));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", e.getMessage(), "data", null));
        }
    }
    
    @PutMapping("/upgrade")
    public ResponseEntity<?> upgradeBuilding(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        String buildingId = (String) body.get("buildingId");
        try {
            GameState building = buildingService.upgradeBuilding(playerId, buildingId, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "建築升級成功", "data", building));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", e.getMessage(), "data", null));
        }
    }
    
    /**
     * 拆除建築
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeBuilding(@RequestBody Map<String, Object> body) {
        String playerId = (String) body.get("playerId");
        String buildingId = (String) body.get("buildingId");
        try {
            GameState result = buildingService.removeBuilding(playerId, buildingId, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "建築拆除成功", "data", result));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", e.getMessage(), "data", null));
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
 