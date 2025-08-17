package com.taco.TinyRealm.module.terrainMapModule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.taco.TinyRealm.module.terrainMapModule.service.MapService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;
import com.taco.TinyRealm.module.terrainMapModule.model.MapTile;
import com.taco.TinyRealm.module.terrainMapModule.model.GameMap;
import com.taco.TinyRealm.module.terrainMapModule.model.Terrain;
import com.taco.TinyRealm.module.terrainMapModule.model.TerrainType;

@RestController
@RequestMapping("/api/terrain")
public class TerrainMapController {
    @Autowired
    private TerrainMapService terrainMapService;
    /*@Autowired
    private MapService mapService;*/

    @GetMapping("/types")
    public ResponseEntity<?> getAllterrain() {
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取地形類型成功", "data", terrainMapService.getAllterrain()));
    }
    
    @PostMapping("/typeById")
    public ResponseEntity<?> getTerrainTypeById(@RequestBody Map<String, Object> body) {
        String terrainID = (String) body.get("terrainID");
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取地形類型成功", "data", terrainMapService.getTerrainTypeById(terrainID)));
    }
    
    @GetMapping("/gameMap")
    public ResponseEntity<?> getgameMap() {
        return ResponseEntity.ok(Map.of("success", true, "message", "獲取遊戲地圖成功", "data", terrainMapService.getGameMap()));
    }
    
    /**
     * 佔領指定位置
     */
    @PostMapping("/occupy")
    public ResponseEntity<?> occupyPosition(@RequestBody Map<String, Object> body) {
        try {
            int x = (int) body.get("x");
            int y = (int) body.get("y");
            String playerId = (String) body.get("playerId");
            
            if (terrainMapService.occupyPosition(x, y, playerId)) {
                return ResponseEntity.ok(Map.of("success", true, "message", "位置佔領成功", "data", null));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "位置無法佔領", "data", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "佔領失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 釋放指定位置
     */
    @PostMapping("/release")
    public ResponseEntity<?> releasePosition(@RequestBody Map<String, Object> body) {
        try {
            int x = (int) body.get("x");
            int y = (int) body.get("y");
            String playerId = (String) body.get("playerId");
            
            if (terrainMapService.releasePosition(x, y, playerId)) {
                return ResponseEntity.ok(Map.of("success", true, "message", "位置釋放成功", "data", null));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "位置無法釋放", "data", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "釋放失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 檢查位置是否可佔領
     */
    @PostMapping("/canOccupy")
    public ResponseEntity<?> canOccupyPosition(@RequestBody Map<String, Object> body) {
        try {
            int x = (int) body.get("x");
            int y = (int) body.get("y");
            String playerId = (String) body.get("playerId");
            
            boolean canOccupy = terrainMapService.canOccupyPosition(x, y, playerId);
            return ResponseEntity.ok(Map.of("success", true, "message", "檢查完成", "data", Map.of("canOccupy", canOccupy)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "檢查失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 獲取玩家佔領的所有位置
     */
    @PostMapping("/playerPositions")
    public ResponseEntity<?> getPlayerOccupiedPositions(@RequestBody Map<String, Object> body) {
        try {
            String playerId = (String) body.get("playerId");
            List<MapTile> positions = terrainMapService.getPlayerOccupiedPositions(playerId);
            return ResponseEntity.ok(Map.of("success", true, "message", "獲取玩家位置成功", "data", positions));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "獲取失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 檢查位置狀態
     */
    @PostMapping("/positionStatus")
    public ResponseEntity<?> getPositionStatus(@RequestBody Map<String, Object> body) {
        try {
            int x = (int) body.get("x");
            int y = (int) body.get("y");
            
            MapTile tile = terrainMapService.getTileAt(x, y);
            if (tile == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "位置不存在", "data", null));
            }
            
            Map<String, Object> status = Map.of(
                "x", x,
                "y", y,
                "terrain", tile.getTerrain(),
                "ownerId", tile.getOwnerId(),
                "buildingId", tile.getBuildingId(),
                "isOccupied", terrainMapService.isPositionOccupied(x, y),
                "hasBuilding", terrainMapService.hasBuilding(x, y),
                "canBuild", tile.getTerrain().isBuildable()
            );
            
            return ResponseEntity.ok(Map.of("success", true, "message", "獲取位置狀態成功", "data", status));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "檢查失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 覆蓋當前地圖為配置地圖
     */
    @PostMapping("/overrideWithConfig")
    public ResponseEntity<?> overrideWithConfig() {
        try {
            terrainMapService.overrideWithConfigMap();
            return ResponseEntity.ok(Map.of("success", true, "message", "地圖已覆蓋為配置地圖", "data", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "覆蓋失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 生成隨機地圖預覽
     */
    @PostMapping("/generatePreview")
    public ResponseEntity<?> generateRandomMapPreview(@RequestBody Map<String, Object> body) {
        try {
            int width = (int) body.getOrDefault("width", 10);
            int height = (int) body.getOrDefault("height", 10);
            
            GameMap previewMap = terrainMapService.generateRandomMapPreview(width, height);
            return ResponseEntity.ok(Map.of("success", true, "message", "隨機地圖預覽生成成功", "data", previewMap));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "生成預覽失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 保存預覽地圖
     */
    @PostMapping("/savePreview")
    public ResponseEntity<?> savePreviewMap(@RequestBody Map<String, Object> body) {
        try {
            System.out.println("收到保存預覽地圖請求，數據: " + body);
            
            // 從請求體中重建GameMap對象
            GameMap previewMap = new GameMap();
            previewMap.setId((String) body.get("id"));
            previewMap.setWidth((int) body.get("width"));
            previewMap.setHeight((int) body.get("height"));
            
            List<Map<String, Object>> tilesData = (List<Map<String, Object>>) body.get("tiles");
            List<MapTile> tiles = new java.util.ArrayList<>();
            
            if (tilesData != null) {
                for (Map<String, Object> tileData : tilesData) {
                    MapTile tile = new MapTile();
                    tile.setX((int) tileData.get("x"));
                    tile.setY((int) tileData.get("y"));
                    tile.setOwnerId((String) tileData.get("ownerId"));
                    tile.setBuildingId((String) tileData.get("buildingId"));
                    
                    // 處理unitIds，可能是null或空列表
                    Object unitIdsObj = tileData.get("unitIds");
                    if (unitIdsObj instanceof List) {
                        tile.setUnitIds((List<String>) unitIdsObj);
                    } else {
                        tile.setUnitIds(new java.util.ArrayList<>());
                    }
                    
                    // 重建Terrain對象
                    Map<String, Object> terrainData = (Map<String, Object>) tileData.get("terrain");
                    if (terrainData != null) {
                        Terrain terrain = new Terrain();
                        terrain.setId((String) terrainData.get("id"));
                        terrain.setName((String) terrainData.get("name"));
                        terrain.setDescription((String) terrainData.get("description"));
                        terrain.setBuildable((Boolean) terrainData.get("buildable"));
                        terrain.setPassable((Boolean) terrainData.get("passable"));
                        
                        // 設置TerrainType
                        String terrainTypeStr = (String) terrainData.get("terrainType");
                        if (terrainTypeStr != null) {
                            try {
                                TerrainType terrainType = TerrainType.valueOf(terrainTypeStr);
                                terrain.setTerrainType(terrainType);
                            } catch (IllegalArgumentException e) {
                                // 如果找不到對應的TerrainType，使用默認值
                                terrain.setTerrainType(TerrainType.PLAIN);
                            }
                        } else {
                            terrain.setTerrainType(TerrainType.PLAIN);
                        }
                        
                        tile.setTerrain(terrain);
                    }
                    
                    tiles.add(tile);
                }
            }
            
            previewMap.setTiles(tiles);
            
            terrainMapService.savePreviewMap(previewMap);
            return ResponseEntity.ok(Map.of("success", true, "message", "預覽地圖已保存", "data", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "保存失敗: " + e.getMessage(), "data", null));
        }
    }
    
    /**
     * 獲取地形配置資訊
     */
    @GetMapping("/terrainConfig")
    public ResponseEntity<?> getTerrainConfig() {
        try {
            List<Map<String, Object>> terrainConfig = terrainMapService.getTerrainConfigInfo();
            return ResponseEntity.ok(Map.of("success", true, "message", "獲取地形配置成功", "data", terrainConfig));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "獲取失敗: " + e.getMessage(), "data", null));
        }
    }
}
