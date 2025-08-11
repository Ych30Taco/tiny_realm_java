package com.taco.TinyRealm.module.soldierModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSoldier {
    /** 士兵類型ID */
    private String id;
    /** 士兵類型 */
    private String type;
    /** 士兵名稱 */
    private String name;
    /** 士兵數量 */
    private int count;
    
}
