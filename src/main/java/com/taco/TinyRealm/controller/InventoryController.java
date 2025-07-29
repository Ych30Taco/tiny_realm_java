package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Item;
import com.taco.TinyRealm.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/{playerId}/add")
    public ResponseEntity<?> addItem(@PathVariable String playerId,
                                     @RequestParam String type,
                                     @RequestParam int quantity) {
        try {
            Item item = inventoryService.addItem(playerId, type, quantity,false);
            return ResponseEntity.ok(item);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/{playerId}/remove/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable String playerId,
                                        @PathVariable String itemId,
                                        @RequestParam int quantity) {
        try {
            Item item = inventoryService.removeItem(playerId, itemId, quantity,false);
            return ResponseEntity.ok(item);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getInventory(@PathVariable String playerId) {
        try {
            List<Item> inventory = inventoryService.getInventory(playerId,false);
            return ResponseEntity.ok(inventory);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
