# 存儲模組 API 文檔

## 概述

存儲模組是 TinyRealm 遊戲的核心數據管理模組，負責遊戲狀態的持久化存儲、載入和管理。提供記憶體快取和檔案系統雙重存儲機制，確保數據的安全性和訪問效率。

## 基礎資訊

- **基礎路徑**: `/api/storage`
- **數據格式**: JSON
- **回應格式**: 統一使用 `{success, message, data}` 格式

## API 端點

### 1. 保存遊戲狀態

**端點**: `POST /api/storage/save`

**描述**: 保存玩家的完整遊戲狀態到檔案系統和記憶體快取

**參數**:
- `playerId` (Query Parameter): 玩家ID
- `gameState` (Request Body): 完整的遊戲狀態物件

**請求範例**:
```bash
curl -X POST "http://localhost:1026/api/storage/save?playerId=player123" \
  -H "Content-Type: application/json" \
  -d '{
    "player": {...},
    "resources": {...},
    "buildings": {...},
    "version": "1.0"
  }'
```

**成功回應**:
```json
{
  "success": true,
  "message": "遊戲狀態保存成功",
  "data": null
}
```

**失敗回應**:
```json
{
  "success": false,
  "message": "保存遊戲狀態失敗: 具體錯誤訊息",
  "data": null
}
```

### 2. 載入遊戲狀態

**端點**: `POST /api/storage/load`

**描述**: 從檔案系統載入玩家的遊戲狀態

**參數**:
- `playerId` (Request Body): 玩家ID

**請求範例**:
```bash
curl -X POST "http://localhost:1026/api/storage/load" \
  -H "Content-Type: application/json" \
  -d '{"playerId": "player123"}'
```

**成功回應**:
```json
{
  "success": true,
  "message": "遊戲狀態載入成功",
  "data": {
    "player": {...},
    "resources": {...},
    "buildings": {...},
    "version": "1.0"
  }
}
```

**失敗回應**:
```json
{
  "success": false,
  "message": "遊戲狀態不存在",
  "data": null
}
```

### 3. 玩家登出

**端點**: `POST /api/storage/logout`

**描述**: 玩家登出時保存最終狀態並從記憶體中移除

**參數**:
- `playerId` (Request Body): 玩家ID

**請求範例**:
```bash
curl -X POST "http://localhost:1026/api/storage/logout" \
  -H "Content-Type: application/json" \
  -d '{"playerId": "player123"}'
```

**成功回應**:
```json
{
  "success": true,
  "message": "玩家登出成功",
  "data": null
}
```

### 4. 獲取所有玩家列表

**端點**: `GET /api/storage/playerList`

**描述**: 獲取存儲目錄中所有玩家ID列表

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/storage/playerList"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取玩家列表成功",
  "data": ["player123", "player456", "player789"]
}
```

### 5. 獲取在線玩家列表

**端點**: `GET /api/storage/onlinePlayers`

**描述**: 獲取目前狀態為在線的玩家ID列表

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/storage/onlinePlayers"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取在線玩家列表成功",
  "data": ["player123", "player456"]
}
```

### 6. 獲取離線玩家列表

**端點**: `GET /api/storage/offlinePlayers`

**描述**: 獲取目前狀態為離線的玩家ID列表

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/storage/offlinePlayers"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取離線玩家列表成功",
  "data": ["player789"]
}
```

### 7. 獲取所有遊戲狀態

**端點**: `GET /api/storage/allGameStateList`

**描述**: 獲取記憶體中所有玩家的遊戲狀態映射

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/storage/allGameStateList"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取所有遊戲狀態成功",
  "data": {
    "player123": {
      "player": {...},
      "resources": {...},
      "buildings": {...}
    },
    "player456": {
      "player": {...},
      "resources": {...},
      "buildings": {...}
    }
  }
}
```

### 8. 根據玩家ID獲取遊戲狀態

**端點**: `GET /api/storage/gameState/{playerId}`

**描述**: 根據玩家ID獲取特定的遊戲狀態

**參數**:
- `playerId` (Path Variable): 玩家ID

**請求範例**:
```bash
curl -X GET "http://localhost:1026/api/storage/gameState/player123"
```

**成功回應**:
```json
{
  "success": true,
  "message": "獲取玩家遊戲狀態成功",
  "data": {
    "player": {...},
    "resources": {...},
    "buildings": {...},
    "version": "1.0"
  }
}
```

**失敗回應**:
```json
{
  "success": false,
  "message": "玩家遊戲狀態不存在",
  "data": null
}
```

### 9. 清除測試資料

**端點**: `DELETE /api/storage/clearTestData`

**描述**: 清除所有以 "test_" 開頭的測試檔案

**請求範例**:
```bash
curl -X DELETE "http://localhost:1026/api/storage/clearTestData"
```

**成功回應**:
```json
{
  "success": true,
  "message": "測試資料清除成功",
  "data": null
}
```

## 數據結構

### GameState 物件結構

```json
{
  "player": {
    "id": "player123",
    "name": "玩家名稱",
    "level": 1,
    "experience": 0,
    "status": 1,
    "lastLoginTime": 1640995200000,
    "lastUpdatedTime": 1640995200000
  },
  "resources": {
    "gold": 1000,
    "food": 500,
    "wood": 300,
    "stone": 200,
    "iron": 100
  },
  "buildings": {
    "townhall": {
      "id": "townhall",
      "level": 1,
      "status": "completed"
    }
  },
  "inventory": [],
  "events": [],
  "terrains": [],
  "units": [],
  "trades": [],
  "battles": [],
  "technologies": [],
  "allianceId": null,
  "tasks": [],
  "version": "1.0"
}
```

## 錯誤處理

### HTTP 狀態碼

- `200 OK`: 操作成功
- `400 Bad Request`: 請求參數錯誤
- `404 Not Found`: 資源不存在
- `500 Internal Server Error`: 伺服器內部錯誤

### 常見錯誤訊息

1. **玩家ID不能為空**: 當 playerId 參數為空或 null 時
2. **遊戲狀態不存在**: 當指定的玩家遊戲狀態檔案不存在時
3. **保存遊戲狀態失敗**: 檔案系統操作失敗時
4. **載入遊戲狀態失敗**: 檔案讀取或解析失敗時

## 使用建議

### 1. 定期保存

建議在以下時機調用保存 API：
- 玩家完成重要操作後
- 定期自動保存（如每5分鐘）
- 玩家主動登出時

### 2. 錯誤處理

前端應妥善處理各種錯誤情況：
```javascript
const saveGameState = async (playerId, gameState) => {
  try {
    const response = await fetch(`/api/storage/save?playerId=${playerId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(gameState)
    });
    
    const result = await response.json();
    if (result.success) {
      console.log('保存成功');
    } else {
      console.error('保存失敗:', result.message);
    }
  } catch (error) {
    console.error('網路錯誤:', error);
  }
};
```

### 3. 性能優化

- 使用記憶體快取減少檔案 I/O 操作
- 批量操作時考慮使用事務機制
- 大數據量時考慮分頁處理

## 配置說明

### 應用程式配置

在 `application.yaml` 中配置存儲路徑：

```yaml
app:
  data:
    storagePath: game_data/
```

### 檔案命名規則

- 正式資料: `{playerId}.json`
- 測試資料: `test_{playerId}.json`

## 版本資訊

- **版本**: 1.0
- **更新日期**: 2024年
- **開發者**: TinyRealm Team

---

如有問題或建議，請聯繫開發團隊或提交 Issue。
