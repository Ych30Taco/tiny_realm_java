package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Item;
import com.taco.TinyRealm.model.Trade;
import com.taco.TinyRealm.module.ResourceModule.model.Resource;
import com.taco.TinyRealm.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trade")
public class TradeController {
    @Autowired
    private TradeService tradeService;

    @PostMapping("/create")
    public ResponseEntity<?> createTrade(@RequestParam String initiatorId,
                                         @RequestParam String receiverId,
                                         @RequestBody Trade tradeRequest) {
        try {
            Trade trade = tradeService.createTrade(
                    initiatorId,
                    receiverId,
                    tradeRequest.getOfferedResources() != null ? tradeRequest.getOfferedResources() : new Resource(),
                    tradeRequest.getOfferedItems() != null ? tradeRequest.getOfferedItems() : new ArrayList<>(),
                    tradeRequest.getRequestedResources() != null ? tradeRequest.getRequestedResources() : new Resource(),
                    tradeRequest.getRequestedItems() != null ? tradeRequest.getRequestedItems() : new ArrayList<>()
            );
            return ResponseEntity.ok(trade);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{playerId}/accept/{tradeId}")
    public ResponseEntity<?> acceptTrade(@PathVariable String playerId,
                                         @PathVariable String tradeId) {
        try {
            Trade trade = tradeService.acceptTrade(playerId, tradeId);
            return ResponseEntity.ok(trade);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{playerId}/reject/{tradeId}")
    public ResponseEntity<?> rejectTrade(@PathVariable String playerId,
                                         @PathVariable String tradeId) {
        try {
            Trade trade = tradeService.rejectTrade(playerId, tradeId);
            return ResponseEntity.ok(trade);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getTrades(@PathVariable String playerId) {
        try {
            List<Trade> trades = tradeService.getTrades(playerId);
            return ResponseEntity.ok(trades);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
