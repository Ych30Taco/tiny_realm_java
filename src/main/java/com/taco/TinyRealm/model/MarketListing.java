package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MarketListing {
    @JsonProperty("id")
    private String id;
    @JsonProperty("sellerId")
    private String sellerId;
    @JsonProperty("type")
    private String type; // "resource" or "item"
    @JsonProperty("resource")
    private Resource resource;
    @JsonProperty("item")
    private Item item;
    @JsonProperty("price")
    private Resource price;
    @JsonProperty("status")
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Resource getResource() { return resource; }
    public void setResource(Resource resource) { this.resource = resource; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public Resource getPrice() { return price; }
    public void setPrice(Resource price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
