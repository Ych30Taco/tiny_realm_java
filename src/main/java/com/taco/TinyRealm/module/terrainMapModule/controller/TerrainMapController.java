package com.taco.TinyRealm.module.terrainMapModule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.taco.TinyRealm.module.terrainMapModule.service.MapService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;
import com.taco.TinyRealm.module.terrainMapModule.model.MapTile;

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
}
