package com.taco.TinyRealm.module.ResourceModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource {
    @JsonProperty("gold")
    private int gold;
    @JsonProperty("wood")
    private int wood;

    // Getters and Setters
    public int getGold() { return gold; }
    public void setGold(int gold) { this.gold = gold; }
    public int getWood() { return wood; }
    public void setWood(int wood) { this.wood = wood; }

    public enum ResourceType
    {
        Food,   // 糧食
        Wood,   // 木材
        Stone,  // 石頭
        Iron,   // 鐵礦
        Coin,   // 金幣
        // Gem, // 付費貨幣（如需獨立管理可加入）
        // ...未來可擴充更多資源類型...
    }
}
