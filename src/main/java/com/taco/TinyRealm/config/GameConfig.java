package com.taco.TinyRealm.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class GameConfig {
    private List<Map<String, String>> resources = Collections.emptyList();
    private List<Map<String, String>> buildings = Collections.emptyList();
    // 可依需求增加更多靜態配置

    @PostConstruct
    public void loadConfigs() {
        ObjectMapper mapper = new ObjectMapper();
        // 讀取 resources.json
        /*try {
            File resFile = new File("src/main/resources/config/resources.json");
            System.out.println(resFile);
            if (resFile.exists()) {
                resources = mapper.readValue(resFile, new TypeReference<List<Map<String, String>>>() {});
                System.out.println("Loaded resources.json, count: " + resources.size());
            }
        } catch (Exception e) {
            System.err.println("Failed to load resources.json: " + e.getMessage());
        }*/
        // 讀取 buildings.json
        /*try {
            File buildFile = new File("src/main/resources/config/buildings.json");
            if (buildFile.exists()) {
                buildings = mapper.readValue(buildFile, new TypeReference<List<Map<String, String>>>() {});
                System.out.println("Loaded buildings.json, count: " + buildings.size());
            }
        } catch (Exception e) {
            System.err.println("Failed to load buildings.json: " + e.getMessage());
        }*/
    }

    public List<Map<String, String>> getResources() {
        return resources;
    }

    public List<Map<String, String>> getBuildings() {
        return buildings;
    }
}
