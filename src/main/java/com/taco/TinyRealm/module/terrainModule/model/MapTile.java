package com.taco.TinyRealm.module.terrainModule.model;

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
    private Terrain terrain; // 地形類型
    private String ownerId; // 擁有者（玩家 ID，null 表示無主）
    private String buildingId; // 建築 ID（null 表示無建築）
    private List<String> unitIds; // 駐紮單位 ID 列表（可選）

    // Getters and Setters
}
