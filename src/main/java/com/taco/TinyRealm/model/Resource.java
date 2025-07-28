package com.taco.TinyRealm.model;

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
}
