/* package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Unit;
import com.taco.TinyRealm.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/unit")
public class UnitController {
    @Autowired
    private UnitService unitService;

    @PostMapping("/{playerId}/create")
    public ResponseEntity<?> createUnit(@PathVariable String playerId,
                                        @RequestParam String type,
                                        @RequestParam int count,
                                        @RequestParam int x,
                                        @RequestParam int y) {
        try {
            Unit unit = unitService.createUnit(playerId, type, count, x, y,false);
            return ResponseEntity.ok(unit);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{playerId}/move/{unitId}")
    public ResponseEntity<?> moveUnit(@PathVariable String playerId,
                                      @PathVariable String unitId,
                                      @RequestParam int newX,
                                      @RequestParam int newY) {
        try {
            Unit unit = unitService.moveUnit(playerId, unitId, newX, newY);
            return ResponseEntity.ok(unit);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getUnits(@PathVariable String playerId) {
        try {
            List<Unit> units = unitService.getUnits(playerId);
            return ResponseEntity.ok(units);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
 */