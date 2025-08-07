package com.taco.TinyRealm.module.terrainModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taco.TinyRealm.module.terrainModule.model.GameMap;
import com.taco.TinyRealm.module.terrainModule.model.MapTile;
import com.taco.TinyRealm.module.terrainModule.model.Terrain;
import com.taco.TinyRealm.module.terrainModule.service.TerrainService;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;


@Service
public class MapService {
    // 文件路徑，假設在 resources/data 目錄下
    private static final String MAP_FILE_PATH = "src/main/resources/data/map.json";
    
    private GameMap gameMap;
    private List<Terrain> terrain;

    @Value("${app.data.map-path}")
    private org.springframework.core.io.Resource mapPath;
    @Autowired
    private TerrainService terrainService;

    private final ObjectMapper objectMapper;


    /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     */
    public MapService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    /**
     * 初始化方法，在 Spring 容器啟動後執行
     * 載入 terrain.json 和 map.json，關聯地形數據
     */
    @PostConstruct
    public void init() throws IOException {
        // 載入地形配置
        
        System.out.println("---- 應用程式啟動中，載入地圖模組 ----");
        try {
            try (InputStream is = mapPath.getInputStream()) {
                gameMap = objectMapper.readValue(is, GameMap.class);
                //String terrainNames = getTerrainName();
                //System.out.println("---- 應用程式啟動中，已載入" + terrainNames + " ----");
            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入地圖模組失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load resource.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入地圖模組完成 ----");

        /* 
        // 檢查 map.json 是否存在，若存在則載入，否則創建新地圖
        File mapFile = new File(MAP_FILE_PATH);
        if (mapFile.exists()) {
            gameMap = mapStorageUtil.loadMap(MAP_FILE_PATH, terrainConfig);
        } else {
            // 創建新地圖（100x100）並生成隨機地形
            gameMap = new GameMap(100, 100);
            generateRandomMap(gameMap);
            mapStorageUtil.saveMap(gameMap, MAP_FILE_PATH);
        }*/
    }

    /**
     * 查詢指定座標的格子
     * @param x X 座標
     * @param y Y 座標
     * @return MapTile，若不存在則返回 null
     */
    public MapTile getTile(int x, int y) {
        if (x < 0 || x >= gameMap.getWidth() || y < 0 || y >= gameMap.getHeight()) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + "," + y + ")");
        }
        return gameMap.getTile(x, y);
    }

    /**
     * 更新指定座標的格子
     * @param x X 座標
     * @param y Y 座標
     * @param tile 更新後的格子數據
     */
    public void updateTile(int x, int y, MapTile tile) throws IOException {
        if (x < 0 || x >= gameMap.getWidth() || y < 0 || y >= gameMap.getHeight()) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + "," + y + ")");
        }
        // 驗證 terrain 是否有效
        if (tile.getTerrain() != null && terrainService.getTerrainTypeById(tile.getTerrain().getId()) == null) {
            throw new IllegalArgumentException("Invalid terrain ID: " + tile.getTerrain().getId());
        }
        gameMap.setTile(x, y, tile);
       // mapStorageUtil.saveMap(gameMap, MAP_FILE_PATH);
    }

    /**
     * 生成隨機地圖（僅用於初始化新地圖）
     * @param map 要填充的 GameMap
     */
    private void generateRandomMap(GameMap map) {
        Random random = new Random();
        // 獲取所有可用地形
        Terrain[] availableTerrains = terrain.toArray(new Terrain[0]);
        
        // 為每個格子分配隨機地形
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                MapTile tile = new MapTile();
                tile.setX(x);
                tile.setY(y);
                tile.setTerrain(availableTerrains[random.nextInt(availableTerrains.length)]);
                tile.setOwnerId(null);
                tile.setBuildingId(null);
                tile.setUnitIds(new java.util.ArrayList<>());
                map.setTile(x, y, tile);
            }
        }
    }

    /**
     * 查詢範圍內的格子（例如 3x3 範圍）
     * @param x 中心 X 座標
     * @param y 中心 Y 座標
     * @param range 範圍（例如 1 表示 3x3）
     * @return 範圍內的格子陣列
     */
    public MapTile[] getNearbyTiles(int x, int y, int range) {
        if (x < 0 || x >= gameMap.getWidth() || y < 0 || y >= gameMap.getHeight()) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + "," + y + ")");
        }
        java.util.List<MapTile> nearbyTiles = new java.util.ArrayList<>();
        for (int i = Math.max(0, x - range); i <= Math.min(gameMap.getWidth() - 1, x + range); i++) {
            for (int j = Math.max(0, y - range); j <= Math.min(gameMap.getHeight() - 1, y + range); j++) {
                MapTile tile = gameMap.getTile(i, j);
                if (tile != null) {
                    nearbyTiles.add(tile);
                }
            }
        }
        return nearbyTiles.toArray(new MapTile[0]);
    }
}
