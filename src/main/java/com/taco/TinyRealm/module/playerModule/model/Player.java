package com.taco.TinyRealm.module.playerModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList; // 引入 ArrayList

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String playerId;
    private String playerName;
    private int level;
    private long experience;
    private Location currentLocation;
    private Map<String, Integer> resources; // 儲存玩家持有的資源數量
    private List<String> unlockedTechnologies; // 已解鎖科技的ID列表
    private Map<String, Integer> resourceProductionRates; // <-- 新增：每秒產出率 (資源ID -> 數量)
    // ... 未來可以加入更多屬性，例如背包、部隊列表、建築列表等

    // 用於創建新玩家的簡化建構子
    public Player(String playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.level = 1;
        this.experience = 0;
        this.currentLocation = new Location(0, 0); // 預設出生點
        this.resources = new HashMap<>();
        // 給予新玩家一些初始資源
        this.resources.put("gold", 100);
        this.resources.put("wood", 100);
        this.resources.put("stone", 100);
        this.resources.put("food", 100);
        this.resources.put("iron", 100);
        this.unlockedTechnologies = new ArrayList<>();

        // 初始化玩家的基礎生產率 (範例)
        this.resourceProductionRates = new HashMap<>();
        this.resourceProductionRates.put("gold", 0);  
        this.resourceProductionRates.put("wood", 1);  // 每秒產出 1 個木材
        this.resourceProductionRates.put("stone", 1);  // 每秒產出 1 個石頭
        this.resourceProductionRates.put("food", 1);  // 每秒產出 1 個食物
        this.resourceProductionRates.put("iron", 1);  // 每秒產出 1 個鐵礦
        // ... 其他資源的初始產出率
    }
}