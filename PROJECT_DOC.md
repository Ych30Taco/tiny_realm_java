# TinyRealm Java 後端專案文件

## 專案概覽

TinyRealm 的後端服務，使用 Spring Boot 3.5.3 + Java 17 撰寫。提供玩家管理、資源生產、建築系統、士兵系統、戰鬥系統、地形地圖等核心遊戲 API，並以 JSON 檔案作為持久化儲存層。

---

## 技術棧

| 項目 | 版本 / 說明 |
|------|------------|
| Spring Boot | 3.5.3 |
| Java | 17 |
| Maven | 建置工具 |
| Jackson | 2.17.2，JSON 序列化 |
| Lombok | 1.18.32，簡化 Model 程式碼 |
| Spring WebSocket | STOMP 協議，支援實時通訊 |
| 儲存層 | JSON 檔案（`game_data/` 目錄） |

---

## 目錄結構

```
src/main/java/com/taco/TinyRealm/
├── TinyRealmApplication.java         # 主入口（啟用排程）
├── config/                           # 應用設定
│   ├── AppConfig.java                # CORS 設定
│   ├── GameConfig.java               # 遊戲設定加載
│   └── WebSocketConfig.java          # WebSocket / STOMP 設定
├── controller/                       # 輔助控制器（過渡用）
├── model/                            # 共用模型
├── service/                          # 共用服務
└── module/                           # 核心遊戲模組（9 個）
    ├── playerModule/
    ├── resourceModule/
    ├── buildingModule/
    ├── soldierModule/
    ├── battleModule/
    ├── terrainMapModule/
    ├── storageModule/
    ├── inventoryModule/
    └── AIModule/

src/main/resources/
├── application.yaml                  # 應用設定檔
├── config/                           # 遊戲內容 JSON 設定
│   ├── resource/resources.json
│   ├── building/buildings.json
│   ├── soldier/soldiers.json
│   ├── terrain/terrains.json
│   ├── battle/enemies.json
│   ├── item/items.json
│   ├── map/maps.json
│   └── ...（其他配置）
└── templates/                        # Thymeleaf 模板（輔助用）

game_data/                            # 玩家遊戲狀態持久化（JSON，按 UUID 命名）
```

---

## 應用設定

```yaml
# application.yaml
server:
  port: 1026

app:
  data:
    storagePath: game_data/
    resource-path: classpath:config/resource/resources.json
    building-path: classpath:config/building/buildings.json
    terrain-path: classpath:config/terrain/terrains.json
    map-path: classpath:config/map/maps.json
    soldier-path: classpath:config/soldier/soldiers.json
    item-path: classpath:config/item/items.json
    enemies-path: classpath:config/battle/enemies.json
```

---

## 模組說明

每個模組採用標準三層架構：`Controller → Service → Model`，共享 `StorageService` 存取遊戲狀態。

---

### playerModule（玩家模組）

**模型**：`Player.java`
- `id`（UUID）、`name`、`level`、`experience`
- `status`：`0` 離線、`1` 在線、`2` 封禁
- 登入/登出時間戳記

**服務**：`PlayerService`
- 建立玩家、取得玩家資料、登入/登出
- 建立時自動初始化資源（`initializePlayerResources`）和主城（`initializePlayerBuilding`）

**API 端點**：

| 方法 | 路徑 | 說明 |
|------|------|------|
| POST | `/api/player/create` | 建立玩家 |
| POST | `/api/player/userdata` | 取得玩家資料 |
| POST | `/api/player/login` | 登入 |
| POST | `/api/player/logOut` | 登出 |

---

### resourceModule（資源模組）

**模型**：
- `Resource.java`：資源類型定義（id、多語言名稱、圖示、稀有度、生產速率等）
- `PlayerResource.java`：玩家當前資源狀態（現有量、上限、生產速率的 Map）

**內建資源類型**（`resources.json`）：

| ID | 名稱 | 類型 | 基礎生產速率/hr |
|----|------|------|----------------|
| food | 糧食 | 基礎 | 60 |
| wood | 木頭 | 基礎 | 60 |
| stone | 石頭 | 基礎 | 60 |
| iron | 鐵礦 | 基礎 | 60 |
| gold | 金幣 | 高級 | 1 |
| DIAMOND | 鑽石 | 高級 | 0 |

**服務**：
- `ResourceService`：資源類型 CRUD
- `ResourceProductionService`：計算生產量
- `ResourceScheduler`：定時執行資源生產

**API 端點**：

| 方法 | 路徑 | 說明 |
|------|------|------|
| GET | `/api/resource/types` | 取得所有資源類型 |
| POST | `/api/resource/typeById` | 依 ID 取得資源 |
| POST | `/api/resource/add` | 增加玩家資源 |
| POST | `/api/resource/ded` | 扣除玩家資源 |
| POST/PUT/DELETE | `/api/resource/*` | 資源類型 CRUD |

---

### buildingModule（建築模組）

**模型**：
- `Building.java`：建築類型（id、名稱、類型、生產資源、最大建造數量、各等級資料）
- `PlayerBuliding.java`：玩家建築實例（位置、等級、狀態、建造時間）
- `LevelData.java`：等級資料（建造時間、成本、產出、前置條件、生產速率）
- `BuildingStatus.java`：`IDLE` / `BUILDING` / `UPGRADE`

**建築類型**：`function`（功能）、`resource`（資源）、`military`（軍事）、`defense`（防禦）

**內建建築**（`buildings.json`）：
- `mainHall`（主城）：function 型，最多 5 座
- `barracks`（兵營）：military 型，最多 2 座
- `farm`（農田）：resource 型，生產 FOOD

**服務**：`BuildingService`
- 建造前檢查：前置條件、資源是否足夠、地形是否允許
- `updateAllPlayersBulidingStatus()`：每秒定時更新建造/升級完成狀態

**API 端點**：

| 方法 | 路徑 | 說明 |
|------|------|------|
| GET | `/api/building/types` | 取得建築類型列表 |
| POST | `/api/building/create` | 建造建築 |
| PUT | `/api/building/upgrade` | 升級建築 |
| DELETE | `/api/building/remove` | 拆除建築 |
| POST/PUT/DELETE | `/api/building/type/*` | 建築類型 CRUD |

---

### soldierModule（士兵模組）

**模型**：
- `SoldierType.java`：士兵類型定義（類型、站位、屬性表、需求、科技前置）
- `PlayerSoldier.java`：玩家士兵實例（HP、攻擊、防禦、射程、速度、位置、數量、等級）

**士兵類型（UnitType）**：`INFANTRY`、`ARCHER`、`CAVALRY`、`SIEGE`、`MAGE`、`DEFENDER`

**站位（FormationPosition）**：`FRONT`、`MIDDLE`、`BACK`

**服務**：`SoldierService`（士兵建立、升級、移動、傷害計算）

---

### battleModule（戰鬥模組）

**模型**：
- `Battle.java`：戰鬥記錄（玩家、敵人類型、結果、獎勵、位置、統計）
- `BattleResult.java`：完整戰鬥結果（回合數、存活者、每回合結果、日誌）
- 輔助模型：`BattleLogEntry`、`RoundResult`、`BattleStatistics`、`EnemyType`、`SiegeResult`

**服務**：
- `BattleService`：戰鬥流程與獎勵發放
- `BattleCalculator`：傷害計算
- `SiegeCalculator`：攻城計算

---

### terrainMapModule（地形地圖模組）

**模型**：
- `GameMap.java`：地圖（寬、高、Tile 列表）
- `MapTile.java`：單一地格（座標、地形、擁有者、建築 ID、敵人資訊）
- `TerrainType.java`：`plain`、`forest`、`mountain`、`iron_mine`、`stone_mine`、`waterfront`、`river`、`grassland`、`desert`

**服務**：`TerrainMapService`
- 隨機生成地圖、載入設定地圖
- 位置可建造檢查（`canOccupyPosition`）
- 標記/釋放建築位置

---

### storageModule（存儲模組）

**核心模型**：`GameState.java`（遊戲完整狀態容器）
- 包含：`Player`、`PlayerResource`、`buildings`（Map）、`soldiers`（Map）、`inventory`、`battles`、`technologies`、`tasks`、`events` 等

**服務**：`StorageService`
- 記憶體快取：`ConcurrentHashMap<String, GameState>`
- 持久化：讀寫 `game_data/{playerId}.json`
- 提供 `getOnlineGameStateIdList()`、`getAllGameStateList()` 等查詢方法

**StorageController API**：

| 方法 | 路徑 | 說明 |
|------|------|------|
| GET | `/api/storage/playerList` | 所有玩家列表 |
| GET | `/api/storage/onlinePlayers` | 在線玩家列表 |
| GET | `/api/storage/offlinePlayers` | 離線玩家列表 |
| GET | `/api/storage/allGameStateList` | 所有遊戲狀態 |

---

### inventoryModule（背包模組）

**模型**：`PlayerItem.java`（物品 id、類型、名稱、數量、稀有度、屬性、可否交易/丟棄/消耗/裝備等）

**服務**：`InventoryService`（背包物品的新增、查詢、移除）

---

### AIModule（AI 模組）

**模型**：`AiModel.java`

**服務**：`AiService.java`

**控制器**：`AiController.java`

（目前為基本架構，詳細功能待補充）

---

## 遊戲狀態資料流

```
玩家請求
    ↓
Controller（驗證參數）
    ↓
Service（業務邏輯）
    ↓
StorageService.getGameState(playerId)   ← 從快取或 game_data/ 讀取
    ↓
修改 GameState
    ↓
StorageService.saveGameState(playerId)  → 寫入 game_data/{id}.json
    ↓
回傳結果
```

---

## 定時任務

| 任務 | 類別 | 頻率 | 說明 |
|------|------|------|------|
| 資源生產 | `ResourceScheduler` | 定期 | 依生產速率增加玩家資源 |
| 建築狀態更新 | `BuildingService` | 每秒 | 檢查建造/升級是否完成 |

---

## 啟動方式

```bash
./mvnw spring-boot:run
```

服務啟動於 `http://localhost:1026`

---

## 已知問題 / 待改進

1. **部分控制器功能停用**：`ConfigReloadController` 大部分功能已被注解禁用
2. **無資料庫**：目前使用 JSON 檔案作為持久化，並發寫入需注意競態問題
3. **AI 模組未完善**：`AIModule` 目前僅有基本結構
4. **`Battle`、`DataAdmin` 前端**：對應的後端 API 端點需確認是否完整對應前端需求
