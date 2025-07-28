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

    public Resource addResources(String playerId, int gold, int wood) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) {
            gameState = new GameState();
            gameState.setPlayer(new Player());
        }
        Resource resources = gameState.getResources();
        if (resources == null) {
            resources = new Resource();
            gameState.setResources(resources);
        }
        resources.setGold(resources.getGold() + gold);
        resources.setWood(resources.getWood() + wood);
        storageService.saveGameState(playerId, gameState);
        return resources;
    }

    public Resource getResources(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        return gameState != null ? gameState.getResources() : null;
    }
}
