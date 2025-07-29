package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Building;
import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BuildingService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TerrainService terrainService; // 新增：地形服務
    @Autowired
    private TechnologyService technologyService; // 新增：科技服務
    @Autowired
    private TaskService taskService; // 新增：任務服務

    private static final int BARRACKS_GOLD_COST = 50;
    private static final int BARRACKS_WOOD_COST = 20;
    private static final int UPGRADE_GOLD_COST = 30;
    private static final int UPGRADE_WOOD_COST = 10;

    public Building createBuilding(String playerId, String type, int x, int y, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        // 檢查地形位置
        if (!terrainService.isPositionValid(playerId, x, y)) throw new IllegalArgumentException("Invalid or occupied position");
        // 檢查科技要求（例如兵營需要 basic_military 科技）
        if (type.equals("barracks")) {
            boolean hasTech = gameState.getTechnologies().stream()
                    .anyMatch(t -> t.getType().equals("basic_military"));
            if (!hasTech) throw new IllegalArgumentException("Basic military technology required");
        }
        Resource resources = gameState.getResources();
        if (resources == null || resources.getGold() < BARRACKS_GOLD_COST || resources.getWood() < BARRACKS_WOOD_COST)
            throw new IllegalArgumentException("Insufficient resources");
        resourceService.addResources(playerId, -BARRACKS_GOLD_COST, -BARRACKS_WOOD_COST, isTest);
        terrainService.occupyPosition(playerId, x, y, isTest); // 佔用地形
        Building building = new Building();
        building.setId(UUID.randomUUID().toString());
        building.setType(type);
        building.setLevel(1);
        building.setX(x);
        building.setY(y);

        if (gameState.getBuildings() == null) gameState.setBuildings(new java.util.ArrayList<>());
        gameState.getBuildings().add(building);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "building_created", "Created " + type + " at (" + x + "," + y + ")", isTest);

        // 更新任務進度
        if (type.equals("barracks")) {
            if (gameState.getTasks() != null) {
                gameState.getTasks().stream()
                    .filter(t -> t.getType().equals("build_barracks") && "ACTIVE".equals(t.getStatus()))
                    .forEach(t -> {
                        try {
                            taskService.updateTaskProgress(playerId, t.getId(), 1,isTest);
                        } catch (IOException e) {
                            // ignore
                        }
                    });
            }
        }
        return building;
    }

    public Building upgradeBuilding(String playerId, String buildingId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        Building building = gameState.getBuildings().stream()
                .filter(b -> b.getId().equals(buildingId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));
        Resource resources = gameState.getResources();
        if (resources == null || resources.getGold() < UPGRADE_GOLD_COST || resources.getWood() < UPGRADE_WOOD_COST)
            throw new IllegalArgumentException("Insufficient resources");
        resourceService.addResources(playerId, -UPGRADE_GOLD_COST, -UPGRADE_WOOD_COST, isTest);
        building.setLevel(building.getLevel() + 1);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "building_upgraded", "Upgraded " + building.getType() + " to level " + building.getLevel(), isTest);
        return building;
    }

    public List<Building> getBuildings(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getBuildings();
    }
}
