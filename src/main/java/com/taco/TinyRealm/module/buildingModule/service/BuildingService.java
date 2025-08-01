package com.taco.TinyRealm.module.buildingModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.buildingModule.model.Building;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class BuildingService {
    private final ObjectMapper objectMapper;      // Jackson JSON 處理器

    private List<Building> buildingsList = Collections.emptyList();

    // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.building-path}")
    private org.springframework.core.io.Resource buildingPath;
         /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     */
    public BuildingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


        @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入建築模組 ----");
        try {
            try (InputStream is = buildingPath.getInputStream()) {
                buildingsList = objectMapper.readValue(is, new TypeReference<List<Building>>() {});
                String buildingNames = getBuildingName();
                System.out.println("---- 應用程式啟動中，已載入" + buildingNames + " ----");
            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入建築模組失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load resource.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入資源模組完成 ----");
    }
    public List<Building> getAllbuilding() {
        return buildingsList;
    }

    public Building getBuildingById(String buildingID) {
        return buildingsList.stream()
                .filter(r -> r.getId().equals(buildingID))
                .findFirst()
                .orElse(null);
    }
    public String getBuildingName() {
        List<Building> resourceList = buildingsList;
        String building_name = "";
        for (Building building : buildingsList) {
            building_name+=building.getName()+ " , ";
        }
        building_name+= "共"+resourceList.size()+"種建築";
        return building_name;
    }
    
    /* 
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
    }*/
}
 