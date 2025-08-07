package com.taco.TinyRealm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/modules")
public class ModuleTestController {

    @GetMapping
    public String showModulesPage(Model model) {
        return "modules";
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
} 