package com.taco.TinyRealm.module.ResourceModule.model;

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
    private long baseStorageCapacity;
    private double protectedStoragePercentage;
    private long protectedStorageMaxAmount;
    private List<String> acquisitionMethods;
}
