package com.taco.TinyRealm.module.terrainModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terrain {
    private String id;
    private String name;
    private Map<String, Double> movementCost;
    private String resourceType;
    private Integer resourceAmount;
    private Boolean isPassable;
    private Boolean isRoadRequired;
    private Integer defenseBonus;
    private Boolean buildable;
    private String description;
}


