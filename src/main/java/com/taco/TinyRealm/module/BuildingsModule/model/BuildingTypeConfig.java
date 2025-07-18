package com.taco.TinyRealm.module.BuildingsModule.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections; // 用於返回不可修改的集合，防止外部修改
import java.util.List;
import java.util.Map;
import java.util.HashMap;    // 用於初始化 Map
import java.util.ArrayList;  // 用於初始化 List

/**
 * 代表遊戲中一種建築物的靜態配置（藍圖），例如總部、農場、兵營等。
 * 這些數據通常從配置文件（如 JSON）載入，並在遊戲啟動時讀取一次，不隨遊戲進行而改變。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingTypeConfig {
    private static final Logger log = LoggerFactory.getLogger(BuildingTypeConfig.class);
    private String typeId;               // 建築物類型唯一ID (例如："HEADQUARTERS", "FARM", "BARRACKS")
    private String name;                 // 建築物的顯示名稱，用於UI
    private String description;          // 建築物的簡短描述

    private int maxLevel;                // 該類型建築物的最大可升級等級
    private int baseBuildTimeSeconds;    // 建造該建築物（1級）所需基礎時間 (秒)
    private Map<String, Integer> baseConstructionCost; // 建造該建築物（1級）所需基礎資源及數量

    // 升級成本定義：外層Map的Key是目標等級(String，Jackson會將JSON中的數字key讀成String)，內層Map是資源類型和數量
    // 例如: "2": { "WOOD": 150, "STONE": 75 } 表示升到2級所需資源
    private Map<String, Map<String, Integer>> upgradeCosts;
    // 升級時間定義：Key是當前等級(String)，Value是升級到下一級所需時間(秒)
    // 例如: "1": 90 表示從1級升到2級需要90秒
    private Map<String, Integer> upgradeTimesSeconds;

    private int requiredPlayerLevel;     // 建造/解鎖該建築所需玩家的最低等級
    private String requiredTechnologyId; // 建造/解鎖該建築所需科技的ID (如果遊戲有科技樹系統)
    private List<String> requiredBuildingTypeIds; // 建造該建築所需的前置建築類型ID列表
                                                   // 例如：兵營可能需要先有總部

    private boolean hasProductionQueue;  // 該建築類型是否支持生產隊列 (e.g., 兵營為true, 農場為false)
    private int productionQueueCapacity; // 如果支持生產，其生產隊列的最大容量
    private List<String> producibleUnitTypes; // 該建築可以生產的單位/物品類型ID列表 (e.g., "SOLDIER", "ARCHER")

    // --- 潛在擴展屬性 (可選，根據遊戲需求決定是否加入) ---
    private int baseHealth; // 建築物基礎生命值 (1級時的生命值)
    private int maxStorageCapacity; // 最大資源儲存容量 (如果建築是倉庫類)
    private Map<String, Integer> productionRates; // 該建築每週期（例如每秒或每分鐘）生產的資源量及類型
                                                   // 例如: {"FOOD": 1} 表示每秒生產1單位食物
    private List<String> terrainRestrictions; // 只能建造在哪些地形類型上 (例如："GRASS", "FOREST")

    // --- 實用方法 ---

    /**
     * 根據目標等級獲取升級所需資源。
     * @param targetLevel 建築物將要升級到的等級 (例如，從1級升到2級，targetLevel 為 2)。
     * @return 升級所需資源 Map (資源類型 -> 數量)，如果該等級的升級成本未定義，則返回空 Map。
     */
    public Map<String, Integer> getUpgradeCost(int targetLevel) {
        log.debug("查詢升級成本: typeId={}, targetLevel={}", typeId, targetLevel);
        // Jackson 將 JSON 中的數字 key 讀取為 String，所以這裡用 String.valueOf
        return upgradeCosts.getOrDefault(String.valueOf(targetLevel), Collections.emptyMap());
    }

    /**
     * 根據當前等級獲取升級到下一級所需時間。
     * @param currentLevel 建築物目前的等級。
     * @return 升級所需時間（秒），如果該等級的升級時間未定義，則返回 0。
     */
    public int getUpgradeTimeSeconds(int currentLevel) {
        log.debug("查詢升級時間: typeId={}, currentLevel={}", typeId, currentLevel);
        // Jackson 將 JSON 中的數字 key 讀取為 String，所以這裡用 String.valueOf
        return upgradeTimesSeconds.getOrDefault(String.valueOf(currentLevel), 0);
    }
}
