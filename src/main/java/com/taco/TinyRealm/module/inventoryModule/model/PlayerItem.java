package com.taco.TinyRealm.module.inventoryModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;



/**
 * 玩家背包物品簡化模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerItem {
    private String id;  // 物品唯一識別碼
    private String type; // 物品類型
    private String name; // 物品名稱
    private String description; // 物品描述
    private int quantity; // 物品數量
    private String rarity; // 稀有度
    private String category; // 類別
    private int level; // 物品等級
    private int durability; // 耐久度
    private Map<String, Object> attributes; // 物品屬性
    private boolean stackable; // 是否可堆疊
    private boolean tradeable; // 是否可交易
    private boolean droppable; // 是否可掉落
    private boolean consumable; // 是否為消耗品
    private boolean isEquippable; // 是否可裝備
    private String equipmentSlot; // 裝備槽
    private int sellPrice; // 賣出價格
    private int buyPrice; // 購買價格
}
