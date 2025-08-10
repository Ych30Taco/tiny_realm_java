package com.taco.TinyRealm.module.resourceModule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/types")
    public ResponseEntity<?> getAllResourceTypes() {
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取資源類型成功", "data", resourceService.getAllResourceTypes()));
    }
    
    @PostMapping("/typeById")
    public ResponseEntity<?> getResourceTypeById(@RequestBody Map<String, Object> body) {
        String resourceID = (String) body.get("resourceID");
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取資源類型成功", "data", resourceService.getResourceTypeById(resourceID)));
    }

    /**
     * 新增資源給指定玩家
     * 
     * API 範例:
     * POST /api/resource/add
     * Content-Type: application/json
     * 
     * {
     *   "playerId": "player123",
     *   "addResource": {
     *     "gold": 100,
     *     "wood": 50
     *   }
     * }
     */
    @PostMapping("/add")
    public ResponseEntity<?> addResources(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            Map<String, Integer> addResource = (Map<String, Integer>) body.get("resources");
            GameState resources = resourceService.addResources(playerId, addResource, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "資源添加成功", "data", resources));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }
    
    @PostMapping("/ded")
    public ResponseEntity<?> dedResources(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            Map<String, Integer> dedResource = (Map<String, Integer>) body.get("resources");
            GameState resources = resourceService.dedResources(playerId, dedResource, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "資源扣除成功", "data", resources));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "內部錯誤", "data", null));
        }
    }
    
/* 
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
