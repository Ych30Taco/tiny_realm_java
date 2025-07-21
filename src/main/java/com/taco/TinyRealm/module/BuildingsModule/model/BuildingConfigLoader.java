package com.taco.TinyRealm.module.BuildingsModule.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 負責從 JSON 配置文件中載入所有建築物的類型配置。
 * 這些配置數據在應用程式啟動時載入，並在運行時作為靜態數據提供。
 */
@Component
public class BuildingConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(BuildingConfigLoader.class);
    private static final String CONFIG_FILE_PATH = "classpath:config/buildings.json";

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    private Map<String, BuildingTypeConfig> buildingConfigs;

    public BuildingConfigLoader(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.buildingConfigs = Collections.emptyMap();
    }

    @PostConstruct
    public void init() {
        logger.info("Attempting to load building configurations from: {}", CONFIG_FILE_PATH);
        try {
            Resource resource = resourceLoader.getResource(CONFIG_FILE_PATH);
            if (!resource.exists() || !resource.isReadable()) {
                logger.error("Building configuration file not found or not readable: {}", CONFIG_FILE_PATH);
                return;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                List<BuildingTypeConfig> loadedConfigs = objectMapper.readValue(inputStream,
                        new TypeReference<List<BuildingTypeConfig>>() {});

                buildingConfigs = loadedConfigs.stream()
                        .collect(Collectors.toMap(BuildingTypeConfig::getTypeId, Function.identity()));

                logger.info("Successfully loaded {} building configurations.", buildingConfigs.size());
                logger.debug("Loaded building TypeIds: {}", buildingConfigs.keySet());
            }
        } catch (IOException e) {
            logger.error("Failed to load building configurations from {}: {}", CONFIG_FILE_PATH, e.getMessage(), e);
        }
    }

    public Optional<BuildingTypeConfig> getBuildingConfig(String typeId) {
        return Optional.ofNullable(buildingConfigs.get(typeId));
    }

    public List<BuildingTypeConfig> getAllBuildingConfigs() {
        return Collections.unmodifiableList(new ArrayList<>(buildingConfigs.values()));
    }
}