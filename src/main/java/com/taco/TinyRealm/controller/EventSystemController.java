package com.taco.TinyRealm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.taco.TinyRealm.modules.ResourcesModule.ResourceData;
import com.taco.TinyRealm.modules.ResourcesModule.ResourceManager;
import com.taco.TinyRealm.modules.ResourcesModule.ResourceType;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventSystemController {
    @Autowired
    private ResourceManager resourceService;

    // 查詢所有資源
    @GetMapping("/resources")
    public Map<ResourceType, ResourceData> getAllResources() {
        return resourceService.getAllResources();
    }

    // 新增資源
    /*@PostMapping("/resources")
    public ResourceType addResource(@RequestBody ResourceType resource) {
        return resourceService.add(resource.);
    }*/

}
