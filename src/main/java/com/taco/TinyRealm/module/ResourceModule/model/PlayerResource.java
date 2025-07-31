package com.taco.TinyRealm.module.resourceModule.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResource {
    private Map<String, Integer> nowAmount; // 資源ID -> 數量，例如 {"wood": 1000, "food": 500}
    private Map<String, Integer> maxAmount; // 資源ID -> 儲存上限（可隨建築升級變化）
    private long lastUpdatedTime; // 上次更新時間戳（毫秒）
}
