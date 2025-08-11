package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.resourceModule.model.Resource;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;




@Service
public class ResourceService {

    private final ObjectMapper objectMapper;      // Jackson JSON 處理器

    private List<Resource> resourcesList = Collections.emptyList();

    // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.resource-path}")
    private org.springframework.core.io.Resource resourcePath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private StorageService storageService;

     /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     */
    public ResourceService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入資源模組 ----");
        try {
            try (InputStream is = resourcePath.getInputStream()) {
                resourcesList = objectMapper.readValue(is, new TypeReference<List<Resource>>() {});
                String resourceNames = getResourceName();
                System.out.println("---- 應用程式啟動中，已載入" + resourceNames + " ----");
            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入資源模組失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load resource.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入資源模組完成 ----");
    }

    public void reloadResources(String overridePath) throws IOException {
        org.springframework.core.io.Resource target = resourcePath;
        if (overridePath != null && !overridePath.isBlank()) {
            target = resourceLoader.getResource(overridePath);
        }
        try (InputStream is = target.getInputStream()) {
            resourcesList = objectMapper.readValue(is, new TypeReference<List<Resource>>() {});
        }
    }

    public List<Resource> getAllResourceTypes() {
        return resourcesList;
    }

    public Resource getResourceTypeById(String resourceID) {
        return resourcesList.stream()
                .filter(r -> r.getId().equals(resourceID))
                .findFirst()
                .orElse(null);
    }
    public String getResourceName() {
        List<Resource> resourceList = resourcesList;
        String resource_name = "";
        for (Resource resource : resourceList) {
            Map<String, String> name = resource.getName();
            resource_name+=name.get("zh-TW")+ " , ";
        }
        resource_name+= "共"+resourceList.size()+"種資源";
        return resource_name;
    }

    public GameState addResources(String playerId, Map<String, Integer>  addResource, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        try {
            Map<String, Integer> resources = gameState.getResources().getNowAmount();
            if (resources == null) {
                throw new IllegalArgumentException("Player not found");
            }
            for (Map.Entry<String, Integer> entry : addResource.entrySet()) {
                String resourceId = entry.getKey();
                int amountToAdd = entry.getValue();
                if (amountToAdd < 0) {
                    throw new IllegalArgumentException("Cannot add negative amount of resource: " + resourceId);
                }
                resources.put(resourceId, resources.getOrDefault(resourceId, 0) + amountToAdd);
            }
            gameState.getResources().setLastUpdatedTime(System.currentTimeMillis());
            storageService.saveGameState(playerId, gameState,"增加玩家資源", isTest);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameState;
    }

    public GameState dedResources(String playerId, Map<String, Integer>  dedResource, boolean isTest) throws IOException {
        GameState gameState = storageService.getGameStateListById(playerId);
        try {
            Map<String, Integer> resources = gameState.getResources().getNowAmount();
            if (resources == null) {
                throw new IllegalArgumentException("Player not found");
            }
            for (Map.Entry<String, Integer> entry : dedResource.entrySet()) {;
                String resourceId = entry.getKey();
                int amountToDed = entry.getValue();
                int nowAmount=resources.getOrDefault(resourceId, 0);
                if (amountToDed < 0) {
                    throw new IllegalArgumentException("Cannot deduct negative amount of resource: " + resourceId);
                }
                if (nowAmount < amountToDed) {
                    
                    return gameState ; // 如果資源不足，則不進行扣除
                }
                resources.put(resourceId, nowAmount - amountToDed);
            }
            gameState.getResources().setLastUpdatedTime(System.currentTimeMillis());
            storageService.saveGameState(playerId, gameState,"扣除玩家資源", isTest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameState;
    }

    public boolean hasEnoughResources(String playerId, Map<String, Integer> requiredResources) {
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null || gameState.getResources() == null || gameState.getResources().getNowAmount() == null) {
            return false;
        }
        Map<String, Integer> nowAmount = gameState.getResources().getNowAmount();
        for (Map.Entry<String, Integer> entry : requiredResources.entrySet()) {
            String resourceId = entry.getKey();
            int requiredAmount = entry.getValue();
            int currentAmount = nowAmount.getOrDefault(resourceId, 0);
            if (currentAmount < requiredAmount) {
                return false;
            }
        }
        return true;
    }
}
