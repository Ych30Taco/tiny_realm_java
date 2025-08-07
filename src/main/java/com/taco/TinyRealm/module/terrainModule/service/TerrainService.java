package com.taco.TinyRealm.module.terrainModule.service;

import com.taco.TinyRealm.module.terrainModule.model.Terrain;

import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.buildingModule.model.Building;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class TerrainService {
    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private List<Terrain> terrainsList = Collections.emptyList();
    @Autowired
    private StorageService storageService;

    @Value("${app.data.terrain-path}")
    private org.springframework.core.io.Resource terrainPath;

    private static final int MAP_SIZE = 10;

             /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     */
    public TerrainService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入地形模組 ----");
        try {
            try (InputStream is = terrainPath.getInputStream()) {
                terrainsList = objectMapper.readValue(is, new TypeReference<List<Terrain>>() {});
                String terrainNames = getTerrainName();
                System.out.println("---- 應用程式啟動中，已載入" + terrainNames + " ----");
            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入地形模組失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load resource.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入地形模組完成 ----");
    }
    public String getTerrainName() {
        List<Terrain> resourceList = terrainsList;
        String terrain_name = "";
        for (Terrain terrain : terrainsList) {
            terrain_name+=terrain.getName()+ " , ";
        }
        terrain_name+= "共"+resourceList.size()+"種地形";
        return terrain_name;
    }
    public List<Terrain> getAllerrain() {
        return terrainsList;
    }
    
    public Terrain getTerrainTypeById(String terrainID) {
        return terrainsList.stream()
                .filter(r -> r.getId().equals(terrainID))
                .findFirst()
                .orElse(null);
    }
/* 
    public void initializeMap(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
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
            storageService.saveGameState(playerId, gameState, false);
        }
    }

    public boolean isPositionValid(String playerId, int x, int y) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (x < 0 || x >= MAP_SIZE || y < 0 || y >= MAP_SIZE) return false;
        return gameState.getTerrains().stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .map(Terrain::isOccupied)
                .findFirst()
                .orElse(false) == false;
    }

    public void occupyPosition(String playerId, int x, int y, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Terrain terrain = gameState.getTerrains().stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Terrain not found"));
        if (terrain.isOccupied()) throw new IllegalArgumentException("Position already occupied");
        terrain.setOccupied(true);
        storageService.saveGameState(playerId, gameState, isTest);
    }

    public void releasePosition(String playerId, int x, int y) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Terrain terrain = gameState.getTerrains().stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Terrain not found"));
        terrain.setOccupied(false);
        storageService.saveGameState(playerId, gameState, false);
    }

    public List<Terrain> getMap(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId,false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getTerrains();
    }*/
}
 