# 地形佔領API功能說明

## 概述
本模組新增了地形佔領功能，允許玩家佔領地圖上的位置，並在建造建築時進行地形判斷與占用。

## 新增的API端點

### 1. 地形佔領相關API (`/api/terrain`)

#### 佔領位置
- **端點**: `POST /api/terrain/occupy`
- **功能**: 佔領指定位置
- **請求體**:
```json
{
    "x": 5,
    "y": 3,
    "playerId": "player123"
}
```
- **回應**: 成功或失敗訊息

#### 釋放位置
- **端點**: `POST /api/terrain/release`
- **功能**: 釋放已佔領的位置
- **請求體**:
```json
{
    "x": 5,
    "y": 3,
    "playerId": "player123"
}
```
- **回應**: 成功或失敗訊息

#### 檢查位置是否可佔領
- **端點**: `POST /api/terrain/canOccupy`
- **功能**: 檢查指定位置是否可被佔領
- **請求體**:
```json
{
    "x": 5,
    "y": 3,
    "playerId": "player123"
}
```
- **回應**: `{"canOccupy": true/false}`

#### 獲取玩家佔領的所有位置
- **端點**: `POST /api/terrain/playerPositions`
- **功能**: 獲取指定玩家佔領的所有位置
- **請求體**:
```json
{
    "playerId": "player123"
}
```
- **回應**: 位置列表

#### 檢查位置狀態
- **端點**: `POST /api/terrain/positionStatus`
- **功能**: 獲取指定位置的詳細狀態
- **請求體**:
```json
{
    "x": 5,
    "y": 3
}
```
- **回應**: 位置詳細資訊

### 2. 建築模組增強API (`/api/building`)

#### 建造建築（已增強）
- **端點**: `POST /api/building/create`
- **新增功能**: 
  - 自動檢查地形是否可建造
  - 自動佔用建造位置
  - 地形類型驗證
- **請求體**:
```json
{
    "playerId": "player123",
    "buildingId": "barracks",
    "x": 5,
    "y": 3
}
```

#### 拆除建築（新增）
- **端點**: `DELETE /api/building/remove`
- **功能**: 拆除建築並自動釋放地形
- **請求體**:
```json
{
    "playerId": "player123",
    "buildingId": "barracks"
}
```

## 地形佔領規則

### 可佔領條件
1. 位置在地圖範圍內
2. 地形類型允許建造 (`buildable: true`)
3. 位置未被其他玩家佔領
4. 位置上沒有建築

### 建築建造規則
1. 必須先檢查地形是否可建造
2. 建造成功後自動佔用該位置
3. 建築拆除後自動釋放地形

### 地形類型限制
- **平原**: 可建造大部分建築
- **山脈**: 可建造礦場、要塞等
- **水域**: 不可建造（除非特殊建築）
- **森林**: 可建造伐木場、獵人小屋等

## 使用範例

### 1. 檢查位置是否可建造
```bash
curl -X POST http://localhost:8080/api/terrain/canOccupy \
  -H "Content-Type: application/json" \
  -d '{"x": 5, "y": 3, "playerId": "player123"}'
```

### 2. 建造建築
```bash
curl -X POST http://localhost:8080/api/building/create \
  -H "Content-Type: application/json" \
  -d '{"playerId": "player123", "buildingId": "barracks", "x": 5, "y": 3}'
```

### 3. 查看位置狀態
```bash
curl -X POST http://localhost:8080/api/terrain/positionStatus \
  -H "Content-Type: application/json" \
  -d '{"x": 5, "y": 3}'
```

## 錯誤處理

### 常見錯誤
- **位置超出範圍**: 座標超出地圖邊界
- **地形不可建造**: 該地形類型不允許建造
- **位置已被佔領**: 其他玩家已佔領該位置
- **位置已有建築**: 該位置已有建築存在
- **玩家不存在**: 指定的玩家ID不存在

### 錯誤回應格式
```json
{
    "success": false,
    "message": "錯誤描述"
}
```

## 技術實現

### 核心服務
- `TerrainMapService`: 地形地圖管理服務
- `BuildingService`: 建築管理服務（已增強）

### 主要功能
- 地形佔領管理
- 位置驗證
- 建築與地形關聯
- 地圖狀態持久化

### 數據模型
- `MapTile`: 地圖瓦片（包含位置、地形、擁有者、建築信息）
- `Terrain`: 地形類型定義
- `GameMap`: 遊戲地圖整體結構 