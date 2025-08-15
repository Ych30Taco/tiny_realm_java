package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Unit {
    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("count")
    private int count;
    @JsonProperty("x")
    private int x;
    @JsonProperty("y")
    private int y;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
}
