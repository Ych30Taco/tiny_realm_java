package com.taco.TinyRealm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    @JsonProperty("player")
    private Player player;
    @JsonProperty("resources")
    private Resource resources;
    @JsonProperty("buildings")
    private List<Building> buildings = new ArrayList<>();
    @JsonProperty("inventory")
    private List<Item> inventory = new ArrayList<>();
    @JsonProperty("events")
    private List<GameEvent> events = new ArrayList<>();
    @JsonProperty("terrains")
    private List<Terrain> terrains = new ArrayList<>();
    @JsonProperty("units")
    private List<Unit> units = new ArrayList<>();
    @JsonProperty("trades")
    private List<Trade> trades = new ArrayList<>();
    @JsonProperty("version")
    private String version = "1.0";

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Resource getResources() { return resources; }
    public void setResources(Resource resources) { this.resources = resources; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public List<Building> getBuildings() { return buildings; }
    public void setBuildings(List<Building> buildings) { this.buildings = buildings; }
    public List<Item> getInventory() { return inventory; }
    public void setInventory(List<Item> inventory) { this.inventory = inventory; }
    public List<GameEvent> getEvents() { return events; }
    public void setEvents(List<GameEvent> events) { this.events = events; }
    public List<Terrain> getTerrains() { return terrains; }
    public void setTerrains(List<Terrain> terrains) { this.terrains = terrains; }
    public List<Unit> getUnits() { return units; }
    public void setUnits(List<Unit> units) { this.units = units; }
    public List<Trade> getTrades() { return trades; }
    public void setTrades(List<Trade> trades) { this.trades = trades; }
}
