package com.taco.TinyRealm.modules.ResourcesModule;

/**
 * ResourceData 用於儲存單一資源的相關資料。
 * 包含資源種類、數量與上限。
 */
public class ResourceData {
    /**
     * 資源種類。
     * 例如：金幣、木材、石頭等。
     */
    public ResourceType Type;
    /**
     * 當前擁有的資源數量。
     */
    public int Amount;
    /**
     * 此資源的最大上限。
     */
    public int Limit;
}
