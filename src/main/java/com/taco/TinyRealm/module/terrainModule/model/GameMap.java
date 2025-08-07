package com.taco.TinyRealm.module.terrainModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMap {
    private int width; // 地圖寬度
    private int height; // 地圖高度
    private Map<String, MapTile> tiles; // 座標 (x,y) -> MapTile，key 為 "x,y"

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new HashMap<>();
    }

    public MapTile getTile(int x, int y) {
        return tiles.get(x + "," + y);
    }

    public void setTile(int x, int y, MapTile tile) {
        tiles.put(x + "," + y, tile);
    }

}