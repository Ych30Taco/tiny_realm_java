package com.taco.TinyRealm.module.terrainMapModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMap {
    private String id; // 地圖ID
    private int width; // 地圖寬度
    private int height; // 地圖高度
    private List<MapTile> tiles; // 地圖磚塊清單

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new ArrayList<>();
    }

    public List<MapTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<MapTile> tiles) {
        this.tiles = tiles;
    }

}