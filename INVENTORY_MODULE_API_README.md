# 背包模組 API 文檔

## 概述

背包模組（Inventory Module）負責管理玩家的物品系統，包括物品的添加、移除、使用、修復等功能。該模組提供了完整的物品管理解決方案，支持不同類型的物品（武器、防具、消耗品、材料等）。

## 模組結構

```
src/main/java/com/taco/TinyRealm/module/inventoryModule/
├── controller/
│   └── InventoryController.java          # RESTful API 控制器
├── model/
│   ├── Item.java                         # 物品實體模型
│   └── ItemType.java                     # 物品類型配置模型
└── service/
    └── InventoryService.java             # 業務邏輯服務
```

## API 端點

### 1. 獲取所有物品類型

**端點：** `GET /api/inventory/types`

**描述：** 獲取所有可用的物品類型配置

**請求參數：** 無

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully retrieved all item types",
  "data": {
    "sword": {
      "type": "sword",
      "name": "鐵劍",
      "description": "鋒利的鐵製長劍，適合近戰戰鬥",
      "rarity": "common",
      "category": "weapon",
      "maxQuantity": 1,
      "maxDurability": 100,
      "level": 1,
      "attributes": {
        "attack": 15,
        "speed": 1.0,
        "weight": 3
      },
      "effects": {
        "critical_chance": 0.05
      },
      "isStackable": false,
      "isTradeable": true,
      "isDroppable": true,
      "isConsumable": false,
      "isEquippable": true,
      "equipmentSlot": "weapon",
      "sellPrice": 50,
      "buyPrice": 100
    }
  }
}
```

### 2. 獲取特定物品類型

**端點：** `GET /api/inventory/types/{type}`

**描述：** 獲取特定物品類型的詳細配置

**路徑參數：**
- `type` (String): 物品類型

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully retrieved item type: sword",
  "data": {
    "type": "sword",
    "name": "鐵劍",
    "description": "鋒利的鐵製長劍，適合近戰戰鬥",
    "rarity": "common",
    "category": "weapon",
    "maxQuantity": 1,
    "maxDurability": 100,
    "level": 1,
    "attributes": {
      "attack": 15,
      "speed": 1.0,
      "weight": 3
    },
    "effects": {
      "critical_chance": 0.05
    },
    "isStackable": false,
    "isTradeable": true,
    "isDroppable": true,
    "isConsumable": false,
    "isEquippable": true,
    "equipmentSlot": "weapon",
    "sellPrice": 50,
    "buyPrice": 100
  }
}
```

### 3. 添加物品到背包

**端點：** `POST /api/inventory/add`

**描述：** 向指定玩家的背包添加物品

**請求參數：**
- `playerId` (String, 必需): 玩家ID
- `type` (String, 必需): 物品類型
- `quantity` (int, 必需): 數量
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl -X POST "http://localhost:8080/api/inventory/add" \
  -d "playerId=player123" \
  -d "type=sword" \
  -d "quantity=1" \
  -d "isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully added 1 sword to player player123",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "type": "sword",
    "name": "鐵劍",
    "description": "鋒利的鐵製長劍，適合近戰戰鬥",
    "quantity": 1,
    "maxQuantity": 1,
    "rarity": "common",
    "category": "weapon",
    "level": 1,
    "durability": 100,
    "maxDurability": 100,
    "createdTime": "2024-01-01T12:00:00",
    "isStackable": false,
    "isTradeable": true,
    "isDroppable": true,
    "isConsumable": false,
    "isEquippable": true,
    "equipmentSlot": "weapon",
    "sellPrice": 50,
    "buyPrice": 100
  }
}
```

### 4. 從背包移除物品

**端點：** `DELETE /api/inventory/remove`

**描述：** 從指定玩家的背包移除物品

**請求參數：**
- `playerId` (String, 必需): 玩家ID
- `itemId` (String, 必需): 物品ID
- `quantity` (int, 必需): 移除數量
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl -X DELETE "http://localhost:8080/api/inventory/remove" \
  -d "playerId=player123" \
  -d "itemId=550e8400-e29b-41d4-a716-446655440000" \
  -d "quantity=1" \
  -d "isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully removed 1 items from player player123",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "type": "sword",
    "name": "鐵劍",
    "quantity": 0,
    "durability": 100
  }
}
```

### 5. 獲取玩家背包

**端點：** `GET /api/inventory/{playerId}`

**描述：** 獲取指定玩家的完整背包內容

**路徑參數：**
- `playerId` (String): 玩家ID

**查詢參數：**
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl "http://localhost:8080/api/inventory/player123?isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully retrieved inventory for player player123",
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "type": "sword",
      "name": "鐵劍",
      "description": "鋒利的鐵製長劍，適合近戰戰鬥",
      "quantity": 1,
      "maxQuantity": 1,
      "rarity": "common",
      "category": "weapon",
      "level": 1,
      "durability": 100,
      "maxDurability": 100,
      "createdTime": "2024-01-01T12:00:00",
      "isStackable": false,
      "isTradeable": true,
      "isDroppable": true,
      "isConsumable": false,
      "isEquippable": true,
      "equipmentSlot": "weapon",
      "sellPrice": 50,
      "buyPrice": 100
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "type": "health_potion",
      "name": "生命藥水",
      "description": "恢復生命值的紅色藥水",
      "quantity": 5,
      "maxQuantity": 99,
      "rarity": "common",
      "category": "consumable",
      "level": 1,
      "durability": 1,
      "maxDurability": 1,
      "createdTime": "2024-01-01T12:30:00",
      "isStackable": true,
      "isTradeable": true,
      "isDroppable": true,
      "isConsumable": true,
      "isEquippable": false,
      "equipmentSlot": null,
      "sellPrice": 10,
      "buyPrice": 20
    }
  ]
}
```

### 6. 使用物品

**端點：** `PUT /api/inventory/use`

**描述：** 使用指定物品（消耗品或裝備）

**請求參數：**
- `playerId` (String, 必需): 玩家ID
- `itemId` (String, 必需): 物品ID
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl -X PUT "http://localhost:8080/api/inventory/use" \
  -d "playerId=player123" \
  -d "itemId=550e8400-e29b-41d4-a716-446655440001" \
  -d "isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully used item 550e8400-e29b-41d4-a716-446655440001",
  "data": {
    "heal": 50
  }
}
```

### 7. 修復物品

**端點：** `PUT /api/inventory/repair`

**描述：** 修復指定物品的耐久度

**請求參數：**
- `playerId` (String, 必需): 玩家ID
- `itemId` (String, 必需): 物品ID
- `repairAmount` (int, 必需): 修復數量
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl -X PUT "http://localhost:8080/api/inventory/repair" \
  -d "playerId=player123" \
  -d "itemId=550e8400-e29b-41d4-a716-446655440000" \
  -d "repairAmount=25" \
  -d "isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully repaired item 550e8400-e29b-41d4-a716-446655440000",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "type": "sword",
    "name": "鐵劍",
    "durability": 100,
    "maxDurability": 100
  }
}
```

### 8. 獲取背包統計信息

**端點：** `GET /api/inventory/stats/{playerId}`

**描述：** 獲取指定玩家背包的統計信息

**路徑參數：**
- `playerId` (String): 玩家ID

**查詢參數：**
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl "http://localhost:8080/api/inventory/stats/player123?isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully retrieved inventory stats for player player123",
  "data": {
    "totalItems": 2,
    "totalQuantity": 6,
    "categoryStats": {
      "weapon": 1,
      "consumable": 1
    },
    "rarityStats": {
      "common": 2
    },
    "equipmentCount": 1,
    "consumableCount": 1
  }
}
```

### 9. 搜索背包物品

**端點：** `GET /api/inventory/search/{playerId}`

**描述：** 根據條件搜索背包中的物品

**路徑參數：**
- `playerId` (String): 玩家ID

**查詢參數：**
- `searchTerm` (String, 可選): 搜索關鍵詞
- `category` (String, 可選): 物品類別
- `rarity` (String, 可選): 物品稀有度
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl "http://localhost:8080/api/inventory/search/player123?category=weapon&rarity=common&isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully searched inventory for player player123",
  "data": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "type": "sword",
      "name": "鐵劍",
      "category": "weapon",
      "rarity": "common",
      "quantity": 1
    }
  ]
}
```

### 10. 獲取特定物品

**端點：** `GET /api/inventory/{playerId}/{itemId}`

**描述：** 獲取指定玩家的特定物品詳情

**路徑參數：**
- `playerId` (String): 玩家ID
- `itemId` (String): 物品ID

**查詢參數：**
- `isTest` (boolean, 可選): 是否為測試模式，默認為false

**請求示例：**
```bash
curl "http://localhost:8080/api/inventory/player123/550e8400-e29b-41d4-a716-446655440000?isTest=false"
```

**響應示例：**
```json
{
  "success": true,
  "message": "Successfully retrieved item 550e8400-e29b-41d4-a716-446655440000",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "type": "sword",
    "name": "鐵劍",
    "description": "鋒利的鐵製長劍，適合近戰戰鬥",
    "quantity": 1,
    "maxQuantity": 1,
    "rarity": "common",
    "category": "weapon",
    "level": 1,
    "durability": 100,
    "maxDurability": 100,
    "createdTime": "2024-01-01T12:00:00",
    "isStackable": false,
    "isTradeable": true,
    "isDroppable": true,
    "isConsumable": false,
    "isEquippable": true,
    "equipmentSlot": "weapon",
    "sellPrice": 50,
    "buyPrice": 100
  }
}
```

## 數據模型

### Item 模型

```java
public class Item {
    private String id;                    // 物品唯一ID
    private String type;                  // 物品類型
    private String name;                  // 物品名稱
    private String description;           // 物品描述
    private int quantity;                 // 數量
    private int maxQuantity;              // 最大堆疊數量
    private String rarity;                // 稀有度 (common, uncommon, rare, epic, legendary)
    private String category;              // 類別 (weapon, armor, consumable, material, quest)
    private int level;                    // 等級要求
    private int durability;               // 當前耐久度
    private int maxDurability;            // 最大耐久度
    private Map<String, Object> attributes; // 屬性
    private Map<String, Object> effects;   // 效果
    private LocalDateTime createdTime;    // 創建時間
    private LocalDateTime lastUsedTime;   // 最後使用時間
    private boolean isStackable;          // 是否可堆疊
    private boolean isTradeable;          // 是否可交易
    private boolean isDroppable;          // 是否可丟棄
    private boolean isConsumable;         // 是否為消耗品
    private boolean isEquippable;         // 是否為裝備
    private String equipmentSlot;         // 裝備槽位
    private int sellPrice;                // 售價
    private int buyPrice;                 // 購買價格
}
```

### ItemType 模型

```java
public class ItemType {
    private String type;                  // 物品類型
    private String name;                  // 物品名稱
    private String description;           // 物品描述
    private String rarity;                // 稀有度
    private String category;              // 類別
    private int maxQuantity;              // 最大堆疊數量
    private int maxDurability;            // 最大耐久度
    private int level;                    // 等級要求
    private Map<String, Object> attributes; // 屬性
    private Map<String, Object> effects;   // 效果
    private boolean isStackable;          // 是否可堆疊
    private boolean isTradeable;          // 是否可交易
    private boolean isDroppable;          // 是否可丟棄
    private boolean isConsumable;         // 是否為消耗品
    private boolean isEquippable;         // 是否為裝備
    private String equipmentSlot;         // 裝備槽位
    private int sellPrice;                // 售價
    private int buyPrice;                 // 購買價格
    private Map<String, Integer> craftingRecipe; // 製作配方
    private int requiredLevel;            // 需求等級
    private String requiredTech;          // 需求科技
    private double dropRate;              // 掉落率
    private Map<String, Object> useEffect; // 使用效果
}
```

## 物品類別說明

### 稀有度 (Rarity)
- **common**: 普通 - 最常見的物品
- **uncommon**: 罕見 - 較為稀有的物品
- **rare**: 稀有 - 非常稀有的物品
- **epic**: 史詩 - 極其稀有的物品
- **legendary**: 傳說 - 最稀有的物品

### 物品類別 (Category)
- **weapon**: 武器 - 用於攻擊的裝備
- **armor**: 防具 - 用於防禦的裝備
- **consumable**: 消耗品 - 使用後會消耗的物品
- **material**: 材料 - 用於製作其他物品的材料
- **quest**: 任務物品 - 與任務相關的特殊物品

### 裝備槽位 (EquipmentSlot)
- **weapon**: 武器槽
- **armor**: 護甲槽
- **accessory**: 飾品槽

## 錯誤處理

所有API端點都使用標準化的錯誤響應格式：

```json
{
  "success": false,
  "message": "錯誤描述",
  "data": null
}
```

常見錯誤：
- **400 Bad Request**: 請求參數錯誤
- **404 Not Found**: 玩家或物品不存在
- **500 Internal Server Error**: 服務器內部錯誤

## 測試端點

### 模組測試端點

訪問 `http://localhost:8080/modules` 可以查看所有模組的測試界面。

#### 背包模組測試端點：

1. **GET /modules/inventory** - 獲取所有物品類型
2. **POST /modules/inventory/add** - 添加物品到玩家背包
3. **GET /modules/inventory/player/{playerId}** - 獲取玩家背包
4. **GET /modules/inventory/stats/{playerId}** - 獲取背包統計

## 配置

### 物品配置文件

物品類型配置位於 `src/main/resources/config/items.json`，包含所有可用物品的詳細配置。

### 默認物品類型

如果配置文件無法載入，系統會自動創建以下默認物品類型：
- **sword**: 鐵劍（武器）
- **armor**: 鐵甲（防具）
- **health_potion**: 生命藥水（消耗品）
- **wood**: 木材（材料）

## 使用建議

1. **物品管理**: 使用標準的CRUD操作管理玩家背包
2. **物品使用**: 對於消耗品，使用 `use` 端點；對於裝備，檢查耐久度
3. **物品修復**: 定期修復裝備以維持其效果
4. **背包統計**: 使用統計端點監控玩家背包狀態
5. **搜索功能**: 利用搜索端點快速找到特定物品

## 依賴關係

背包模組依賴以下其他模組：
- **Storage Module**: 用於保存和載入玩家遊戲狀態
- **Event Module**: 用於記錄物品相關事件

## 注意事項

1. 物品ID是全局唯一的UUID
2. 堆疊物品會自動合併到現有物品中
3. 使用消耗品會自動減少數量
4. 裝備使用會減少耐久度
5. 所有操作都會記錄到事件系統中
