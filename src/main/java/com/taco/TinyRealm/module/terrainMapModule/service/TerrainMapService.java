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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class TerrainMapService {
    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private List<Terrain> terrainsList = Collections.emptyList();
    private GameMap gameMap = new GameMap(); // 讓 gameMap 預設存在記憶體
    @Autowired
    private StorageService storageService;

    @Value("${app.data.terrain-path}")
    private org.springframework.core.io.Resource terrainPath;
    @Value("${app.data.map-path}")
    private org.springframework.core.io.Resource mapPath;
    private static final String MAP_FILE_PATH = "src/main/resources/config/map/"; // 假設地圖存儲在這個路徑

    /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     */
    public TerrainMapService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @PostConstruct
    public void init() throws IOException{
        System.out.println("---- 應用程式啟動中，載入地形地圖模組 ----");
        try {
            try (InputStream is = terrainPath.getInputStream()) {
                terrainsList = objectMapper.readValue(is, new TypeReference<List<Terrain>>() {});
                String terrainNames = getTerrainName();
                System.out.println("---- 應用程式啟動中，已載入" + terrainNames + " ----");
            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入地形失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load resource.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入地形完成 ----");

        System.out.println("---- 應用程式啟動中，載入地圖... ----");
        try (InputStream is = mapPath.getInputStream()){
            // 檔案存在，直接讀取
            gameMap = objectMapper.readValue(is, new TypeReference<GameMap>(){});
            System.out.println("---- 應用程式啟動中，已載入地圖"+gameMap.getHeight()+"*"+gameMap.getWidth()+" ----"); 
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入地圖失敗，創建新地圖 ----");
            gameMap = new GameMap(10, 10);
            generateRandomMap(gameMap);
            System.out.println("---- 應用程式啟動中，重新載入地圖 ----");
            System.out.println("---- 應用程式啟動中，載入地圖完成 ----");
        }
        System.out.println("---- 應用程式啟動中，載入地形地圖模組完成 ----");

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
        objectMapper.writeValue(new File(MAP_FILE_PATH+"map.json"), gameMap);
        System.out.println("---- 應用程式啟動中，創建新地圖成功 ----");
    }

}
