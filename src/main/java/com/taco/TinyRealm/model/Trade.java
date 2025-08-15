package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.taco.TinyRealm.module.ResourceModule.model.Resource;

import java.util.List;

/**
 * 玩家間交易物件，紀錄雙方資源與物品交換資訊。
 */
public class Trade {
    /** 交易唯一識別碼 */
    @JsonProperty("id")
    private String id;
    /** 發起方玩家 ID */
    @JsonProperty("initiatorId")
    private String initiatorId;
    /** 接收方玩家 ID */
    @JsonProperty("receiverId")
    private String receiverId;
    /** 發起方提供的資源 */
    /*@JsonProperty("offeredResources")
    private Resource offeredResources;*/
    /** 發起方提供的物品列表 */
   // @JsonProperty("offeredItems")
    //private List<Item> offeredItems;
    /** 發起方要求的資源 */
   /* @JsonProperty("requestedResources")
    private Resource requestedResources;*/
    /** 發起方要求的物品列表 */
    //@JsonProperty("requestedItems")
   // private List<Item> requestedItems;
    /** 交易狀態（PENDING/ACCEPTED/REJECTED） */
    @JsonProperty("status")
    private String status;

    /** 取得交易 ID */
    public String getId() { return id; }
    /** 設定交易 ID */
    public void setId(String id) { this.id = id; }
    /** 取得發起方 ID */
    public String getInitiatorId() { return initiatorId; }
    /** 設定發起方 ID */
    public void setInitiatorId(String initiatorId) { this.initiatorId = initiatorId; }
    /** 取得接收方 ID */
    public String getReceiverId() { return receiverId; }
    /** 設定接收方 ID */
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    /** 取得發起方提供資源 */
    //public Resource getOfferedResources() { return offeredResources; }
    /** 設定發起方提供資源 */
   // public void setOfferedResources(Resource offeredResources) { this.offeredResources = offeredResources; }
    /** 取得發起方提供物品 */
   // public List<Item> getOfferedItems() { return offeredItems; }
    /** 設定發起方提供物品 */
    //public void setOfferedItems(List<Item> offeredItems) { this.offeredItems = offeredItems; }
    /** 取得要求資源 */
    //public Resource getRequestedResources() { return requestedResources; }
    /** 設定要求資源 */
    //public void setRequestedResources(Resource requestedResources) { this.requestedResources = requestedResources; }
    /** 取得要求物品 */
    //public List<Item> getRequestedItems() { return requestedItems; }
    /** 設定要求物品 */
    //public void setRequestedItems(List<Item> requestedItems) { this.requestedItems = requestedItems; }
    /** 取得交易狀態 */
    public String getStatus() { return status; }
    /** 設定交易狀態 */
    public void setStatus(String status) { this.status = status; }
}
