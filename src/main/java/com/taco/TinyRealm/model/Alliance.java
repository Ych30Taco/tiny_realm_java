package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Alliance {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("leaderId")
    private String leaderId;
    @JsonProperty("members")
    private List<String> members = new ArrayList<>();
    @JsonProperty("level")
    private int level;
    @JsonProperty("resources")
    private Resource resources;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLeaderId() { return leaderId; }
    public void setLeaderId(String leaderId) { this.leaderId = leaderId; }
    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public Resource getResources() { return resources; }
    public void setResources(Resource resources) { this.resources = resources; }
}
