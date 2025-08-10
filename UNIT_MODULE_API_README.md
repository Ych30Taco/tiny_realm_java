# 部隊模組 API 文檔

## 概述

部隊模組是 TinyRealm 遊戲的核心戰鬥單位管理模組，負責部隊的創建、訓練、升級、移動和戰鬥。提供完整的部隊生命週期管理，包括不同部隊類型的配置、屬性系統、技能系統等。

## 基礎資訊

- **基礎路徑**: `/api/unit`
- **數據格式**: JSON
- **回應格式**: 統一使用 `{success, message, data}` 格式

## API 端點

### 1. 獲取所有部隊類型

**端點**: `GET /api/unit/types`

**描述**: 獲取所有可用的部隊類型配置

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/unit/types"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取部隊類型成功",
  "data": [
    {
      "type": "soldier",
      "name": "步兵",
      "description": "基礎步兵單位，適合近戰",
      "cost": { "gold": 10, "wood": 5 },
      "attack": 10,
      "defense": 8,
      "health": 50,
      "speed": 3,
      "range": 1,
      "requiredBuilding": "barracks",
      "requiredTech": null,
      "trainingTime": 60,
      "maxLevel": 10,
      "expToLevel": 100,
      "skills": {
        "shield_wall": {
          "name": "盾牆",
          "description": "防禦力提升20%",
          "level": 3
        }
      }
    }
  ]
}
```

### 2. 根據類型獲取部隊配置

**端點**: `GET /api/unit/types/{type}`

**描述**: 獲取特定部隊類型的詳細配置

**參數**:
- `type` (Path Variable): 部隊類型

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/unit/types/soldier"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取部隊配置成功",
  "data": {
    "type": "soldier",
    "name": "步兵",
    "description": "基礎步兵單位，適合近戰",
    "cost": { "gold": 10, "wood": 5 },
    "attack": 10,
    "defense": 8,
    "health": 50,
    "speed": 3,
    "range": 1,
    "requiredBuilding": "barracks",
    "requiredTech": null,
    "trainingTime": 60,
    "maxLevel": 10,
    "expToLevel": 100
  }
}
```

### 3. 創建部隊

**端點**: `POST /api/unit/create`

**描述**: 創建新的部隊單位

**參數**:
- `playerId` (Query Parameter): 玩家ID
- `type` (Query Parameter): 部隊類型
- `count` (Query Parameter): 部隊數量
- `x` (Query Parameter): X座標
- `y` (Query Parameter): Y座標

**請求範例**:
```bash
curl -X POST "http://localhost:1026/api/unit/create?playerId=player123&type=soldier&count=10&x=5&y=5"
```

**成功回應**:
```json
{
  "success": true,
  "message": "部隊創建成功",
  "data": {
    "id": "unit-uuid-123",
    "type": "soldier",
    "name": "步兵",
    "count": 10,
    "level": 1,
    "experience": 0,
    "x": 5,
    "y": 5,
    "status": 0,
    "attack": 10,
    "defense": 8,
    "health": 50,
    "maxHealth": 50,
    "speed": 3,
    "range": 1,
    "playerId": "player123",
    "createdTime": 1640995200000,
    "lastUpdatedTime": 1640995200000
  }
}
```

**失敗回應**:
```json
{
  "success": false,
  "message": "資源不足: gold (需要: 100, 擁有: 50)",
  "data": null
}
```

### 4. 移動部隊

**端點**: `PUT /api/unit/move`

**描述**: 移動部隊到新位置

**參數**:
- `playerId` (Query Parameter): 玩家ID
- `unitId` (Query Parameter): 部隊ID
- `newX` (Query Parameter): 新X座標
- `newY` (Query Parameter): 新Y座標

**請求範例**:
```bash
curl -X PUT "http://localhost:1026/api/unit/move?playerId=player123&unitId=unit-uuid-123&newX=6&newY=6"
```

**成功回應**:
```json
{
  "success": true,
  "message": "部隊移動成功",
  "data": {
    "id": "unit-uuid-123",
    "x": 6,
    "y": 6,
    "status": 3
  }
}
```

### 5. 獲取玩家的所有部隊

**端點**: `GET /api/unit/player/{playerId}`

**描述**: 獲取指定玩家的所有部隊

**參數**:
- `playerId` (Path Variable): 玩家ID

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/unit/player/player123"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取玩家部隊成功",
  "data": [
    {
      "id": "unit-uuid-123",
      "type": "soldier",
      "name": "步兵",
      "count": 10,
      "level": 1,
      "x": 5,
      "y": 5,
      "status": 1,
      "attack": 10,
      "defense": 8,
      "health": 50
    }
  ]
}
```

### 6. 升級部隊

**端點**: `PUT /api/unit/upgrade`

**描述**: 升級部隊等級

**參數**:
- `playerId` (Query Parameter): 玩家ID
- `unitId` (Query Parameter): 部隊ID

**請求範例**:
```bash
curl -X PUT "http://localhost:1026/api/unit/upgrade?playerId=player123&unitId=unit-uuid-123"
```

**成功回應**:
```json
{
  "success": true,
  "message": "部隊升級成功",
  "data": {
    "id": "unit-uuid-123",
    "level": 2,
    "attack": 12,
    "defense": 9,
    "health": 55,
    "maxHealth": 55
  }
}
```

### 7. 治療部隊

**端點**: `PUT /api/unit/heal`

**描述**: 治療部隊恢復生命值

**參數**:
- `playerId` (Query Parameter): 玩家ID
- `unitId` (Query Parameter): 部隊ID
- `healAmount` (Query Parameter): 治療量

**請求範例**:
```bash
curl -X PUT "http://localhost:1026/api/unit/heal?playerId=player123&unitId=unit-uuid-123&healAmount=20"
```

**成功回應**:
```json
{
  "success": true,
  "message": "部隊治療成功",
  "data": {
    "id": "unit-uuid-123",
    "health": 70,
    "maxHealth": 55
  }
}
```

### 8. 解散部隊

**端點**: `DELETE /api/unit/disband`

**描述**: 解散部隊並釋放位置

**參數**:
- `playerId` (Query Parameter): 玩家ID
- `unitId` (Query Parameter): 部隊ID

**請求範例**:
```bash
curl -X DELETE "http://localhost:1026/api/unit/disband?playerId=player123&unitId=unit-uuid-123"
```

**成功回應**:
```json
{
  "success": true,
  "message": "部隊解散成功",
  "data": true
}
```

### 9. 獲取部隊統計資訊

**端點**: `GET /api/unit/stats/{playerId}`

**描述**: 獲取玩家的部隊統計資訊

**參數**:
- `playerId` (Path Variable): 玩家ID

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/unit/stats/player123"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取部隊統計成功",
  "data": {
    "totalUnits": 5,
    "totalCount": 50,
    "aliveUnits": 5,
    "averageLevel": 2.4,
    "typeStats": {
      "soldier": 30,
      "archer": 20
    }
  }
}
```

### 10. 根據ID獲取部隊

**端點**: `GET /api/unit/{playerId}/{unitId}`

**描述**: 獲取特定部隊的詳細資訊

**參數**:
- `playerId` (Path Variable): 玩家ID
- `unitId` (Path Variable): 部隊ID

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/unit/player123/unit-uuid-123"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取部隊資訊成功",
  "data": {
    "id": "unit-uuid-123",
    "type": "soldier",
    "name": "步兵",
    "count": 10,
    "level": 2,
    "experience": 50,
    "x": 5,
    "y": 5,
    "status": 1,
    "attack": 12,
    "defense": 9,
    "health": 55,
    "maxHealth": 55,
    "speed": 3,
    "range": 1,
    "skills": {
      "shield_wall": {
        "name": "盾牆",
        "description": "防禦力提升20%",
        "level": 3
      }
    },
    "playerId": "player123",
    "createdTime": 1640995200000,
    "lastUpdatedTime": 1640995200000
  }
}
```

## 部隊類型配置

### 可用的部隊類型

1. **步兵 (soldier)**
   - 基礎近戰單位
   - 需要：軍營 (barracks)
   - 技能：盾牆 (防禦力提升20%)

2. **弓箭手 (archer)**
   - 遠程攻擊單位
   - 需要：軍營 (barracks) + 弓箭術 (archery)
   - 技能：精準射擊 (攻擊力提升25%)

3. **騎兵 (cavalry)**
   - 高機動性單位
   - 需要：馬廄 (stable) + 騎術 (horsemanship)
   - 技能：衝鋒 (首次攻擊傷害翻倍)

4. **長槍兵 (spearman)**
   - 反騎兵單位
   - 需要：軍營 (barracks) + 長槍訓練 (spear_training)
   - 技能：反騎兵 (對騎兵傷害提升50%)

5. **法師 (mage)**
   - 魔法攻擊單位
   - 需要：法師塔 (tower) + 魔法 (magic)
   - 技能：火球術 (範圍攻擊)

6. **治療師 (healer)**
   - 支援單位
   - 需要：神殿 (temple) + 治療術 (healing)
   - 技能：治療術 (恢復友軍生命值)

## 部隊狀態說明

- **0**: 訓練中 - 部隊正在訓練，無法移動或戰鬥
- **1**: 待命 - 部隊可以移動和戰鬥
- **2**: 戰鬥中 - 部隊正在戰鬥
- **3**: 移動中 - 部隊正在移動

## 錯誤處理

### 常見錯誤訊息

1. **玩家不存在**: 當指定的玩家ID不存在時
2. **部隊類型不存在**: 當指定的部隊類型未配置時
3. **需要建築物**: 當缺少必要的建築物時
4. **需要科技**: 當缺少必要的科技時
5. **資源不足**: 當資源不足以創建部隊時
6. **無效或已被佔領的位置**: 當位置無效或已被佔領時
7. **部隊不存在**: 當指定的部隊ID不存在時
8. **部隊無法移動**: 當部隊狀態不允許移動時
9. **部隊已達到最高等級**: 當部隊等級已達上限時
10. **經驗值不足**: 當經驗值不足以升級時

## 使用建議

### 1. 部隊創建策略

- 根據玩家等級和資源狀況選擇合適的部隊類型
- 考慮部隊的相剋關係（如長槍兵對騎兵）
- 平衡攻擊型和防禦型部隊的數量

### 2. 部隊管理

- 定期升級高級部隊以提升戰鬥力
- 及時治療受傷的部隊
- 合理分配部隊位置以保護重要建築

### 3. 資源管理

- 部隊創建需要消耗資源，要合理規劃
- 不同部隊類型的成本不同，要根據資源狀況選擇

## 測試端點

### 模組測試平台端點

- `GET /modules/unit` - 獲取部隊類型
- `POST /modules/unit/create` - 創建部隊
- `GET /modules/unit/player/{playerId}` - 獲取玩家部隊
- `GET /modules/unit/stats/{playerId}` - 獲取部隊統計

## 版本資訊

- **版本**: 1.0
- **更新日期**: 2024年
- **開發者**: TinyRealm Team

---

如有問題或建議，請聯繫開發團隊或提交 Issue。
