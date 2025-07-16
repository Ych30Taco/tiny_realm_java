package com.taco.TinyRealm.controller;

import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.taco.TinyRealm.modules.ResourcesModule.ResourceData;
import com.taco.TinyRealm.modules.ResourcesModule.ResourceType;
import com.taco.TinyRealm.service.resourcesService;

@RestController
public class resourcesController {
    @Autowired
    private resourcesService resourcesService;

    @PostMapping("/getAllResources")
    public Map<ResourceType, ResourceData> getAllResources(@RequestBody Map<String, Object> body) {
        return resourcesService.getAllResources();
    }

    /**
     * 查詢單一資源
     * @param body {"type": "GOLD"}
     * @return ResourceData
     */
    @PostMapping("/getResource")
    public ResourceData getResource(@RequestBody Map<String, Object> body) {
        ResourceType type = ResourceType.valueOf((String) body.get("type"));
        return resourcesService.getResource(type);
    }

    /**
     * 批次增加或消耗多項資源
     * @param body [{"type": "GOLD", "amount": 100}, {"type": "FOOD", "amount": -50}]
     * @return List<{"type":..., "success": true/false, "data": ResourceData}>
     */
    @PostMapping("/updateResource")
    public List<Map<String, Object>> updateResource(@RequestBody List<Map<String, Object>> body) {
        return resourcesService.updateResource(body);
    }

    /**
     * 設定資源上限
     * @param body {"type": "GOLD", "limit": 9999}
     * @return ResourceData
     */
    @PostMapping("/setResourceLimit")
    public ResourceData setResourceLimit(@RequestBody Map<String, Object> body) {
        ResourceType type = ResourceType.valueOf((String) body.get("type"));
        int limit = (int) body.get("limit");
        return resourcesService.setResourceLimit(type, limit);
    }
}
