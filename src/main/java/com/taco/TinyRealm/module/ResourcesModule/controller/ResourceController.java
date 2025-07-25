package com.taco.TinyRealm.module.ResourcesModule.controller;

import com.taco.TinyRealm.module.playerModule.model.Player;     // 因為資源操作會影響玩家，API 響應可能返回玩家物件
import com.taco.TinyRealm.module.ResourcesModule.model.Resource; // 引入資源類型模型
import com.taco.TinyRealm.module.ResourcesModule.service.ResourceService; // 引入資源服務
import com.taco.TinyRealm.module.EventSystemModule.model.EventPublisher;
import com.taco.TinyRealm.module.EventSystemModule.model.EventType;

import lombok.Data;          // Lombok 註解，自動生成 getter, setter 等
import lombok.NoArgsConstructor; // Lombok 註解
import lombok.AllArgsConstructor; // Lombok 註解

import org.springframework.http.HttpStatus;   // HTTP 狀態碼
import org.springframework.http.ResponseEntity; // Spring HTTP 響應封裝
import org.springframework.web.bind.annotation.*; // Spring Web 註解

import java.util.List;
import java.util.Optional;

/**
 * 資源模組的 REST 控制器。
 * 處理所有與資源相關的 HTTP 請求。
 */
@RestController // 標記為 RESTful 控制器，處理 HTTP 請求並返回 JSON/XML 響應
@RequestMapping("/api/resource") // 設定所有此控制器下的端點都以 "/api/resource" 開頭
public class ResourceController {

    private final ResourceService resourceService; // 注入 ResourceService 實例
    private final EventPublisher eventPublisher;

    /**
     * 建構子注入 ResourceService。
     * @param resourceService 資源服務的實例。
     */
    public ResourceController(ResourceService resourceService, EventPublisher eventPublisher) {
        this.resourceService = resourceService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * GET /api/resource/definitions
     * 獲取所有已定義的資源類型列表。
     * 範例響應:
     * [
     * { "id": "gold", "name": "黃金", ... },
     * { "id": "wood", "name": "木材", ... }
     * ]
     */
    @GetMapping("/definitions")
    public ResponseEntity<List<Resource>> getAllResourceDefinitions() {
        List<Resource> definitions = resourceService.getAllResourceDefinitions();
        // 返回 HTTP 200 OK 和資源定義列表
        return new ResponseEntity<>(definitions, HttpStatus.OK);
    }

    /**
     * GET /api/resource/definitions/{resourceId}
     * 根據資源 ID 獲取單個資源類型的詳細定義。
     * 範例請求: GET /api/resource/definitions/wood
     * 範例響應:
     * { "id": "wood", "name": "木材", "description": "基本的建築材料。", "iconPath": "path/to/icon_wood.png" }
     * @param resourceId 路徑變數，要查詢的資源 ID。
     */
    @GetMapping("/definitions/{resourceId}")
    public ResponseEntity<Resource> getResourceDefinitionById(@PathVariable String resourceId) {
        return resourceService.getResourceDefinitionById(resourceId)
                // 如果找到資源，返回 HTTP 200 OK 和 Resource 物件
                .map(resource -> new ResponseEntity<>(resource, HttpStatus.OK))
                // 如果找不到資源，返回 HTTP 404 Not Found
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST /api/resource/{playerId}/add
     * 為指定玩家增加特定類型的資源數量。
     * 範例請求體: {"resourceId": "wood", "amount": 10}
     * 範例響應: 更新後的 Player 物件 (如果操作成功)
     * @param playerId 路徑變數，玩家的唯一 ID。
     * @param request 包含資源 ID 和增加數量的請求體。
     */
    @PostMapping("/{playerId}/add")
    public ResponseEntity<Void> addPlayerResource(@PathVariable String playerId, @RequestBody ResourceOperationRequest request) {
        java.util.Map<String, Object> payload = java.util.Map.of(
            "playerId", playerId,
            "resourceId", request.getResourceId(),
            "amount", request.getAmount(),
            "action", "add"
        );
        eventPublisher.publish(EventType.RESOURCE_CHANGED, payload, this);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * POST /api/resource/{playerId}/consume
     * 讓指定玩家消耗特定類型的資源數量。
     * 範例請求體: {"resourceId": "gold", "amount": 50}
     * 範例響應: 更新後的 Player 物件 (如果操作成功且資源充足)
     * @param playerId 路徑變數，玩家的唯一 ID。
     * @param request 包含資源 ID 和消耗數量的請求體。
     */
    @PostMapping("/{playerId}/consume")
    public ResponseEntity<Void> consumePlayerResource(@PathVariable String playerId, @RequestBody ResourceOperationRequest request) {
        java.util.Map<String, Object> payload = java.util.Map.of(
            "playerId", playerId,
            "resourceId", request.getResourceId(),
            "amount", request.getAmount(),
            "action", "consume"
        );
        eventPublisher.publish(EventType.RESOURCE_CHANGED, payload, this);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * 內部靜態類：用於接收資源操作 (增加/消耗) 請求的數據傳輸物件 (DTO)。
     * 使用 @RequestBody 註解時，Spring 會自動將 JSON 請求體映射到這個物件。
     */
    @Data // Lombok 註解，自動生成 getter, setter 等
    @NoArgsConstructor // Lombok 註解
    @AllArgsConstructor // Lombok 註解
    public static class ResourceOperationRequest {
        private String resourceId; // 要操作的資源類型 ID
        private int amount;        // 操作的數量 (增加或消耗)
    }
}