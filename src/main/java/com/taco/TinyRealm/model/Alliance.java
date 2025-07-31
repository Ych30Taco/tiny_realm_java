package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.taco.TinyRealm.module.ResourceModule.model.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 聯盟物件，紀錄聯盟資訊與成員。
 */
public class Alliance {
    /** 聯盟唯一識別碼 */
    @JsonProperty("id")
    private String id;
    /** 聯盟名稱 */
    @JsonProperty("name")
    private String name;
    /** 聯盟領袖玩家 ID */
    @JsonProperty("leaderId")
    private String leaderId;
    /** 聯盟成員列表（玩家 ID） */
    @JsonProperty("members")
    private List<String> members = new ArrayList<>();
    /** 聯盟等級 */
    @JsonProperty("level")
    private int level;
    /** 聯盟資源 */
    @JsonProperty("resources")
    //private Resource resources;

    /** 取得聯盟 ID */
    public String getId() { return id; }
    /** 設定聯盟 ID */
    public void setId(String id) { this.id = id; }
    /** 取得聯盟名稱 */
    public String getName() { return name; }
    /** 設定聯盟名稱 */
    public void setName(String name) { this.name = name; }
    /** 取得領袖 ID */
    public String getLeaderId() { return leaderId; }
    /** 設定領袖 ID */
    public void setLeaderId(String leaderId) { this.leaderId = leaderId; }
    /** 取得成員列表 */
    public List<String> getMembers() { return members; }
    /** 設定成員列表 */
    public void setMembers(List<String> members) { this.members = members; }
    /** 取得聯盟等級 */
    public int getLevel() { return level; }
    /** 設定聯盟等級 */
    public void setLevel(int level) { this.level = level; }
    /** 取得聯盟資源 */
    //public Resource getResources() { return resources; }
    /** 設定聯盟資源 */
    //public void setResources(Resource resources) { this.resources = resources; }
}
