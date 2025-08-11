package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.buildingModule.service.BuildingService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;
import com.taco.TinyRealm.module.soldierModule.service.SoldierService;
import com.taco.TinyRealm.module.inventoryModule.service.InventoryService;
import com.taco.TinyRealm.module.battleModule.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigReloadController {

    @Autowired private ResourceService resourceService;
    @Autowired private BuildingService buildingService;
    @Autowired private TerrainMapService terrainMapService;
    @Autowired private SoldierService soldierService;
    @Autowired private InventoryService inventoryService;
    @Autowired private BattleService battleService;

    @PostMapping("/reload")
    public ResponseEntity<Map<String, Object>> reload(@RequestParam String module,
                                                      @RequestParam(required = false) String path) {
        Map<String, Object> resp = new HashMap<>();
        try {
            String normalized = module.trim().toLowerCase();
            switch (normalized) {
                case "resource":
                case "resources":
                    resourceService.reloadResources(path);
                    resp.put("message", "Resources reloaded");
                    break;
                case "building":
                case "buildings":
                    buildingService.reloadBuildings(path);
                    resp.put("message", "Buildings reloaded");
                    break;
                case "terrain":
                    terrainMapService.reloadTerrains(path);
                    resp.put("message", "Terrains reloaded");
                    break;
                case "map":
                    terrainMapService.reloadMap(path);
                    resp.put("message", "Map reloaded");
                    break;
                case "soldier":
                case "soldiers":
                    soldierService.reloadSoldierTypes(path);
                    resp.put("message", "Soldier types reloaded");
                    break;
                case "inventory":
                case "items":
                    inventoryService.reloadItemTypes(path);
                    resp.put("message", "Item types reloaded");
                    break;
                case "battle":
                case "enemies":
                    battleService.reloadEnemies(path);
                    resp.put("message", "Enemies reloaded");
                    break;
                default:
                    return ResponseEntity.badRequest().body(api(false, "Unknown module: " + module, null));
            }
            return ResponseEntity.ok(api(true, (String) resp.get("message"), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(api(false, e.getMessage(), null));
        }
    }

    @PostMapping("/reloadAll")
    public ResponseEntity<Map<String, Object>> reloadAll() {
        try {
            resourceService.reloadResources(null);
            buildingService.reloadBuildings(null);
            terrainMapService.reloadTerrains(null);
            terrainMapService.reloadMap(null);
            soldierService.reloadSoldierTypes(null);
            inventoryService.reloadItemTypes(null);
            battleService.reloadEnemies(null);
            return ResponseEntity.ok(api(true, "All modules reloaded", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(api(false, e.getMessage(), null));
        }
    }

    private Map<String, Object> api(boolean success, String message, Object data) {
        Map<String, Object> m = new HashMap<>();
        m.put("success", success);
        m.put("message", message);
        m.put("data", data);
        return m;
    }
}
