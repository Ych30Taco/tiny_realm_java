package com.taco.TinyRealm.module.terrainMapModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapTile {
    private int x; // X 座標
    private int y; // Y 座標
    private Terrain terrain; // 地形
    private String ownerId; // 擁有者ID
    private String buildingId; // 建築ID
    private List<String> unitIds; // 單位ID列表
    private String enemyType; // 野怪類型
    private boolean hasEnemy; // 是否有野怪
    private int enemyLevel; // 野怪等級
}
