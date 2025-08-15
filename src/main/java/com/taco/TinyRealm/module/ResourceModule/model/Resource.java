package com.taco.TinyRealm.module.resourceModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private String id;
    private Map<String, String> name;
    private Map<String, String> description;
    private String resourceIconURL;
    private boolean premium;
    private boolean stackable;
    private String category;
    private int sortOrder;
    private int nowAmount;
    private int maxAmount;
    private double protectedStoragePercentage;
    private int protectedStorageMaxAmount;
    private List<String> acquisitionMethods;
    private int baseProductionRate;// 基礎生產速率（每小時）
}
