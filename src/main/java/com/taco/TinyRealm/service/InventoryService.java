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

    public Item addItem(String playerId, String type, int quantity) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Item existingItem = gameState.getInventory().stream()
                .filter(item -> item.getType().equals(type))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            eventService.addEvent(playerId, "item_added", "Added " + quantity + " " + type);
        } else {
            Item newItem = new Item();
            newItem.setId(UUID.randomUUID().toString());
            newItem.setType(type);
            newItem.setQuantity(quantity);
            gameState.getInventory().add(newItem);
            eventService.addEvent(playerId, "item_added", "Added " + quantity + " " + type);
        }
        storageService.saveGameState(playerId, gameState);
        return existingItem != null ? existingItem : gameState.getInventory().get(gameState.getInventory().size() - 1);
    }

    public Item removeItem(String playerId, String itemId, int quantity) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Item item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (item.getQuantity() < quantity) throw new IllegalArgumentException("Insufficient item quantity");
        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) gameState.getInventory().remove(item);
        storageService.saveGameState(playerId, gameState);
        eventService.addEvent(playerId, "item_removed", "Removed " + quantity + " " + item.getType());
        return item;
    }

    public List<Item> getInventory(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getInventory();
    }
}
