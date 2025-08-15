/* package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Alliance;
import com.taco.TinyRealm.service.AllianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/alliance")
public class AllianceController {
    @Autowired
    private AllianceService allianceService;

    @PostMapping("/{playerId}/create")
    public ResponseEntity<?> createAlliance(@PathVariable String playerId, @RequestParam String allianceName) {
        try {
            Alliance alliance = allianceService.createAlliance(playerId, allianceName,false);
            return ResponseEntity.ok(alliance);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{playerId}/join")
    public ResponseEntity<?> joinAlliance(@PathVariable String playerId, @RequestParam String allianceId) {
        try {
            Alliance alliance = allianceService.joinAlliance(playerId, allianceId,false);
            return ResponseEntity.ok(alliance);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{playerId}/leave")
    public ResponseEntity<?> leaveAlliance(@PathVariable String playerId) {
        try {
            allianceService.leaveAlliance(playerId);
            return ResponseEntity.ok("Left alliance successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to leave alliance: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{playerId}/donate")
    public ResponseEntity<?> donateResources(@PathVariable String playerId,
                                             @RequestParam String allianceId,
                                             @RequestParam int gold,
                                             @RequestParam int wood) {
        try {
            allianceService.donateResources(playerId, allianceId, gold, wood,false);
            return ResponseEntity.ok("Donated resources successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to donate resources: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{playerId}/message")
    public ResponseEntity<?> sendAllianceMessage(@PathVariable String playerId,
                                                 @RequestParam String allianceId,
                                                 @RequestParam String message) {
        try {
            allianceService.sendAllianceMessage(playerId, allianceId, message,false);
            return ResponseEntity.ok("Message sent successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{allianceId}")
    public ResponseEntity<?> getAlliance(@PathVariable String allianceId) {
        try {
            Alliance alliance = allianceService.getAlliance(allianceId,false);
            return ResponseEntity.ok(alliance);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
 */