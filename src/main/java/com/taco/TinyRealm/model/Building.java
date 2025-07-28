package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Building {
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("level")
    private int level;
    @JsonProperty("x")
    private int x;
    @JsonProperty("y")
    private int y;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
}
