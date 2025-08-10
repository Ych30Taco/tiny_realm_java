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
        return ResponseEntity.ok(terrainMapService.getAllterrain());
    }
    @PostMapping("/typeById")
    public ResponseEntity<?> getResourceTypeById(@RequestBody Map<String, Object> body) {
        String terrainID = (String) body.get("terrainID");
        return ResponseEntity.ok(terrainMapService.getTerrainTypeById(terrainID ));
    }
    @GetMapping("/gameMap")
    public ResponseEntity<?> getgameMap() {
        return ResponseEntity.ok(terrainMapService.getGameMap());
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
                return ResponseEntity.ok(Map.of("success", true, "message", "位置佔領成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "位置無法佔領"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "佔領失敗: " + e.getMessage()));
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
                return ResponseEntity.ok(Map.of("success", true, "message", "位置釋放成功"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "位置無法釋放"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "釋放失敗: " + e.getMessage()));
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
            return ResponseEntity.ok(Map.of("canOccupy", canOccupy));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "檢查失敗: " + e.getMessage()));
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
            return ResponseEntity.ok(positions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "獲取失敗: " + e.getMessage()));
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
                return ResponseEntity.badRequest().body(Map.of("error", "位置不存在"));
            }
            
            Map<String, Object> status = Map.of(
                "x", x,
                "y", y,
                "terrain", tile.getTerrain(),
                "ownerId", tile.getOwnerId(),
                "buildingId", tile.getBuildingId(),
                "isOccupied", terrainMapService.isPositionOccupied(x, y),
                "hasBuilding", terrainMapService.hasBuilding(x, y),
                "canBuild", tile.getTerrain().getBuildable()
            );
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "檢查失敗: " + e.getMessage()));
        }
    }
}
