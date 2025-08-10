package com.taco.TinyRealm.module.inventoryModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * 遊戲物品模型
 * 代表玩家背包中的各種物品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("quantity")
    private int quantity;
    
    @JsonProperty("maxQuantity")
    private int maxQuantity;
    
    @JsonProperty("rarity")
    private String rarity; // common, uncommon, rare, epic, legendary
    
    @JsonProperty("category")
    private String category; // weapon, armor, consumable, material, quest, etc.
    
    @JsonProperty("level")
    private int level;
    
    @JsonProperty("durability")
    private int durability;
    
    @JsonProperty("maxDurability")
    private int maxDurability;
    
    @JsonProperty("attributes")
    private Map<String, Object> attributes;
    
    @JsonProperty("effects")
    private Map<String, Object> effects;
    
    @JsonProperty("createdTime")
    private LocalDateTime createdTime;
    
    @JsonProperty("lastUsedTime")
    private LocalDateTime lastUsedTime;
    
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
    private String equipmentSlot; // weapon, armor, accessory, etc.
    
    @JsonProperty("sellPrice")
    private int sellPrice;
    
    @JsonProperty("buyPrice")
    private int buyPrice;
    
    /**
     * 檢查物品是否可用（耐久度大於0）
     */
    public boolean isUsable() {
        return durability > 0;
    }
    
    /**
     * 檢查物品是否可以堆疊
     */
    public boolean canStack() {
        return isStackable && quantity < maxQuantity;
    }
    
    /**
     * 檢查物品是否為裝備
     */
    public boolean isEquipment() {
        return isEquippable && equipmentSlot != null;
    }
    
    /**
     * 檢查物品是否為消耗品
     */
    public boolean isConsumable() {
        return isConsumable;
    }
    
    /**
     * 使用物品（減少耐久度）
     */
    public void use() {
        if (isConsumable) {
            quantity--;
        } else if (durability > 0) {
            durability--;
        }
        lastUsedTime = LocalDateTime.now();
    }
    
    /**
     * 修復物品耐久度
     */
    public void repair(int amount) {
        durability = Math.min(durability + amount, maxDurability);
    }
    
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
     * 檢查物品是否為特定稀有度
     */
    public boolean isRarity(String rarity) {
        return this.rarity != null && this.rarity.equalsIgnoreCase(rarity);
    }
    
    /**
     * 檢查物品是否為特定類別
     */
    public boolean isCategory(String category) {
        return this.category != null && this.category.equalsIgnoreCase(category);
    }
}
