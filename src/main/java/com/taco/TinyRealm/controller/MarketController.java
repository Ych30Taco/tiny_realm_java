package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.MarketListing;
import com.taco.TinyRealm.model.Resource;
import com.taco.TinyRealm.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/market")
public class MarketController {
    @Autowired
    private MarketService marketService;

    @PostMapping("/{playerId}/list/resource")
    public ResponseEntity<?> listResource(@PathVariable String playerId,
                                          @RequestParam int gold,
                                          @RequestParam int wood,
                                          @RequestBody Resource price) {
        try {
            MarketListing listing = marketService.listResource(playerId, gold, wood, price,false);
            return ResponseEntity.ok(listing);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/{playerId}/list/item")
    public ResponseEntity<?> listItem(@PathVariable String playerId,
                                      @RequestParam String itemId,
                                      @RequestParam int quantity,
                                      @RequestBody Resource price) {
        try {
            MarketListing listing = marketService.listItem(playerId, itemId, quantity, price,false);
            return ResponseEntity.ok(listing);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PostMapping("/{playerId}/buy")
    public ResponseEntity<?> buyListing(@PathVariable String playerId,
                                        @RequestParam String listingId) {
        try {
            MarketListing listing = marketService.buyListing(playerId, listingId ,false);
            return ResponseEntity.ok(listing);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<?> getMarketListings() {
        try {
            List<MarketListing> listings = marketService.getMarketListings(false);
            return ResponseEntity.ok(listings);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
