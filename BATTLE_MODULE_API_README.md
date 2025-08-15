# 戰鬥模組 API 文檔

## 概述

戰鬥模組提供完整的戰鬥系統功能，包括戰鬥開始、結果計算、獎勵發放、戰鬥記錄查詢等。該模組與其他模組（單位、資源、地形、任務、事件）緊密整合，提供豐富的遊戲體驗。

## API 端點

### 1. 開始戰鬥
**POST** `/api/battle/start`

開始一場新的戰鬥。

**請求參數：**
- `playerId` (String, 必需): 玩家ID
- `unitIds` (String, 必需): 參戰單位ID列表，用逗號分隔
- `enemyType` (String, 必需): 敵人類型
- `locationX` (int, 可選): 戰鬥位置X座標，預設0
- `locationY` (int, 可選): 戰鬥位置Y座標，預設0
- `isTest` (boolean, 可選): 是否為測試模式，預設false

**請求範例：**
```bash
curl -X POST "http://localhost:8080/api/battle/start" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "playerId=player123&unitIds=unit1,unit2,unit3&enemyType=bandit&locationX=10&locationY=15&isTest=false"
```

**成功回應：**
```json
{
  "success": true,
  "message": "Battle started successfully",
  "data": {
    "id": "battle-uuid-123",
    "playerId": "player123",
    "enemyType": "bandit",
    "playerUnits": [...],
    "enemyUnits": [...],
    "result": "WIN",
    "rewards": {
      "gold": 50,
      "wood": 20,
      "stone": 10,
      "iron": 5,
      "food": 15
    },
    "timestamp": 1640995200000,
    "duration": 60,
    "difficulty": "EASY",
    "locationX": 10,
    "locationY": 15,
    "statistics": {
      "playerStrength": 150,
      "enemyStrength": 120,
      "playerRandomFactor": 1.1,
      "enemyRandomFactor": 0.9,
      "finalPlayerStrength": 165.0,
      "finalEnemyStrength": 108.0
    },
    "status": "COMPLETED",
    "description": "你遇到了一群強盜！他們看起來很兇狠，準備戰鬥吧！"
  }
}
```

**失敗回應：**
```json
{
  "success": false,
  "message": "Invalid battle parameters: Player level too low for this enemy type",
  "data": null
}
```

### 2. 獲取玩家戰鬥記錄
**GET** `/api/battle/player/{playerId}`

獲取指定玩家的所有戰鬥記錄。

**路徑參數：**
- `playerId` (String, 必需): 玩家ID

**查詢參數：**
- `isTest` (boolean, 可選): 是否為測試模式，預設false

**請求範例：**
```bash
curl -X GET "http://localhost:8080/api/battle/player/player123?isTest=false"
```

**成功回應：**
```json
{
  "success": true,
  "message": "Successfully retrieved battle history",
  "data": [
    {
      "id": "battle-uuid-123",
      "playerId": "player123",
      "enemyType": "bandit",
      "result": "WIN",
      "timestamp": 1640995200000,
      "duration": 60,
      "difficulty": "EASY",
      "status": "COMPLETED"
    }
  ]
}
```

### 3. 獲取特定戰鬥記錄
**GET** `/api/battle/{playerId}/{battleId}`

獲取指定玩家的特定戰鬥記錄。

**路徑參數：**
- `playerId` (String, 必需): 玩家ID
- `battleId` (String, 必需): 戰鬥ID

**查詢參數：**
- `isTest` (boolean, 可選): 是否為測試模式，預設false

**請求範例：**
```bash
curl -X GET "http://localhost:8080/api/battle/player123/battle-uuid-123?isTest=false"
```

### 4. 獲取所有敵人類型
**GET** `/api/battle/enemies`

獲取所有可用的敵人類型配置。

**請求範例：**
```bash
curl -X GET "http://localhost:8080/api/battle/enemies"
```

**成功回應：**
```json
{
  "success": true,
  "message": "Successfully retrieved all enemy types",
  "data": {
    "bandit": {
      "type": "bandit",
      "name": "強盜",
      "description": "一群兇狠的強盜，經常襲擊過往的商隊",
      "level": 1,
      "units": [...],
      "rewards": {...},
      "difficulty": "EASY",
      "minPlayerLevel": 1,
      "battleDuration": 60,
      "skills": {...},
      "spawnChance": 0.8,
      "isBoss": false,
      "battleDescription": "你遇到了一群強盜！他們看起來很兇狠，準備戰鬥吧！"
    }
  }
}
```

### 5. 獲取特定敵人類型
**GET** `/api/battle/enemies/{enemyType}`

獲取特定敵人類型的詳細配置。

**路徑參數：**
- `enemyType` (String, 必需): 敵人類型識別碼

**請求範例：**
```bash
curl -X GET "http://localhost:8080/api/battle/enemies/bandit"
```

### 6. 獲取玩家戰鬥統計
**GET** `/api/battle/stats/{playerId}`

獲取指定玩家的戰鬥統計資訊。

**路徑參數：**
- `playerId` (String, 必需): 玩家ID

**查詢參數：**
- `isTest` (boolean, 可選): 是否為測試模式，預設false

**請求範例：**
```bash
curl -X GET "http://localhost:8080/api/battle/stats/player123?isTest=false"
```

**成功回應：**
```json
{
  "success": true,
  "message": "Successfully retrieved battle statistics",
  "data": {
    "totalBattles": 10,
    "victories": 7,
    "defeats": 2,
    "draws": 1,
    "winRate": 0.7,
    "lastBattle": {
      "id": "battle-uuid-123",
      "enemyType": "bandit",
      "result": "WIN",
      "timestamp": 1640995200000
    }
  }
}
```

### 7. 清理測試戰鬥數據
**DELETE** `/api/battle/clearTest/{playerId}`

清理指定玩家的測試戰鬥數據。

**路徑參數：**
- `playerId` (String, 必需): 玩家ID

**查詢參數：**
- `isTest` (boolean, 必需): 必須設為true

**請求範例：**
```bash
curl -X DELETE "http://localhost:8080/api/battle/clearTest/player123?isTest=true"
```

### 8. 獲取戰鬥系統狀態
**GET** `/api/battle/status`

獲取戰鬥系統的運行狀態。

**請求範例：**
```bash
curl -X GET "http://localhost:8080/api/battle/status"
```

**成功回應：**
```json
{
  "success": true,
  "message": "Battle system is operational",
  "data": {
    "enemyTypesCount": 8,
    "systemStatus": "ACTIVE",
    "version": "1.0.0"
  }
}
```

## 數據結構

### Battle 物件
```json
{
  "id": "戰鬥唯一識別碼",
  "playerId": "玩家ID",
  "enemyType": "敵人類型",
  "playerUnits": [
    {
      "id": "單位ID",
      "type": "單位類型",
      "name": "單位名稱",
      "level": 單位等級,
      "attack": 攻擊力,
      "defense": 防禦力,
      "health": 當前生命值,
      "maxHealth": 最大生命值,
      "status": "單位狀態"
    }
  ],
  "enemyUnits": [...],
  "result": "戰鬥結果 (WIN/LOSE/DRAW)",
  "rewards": {
    "gold": 金幣獎勵,
    "wood": 木材獎勵,
    "stone": 石頭獎勵,
    "iron": 鐵礦獎勵,
    "food": 食物獎勵
  },
  "timestamp": 戰鬥時間戳,
  "duration": 戰鬥持續時間（秒）,
  "difficulty": "戰鬥難度",
  "locationX": 戰鬥位置X座標,
  "locationY": 戰鬥位置Y座標,
  "statistics": {
    "playerStrength": 玩家總戰力,
    "enemyStrength": 敵方總戰力,
    "playerRandomFactor": 玩家隨機因子,
    "enemyRandomFactor": 敵方隨機因子,
    "finalPlayerStrength": 最終玩家戰力,
    "finalEnemyStrength": 最終敵方戰力
  },
  "status": "戰鬥狀態 (PENDING/IN_PROGRESS/COMPLETED)",
  "description": "戰鬥描述"
}
```

### EnemyType 物件
```json
{
  "type": "敵人類型識別碼",
  "name": "敵人類型名稱",
  "description": "敵人類型描述",
  "level": 敵人類型等級,
  "units": [
    {
      "type": "單位類型",
      "count": 單位數量,
      "level": 單位等級,
      "attack": 攻擊力,
      "defense": 防禦力,
      "health": 生命值
    }
  ],
  "rewards": {
    "gold": 金幣獎勵,
    "wood": 木材獎勵,
    "stone": 石頭獎勵,
    "iron": 鐵礦獎勵,
    "food": 食物獎勵
  },
  "difficulty": "戰鬥難度 (EASY/MEDIUM/HARD/EPIC/LEGENDARY)",
  "minPlayerLevel": 最小玩家等級要求,
  "battleDuration": 戰鬥持續時間（秒）,
  "skills": {
    "技能名稱": 技能值或機率
  },
  "spawnChance": 出現機率,
  "isBoss": 是否為Boss,
  "battleDescription": "戰鬥背景描述"
}
```

## 敵人類型配置

### 可用敵人類型

1. **bandit** (強盜) - 等級1，簡單難度
2. **wolf_pack** (狼群) - 等級2，中等難度
3. **goblin_raiders** (哥布林突襲者) - 等級3，中等難度
4. **undead_horde** (不死軍團) - 等級4，困難難度
5. **orc_warband** (獸人戰團) - 等級5，困難難度
6. **dark_elf_assassins** (黑暗精靈刺客) - 等級6，困難難度
7. **troll_king** (巨魔之王) - 等級8，史詩難度 (Boss)
8. **dragon** (巨龍) - 等級10，傳說難度 (Boss)

### 戰鬥難度等級

- **EASY**: 適合新手玩家
- **MEDIUM**: 需要一定戰鬥經驗
- **HARD**: 需要良好的單位配置和策略
- **EPIC**: 極具挑戰性的Boss戰
- **LEGENDARY**: 最高難度的傳說級戰鬥

## 戰鬥機制

### 戰鬥結果計算
1. 計算玩家和敵方的總戰力
2. 應用隨機因子（0.8-1.2倍）
3. 比較最終戰力決定勝負
4. 根據結果計算單位損失

### 單位損失機制
- **勝利**: 單位損失10-30%
- **失敗**: 單位損失40-70%
- **平手**: 單位損失20-40%

### 獎勵系統
- 只有勝利才能獲得獎勵
- 獎勵根據敵人類型配置發放
- 自動更新任務進度
- 記錄戰鬥事件

## 錯誤處理

### 常見錯誤

1. **玩家不存在**
   ```json
   {
     "success": false,
     "message": "Player not found: player123",
     "data": null
   }
   ```

2. **單位不存在或已死亡**
   ```json
   {
     "success": false,
     "message": "No valid units found for battle",
     "data": null
   }
   ```

3. **玩家等級不足**
   ```json
   {
     "success": false,
     "message": "Player level too low for this enemy type",
     "data": null
   }
   ```

4. **未知敵人類型**
   ```json
   {
     "success": false,
     "message": "Unknown enemy type: invalid_enemy",
     "data": null
   }
   ```

## 使用建議

### 戰鬥前準備
1. 確保玩家等級滿足敵人類型要求
2. 選擇健康的單位參戰
3. 考慮單位類型的搭配
4. 準備足夠的資源進行戰鬥

### 戰鬥策略
1. 根據敵人類型選擇合適的單位組合
2. 注意敵方的特殊技能
3. 考慮戰鬥位置的地形影響
4. 準備應對不同的戰鬥結果

### 戰後管理
1. 檢查單位損失情況
2. 治療受傷的單位
3. 收集戰鬥獎勵
4. 查看任務進度更新

## 測試端點

### 測試戰鬥
```bash
# 開始測試戰鬥
curl -X POST "http://localhost:8080/api/battle/start" \
  -d "playerId=testPlayer&unitIds=testUnit1,testUnit2&enemyType=bandit&isTest=true"

# 查看測試戰鬥記錄
curl -X GET "http://localhost:8080/api/battle/player/testPlayer?isTest=true"

# 清理測試數據
curl -X DELETE "http://localhost:8080/api/battle/clearTest/testPlayer?isTest=true"
```

## 配置說明

### 敵人類型配置文件
位置：`src/main/resources/config/enemies.json`

可以通過修改此文件來：
- 調整敵人類型的屬性
- 新增敵人類型
- 修改獎勵配置
- 調整難度平衡

### 系統配置
- 戰鬥隨機因子範圍：0.8-1.2
- 單位損失比例：根據戰鬥結果動態調整
- 戰鬥持續時間：根據敵人類型配置
- 出現機率：影響敵人的生成頻率

## 模組整合

戰鬥模組與以下模組緊密整合：

1. **單位模組**: 獲取玩家單位資訊，更新單位狀態
2. **資源模組**: 發放戰鬥獎勵
3. **地形模組**: 釋放死亡單位佔據的位置
4. **任務模組**: 更新相關任務進度
5. **事件模組**: 記錄戰鬥事件
6. **儲存模組**: 保存戰鬥記錄和遊戲狀態

## 版本資訊

- **版本**: 1.0.0
- **最後更新**: 2024年8月
- **相容性**: Spring Boot 2.x+
- **Java版本**: 8+
