package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taco.TinyRealm.module.ResourceModule.model.Resource;

public class Task {
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("description")
    private String description;
    @JsonProperty("progress")
    private int progress;
    @JsonProperty("target")
    private int target;
    @JsonProperty("rewards")
    private Resource rewards;
    @JsonProperty("status")
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
    public int getTarget() { return target; }
    public void setTarget(int target) { this.target = target; }
    public Resource getRewards() { return rewards; }
    public void setRewards(Resource rewards) { this.rewards = rewards; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
