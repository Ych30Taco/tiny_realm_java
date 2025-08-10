package com.taco.TinyRealm.module.inventoryModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.HashMap;

/**
 * 物品類型配置模型
 * 定義不同物品類型的基本屬性和配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemType {
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("rarity")
    private String rarity;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("maxQuantity")
    private int maxQuantity;
    
    @JsonProperty("maxDurability")
    private int maxDurability;
    
    @JsonProperty("level")
    private int level;
    
    @JsonProperty("attributes")
    private Map<String, Object> attributes;
    
    @JsonProperty("effects")
    private Map<String, Object> effects;
    
    @JsonProperty("isStackable")
    private boolean isStackable;
    
    @JsonProperty("isTradeable")
    private boolean isTradeable;
    
    @JsonProperty("isDroppable")
    private boolean isDroppable;
    
    @JsonProperty("isConsumable")
    private boolean isConsumable;
    
    @JsonProperty("isEquippable")
    private boolean isEquippable;
    
    @JsonProperty("equipmentSlot")
    private String equipmentSlot;
    
    @JsonProperty("sellPrice")
    private int sellPrice;
    
    @JsonProperty("buyPrice")
    private int buyPrice;
    
    @JsonProperty("craftingRecipe")
    private Map<String, Integer> craftingRecipe;
    
    @JsonProperty("requiredLevel")
    private int requiredLevel;
    
    @JsonProperty("requiredTech")
    private String requiredTech;
    
    @JsonProperty("dropRate")
    private double dropRate;
    
    @JsonProperty("useEffect")
    private Map<String, Object> useEffect;
    
    /**
     * 獲取物品的特定屬性值
     */
    public Object getAttribute(String key) {
        return attributes != null ? attributes.get(key) : null;
    }
    
    /**
     * 設置物品的特定屬性值
     */
    public void setAttribute(String key, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
    
    /**
     * 獲取物品的特定效果值
     */
    public Object getEffect(String key) {
        return effects != null ? effects.get(key) : null;
    }
    
    /**
     * 設置物品的特定效果值
     */
    public void setEffect(String key, Object value) {
        if (effects == null) {
            effects = new HashMap<>();
        }
        effects.put(key, value);
    }
    
    /**
     * 獲取製作配方中特定材料的數量
     */
    public int getCraftingMaterial(String material) {
        return craftingRecipe != null ? craftingRecipe.getOrDefault(material, 0) : 0;
    }
    
    /**
     * 檢查是否為特定稀有度
     */
    public boolean isRarity(String rarity) {
        return this.rarity != null && this.rarity.equalsIgnoreCase(rarity);
    }
    
    /**
     * 檢查是否為特定類別
     */
    public boolean isCategory(String category) {
        return this.category != null && this.category.equalsIgnoreCase(category);
    }
    
    /**
     * 檢查是否為裝備
     */
    public boolean isEquipment() {
        return isEquippable && equipmentSlot != null;
    }
    
    /**
     * 檢查是否為消耗品
     */
    public boolean isConsumable() {
        return isConsumable;
    }
    
    /**
     * 檢查是否可以堆疊
     */
    public boolean canStack() {
        return isStackable && maxQuantity > 1;
    }
}
