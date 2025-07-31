package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.taco.TinyRealm.module.ResourceModule.model.Resource;

/**
 * 市場掛單物件，紀錄資源或物品的交易資訊。
 */
public class MarketListing {
    /** 掛單唯一識別碼 */
    @JsonProperty("id")
    private String id;
    /** 賣家玩家 ID */
    @JsonProperty("sellerId")
    private String sellerId;
    /** 掛單類型（resource/item） */
    @JsonProperty("type")
    private String type; // "resource" or "item"
    /** 掛單資源內容（若為資源掛單） */
/*     @JsonProperty("resource")
    private Resource resource; */
    /** 掛單物品內容（若為物品掛單） */
    @JsonProperty("item")
    private Item item;
    /** 價格（資源型態） */
/*     @JsonProperty("price")
    private Resource price; */
    /** 掛單狀態（ACTIVE/SOLD） */
    @JsonProperty("status")
    private String status;

    /** 取得掛單 ID */
    public String getId() { return id; }
    /** 設定掛單 ID */
    public void setId(String id) { this.id = id; }
    /** 取得賣家 ID */
    public String getSellerId() { return sellerId; }
    /** 設定賣家 ID */
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    /** 取得掛單類型 */
    public String getType() { return type; }
    /** 設定掛單類型 */
    public void setType(String type) { this.type = type; }
    /** 取得資源內容 */
    //public Resource getResource() { return resource; }
    /** 設定資源內容 */
   // public void setResource(Resource resource) { this.resource = resource; }
    /** 取得物品內容 */
    public Item getItem() { return item; }
    /** 設定物品內容 */
    public void setItem(Item item) { this.item = item; }
    /** 取得價格 */
    //public Resource getPrice() { return price; }
    /** 設定價格 */
    //public void setPrice(Resource price) { this.price = price; }
    /** 取得掛單狀態 */
    public String getStatus() { return status; }
    /** 設定掛單狀態 */
    public void setStatus(String status) { this.status = status; }
}
