package com.taco.TinyRealm.module.buildingModule.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 建築類型定義模型
 * 
 * 定義遊戲中各種建築的靜態屬性，包括名稱、類型、等級數據等。
 * 這個模型用於配置文件中，定義所有可用的建築類型。
 * 
 * 建築分類：
 * - function: 功能性建築（主城、倉庫等）
 * - resource: 資源生產建築（農田、伐木場等）
 * - military: 軍事建築（兵營、箭塔等）
 * - defense: 防禦建築（城牆、陷阱等）
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Building {

    /**
     * 建築唯一識別碼
     * 
     * 例如：mainHall, farm, barracks, lumberMill
     */
    private String id;
    
    /**
     * 建築名稱
     * 
     * 例如：主城、農田、兵營、伐木場
     */
    private String name;
    
    /**
     * 建築類型
     * 
     * 例如：function（功能性）、resource（資源生產）、military（軍事）
     */
    private String type;
    
    /**
     * 該建築影響的資源類型
     * 
     * 只有資源生產建築才會有此屬性
     * 例如：WOOD（伐木場）、FOOD（農田）
     * 非資源生產建築為null
     */
    private String resourceType;
    
    /**
     * 最大建造數量
     * 
     * 玩家可以建造該建築的最大數量
     * 例如：主城只能建造1個，農田可以建造5個
     */
    private int maxCount;
    
    /**
     * 等級數據列表
     * 
     * 包含建築各個等級的詳細信息
     * 例如：建造時間、升級成本、生產速率等
     */
    private List<LevelData> levels;
    /**
     * 允許建築的地形類型
     * 
     * 定義該建築可以建造的地形，例如：平地、山地等
     */
    private List<String> allowedTerrains; 
}


