package com.taco.TinyRealm.module.ResourcesModule.service;

import com.fasterxml.jackson.core.type.TypeReference; // 用於 Jackson 反序列化 List
import com.fasterxml.jackson.databind.ObjectMapper;   // Jackson 核心物件，用於 JSON 處理
import com.taco.TinyRealm.module.ResourcesModule.model.Resource;     // 引入資源類型模型
import com.taco.TinyRealm.module.EventSystemModule.EventPublisher; // 匯入事件發佈器
import com.taco.TinyRealm.module.EventSystemModule.EventType;      // 匯入事件型別
import org.springframework.beans.factory.annotation.Value; // 用於從配置檔讀取屬性
import org.springframework.core.io.ResourceLoader;       // 用於載入 Spring 資源 (如 classpath 中的檔案)
import org.springframework.stereotype.Service;           // 標記為 Spring 服務組件

import jakarta.annotation.PostConstruct; // 用於標記初始化方法
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 資源模組的服務層。
 * 負責管理資源的靜態定義，以及處理玩家資源的增加和消耗。
 * 也包含了每秒自動產出資源的定時任務。
 */
@Service
public class ResourceService {
    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    private final ObjectMapper objectMapper;      // Jackson JSON 處理器
    private final ResourceLoader resourceLoader;  // Spring 資源載入器
    private final EventPublisher eventPublisher; // 新增事件發佈器欄位

    // 用於儲存所有資源類型的 Map，以資源 ID 為鍵，方便快速查找其定義
    private Map<String, Resource> resourceDefinitions;

    // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.resource-types-path}")
    private String resourceTypesPath;

    /**
     * 建構子注入依賴。
     * Spring 會自動提供這些依賴的實例。
     * @param objectMapper Jackson ObjectMapper 實例
     * @param resourceLoader Spring ResourceLoader 實例
     * @param eventPublisher 事件發佈器實例
     */
    public ResourceService(
        ObjectMapper objectMapper,
        ResourceLoader resourceLoader,
        EventPublisher eventPublisher // 新增參數
    ) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.eventPublisher = eventPublisher; // 注入事件發佈器
    }

    /**
     * 初始化方法：在 ResourceService 實例化並注入所有依賴後自動執行。
     * 負責從配置的 JSON 檔案中載入所有資源類型的定義。
     */
    @PostConstruct
    public void init() {
        try {
            // 從 classpath 載入 resource_types.json 檔案
            org.springframework.core.io.Resource resourceFile = resourceLoader.getResource(resourceTypesPath);
            try (InputStream inputStream = resourceFile.getInputStream()) {
                // 使用 ObjectMapper 將 JSON 檔案內容反序列化為 List<Resource>
                List<Resource> resources = objectMapper.readValue(inputStream, new TypeReference<List<Resource>>() {});
                // 將 List 轉換為 Map (以資源 ID 為鍵，Resource 物件為值)，方便快速查找
                resourceDefinitions = resources.stream()
                                                .collect(Collectors.toMap(Resource::getId, Function.identity()));
                System.out.println("DEBUG: 資源類型已成功載入 (" + resourceDefinitions.size() + " 種)。");
                logger.info("資源類型已成功載入 ({} 種)。", resourceDefinitions.size());
            }
        } catch (IOException e) {
            // 如果載入失敗，列印錯誤訊息並堆疊追蹤。
            // 在實際應用中，可以考慮拋出 RuntimeException 讓應用程式啟動失敗，
            // 因為靜態數據載入失敗通常是不可接受的錯誤。
            System.err.println("錯誤：載入資源類型失敗！請檢查 " + resourceTypesPath + " 檔案。" + e.getMessage());
            logger.error("載入資源類型失敗！請檢查 {} 檔案。{}", resourceTypesPath, e.getMessage(), e);
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

    /**
     * 根據資源 ID 獲取單個資源類型的定義。
     * @param resourceId 要查詢的資源 ID。
     * @return 如果找到，則返回包含 Resource 物件的 Optional；否則返回 Optional.empty()。
     */
    public Optional<Resource> getResourceDefinitionById(String resourceId) {
        return Optional.ofNullable(resourceDefinitions.get(resourceId));
    }
}