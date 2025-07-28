package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Terrain {
    @JsonProperty("x")
    private int x;
    @JsonProperty("y")
    private int y;
    @JsonProperty("type")
    private String type;
    @JsonProperty("occupied")
    private boolean occupied;

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
}
