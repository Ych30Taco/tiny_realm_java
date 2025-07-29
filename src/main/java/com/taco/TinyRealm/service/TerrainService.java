package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Terrain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TerrainService {
    @Autowired
    private StorageService storageService;

    private static final int MAP_SIZE = 10;

    public void initializeMap(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        List<Terrain> terrains = gameState.getTerrains();
        if (terrains.isEmpty()) {
            for (int x = 0; x < MAP_SIZE; x++) {
                for (int y = 0; y < MAP_SIZE; y++) {
                    Terrain terrain = new Terrain();
                    terrain.setX(x);
                    terrain.setY(y);
                    terrain.setType("plain");
                    terrain.setOccupied(false);
                    terrains.add(terrain);
                }
            }
            storageService.saveGameState(playerId, gameState);
        }
    }

    public boolean isPositionValid(String playerId, int x, int y) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (x < 0 || x >= MAP_SIZE || y < 0 || y >= MAP_SIZE) return false;
        return gameState.getTerrains().stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .map(Terrain::isOccupied)
                .findFirst()
                .orElse(false) == false;
    }

    public void occupyPosition(String playerId, int x, int y,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Terrain terrain = gameState.getTerrains().stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Terrain not found"));
        if (terrain.isOccupied()) throw new IllegalArgumentException("Position already occupied");
        terrain.setOccupied(true);
        storageService.saveGameState(playerId, gameState);
    }

    public void releasePosition(String playerId, int x, int y) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Terrain terrain = gameState.getTerrains().stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Terrain not found"));
        terrain.setOccupied(false);
        storageService.saveGameState(playerId, gameState);
    }

    public List<Terrain> getMap(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getTerrains();
    }
}
