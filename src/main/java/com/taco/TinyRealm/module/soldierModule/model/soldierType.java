package com.taco.TinyRealm.module.soldierModule.model;


import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldierType {
    /** 士兵類型ID */
    private String id;
    /** 士兵類型 */
    private String type;
    /** 士兵名稱 */
    private String name;
    /** 士兵描述 */
    private String description;
    /** 等級數據列表*/
    private LevelMap stats;  
    /** 要求 */
    private Map<String, Integer> requirements;
    /** 需要的科技 */
    private Map<String, String> requiredTech;
    /** 士兵特殊技能 */
    private Map<String, Object> skills; 
}
