/* package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Battle;
import com.taco.TinyRealm.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/battle")
public class BattleController {
    @Autowired
    private BattleService battleService;

    @PostMapping("/{playerId}/start")
    public ResponseEntity<?> startBattle(@PathVariable String playerId,
                                         @RequestBody List<String> unitIds,
                                         @RequestParam String enemyType) {
        try {
            Battle battle = battleService.startBattle(playerId, unitIds, enemyType,false);
            return ResponseEntity.ok(battle);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getBattles(@PathVariable String playerId) {
        try {
            List<Battle> battles = battleService.getBattles(playerId);
            return ResponseEntity.ok(battles);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
 */