package com.taco.TinyRealm.module.terrainMapModule.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import com.taco.TinyRealm.module.terrainMapModule.service.MapService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;
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
}
