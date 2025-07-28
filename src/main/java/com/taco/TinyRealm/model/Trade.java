package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Trade {
    @JsonProperty("id")
    private String id;
    @JsonProperty("initiatorId")
    private String initiatorId;
    @JsonProperty("receiverId")
    private String receiverId;
    @JsonProperty("offeredResources")
    private Resource offeredResources;
    @JsonProperty("offeredItems")
    private List<Item> offeredItems;
    @JsonProperty("requestedResources")
    private Resource requestedResources;
    @JsonProperty("requestedItems")
    private List<Item> requestedItems;
    @JsonProperty("status")
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getInitiatorId() { return initiatorId; }
    public void setInitiatorId(String initiatorId) { this.initiatorId = initiatorId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public Resource getOfferedResources() { return offeredResources; }
    public void setOfferedResources(Resource offeredResources) { this.offeredResources = offeredResources; }
    public List<Item> getOfferedItems() { return offeredItems; }
    public void setOfferedItems(List<Item> offeredItems) { this.offeredItems = offeredItems; }
    public Resource getRequestedResources() { return requestedResources; }
    public void setRequestedResources(Resource requestedResources) { this.requestedResources = requestedResources; }
    public List<Item> getRequestedItems() { return requestedItems; }
    public void setRequestedItems(List<Item> requestedItems) { this.requestedItems = requestedItems; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
