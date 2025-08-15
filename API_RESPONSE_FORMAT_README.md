# API回應格式統一化說明

## 概述
所有API回應都已統一為以下格式：
```json
{
    "success": true/false,
    "message": "操作結果描述",
    "data": 實際數據或null
}
```

## 統一格式說明

### 成功回應
```json
{
    "success": true,
    "message": "操作成功描述",
    "data": 返回的數據
}
```

### 失敗回應
```json
{
    "success": false,
    "message": "錯誤描述",
    "data": null
}
```

## 已修改的控制器

### 1. 主控制器 (`/main`)
- **端點**: `POST /main`
- **回應格式**: 統一為 `success`, `message`, `data` 格式

### 2. 玩家模組 (`/api/player`)
- **端點**: 
  - `POST /create` - 創建玩家
  - `GET /userdata` - 獲取玩家數據
  - `POST /logOut` - 玩家登出
  - `POST /login` - 玩家登入
- **回應格式**: 統一為 `success`, `message`, `data` 格式

### 3. 資源模組 (`/api/resource`)
- **端點**:
  - `GET /types` - 獲取資源類型
  - `POST /typeById` - 根據ID獲取資源類型
  - `POST /add` - 添加資源
  - `POST /ded` - 扣除資源
- **回應格式**: 統一為 `success`, `message`, `data` 格式

### 4. 建築模組 (`/api/building`)
- **端點**:
  - `GET /types` - 獲取建築類型
  - `POST /typeById` - 根據ID獲取建築類型
  - `POST /create` - 建造建築
  - `PUT /upgrade` - 升級建築
  - `DELETE /remove` - 拆除建築
- **回應格式**: 統一為 `success`, `message`, `data` 格式

### 5. 地形地圖模組 (`/api/terrain`)
- **端點**:
  - `GET /types` - 獲取地形類型
  - `POST /typeById` - 根據ID獲取地形類型
  - `GET /gameMap` - 獲取遊戲地圖
  - `POST /occupy` - 佔領位置
  - `POST /release` - 釋放位置
  - `POST /canOccupy` - 檢查是否可佔領
  - `POST /playerPositions` - 獲取玩家佔領位置
  - `POST /positionStatus` - 檢查位置狀態
- **回應格式**: 統一為 `success`, `message`, `data` 格式

### 6. 存儲模組 (`/api/storage`)
- **端點**:
  - `POST /save` - 保存遊戲狀態
  - `GET /load` - 載入遊戲狀態
  - `GET /playerList` - 獲取玩家列表
  - `GET /onlinePlayers` - 獲取在線玩家
  - `GET /offlinePlayers` - 獲取離線玩家
  - `GET /allGameStateList` - 獲取所有遊戲狀態
- **回應格式**: 統一為 `success`, `message`, `data` 格式

## 回應範例

### 成功範例
```json
{
    "success": true,
    "message": "建築建造成功",
    "data": {
        "playerId": "player123",
        "buildings": {...},
        "resources": {...}
    }
}
```

### 失敗範例
```json
{
    "success": false,
    "message": "資源不足",
    "data": null
}
```

### 錯誤範例
```json
{
    "success": false,
    "message": "內部錯誤",
    "data": null
}
```

## 特殊情況處理

### 404 錯誤
```json
{
    "success": false,
    "message": "玩家不存在",
    "data": null
}
```

### 500 錯誤
```json
{
    "success": false,
    "message": "內部錯誤",
    "data": null
}
```

### 400 錯誤
```json
{
    "success": false,
    "message": "參數錯誤描述",
    "data": null
}
```

## 前端處理建議

### 統一處理邏輯
```javascript
// 建議的前端處理方式
const handleApiResponse = (response) => {
    if (response.success) {
        // 操作成功，處理 data
        console.log(response.message);
        return response.data;
    } else {
        // 操作失敗，顯示錯誤訊息
        console.error(response.message);
        throw new Error(response.message);
    }
};
```

### 錯誤處理
```javascript
// 錯誤處理範例
try {
    const result = await apiCall();
    const data = handleApiResponse(result);
    // 處理成功數據
} catch (error) {
    // 處理錯誤
    showErrorMessage(error.message);
}
```

## 注意事項

1. **所有API回應**都遵循統一的格式
2. **成功操作**的 `data` 字段包含實際返回數據
3. **失敗操作**的 `data` 字段為 `null`
4. **錯誤訊息**使用中文描述，便於用戶理解
5. **HTTP狀態碼**與 `success` 字段保持一致

## 維護說明

當添加新的API端點時，請確保：
1. 使用統一的回應格式
2. 提供清晰的中文訊息
3. 正確設置 `success` 字段
4. 在 `data` 字段中返回實際數據或 `null` 