package com.taco.TinyRealm.module.ResourceModule.service;

import com.taco.TinyRealm.module.ResourceModule.model.Resource;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;



@Service
public class ResourceService {

    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private final ResourceLoader resourceLoader;  // Spring 資源載入器

    // 用於儲存所有資源類型的 Map，以資源 ID 為鍵，方便快速查找其定義
    private Map<String, Resource> resourceDefinitions;

    // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.ResourceType-path}")
    private String resourceTypesPath;

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
        try {
            // 從 classpath 載入 resource_types.json 檔案
            org.springframework.core.io.Resource resourceFile = resourceLoader.getResource(resourceTypesPath);
            try (InputStream inputStream = resourceFile.getInputStream()) {
                // 使用 ObjectMapper 將 JSON 檔案內容反序列化為 List<ResourceType>
                Map<String, List<Map<String,String>>> resources = objectMapper.readValue(inputStream, new TypeReference<Map<String, List<Map<String,String>>>>() {});
                List<Map<String,String>> resourcedata = resources.get("ResourceType");
                System.out.println("DEBUG: 資源類型已成功載入 ("+resourcedata.size() + " 種)。");
                // 印出所有 name
                for (Map<String, String> data : resourcedata) {
                    System.out.println("Resource name: " + data.get("name"));
                }
                
            }
            
            /* 
            // 將 JSON 數組轉為 ResourceType 對象列表
            
            return resourceNames.stream()
                    .map(ResourceType::new)
                    .collect(Collectors.toList());*/

         } catch (IOException e) {
            // 如果載入失敗，列印錯誤訊息並堆疊追蹤。
            // 在實際應用中，可以考慮拋出 RuntimeException 讓應用程式啟動失敗，
            // 因為靜態數據載入失敗通常是不可接受的錯誤。
            System.err.println("錯誤：載入資源類型失敗！請檢查 " + resourceTypesPath + " 檔案。" + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 獲取所有已定義的資源類型列表。
     * @return 包含所有資源類型定義的列表。
     */
    public List<Resource> getAllResourceDefinitions() {
        // 返回一個新的 ArrayList，避免外部直接修改內部 Map
        return new java.util.ArrayList<>(resourceDefinitions.values());
    }




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
}
