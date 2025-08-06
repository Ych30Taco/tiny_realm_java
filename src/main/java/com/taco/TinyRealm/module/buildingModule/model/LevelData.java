package com.taco.TinyRealm.module.buildingModule.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelData {
    private int level;
    private int buildTime;
    private Map<String, Integer> cost;
    private Map<String, Integer> output;
    private Map<String, String> prerequisites;
    private int productionRate; // 每小時生產速率
}
