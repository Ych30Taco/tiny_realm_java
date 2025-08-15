package com.taco.TinyRealm.module.buildingModule.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 建築等級數據模型
 * 
 * 定義建築在特定等級的詳細屬性，包括建造時間、成本、產出等。
 * 每個建築類型都有多個等級，每個等級都有對應的LevelData。
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelData {
    
    /**
     * 建築等級
     * 
     * 從1開始，表示建築的當前等級
     */
    private int level;
    
    /**
     * 建造/升級時間
     * 
     * 單位：毫秒
     * 例如：60000表示1分鐘
     */
    private int buildTime;
    
    /**
     * 建造/升級成本
     * 
     * 格式：資源ID -> 數量
     * 例如：{"WOOD": 100, "FOOD": 50, "IRON": 0}
     */
    private Map<String, Integer> cost;
    
    /**
     * 建築產出
     * 
     * 格式：產出類型 -> 數量
     * 例如：{"soldierCapacity": 50}（兵營容量）
     * 或 {"food": 100}（農田產出）
     */
    private Map<String, Integer> output;
    
    /**
     * 升級前置條件
     * 
     * 格式：建築ID -> 要求等級
     * 例如：{"mainHall": 1}（需要主城等級1）
     */
    private Map<String, String> prerequisites;
    
    /**
     * 每小時生產速率
     * 
     * 只有資源生產建築才有此屬性
     * 單位：每小時生產的資源數量
     * 例如：100表示每小時生產100單位資源
     */
    private int productionRate;
}
