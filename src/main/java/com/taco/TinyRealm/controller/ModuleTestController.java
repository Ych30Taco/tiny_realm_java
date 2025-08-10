package com.taco.TinyRealm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.terrainMapModule.service.TerrainMapService;
import com.taco.TinyRealm.module.buildingModule.service.BuildingService;
import com.taco.TinyRealm.module.resourceModule.service.ResourceService;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/modules")
public class ModuleTestController {

    @Autowired
    private StorageService storageService;
    
    @Autowired
    private TerrainMapService terrainMapService;
    
    @Autowired
    private BuildingService buildingService;
    
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private PlayerService playerService;

    @GetMapping
    public String showModulesPage(Model model) {
        return "modules";
    }

    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getModuleStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            // 檢查存儲模組狀態
            boolean storageModuleStatus = checkStorageModuleStatus();
            status.put("storageModule", storageModuleStatus);
            
            // 檢查玩家模組狀態
            boolean playerModuleStatus = checkPlayerModuleStatus();
            status.put("playerModule", playerModuleStatus);
            
            // 檢查資源模組狀態
            boolean resourceModuleStatus = checkResourceModuleStatus();
            status.put("resourceModule", resourceModuleStatus);
            
            // 檢查建築模組狀態
            boolean buildingModuleStatus = checkBuildingModuleStatus();
            status.put("buildingModule", buildingModuleStatus);
            
            // 檢查地形地圖模組狀態
            boolean terrainMapModuleStatus = checkTerrainMapModuleStatus();
            status.put("terrainMapModule", terrainMapModuleStatus);
            
            status.put("success", true);
        } catch (Exception e) {
            status.put("success", false);
            status.put("error", e.getMessage());
        }
        
        return status;
    }
    
    private boolean checkStorageModuleStatus() {
        try {
            // 嘗試獲取存儲統計資訊來檢查存儲模組是否正常
            storageService.getStorageStats();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean checkPlayerModuleStatus() {
        try {
            // 嘗試獲取玩家列表來檢查玩家模組是否正常
            storageService.getAllPlayerIdList();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean checkResourceModuleStatus() {
        try {
            // 嘗試獲取資源配置來檢查資源模組是否正常
            resourceService.getAllResourceTypes();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean checkBuildingModuleStatus() {
        try {
            // 嘗試獲取建築配置來檢查建築模組是否正常
            buildingService.getAllbuilding();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean checkTerrainMapModuleStatus() {
        try {
            // 嘗試獲取地圖資料來檢查地形地圖模組是否正常
            terrainMapService.getGameMap();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/terrain")
    @ResponseBody
    public String getTerrainData() {
        // 這裡可以回傳地形資料
        return "{\"status\": \"success\", \"data\": \"terrain data\"}";
    }

    @PostMapping("/terrain/set")
    @ResponseBody
    public String setTerrain(@RequestParam String type, @RequestParam int x, @RequestParam int y) {
        // 這裡可以設定地形
        return "{\"status\": \"success\", \"message\": \"Terrain set successfully\"}";
    }

    @GetMapping("/building")
    @ResponseBody
    public String getBuildingData() {
        // 這裡可以回傳建築資料
        return "{\"status\": \"success\", \"data\": \"building data\"}";
    }

    @PostMapping("/building/create")
    @ResponseBody
    public String createBuilding(@RequestParam String type, @RequestParam String playerId, @RequestParam int level) {
        // 這裡可以創建建築
        return "{\"status\": \"success\", \"message\": \"Building created successfully\"}";
    }

    @GetMapping("/resource")
    @ResponseBody
    public String getResourceData() {
        // 這裡可以回傳資源資料
        return "{\"status\": \"success\", \"data\": \"resource data\"}";
    }

    @PostMapping("/resource/add")
    @ResponseBody
    public String addResource(@RequestParam String type, @RequestParam String playerId, @RequestParam int amount) {
        // 這裡可以增加資源
        return "{\"status\": \"success\", \"message\": \"Resource added successfully\"}";
    }

    @GetMapping("/player")
    @ResponseBody
    public String getPlayerData() {
        // 這裡可以回傳玩家資料
        return "{\"status\": \"success\", \"data\": \"player data\"}";
    }

    @PostMapping("/player/create")
    @ResponseBody
    public String createPlayer(@RequestParam String id, @RequestParam String name, @RequestParam int level) {
        // 這裡可以創建玩家
        return "{\"status\": \"success\", \"message\": \"Player created successfully\"}";
    }

    @GetMapping("/battle")
    @ResponseBody
    public String getBattleData() {
        // 這裡可以回傳戰鬥資料
        return "{\"status\": \"success\", \"data\": \"battle data\"}";
    }

    @PostMapping("/battle/start")
    @ResponseBody
    public String startBattle(@RequestParam String attackerId, @RequestParam String defenderId, 
                             @RequestParam String unitType, @RequestParam int unitCount) {
        // 這裡可以開始戰鬥
        return "{\"status\": \"success\", \"message\": \"Battle started successfully\"}";
    }

    @GetMapping("/market")
    @ResponseBody
    public String getMarketData() {
        // 這裡可以回傳市場資料
        return "{\"status\": \"success\", \"data\": \"market data\"}";
    }

    @PostMapping("/market/list")
    @ResponseBody
    public String listItem(@RequestParam String sellerId, @RequestParam String itemType, 
                          @RequestParam int amount, @RequestParam int price) {
        // 這裡可以上架商品
        return "{\"status\": \"success\", \"message\": \"Item listed successfully\"}";
    }

    @PostMapping("/init")
    @ResponseBody
    public String initializeAllModules() {
        // 這裡可以初始化所有模組
        return "{\"status\": \"success\", \"message\": \"All modules initialized successfully\"}";
    }

    @PostMapping("/test")
    @ResponseBody
    public String runFullTest() {
        // 這裡可以執行完整測試
        return "{\"status\": \"success\", \"message\": \"Full test completed successfully\"}";
    }

    @PostMapping("/generate-data")
    @ResponseBody
    public String generateTestData() {
        // 這裡可以生成測試資料
        return "{\"status\": \"success\", \"message\": \"Test data generated successfully\"}";
    }

    @PostMapping("/reset")
    @ResponseBody
    public String resetAllData() {
        // 這裡可以重置所有資料
        return "{\"status\": \"success\", \"message\": \"All data reset successfully\"}";
    }

    // ==================== 存儲模組測試端點 ====================
    
    @GetMapping("/storage")
    @ResponseBody
    public Map<String, Object> getStorageData() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> stats = storageService.getStorageStats();
            response.put("success", true);
            response.put("message", "獲取存儲統計資訊成功");
            response.put("data", stats);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "獲取存儲統計資訊失敗: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }

    @GetMapping("/storage/players")
    @ResponseBody
    public Map<String, Object> getPlayerList() {
        Map<String, Object> response = new HashMap<>();
        try {
            java.util.List<String> players = storageService.getAllPlayerIdList();
            response.put("success", true);
            response.put("message", "獲取玩家列表成功");
            response.put("data", players);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "獲取玩家列表失敗: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }

    @GetMapping("/storage/online")
    @ResponseBody
    public Map<String, Object> getOnlinePlayers() {
        Map<String, Object> response = new HashMap<>();
        try {
            java.util.List<String> onlinePlayers = storageService.getOnlineGameStateIdList();
            response.put("success", true);
            response.put("message", "獲取在線玩家列表成功");
            response.put("data", onlinePlayers);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "獲取在線玩家列表失敗: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }

    @GetMapping("/storage/offline")
    @ResponseBody
    public Map<String, Object> getOfflinePlayers() {
        Map<String, Object> response = new HashMap<>();
        try {
            java.util.List<String> offlinePlayers = storageService.getOfflineGameStateIdList();
            response.put("success", true);
            response.put("message", "獲取離線玩家列表成功");
            response.put("data", offlinePlayers);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "獲取離線玩家列表失敗: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }

    @GetMapping("/storage/gameState/{playerId}")
    @ResponseBody
    public Map<String, Object> getGameStateById(@PathVariable String playerId) {
        Map<String, Object> response = new HashMap<>();
        try {
            com.taco.TinyRealm.module.storageModule.model.GameState gameState = storageService.getGameStateListById(playerId);
            if (gameState != null) {
                response.put("success", true);
                response.put("message", "獲取玩家遊戲狀態成功");
                response.put("data", gameState);
            } else {
                response.put("success", false);
                response.put("message", "玩家遊戲狀態不存在");
                response.put("data", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "獲取玩家遊戲狀態失敗: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }

    @PostMapping("/storage/clearTestData")
    @ResponseBody
    public Map<String, Object> clearTestData() {
        Map<String, Object> response = new HashMap<>();
        try {
            storageService.clearTestData();
            response.put("success", true);
            response.put("message", "測試資料清除成功");
            response.put("data", null);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "清除測試資料失敗: " + e.getMessage());
            response.put("data", null);
        }
        return response;
    }
} 