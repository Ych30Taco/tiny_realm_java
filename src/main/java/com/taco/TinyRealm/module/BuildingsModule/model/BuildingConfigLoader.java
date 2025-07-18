package com.taco.TinyRealm.module.BuildingsModule.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct; // 確保導入
import org.springframework.stereotype.Component; // Spring 組件註解
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;  // 用於初始化 List
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 負責從 JSON 配置文件中載入所有建築物的類型配置。
 * 這些配置數據在應用程式啟動時載入，並在運行時作為靜態數據提供。
 */
@Component // 將其標記為 Spring 組件，讓 Spring 管理其生命週期和依賴注入
public class BuildingConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(BuildingConfigLoader.class);
    // 配置文件的路徑，Spring Boot 會在 classpath 中查找，例如 src/main/resources/config/
     // 從 application.yaml 中讀取靜態資源定義檔案的路徑
    @Value("${app.data.building-types-path}")
    private String buildingTypesPath;

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader; // Spring 提供的資源載入器

    // 記憶體中的建築類型配置快取，Key為 typeId
    private Map<String, BuildingTypeConfig> buildingConfigs;

    // 構造函數：Spring 會自動注入 ObjectMapper 和 ResourceLoader
    public BuildingConfigLoader(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.buildingConfigs = Collections.emptyMap(); // 初始化為空 Map
    }

    /**
     * 在 Spring 容器初始化 BuildingConfigLoader bean 之後自動調用此方法。
     * 負責載入配置檔案並初始化記憶體中的配置快取。
     */
    @PostConstruct
    public void init() {
        logger.info("[BuildingConfigLoader] 準備從 {} 載入建築物配置...", buildingTypesPath);
        try {
            Resource resource = resourceLoader.getResource(buildingTypesPath);
            if (!resource.exists() || !resource.isReadable()) {
                logger.error("[BuildingConfigLoader] 建築物配置檔案不存在或不可讀: {}", buildingTypesPath);
                return;
            }
            logger.info("[BuildingConfigLoader] 成功找到建築物配置檔案: {}，開始讀取...", buildingTypesPath);
            try (InputStream inputStream = resource.getInputStream()) {
                List<BuildingTypeConfig> loadedConfigs = objectMapper.readValue(inputStream,
                        new TypeReference<List<BuildingTypeConfig>>() {});
                buildingConfigs = loadedConfigs.stream()
                        .collect(Collectors.toMap(BuildingTypeConfig::getTypeId, Function.identity()));
                logger.info("[BuildingConfigLoader] 成功載入 {} 筆建築物配置。", buildingConfigs.size());
                logger.debug("[BuildingConfigLoader] 已載入的建築物 TypeIds: {}", buildingConfigs.keySet());
            }
        } catch (IOException e) {
            logger.error("[BuildingConfigLoader] 載入建築物配置失敗: {}，錯誤訊息: {}", buildingTypesPath, e.getMessage(), e);
        }
    }

    /**
     * 根據建築類型ID獲取其配置數據。
     * @param typeId 建築類型ID (例如 "FARM", "BARRACKS")
     * @return 建築類型配置的 Optional 對象，如果找到則包含配置，否則為空 Optional。
     */
    public Optional<BuildingTypeConfig> getBuildingConfig(String typeId) {
        return Optional.ofNullable(buildingConfigs.get(typeId));
    }

    /**
     * 獲取所有建築類型配置的列表。
     * @return 所有建築類型配置的不可修改列表。
     */
    public List<BuildingTypeConfig> getAllBuildingConfigs() {
        return Collections.unmodifiableList(new ArrayList<>(buildingConfigs.values()));
    }
}