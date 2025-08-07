# TinyRealm 模組測試平台

這是一個用於測試和展示 TinyRealm 遊戲所有模組功能的 Web 介面。

## 功能特色

### 🎮 模組總覽
- **系統狀態監控**：即時顯示各模組運行狀態
- **快速操作**：一鍵初始化、測試、重置所有模組
- **地圖預覽**：視覺化展示地形模組功能

### 🗺️ 地形模組
- **地形管理**：設定和查詢特定座標的地形類型
- **地形統計**：顯示各地形類型的數量統計
- **地圖生成**：隨機生成或從配置檔案載入地圖

### 🏗️ 建築模組
- **建築管理**：創建和升級各種建築物
- **建築列表**：查看所有建築物的詳細資訊
- **玩家關聯**：管理建築物與玩家的關聯

### 💰 資源模組
- **資源管理**：增加和消耗各種資源
- **資源概覽**：即時顯示玩家資源狀況
- **資源類型**：支援金幣、食物、木材、石頭、鐵礦

### 👥 玩家模組
- **玩家管理**：創建和更新玩家資訊
- **玩家列表**：查看所有玩家的詳細資料
- **等級系統**：管理玩家等級和經驗

### ⚔️ 戰鬥模組
- **戰鬥管理**：發起和模擬戰鬥
- **戰鬥記錄**：查看歷史戰鬥記錄
- **部隊系統**：管理不同類型的部隊

### 🛒 市場模組
- **商品管理**：上架和購買商品
- **市場列表**：查看所有可交易商品
- **價格系統**：動態價格管理

## 使用方法

### 1. 啟動應用程式
```bash
# 使用 Maven 啟動
mvn spring-boot:run

# 或使用 Maven Wrapper
./mvnw spring-boot:run
```

### 2. 訪問測試平台
在瀏覽器中開啟：`http://localhost:8080/modules`

### 3. 開始測試
1. **初始化模組**：點擊「初始化所有模組」按鈕
2. **生成測試資料**：點擊「生成測試資料」按鈕
3. **執行完整測試**：點擊「執行完整測試」按鈕
4. **測試各模組**：使用各標籤頁測試特定模組功能

## API 端點

### 總覽功能
- `POST /modules/init` - 初始化所有模組
- `POST /modules/test` - 執行完整測試
- `POST /modules/generate-data` - 生成測試資料
- `POST /modules/reset` - 重置所有資料

### 地形模組
- `GET /modules/terrain` - 獲取地形資料
- `POST /modules/terrain/set` - 設定地形

### 建築模組
- `GET /modules/building` - 獲取建築資料
- `POST /modules/building/create` - 創建建築

### 資源模組
- `GET /modules/resource` - 獲取資源資料
- `POST /modules/resource/add` - 增加資源

### 玩家模組
- `GET /modules/player` - 獲取玩家資料
- `POST /modules/player/create` - 創建玩家

### 戰鬥模組
- `GET /modules/battle` - 獲取戰鬥資料
- `POST /modules/battle/start` - 開始戰鬥

### 市場模組
- `GET /modules/market` - 獲取市場資料
- `POST /modules/market/list` - 上架商品

## 技術架構

### 前端
- **HTML5** + **CSS3** + **JavaScript (ES6+)**
- **Bootstrap 5** - 響應式 UI 框架
- **Font Awesome** - 圖示庫
- **Fetch API** - 非同步 HTTP 請求

### 後端
- **Spring Boot** - Java 後端框架
- **Spring MVC** - Web 控制器
- **Thymeleaf** - 模板引擎
- **RESTful API** - REST 風格 API 設計

### 模組架構
```
src/main/java/com/taco/TinyRealm/
├── controller/           # 控制器層
│   ├── ModuleTestController.java
│   └── ...
├── module/              # 模組層
│   ├── terrainModule/   # 地形模組
│   ├── buildingModule/  # 建築模組
│   ├── resourceModule/  # 資源模組
│   ├── playerModule/    # 玩家模組
│   └── ...
├── service/             # 服務層
└── model/               # 模型層
```

## 自訂開發

### 新增模組
1. 在 `module/` 目錄下建立新的模組目錄
2. 實作對應的 Controller、Service、Model
3. 在 `ModuleTestController` 中新增對應的 API 端點
4. 在 `modules.html` 中新增對應的 UI 標籤頁

### 修改樣式
- 主要樣式：`src/main/resources/templates/modules.html` 中的 `<style>` 標籤
- 額外樣式：`src/main/resources/static/css/modules.css`

### 擴充功能
- 在 `modules.html` 的 JavaScript 部分新增功能函數
- 在對應的 Controller 中實作後端邏輯
- 使用 Fetch API 連接前後端

## 注意事項

1. **資料持久化**：目前使用記憶體儲存，重啟後資料會遺失
2. **錯誤處理**：所有 API 呼叫都有錯誤處理機制
3. **響應式設計**：支援桌面和行動裝置
4. **瀏覽器相容性**：支援現代瀏覽器（Chrome、Firefox、Safari、Edge）

## 故障排除

### 常見問題
1. **頁面無法載入**：檢查 Spring Boot 是否正常啟動
2. **API 呼叫失敗**：檢查瀏覽器開發者工具的網路標籤
3. **樣式顯示異常**：清除瀏覽器快取或檢查 CSS 檔案路徑

### 除錯技巧
1. 使用瀏覽器開發者工具查看 Console 錯誤
2. 檢查 Spring Boot 應用程式日誌
3. 使用 Postman 或類似工具測試 API 端點

## 版本資訊

- **版本**：1.0.0
- **更新日期**：2024年
- **開發者**：TinyRealm 開發團隊

---

如有問題或建議，請聯繫開發團隊或提交 Issue。 