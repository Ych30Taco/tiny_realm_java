package com.taco.TinyRealm.modules.EventSystemModule;

/**
 * 事件名稱集中管理類別。
 * 將所有事件名稱定義於此，方便統一管理與自動補全。
 * 操作流程：
 * 1. 新增事件時，於本類別新增 public static final String 欄位。
 *    - 若未來新增資源種類，請同步於此新增相關資源事件名稱（如：Resource_FoodChanged、Resource_GemChanged 等）。
 * 2. 使用事件時，直接引用 EventNames.事件名稱，避免拼字錯誤。
 * 3. 若有新功能模組（如：貿易、背包、付費貨幣等），請於此預留或新增對應事件名稱。
 */
public final class EventNames {
    private EventNames() {}
    // 資源相關事件
    public static final String ResourceChanged = "ResourceChanged"; // 任一資源變動事件（泛用）
    public static final String ResourceUnitPacked = "ResourceUnitPacked"; // 資源打包成單位事件
    public static final String ResourceUnitUsed = "ResourceUnitUsed"; // 使用資源單位事件
    public static final String ResourceExchanged = "ResourceExchanged"; // 資源與金幣兌換事件
    public static final String PremiumCurrencyChanged = "PremiumCurrencyChanged"; // 付費貨幣變動事件

    // 建築相關事件
    public static final String BuildingCompleted = "BuildingCompleted"; // 建築完成事件
    public static final String StorageLimitChanged = "StorageLimitChanged"; // 倉庫/錢莊/糧倉上限變動事件

    // 貿易/交易相關事件
    public static final String TradeWithNPC = "TradeWithNPC"; // 與NPC貿易事件
    public static final String MarketRefreshed = "MarketRefreshed"; // 市場/黑市刷新事件
    public static final String TradeCompleted = "TradeCompleted"; // 交易完成事件（玩家與玩家、玩家與NPC皆可用）

    // 背包相關事件
    public static final String InventoryChanged = "InventoryChanged"; // 背包內容變動事件

    // 資源損失相關事件
    public static final String ResourceLost = "ResourceLost"; // 資源因事件損失

    // ...未來擴充事件請於此新增，並補上註解...
}
