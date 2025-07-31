/* package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Building;
import com.taco.TinyRealm.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/building")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @PostMapping("/{playerId}/create")
    public ResponseEntity<?> createBuilding(@PathVariable String playerId,
                                            @RequestParam String type,
                                            @RequestParam int x,
                                            @RequestParam int y) {
        try {
            Building building = buildingService.createBuilding(playerId, type, x, y,false);
            return ResponseEntity.ok(building);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{playerId}/upgrade/{buildingId}")
    public ResponseEntity<?> upgradeBuilding(@PathVariable String playerId,
                                             @PathVariable String buildingId) {
        try {
            Building building = buildingService.upgradeBuilding(playerId, buildingId,false);
            return ResponseEntity.ok(building);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

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
    }
}
 */