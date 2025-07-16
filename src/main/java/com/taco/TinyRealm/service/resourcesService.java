package com.taco.TinyRealm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taco.TinyRealm.modules.ResourcesModule.ResourceData;
import com.taco.TinyRealm.modules.ResourcesModule.ResourceManager;
import com.taco.TinyRealm.modules.ResourcesModule.ResourceType;

import java.util.*;

@Service
public class resourcesService {
    @Autowired
    private ResourceManager resourceManager;

    public Map<ResourceType, ResourceData> getAllResources() {
        return resourceManager.getAllResources();
    }

    public ResourceData getResource(ResourceType type) {
        return resourceManager.getAllResources().get(type);
    }

    public List<Map<String, Object>> updateResource(List<Map<String, Object>> body) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, Object> item : body) {
            ResourceType type = ResourceType.valueOf((String) item.get("type"));
            int amount = (int) item.get("amount");
            boolean success = true;
            if (amount >= 0) {
                resourceManager.add(type, amount);
            } else {
                success = resourceManager.consume(type, -amount);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("type", type.name());
            result.put("success", success);
            result.put("data", resourceManager.getAllResources().get(type));
            results.add(result);
        }
        return results;
    }

    public ResourceData setResourceLimit(ResourceType type, int limit) {
        resourceManager.setLimit(type, limit);
        return resourceManager.getAllResources().get(type);
    }
}
