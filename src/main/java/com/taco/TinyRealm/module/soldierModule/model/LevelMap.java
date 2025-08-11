package com.taco.TinyRealm.module.soldierModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LevelMap {

    /** 士兵成本 */
    private Map<String, Integer> cost;
    /** 士兵基礎攻擊力 */
    private int attack;
    /** 士兵基礎防禦力 */
    private int defense;
    /** 士兵基礎生命值 */
    private int health;
    /** 士兵移動速度 */
    private int speed;
    /** 士兵攻擊範圍 */
    private int range;
    /** 訓練時間（秒） */
    private int trainingTime;
}