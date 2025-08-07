package com.taco.TinyRealm.module.terrainModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terrain {
    private String id; // 唯一標識（例如 "plains_001"）
    private String name; // 地形名稱（例如 "平原"）
    private Map<String, Double> movementCost; // 不同單位類型的移動成本（例如 {"infantry": 1.0, "cavalry": 1.5}）
    private String resourceType; // 資源類型（例如 "Food", "Stone"）
    private Integer resourceAmount; // 資源數量
    private Boolean isPassable; // 是否可通行
    private Boolean isRoadRequired; // 是否需要道路才能通行
    private Integer defenseBonus; // 防禦加成（例如山脈 +20%）
    private Boolean buildable; // 是否可建造建築
    private String description; // 地形描述（可選）
}


