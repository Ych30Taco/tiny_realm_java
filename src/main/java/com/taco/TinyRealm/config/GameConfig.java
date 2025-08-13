package com.taco.TinyRealm.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameConfig {
    private final Map<String, Object> configCache = new ConcurrentHashMap<>();
    private final ResourceLoader resourceLoader;
    
    @Value("${app.data.resource-path}")
    private String resourcePath;
    
    @Value("${app.data.building-path}")
    private String buildingPath;
    
    public GameConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * 異步載入配置，避免阻塞啟動
     */
    @PostConstruct
    @Async("startupTaskExecutor")
    public void loadConfigs() {
        System.out.println("---- 應用程式啟動中，載入遊戲配置 ----");
        
        // 並行載入多個配置文件
        try {
            loadResourceConfig();
            loadBuildingConfig();
            System.out.println("---- 遊戲配置載入完成 ----");
        } catch (Exception e) {
            System.err.println("載入遊戲配置失敗: " + e.getMessage());
        }
    }
    
    /**
     * 載入資源配置
     */
    private void loadResourceConfig() {
        try {
            Resource resource = resourceLoader.getResource(resourcePath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, String>> resources = mapper.readValue(is, 
                        new TypeReference<List<Map<String, String>>>() {});
                    configCache.put("resources", resources);
                    System.out.println("已載入資源配置，數量: " + resources.size());
                }
            }
        } catch (Exception e) {
            System.err.println("載入資源配置失敗: " + e.getMessage());
        }
    }
    
    /**
     * 載入建築配置
     */
    private void loadBuildingConfig() {
        try {
            Resource resource = resourceLoader.getResource(buildingPath);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, String>> buildings = mapper.readValue(is, 
                        new TypeReference<List<Map<String, String>>>() {});
                    configCache.put("buildings", buildings);
                    System.out.println("已載入建築配置，數量: " + buildings.size());
                }
            }
        } catch (Exception e) {
            System.err.println("載入建築配置失敗: " + e.getMessage());
        }
    }
    
    /**
     * 熱重載配置
     */
    public void reloadConfigs() {
        System.out.println("---- 重新載入遊戲配置 ----");
        configCache.clear();
        loadConfigs();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getResources() {
        return (List<Map<String, String>>) configCache.getOrDefault("resources", Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> getBuildings() {
        return (List<Map<String, String>>) configCache.getOrDefault("buildings", Collections.emptyList());
    }
}
