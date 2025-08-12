package com.taco.TinyRealm.module.inventoryModule.controller;

import com.taco.TinyRealm.module.inventoryModule.model.PlayerItem;
import com.taco.TinyRealm.module.inventoryModule.model.ItemType;
import com.taco.TinyRealm.module.inventoryModule.service.InventoryService;
import com.taco.TinyRealm.module.storageModule.model.GameState;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 背包控制器
 * 提供背包管理的RESTful API端點
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * 獲取所有物品類型
     * GET /api/inventory/types
     */
    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getAllItemTypes() {
        try {
            List<ItemType>  itemTypes = inventoryService.getAllItemTypes();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved all item types");
            response.put("data", itemTypes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve item types: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 獲取特定物品類型
     * GET /api/inventory/types/{type}
     */
    @GetMapping("/type")
    public ResponseEntity<Map<String, Object>> getItemTypeById(@RequestBody Map<String, Object> body) {
        try {
            String itemType = (String) body.get("itemType");
            ItemType itemTypeResult = inventoryService.getItemTypeByType(itemType);
            Map<String, Object> response = new HashMap<>();
            if (itemType != null) {
                response.put("success", true);
                response.put("message", "Successfully retrieved item type: " + itemType);
                response.put("data", itemTypeResult);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Item type not found: " + itemType);
                response.put("data", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve item type: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 添加物品到背包
     * POST /api/inventory/add
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addItem(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            String itemType = (String) body.get("itemType");
            int quantity = (int) body.get("quantity");

            GameState playerItem = inventoryService.addItem(playerId, itemType, quantity, false);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully added " + quantity + " " + itemType + " to player " + playerId);
            response.put("data", playerItem);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to add item: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 從背包移除物品
     * DELETE /api/inventory/remove
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeItem(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            String playerItemId = (String) body.get("playerItemId");
            int quantity = (int) body.get("quantity");
            boolean isTest = (boolean) body.getOrDefault("isTest", false);
            
            GameState item = inventoryService.removeItem(playerId, playerItemId, quantity, isTest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully removed " + quantity + " " + playerItemId + " items from player " + playerId);
            response.put("data", item);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to remove item: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 獲取玩家背包
     * GET /api/inventory/{playerId}
     */
    /*@GetMapping("/{playerId}")
    public ResponseEntity<Map<String, Object>> getInventory(@PathVariable String playerId,
                                                            @RequestParam(defaultValue = "false") boolean isTest) {
        try {
            List<Item> inventory = inventoryService.getInventory(playerId, isTest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved inventory for player " + playerId);
            response.put("data", inventory);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve inventory: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/
    
    /**
     * 使用物品
     * PUT /api/inventory/use
     */
    @PutMapping("/use")
    public ResponseEntity<Map<String, Object>> useItem(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            String playerItemId = (String) body.get("playerItemId");
            int quantity = (int) body.get("quantity");
            boolean isTest = (boolean) body.getOrDefault("isTest", false);
            Map<String, Object> effects = inventoryService.useItem(playerId, playerItemId,quantity, isTest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully used item " + playerItemId);
            response.put("data", effects);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to use item: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 修復物品
     * PUT /api/inventory/repair
     */
    /*@PutMapping("/repair")
    public ResponseEntity<Map<String, Object>> repairItem(@RequestParam String playerId,
                                                          @RequestParam String itemId,
                                                          @RequestParam int repairAmount,
                                                          @RequestParam(defaultValue = "false") boolean isTest) {
        try {
            Item item = inventoryService.repairItem(playerId, itemId, repairAmount, isTest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully repaired item " + itemId);
            response.put("data", item);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to repair item: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/
    
    /**
     * 獲取背包統計信息
     * GET /api/inventory/stats/{playerId}
     */
    /*@GetMapping("/stats/{playerId}")
    public ResponseEntity<Map<String, Object>> getInventoryStats(@PathVariable String playerId,
                                                                 @RequestParam(defaultValue = "false") boolean isTest) {
        try {
            Map<String, Object> stats = inventoryService.getInventoryStats(playerId, isTest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully retrieved inventory stats for player " + playerId);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve inventory stats: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/
    
    /**
     * 搜索背包物品
     * GET /api/inventory/search/{playerId}
     */
    /*@GetMapping("/search/{playerId}")
    public ResponseEntity<Map<String, Object>> searchInventory(@PathVariable String playerId,
                                                               @RequestParam(required = false) String searchTerm,
                                                               @RequestParam(required = false) String category,
                                                               @RequestParam(required = false) String rarity,
                                                               @RequestParam(defaultValue = "false") boolean isTest) {
        try {
            List<Item> results = inventoryService.searchInventory(playerId, searchTerm, category, rarity, isTest);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Successfully searched inventory for player " + playerId);
            response.put("data", results);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to search inventory: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/
    
    /**
     * 獲取特定物品
     * GET /api/inventory/{playerId}/{itemId}
     */
    /*@GetMapping("/{playerId}/{itemId}")
    public ResponseEntity<Map<String, Object>> getItem(@PathVariable String playerId,
                                                       @PathVariable String itemId,
                                                       @RequestParam(defaultValue = "false") boolean isTest) {
        try {
            List<Item> inventory = inventoryService.getInventory(playerId, isTest);
            Item item = inventory.stream()
                    .filter(i -> i.getId().equals(itemId))
                    .findFirst()
                    .orElse(null);
            
            Map<String, Object> response = new HashMap<>();
            if (item != null) {
                response.put("success", true);
                response.put("message", "Successfully retrieved item " + itemId);
                response.put("data", item);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Item not found: " + itemId);
                response.put("data", null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Invalid request: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(400).body(response);
        } catch (IOException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve item: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(500).body(response);
        }
    }*/
}
