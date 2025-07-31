package com.taco.TinyRealm.module.resourceModule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taco.TinyRealm.module.resourceModule.service.ResourceService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllResourceTypes() {
        return ResponseEntity.ok(resourceService.getAllResourceTypes());
    }
    @PostMapping("/typeById")
    public ResponseEntity<?> getResourceTypeById(@RequestBody Map<String, Object> body) {
        String resourceID = (String) body.get("resourceID");
        return ResponseEntity.ok(resourceService.getResourceTypeById(resourceID ));
    }

/* 
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
    }*/


}
