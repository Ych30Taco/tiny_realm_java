package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Player;
import com.taco.TinyRealm.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResourceService {
    @Autowired
    private StorageService storageService;

    public Resource addResources(String playerId, int gold, int wood, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found");
        }
        Resource resources = gameState.getResources();
        if (resources == null) {
            resources = new Resource();
            gameState.setResources(resources);
        }
        resources.setGold(Math.max(0, resources.getGold() + gold));
        resources.setWood(Math.max(0, resources.getWood() + wood));
        storageService.saveGameState(playerId, gameState, isTest);
        return resources;
    }

    public Resource getResources(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        return gameState != null ? gameState.getResources() : null;
    }
}
