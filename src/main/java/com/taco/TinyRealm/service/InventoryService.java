package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private EventService eventService;

    public Item addItem(String playerId, String type, int quantity, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (gameState.getInventory() == null) gameState.setInventory(new java.util.ArrayList<>());
        Item existingItem = gameState.getInventory().stream()
                .filter(item -> item.getType().equals(type))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            eventService.addEvent(playerId, "item_added", "Added " + quantity + " " + type, isTest);
        } else {
            Item newItem = new Item();
            newItem.setId(UUID.randomUUID().toString());
            newItem.setType(type);
            newItem.setQuantity(quantity);
            gameState.getInventory().add(newItem);
            eventService.addEvent(playerId, "item_added", "Added " + quantity + " " + type, isTest);
        }
        storageService.saveGameState(playerId, gameState, isTest);
        return existingItem != null ? existingItem : gameState.getInventory().get(gameState.getInventory().size() - 1);
    }

    public Item removeItem(String playerId, String itemId, int quantity, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (gameState.getInventory() == null) gameState.setInventory(new java.util.ArrayList<>());
        Item item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (item.getQuantity() < quantity) throw new IllegalArgumentException("Insufficient item quantity");
        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) gameState.getInventory().remove(item);
        storageService.saveGameState(playerId, gameState, isTest);
        eventService.addEvent(playerId, "item_removed", "Removed " + quantity + " " + item.getType(), isTest);
        return item;
    }

    public List<Item> getInventory(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getInventory();
    }
}
