package com.taco.TinyRealm.module.buildingModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.buildingModule.model.Building;
import com.taco.TinyRealm.module.buildingModule.model.BuildingStatus;
import com.taco.TinyRealm.module.buildingModule.model.LevelData;
import com.taco.TinyRealm.module.buildingModule.model.PlayerBuliding;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.io.IOException;

@Service
public class BuildingService {
    private final ObjectMapper objectMapper;      // Jackson JSON 處理器

    private List<Building> buildingsList = Collections.emptyList();

    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TerrainMapService terrainMapService;

    @Autowired
    private ResourceLoader resourceLoader;

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
            loadBuildingTypes(buildingPath);
            String buildingNames = getBuildingName();
            System.out.println("---- 應用程式啟動中，已載入" + buildingNames + " ----");
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入建築模組失敗 ----");
            e.printStackTrace();
            throw new RuntimeException("Failed to load building.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入建築模組完成 ----");
    }

    private void loadBuildingTypes(org.springframework.core.io.Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            buildingsList = objectMapper.readValue(is, new TypeReference<List<Building>>() {});
        }
    }

    public void reloadBuildings(String overridePath) throws IOException {
        org.springframework.core.io.Resource target = buildingPath;
        if (overridePath != null && !overridePath.isBlank()) {
            target = resourceLoader.getResource(overridePath);
        }
        loadBuildingTypes(target);
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
    
    
    public GameState createBuilding(String playerId, String buildingId, int x, int y, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        
        // 檢查地形位置是否可建造
        if (!terrainMapService.canOccupyPosition(x, y, playerId)) {
            throw new IllegalArgumentException("Invalid or occupied position at (" + x + ", " + y + ")");
        }
        
        // 檢查科技要求
        /*if (buildingId.equals("barracks")) {
            boolean hasTech = gameState.getTechnologies().stream()
                    .anyMatch(t -> t.getType().equals("basic_military"));
            if (!hasTech) throw new IllegalArgumentException("Basic military technology required");
        }*/
        
        if(gameState.getBuildings().containsKey(buildingId)){
            throw new IllegalArgumentException("Building already exists: " + buildingId);
        }
        LevelData levelData = getBuildingById(buildingId).getLevels().get(0);
        Map<String, String> prerequisites = levelData.getPrerequisites();
        Map<String, Integer> buildingCost = levelData.getCost();
        PlayerResource resources = gameState.getResources();
        //檢查建築要求
        for (String prerequisite : prerequisites.keySet()) {
            if (!gameState.getBuildings().containsKey(prerequisite)) {
                throw new IllegalArgumentException("Prerequisite building not found: " + prerequisite);
            }
        }
        //檢查資源是否足夠
        resources.getNowAmount().forEach((key, value) -> {
            if (value < buildingCost.getOrDefault(key, 0)) {
                throw new IllegalArgumentException("Insufficient resources for building: " + key);
            }
        });
        
        resourceService.dedResources(playerId, buildingCost, isTest);
        
        // 佔用地形位置
        if (!terrainMapService.buildOnPosition(x, y, buildingId, playerId)) {
            throw new IllegalArgumentException("Failed to occupy position at (" + x + ", " + y + ")");
        }
        
        PlayerBuliding playerBuilding = new PlayerBuliding();
        playerBuilding.setOwnerId(playerId);
        playerBuilding.setBuildingId(buildingId);
        playerBuilding.setInstanceId(UUID.randomUUID().toString());
        playerBuilding.setLevel(1);
        playerBuilding.setStatus(BuildingStatus.BUILDING);
        playerBuilding.setPositionX(x);
        playerBuilding.setPositionY(y);
        playerBuilding.setBuildStartTime(System.currentTimeMillis());
        playerBuilding.setBuildEndTime(System.currentTimeMillis() + levelData.getBuildTime());

        gameState.getBuildings().put(buildingId, playerBuilding);
        gameState.getResources().setProductionRates(calculateProductionRates(playerId, isTest));//更新資源生產速率
        storageService.saveGameState(playerId, gameState, "建造建築",isTest);
        
        /*eventService.addEvent(playerId, "building_created", "Created " + buildingId + " at (" + x + "," + y + ")", isTest);

        // 更新任務進度
        if (buildingId.equals("barracks")) {
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
        }*/
        return gameState;
    }
     
    public GameState upgradeBuilding(String playerId, String buildingId, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if(!gameState.getBuildings().containsKey(buildingId)){
            throw new IllegalArgumentException("Building does not exist: " + buildingId);
        }
        PlayerBuliding playerBuilding = gameState.getBuildings().get(buildingId);
        int nowlevel = playerBuilding.getLevel();// 取得當前等級 ,與下一等級陣列一致，等級1的條件為陣列[1]
        LevelData levelData = getBuildingById(buildingId).getLevels().get(nowlevel);
        Map<String, String> prerequisites = levelData.getPrerequisites();// 取得升級要求
        Map<String, Integer> buildingCost = levelData.getCost();// 取得升級所需資源
        PlayerResource resources = gameState.getResources();// 取得目前玩家資源
        //檢查建築要求
        for (String prerequisite : prerequisites.keySet()) {
            if (!gameState.getBuildings().containsKey(prerequisite) || gameState.getBuildings().get(prerequisite).getLevel() != nowlevel) { 
                // 如果要求的建築不存在或等級不符合，則拋出異常
                throw new IllegalArgumentException("Prerequisite building not found or level mismatch: " + prerequisite);
            }
        }
         //檢查資源是否足夠
        resources.getNowAmount().forEach((key, value) -> {
            // 如果資源不足，則拋出異常
            if (value < buildingCost.getOrDefault(key, nowlevel)) {
                throw new IllegalArgumentException("Insufficient resources for building: " + key);
            }
        });
        resourceService.dedResources(playerId, buildingCost, isTest);
        playerBuilding.setLevel(playerBuilding.getLevel() + 1);
        playerBuilding.setStatus(BuildingStatus.UPGRADE);
        playerBuilding.setBuildStartTime(System.currentTimeMillis());
        playerBuilding.setBuildEndTime(System.currentTimeMillis() + levelData.getBuildTime());
        gameState.getBuildings().put(buildingId, playerBuilding);
        gameState.getResources().setProductionRates(calculateProductionRates(playerId, isTest));//更新資源生產速率
        storageService.saveGameState(playerId, gameState,"升級建築", isTest);

        //eventService.addEvent(playerId, "building_upgraded", "Upgraded " + buildingId + " to level " + playerBuilding.getLevel(), isTest);
        return gameState;
    }

    /**
     * 計算玩家的資源生產速率
     */
    public Map<String, Integer> calculateProductionRates(String playerId, boolean isTest) throws IOException {
        Map<String, Integer> productionRates = new HashMap<>();
        
        // 獲取玩家建築
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null || gameState.getBuildings() == null) {
            return productionRates;
        }
        productionRates = gameState.getResources().getProductionRates();   
        Map<String, PlayerBuliding> playerBuildings = gameState.getBuildings();
        
        // 遍歷玩家建築，計算總生產速率
        for (Map.Entry<String, PlayerBuliding> entry : playerBuildings.entrySet()) {
            String buildingId = entry.getKey();
            PlayerBuliding playerBuilding = entry.getValue();
            
            // 獲取建築類型定義
            Building buildingType = getBuildingById(buildingId);
            if (buildingType == null) {
                continue;
            }
            
            // 檢查建築是否生產資源
            String resourceType = buildingType.getResourceType();
            if (resourceType != null && !resourceType.isEmpty() && resourceType.equals(resourceType)) {
                // 根據建築等級獲取生產速率
                int level = playerBuilding.getLevel();
                int rate = getBuildingProductionRate(buildingType, level);
                // 累加生產速率
                productionRates.put(resourceType, rate);
            }
        }
        return productionRates;
    }

        /**
     * 根據建築類型和等級獲取生產速率
     */
    private int getBuildingProductionRate(Building buildingType, int level) {
        // 根據建築等級獲取生產速率
        if (buildingType.getLevels() != null) {
            for (LevelData levelData : buildingType.getLevels()) {
                if (levelData.getLevel() == level) {
                    return levelData.getProductionRate();
                }
            }
        }
        return 0;
    }


    @Scheduled(initialDelay = 0, fixedRate = 1000) // 每1分鐘
    public void updateAllPlayersBulidingStatus() {
        //獲取所有上線玩家ID
        for (String playerId : storageService.getOnlineGameStateIdList()) {
            try {
                updatePlayerBuildingStatus(playerId);
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
    }
    private void updatePlayerBuildingStatus(String playerId) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found");
        }
        gameState.getBuildings().forEach((buildingId, playerBuilding) -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= playerBuilding.getBuildEndTime()) {
                playerBuilding.setStatus(BuildingStatus.IDLE);
            } 
        });
        
        //storageService.saveGameState(playerId, gameState, false);
    }

    /**
     * 拆除建築並釋放地形
     * @param playerId 玩家ID
     * @param buildingId 建築ID
     * @param isTest 是否為測試模式
     * @return 更新後的遊戲狀態
     */
    public GameState removeBuilding(String playerId, String buildingId, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        
        if (!gameState.getBuildings().containsKey(buildingId)) {
            throw new IllegalArgumentException("Building does not exist: " + buildingId);
        }
        
        PlayerBuliding playerBuilding = gameState.getBuildings().get(buildingId);
        int x = playerBuilding.getPositionX();
        int y = playerBuilding.getPositionY();
        
        // 從遊戲狀態中移除建築
        gameState.getBuildings().remove(buildingId);
        
        // 釋放地形位置
        if (!terrainMapService.removeBuildingFromPosition(x, y, playerId)) {
            throw new IllegalArgumentException("Failed to release position at (" + x + ", " + y + ")");
        }
        
        // 更新資源生產速率
        gameState.getResources().setProductionRates(calculateProductionRates(playerId, isTest));
        
        // 保存遊戲狀態
        storageService.saveGameState(playerId, gameState,"拆除建築並釋放地形", isTest);
        
        return gameState;
    }
}
