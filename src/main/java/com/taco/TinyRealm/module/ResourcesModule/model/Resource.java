package com.taco.TinyRealm.module.ResourcesModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 資源類型的資料模型。
 * 用於定義遊戲中各種資源的靜態屬性，例如木材、石頭、黃金等。
 * 這些數據通常從靜態配置檔案 (如 resource_types.json) 中載入。
 */
@Data // Lombok 註解，自動生成 getter, setter, equals, hashCode, toString 方法
@NoArgsConstructor // Lombok 註解，自動生成無參建構子
@AllArgsConstructor // Lombok 註解，自動生成包含所有參數的建構子
public class Resource {
    private String id;          // 資源的唯一識別符 (例如: "gold", "wood")
    private String name;        // 資源的顯示名稱 (例如: "黃金", "木材")
    private String description; // 資源的簡短描述
    private String iconPath;    // 資源圖標的檔案路徑或 URL

    // 您可以根據遊戲需求，在這裡添加更多靜態屬性，例如：
    // private String category; // 資源類別 (例如: "礦物", "植物")
    // private int stackSize;   // 最大堆疊數量
    // private boolean tradeable; // 是否可交易
}
