package com.taco.TinyRealm.module.terrainMapModule.service;

import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.buildingModule.model.Building;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.terrainMapModule.model.GameMap;
import com.taco.TinyRealm.module.terrainMapModule.model.MapTile;
import com.taco.TinyRealm.module.terrainMapModule.model.Terrain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
public class TerrainMapService {
    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private List<Terrain> terrainsList = Collections.emptyList();
    private GameMap gameMap = new GameMap(); // 讓 gameMap 預設存在記憶體
    private Map<String, Object> enemyTypes = new HashMap<>(); // 野怪類型
    @Autowired
    private StorageService storageService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${app.data.terrain-path}")
    private org.springframework.core.io.Resource terrainPath;
    @Value("${app.data.map-path}")
    private org.springframework.core.io.Resource mapPath;
    @Value("${app.data.enemies-path}")
    private org.springframework.core.io.Resource enemiesPath;
    private static final String MAP_FILE_PATH = "src/main/resources/config/map/"; // 地圖存儲路徑
    private static final String MAP_FILE_NAME = "maps.json"; // 地圖文件名

    /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     */
    public TerrainMapService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @PostConstruct
    public void init() throws IOException {
        System.out.println("---- 應用程式啟動中，載入地形地圖模組 ----");
        try {
            loadTerrainTypes(terrainPath);
            String terrainNames = getTerrainName();
            System.out.println("---- 應用程式啟動中，已載入" + terrainNames + " ----");
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入地形失敗 ----");
            e.printStackTrace();
            throw new RuntimeException("Failed to load terrain.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入地形完成 ----");

        System.out.println("---- 應用程式啟動中，載入野怪類型... ----");
        try {
            loadEnemyTypes(enemiesPath);
            System.out.println("---- 應用程式啟動中，已載入野怪類型 ----");
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入野怪類型失敗 ----");
            e.printStackTrace();
        }

        System.out.println("---- 應用程式啟動中，載入地圖... ----");
        try {
            loadMap(mapPath);
            System.out.println("---- 應用程式啟動中，已載入地圖" + gameMap.getHeight() + "*" + gameMap.getWidth() + " ----");
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入地圖失敗，創建新地圖 ----");
            gameMap = new GameMap(10, 10);
            generateRandomMap(gameMap);
            System.out.println("---- 應用程式啟動中，重新載入地圖 ----");
            System.out.println("---- 應用程式啟動中，載入地圖完成 ----");
        }
        System.out.println("---- 應用程式啟動中，載入地形地圖模組完成 ----");
    }

    private void loadTerrainTypes(org.springframework.core.io.Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            terrainsList = objectMapper.readValue(is, new TypeReference<List<Terrain>>() {});
        }
    }

    private void loadMap(org.springframework.core.io.Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            gameMap = objectMapper.readValue(is, new TypeReference<GameMap>() {});
        }
    }

    private void loadEnemyTypes(org.springframework.core.io.Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            enemyTypes = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {});
        }
    }

    public void reloadTerrains(String overridePath) throws IOException {
        org.springframework.core.io.Resource target = terrainPath;
        if (overridePath != null && !overridePath.isBlank()) {
            target = resourceLoader.getResource(overridePath);
        }
        loadTerrainTypes(target);
    }

    public void reloadMap(String overridePath) throws IOException {
        org.springframework.core.io.Resource target = mapPath;
        if (overridePath != null && !overridePath.isBlank()) {
            target = resourceLoader.getResource(overridePath);
        }
        loadMap(target);
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
    public List<Terrain> getAllterrain() {
        return terrainsList;
    }
    
    public Terrain getTerrainTypeById(String terrainID) {
        return terrainsList.stream()
                .filter(r -> r.getId().equals(terrainID))
                .findFirst()
                .orElse(null);
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    /**
     * 生成隨機地圖（僅用於初始化新地圖）
     * @param map 要填充的 GameMap
     */
    private void generateRandomMap(GameMap map) throws IOException {
        System.out.println("---- 應用程式啟動中，創建新地圖中 ----");
        Random random = new Random();
        // 獲取所有可用地形
        List<Terrain> terrainList = getAllterrain();
        List<MapTile> tiles = map.getTiles();
        // 為每個格子分配隨機地形
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                MapTile tile = new MapTile();
                tile.setX(x);
                tile.setY(y);
                tile.setTerrain(terrainList.get(random.nextInt(terrainList.size())));
                tile.setOwnerId(null);
                tile.setBuildingId(null);
                tile.setUnitIds(new java.util.ArrayList<>());
                tiles.add(tile);
            }
        }
        
        map.setTiles(tiles);
        map.setId(UUID.randomUUID().toString()); // Changed from id() to setId()
        File dir = new File(MAP_FILE_PATH);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(MAP_FILE_PATH + MAP_FILE_NAME), gameMap);
        System.out.println("---- 應用程式啟動中，創建新地圖成功 ----");
    }

    /**
     * 生成隨機地圖預覽（不保存到文件）
     * @param width 地圖寬度
     * @param height 地圖高度
     * @return 生成的隨機地圖
     */
    public GameMap generateRandomMapPreview(int width, int height) {
        System.out.println("---- 生成隨機地圖預覽 " + width + "x" + height + " ----");
        Random random = new Random();
        GameMap previewMap = new GameMap(width, height);
        previewMap.setId(UUID.randomUUID().toString());
        
        // 獲取所有可用地形
        List<Terrain> terrainList = getAllterrain();
        List<MapTile> tiles = new java.util.ArrayList<>();
        
        // 為每個格子分配隨機地形
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                MapTile tile = new MapTile();
                tile.setX(x);
                tile.setY(y);
                tile.setTerrain(terrainList.get(random.nextInt(terrainList.size())));
                tile.setOwnerId(null);
                tile.setBuildingId(null);
                tile.setUnitIds(new java.util.ArrayList<>());
                
                // 添加野怪生成邏輯
                if (random.nextDouble() < 0.15) { // 15% 機率生成野怪
                    String enemyType = generateRandomEnemyType(random);
                    if (enemyType != null) {
                        tile.setEnemyType(enemyType);
                        tile.setHasEnemy(true);
                        tile.setEnemyLevel(random.nextInt(10) + 1); // 1-10級
                    }
                }
                
                tiles.add(tile);
            }
        }
        
        previewMap.setTiles(tiles);
        System.out.println("---- 隨機地圖預覽生成完成 ----");
        return previewMap;
    }

    /**
     * 生成隨機野怪類型
     * @param random Random實例
     * @return 野怪類型
     */
    private String generateRandomEnemyType(Random random) {
        if (enemyTypes.isEmpty()) {
            return null;
        }
        
        // 獲取所有可用的野怪類型
        String[] enemyTypeKeys = enemyTypes.keySet().toArray(new String[0]);
        return enemyTypeKeys[random.nextInt(enemyTypeKeys.length)];
    }

    /**
     * 保存預覽地圖到記憶體和文件
     * @param previewMap 要保存的預覽地圖
     */
    public void savePreviewMap(GameMap previewMap) throws IOException {
        System.out.println("---- 保存預覽地圖到記憶體和文件 ----");
        // 更新記憶體中的地圖
        this.gameMap = previewMap;
        
        // 保存到文件
        File dir = new File(MAP_FILE_PATH);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(MAP_FILE_PATH + MAP_FILE_NAME), gameMap);
        System.out.println("---- 預覽地圖已保存 ----");
    }

    /**
     * 檢查指定位置是否可被佔領
     * @param x X 座標
     * @param y Y 座標
     * @param playerId 玩家ID
     * @return 是否可佔領
     */
    public boolean canOccupyPosition(int x, int y, String playerId) {
        if (x < 0 || x >= gameMap.getWidth() || y < 0 || y >= gameMap.getHeight()) {
            return false; // 超出地圖範圍
        }
        
        MapTile tile = getTileAt(x, y);
        if (tile == null) {
            return false; // 地圖瓦片不存在
        }
        
        // 檢查地形是否可建造
        if (!tile.getTerrain().isBuildable()) {
            return false; // 地形不可建造
        }
        
        // 檢查是否已被其他玩家佔領
        if (tile.getOwnerId() != null && !tile.getOwnerId().equals(playerId)) {
            return false; // 已被其他玩家佔領
        }
        
        // 檢查是否已有建築
        if (tile.getBuildingId() != null) {
            return false; // 已有建築
        }
        
        return true;
    }
    
    /**
     * 佔領指定位置
     * @param x X 座標
     * @param y Y 座標
     * @param playerId 玩家ID
     * @return 是否成功佔領
     */
    public boolean occupyPosition(int x, int y, String playerId) {
        if (!canOccupyPosition(x, y, playerId)) {
            return false;
        }
        
        MapTile tile = getTileAt(x, y);
        tile.setOwnerId(playerId);
        
        // 保存地圖狀態
        try {
            saveGameMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 釋放指定位置
     * @param x X 座標
     * @param y Y 座標
     * @param playerId 玩家ID
     * @return 是否成功釋放
     */
    public boolean releasePosition(int x, int y, String playerId) {
        MapTile tile = getTileAt(x, y);
        if (tile == null || !playerId.equals(tile.getOwnerId())) {
            return false; // 不是該玩家的地盤或地圖瓦片不存在
        }
        
        tile.setOwnerId(null);
        
        // 保存地圖狀態
        try {
            saveGameMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 在指定位置建造建築
     * @param x X 座標
     * @param y Y 座標
     * @param buildingId 建築ID
     * @param playerId 玩家ID
     * @return 是否成功建造
     */
    public boolean buildOnPosition(int x, int y, String buildingId, String playerId) {
        if (!canOccupyPosition(x, y, playerId)) {
            return false;
        }
        
        MapTile tile = getTileAt(x, y);
        tile.setOwnerId(playerId);
        tile.setBuildingId(buildingId);
        
        // 保存地圖狀態
        try {
            saveGameMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 移除指定位置的建築
     * @param x X 座標
     * @param y Y 座標
     * @param playerId 玩家ID
     * @return 是否成功移除
     */
    public boolean removeBuildingFromPosition(int x, int y, String playerId) {
        MapTile tile = getTileAt(x, y);
        if (tile == null || !playerId.equals(tile.getOwnerId())) {
            return false; // 不是該玩家的地盤或地圖瓦片不存在
        }
        
        tile.setBuildingId(null);
        
        // 保存地圖狀態
        try {
            saveGameMap();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 獲取指定位置的瓦片
     * @param x X 座標
     * @param y Y 座標
     * @return 地圖瓦片
     */
    public MapTile getTileAt(int x, int y) {
        return gameMap.getTiles().stream()
                .filter(tile -> tile.getX() == x && tile.getY() == y)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 獲取玩家佔領的所有位置
     * @param playerId 玩家ID
     * @return 玩家佔領的位置列表
     */
    public List<MapTile> getPlayerOccupiedPositions(String playerId) {
        return gameMap.getTiles().stream()
                .filter(tile -> playerId.equals(tile.getOwnerId()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 檢查位置是否被佔用
     * @param x X 座標
     * @param y Y 座標
     * @return 是否被佔用
     */
    public boolean isPositionOccupied(int x, int y) {
        MapTile tile = getTileAt(x, y);
        return tile != null && tile.getOwnerId() != null;
    }
    
    /**
     * 檢查位置是否有建築
     * @param x X 座標
     * @param y Y 座標
     * @return 是否有建築
     */
    public boolean hasBuilding(int x, int y) {
        MapTile tile = getTileAt(x, y);
        return tile != null && tile.getBuildingId() != null;
    }
    
    /**
     * 保存地圖到文件
     */
    private void saveGameMap() throws IOException {
        File dir = new File(MAP_FILE_PATH);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(MAP_FILE_PATH + "map.json"), gameMap);
    }
    
    /**
     * 覆蓋當前地圖為配置地圖
     */
    public void overrideWithConfigMap() throws IOException {
        System.out.println("---- 覆蓋地圖為配置地圖 ----");
        try {
            System.out.println("嘗試從路徑載入地圖: " + mapPath);
            loadMap(mapPath);
            System.out.println("---- 地圖已覆蓋為配置地圖 ----");
        } catch (Exception e) {
            System.out.println("---- 覆蓋地圖失敗，錯誤: " + e.getMessage() + " ----");
            e.printStackTrace();
            throw new IOException("覆蓋地圖失敗: " + e.getMessage(), e);
        }
    }
    
    /**
     * 獲取地形配置資訊
     */
    public List<Map<String, Object>> getTerrainConfigInfo() {
        return terrainsList.stream()
                .map(terrain -> {
                    Map<String, Object> config = new java.util.HashMap<>();
                    config.put("id", terrain.getId());
                    config.put("name", terrain.getName());
                    config.put("description", terrain.getDescription() != null ? terrain.getDescription() : "");
                    config.put("buildable", terrain.isBuildable());
                    config.put("passable", terrain.isPassable());
                    config.put("terrainType", terrain.getTerrainType() != null ? terrain.getTerrainType().getDisplayName() : "");
                    config.put("speedModifier", terrain.getSpeedModifier());
                    config.put("defenseModifier", terrain.getDefenseModifier());
                    config.put("rangeBonus", terrain.getRangeBonus());
                    config.put("combatEffect", terrain.getCombatEffectDescription());
                    return config;
                })
                .collect(java.util.stream.Collectors.toList());
    }

}
