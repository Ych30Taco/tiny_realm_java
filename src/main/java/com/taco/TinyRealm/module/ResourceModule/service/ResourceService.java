package com.taco.TinyRealm.module.ResourceModule.service;

import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.ResourceModule.model.ResourceType;

import jakarta.annotation.PostConstruct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;



@Service
public class ResourceService {

    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private final ResourceLoader resourceLoader;  // Spring 資源載入器

    private List<ResourceType> resourceTypeList = Collections.emptyList();

    // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.ResourceType-path}")
    private org.springframework.core.io.Resource resourceTypesPath;

    @Autowired
    private StorageService storageService;

     /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     * @param resourceLoader Spring ResourceLoader 實例
     */
    public ResourceService(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
    }

     @PostConstruct
    public void init() {
        try (InputStream is = resourceTypesPath.getInputStream()) {
            resourceTypeList = objectMapper.readValue(is, new TypeReference<List<ResourceType>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ResourceType.json", e);
        }
    }

    public List<ResourceType> getAllResourceTypes() {
        return resourceTypeList;
    }

    public ResourceType getResourceTypeById(String resourceID) {
        return resourceTypeList.stream()
                .filter(r -> r.getResourceID().equals(resourceID))
                .findFirst()
                .orElse(null);
    }



/* 
    public Resource addResources(String playerId, int gold, int wood, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found");
        }
        Resource resources = gameState.getResources();
        if (resources == null) {
            resources = new Resource();
            gameState.setResources(resources);
        }
        resources.setGold(Math.max(0, resources.getGold() + gold));
        resources.setWood(Math.max(0, resources.getWood() + wood));
        storageService.saveGameState(playerId, gameState, isTest);
        return resources;
    }

    public Resource getResources(String playerId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        return gameState != null ? gameState.getResources() : null;
    }
    */
}
