package com.taco.TinyRealm.module.resourceModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType {
    private String resourceID;
    private Map<String, String> resourceName;
    private Map<String, String> resourceDescription;
    private String resourceIconURL;
    private boolean isPremium;
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
