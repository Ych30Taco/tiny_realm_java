package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Technology;
import com.taco.TinyRealm.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/technology")
public class TechnologyController {
    @Autowired
    private TechnologyService technologyService;

    @PostMapping("/{playerId}/research")
    public ResponseEntity<?> researchTechnology(@PathVariable String playerId,
                                                @RequestParam String type) {
        try {
            Technology technology = technologyService.researchTechnology(playerId, type,false);
            return ResponseEntity.ok(technology);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getTechnologies(@PathVariable String playerId) {
        try {
            List<Technology> technologies = technologyService.getTechnologies(playerId);
            return ResponseEntity.ok(technologies);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
