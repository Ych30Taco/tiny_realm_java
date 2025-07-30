package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Unit;
import com.taco.TinyRealm.module.ResourceModule.model.Resource;
import com.taco.TinyRealm.module.ResourceModule.service.ResourceService;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UnitService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TerrainService terrainService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TechnologyService technologyService; // 新增：科技服務

    private static final int SOLDIER_GOLD_COST = 20;
    private static final int SOLDIER_WOOD_COST = 10;

    public Unit createUnit(String playerId, String type, int count, int x, int y ,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        boolean hasBarracks = gameState.getBuildings().stream()
                .anyMatch(b -> b.getType().equals("barracks"));
        if (!hasBarracks) throw new IllegalArgumentException("Barracks required to create units");
        // 檢查科技要求（例如弓箭手需要 archery 科技）
        if (type.equals("archer")) {
            boolean hasTech = gameState.getTechnologies().stream()
                    .anyMatch(t -> t.getType().equals("archery"));
            if (!hasTech) throw new IllegalArgumentException("Archery technology required");
        }
        if (!terrainService.isPositionValid(playerId, x, y)) throw new IllegalArgumentException("Invalid or occupied position");
        Resource resources = gameState.getResources();
        if (resources == null || resources.getGold() < SOLDIER_GOLD_COST * count || resources.getWood() < SOLDIER_WOOD_COST * count)
            throw new IllegalArgumentException("Insufficient resources");
        resourceService.addResources(playerId, -SOLDIER_GOLD_COST * count, -SOLDIER_WOOD_COST * count, isTest);
        terrainService.occupyPosition(playerId, x, y,isTest);

        Unit unit = new Unit();
        unit.setId(UUID.randomUUID().toString());
        unit.setType(type);
        unit.setCount(count);
        unit.setX(x);
        unit.setY(y);

        if (gameState.getUnits() == null) gameState.setUnits(new java.util.ArrayList<>());
        gameState.getUnits().add(unit);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "unit_created", "Created " + count + " " + type + " at (" + x + "," + y + ")",isTest);
        return unit;
    }

    public Unit moveUnit(String playerId, String unitId, int newX, int newY) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Unit unit = gameState.getUnits().stream()
                .filter(u -> u.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));
        if (!terrainService.isPositionValid(playerId, newX, newY)) throw new IllegalArgumentException("Invalid or occupied position");
        terrainService.releasePosition(playerId, unit.getX(), unit.getY());
        terrainService.occupyPosition(playerId, newX, newY,false);
        unit.setX(newX);
        unit.setY(newY);
        storageService.saveGameState(playerId, gameState,false);

        eventService.addEvent(playerId, "unit_moved", "Moved " + unit.getType() + " to (" + newX + "," + newY + ")",false);
        return unit;
    }

    public List<Unit> getUnits(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (gameState.getUnits() == null) gameState.setUnits(new java.util.ArrayList<>());
        return gameState.getUnits();
    }
}
