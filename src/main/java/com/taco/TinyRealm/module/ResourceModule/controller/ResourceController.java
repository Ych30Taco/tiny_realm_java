package com.taco.TinyRealm.module.ResourceModule.controller;

import com.taco.TinyRealm.module.ResourceModule.model.Resource;
import com.taco.TinyRealm.module.ResourceModule.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/{playerId}/add")
    public ResponseEntity<?> addResources(@PathVariable String playerId, @RequestParam int gold, @RequestParam int wood) {
        try {
            Resource resources = resourceService.addResources(playerId, gold, wood,false);
            return ResponseEntity.ok(resources);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getResources(@PathVariable String playerId) {
        try {
            Resource resources = resourceService.getResources(playerId, false);
            if (resources == null) return ResponseEntity.status(404).body(null);
            return ResponseEntity.ok(resources);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
