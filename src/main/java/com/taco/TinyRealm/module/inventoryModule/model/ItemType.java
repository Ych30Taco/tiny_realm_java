package com.taco.TinyRealm.module.inventoryModule.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

/**
 * 物品類型配置模型
 * 定義不同物品類型的基本屬性和配置
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemType {
    private String id; // 物品類型唯一識別碼
    private String type; // 物品類型，例如武器、護甲、消耗品等
    private String name; // 物品類型名稱
    private String description; // 物品類型描述
    private String rarity; // 稀有度 普通、稀有、史詩、傳奇等
    private String category; // 物品類別，例如武器、盔甲、消耗品等
    private int maxQuantity; // 物品最大堆疊數量
    private int maxDurability; // 最大耐久度
    private int level; // 物品等級
    private Map<String, Object> attributes; // 物品屬性，例如攻擊力、防禦力、魔法值等
    private Map<String, Object> effects; // 物品效果，例如恢復生命值、增加經驗值等
    private boolean stackable; // 是否可以堆疊
    private boolean tradeable; // 是否可以交易
    private boolean droppable; // 是否可以掉落
    private boolean consumable; // 是否為消耗品
    private boolean equippable; // 是否可以裝備
    private String equipmentSlot; // 裝備槽 武器、護甲、配件等
    private int sellPrice; // 賣出價格
    private int buyPrice;   // 購買價格
    private Map<String, Integer> craftingRecipe; // 製作配方，包含所需材料和數量
    private int requiredLevel; // 製作或使用物品所需的等級
    private String requiredTech; // 製作或使用物品所需的科技或技能
    private double dropRate; // 掉落率，表示物品在戰鬥或探索中掉落的機率
    private Map<String, Object> useEffect; // 使用物品時的效果，例如恢復生命值、增加經驗值等
}
