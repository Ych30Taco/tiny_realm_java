package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Resource;
import com.taco.TinyRealm.model.Unit;
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

    private static final int SOLDIER_GOLD_COST = 20;
    private static final int SOLDIER_WOOD_COST = 10;

    public Unit createUnit(String playerId, String type, int count, int x, int y) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        boolean hasBarracks = gameState.getBuildings().stream()
                .anyMatch(b -> b.getType().equals("barracks"));
        if (!hasBarracks) throw new IllegalArgumentException("Barracks required to create units");
        if (!terrainService.isPositionValid(playerId, x, y)) throw new IllegalArgumentException("Invalid or occupied position");
        Resource resources = gameState.getResources();
        if (resources == null || resources.getGold() < SOLDIER_GOLD_COST * count || resources.getWood() < SOLDIER_WOOD_COST * count)
            throw new IllegalArgumentException("Insufficient resources");
        resourceService.addResources(playerId, -SOLDIER_GOLD_COST * count, -SOLDIER_WOOD_COST * count);
        terrainService.occupyPosition(playerId, x, y);

        Unit unit = new Unit();
        unit.setId(UUID.randomUUID().toString());
        unit.setType(type);
        unit.setCount(count);
        unit.setX(x);
        unit.setY(y);

        gameState.getUnits().add(unit);
        storageService.saveGameState(playerId, gameState);

        eventService.addEvent(playerId, "unit_created", "Created " + count + " " + type + " at (" + x + "," + y + ")");
        return unit;
    }

    public Unit moveUnit(String playerId, String unitId, int newX, int newY) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Unit unit = gameState.getUnits().stream()
                .filter(u -> u.getId().equals(unitId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));
        if (!terrainService.isPositionValid(playerId, newX, newY)) throw new IllegalArgumentException("Invalid or occupied position");
        terrainService.releasePosition(playerId, unit.getX(), unit.getY());
        terrainService.occupyPosition(playerId, newX, newY);
        unit.setX(newX);
        unit.setY(newY);
        storageService.saveGameState(playerId, gameState);

        eventService.addEvent(playerId, "unit_moved", "Moved " + unit.getType() + " to (" + newX + "," + newY + ")");
        return unit;
    }

    public List<Unit> getUnits(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getUnits();
    }
}
