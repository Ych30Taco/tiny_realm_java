package com.taco.TinyRealm.module.playerModule.controller;

import com.taco.TinyRealm.module.EventSystemModule.EventPublisher;
import com.taco.TinyRealm.module.EventSystemModule.EventType;
import com.taco.TinyRealm.module.playerModule.model.Location;
import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;
import lombok.Data; // 引入 Lombok 的 Data
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; // 引入 AllArgsConstructor

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 標記為 REST Controller
@RequestMapping("/api/player") // 所有玩家相關的 API 都以 /api/player 開頭
public class PlayerController {

    private final PlayerService playerService;
    private final EventPublisher eventPublisher;

    public PlayerController(PlayerService playerService, EventPublisher eventPublisher) {
        this.playerService = playerService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * POST /api/player/create
     * 創建一個新玩家。
     * 請求體範例: {"playerName": "Hero"}
     * 響應範例: 新創建的 Player 物件
     */
    @PostMapping("/create")
    public ResponseEntity<Player> createPlayer(@RequestBody PlayerCreationRequest request) {
        Player newPlayer = playerService.createPlayer(request.getPlayerName());
        if (newPlayer != null) {
            return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 如果創建失敗 (例如名稱重複等)
    }

    /**
     * GET /api/player/{playerId}
     * 獲取指定玩家的資訊。
     * 響應範例: Player 物件
     */
    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayer(@PathVariable String playerId) {
        return playerService.getPlayer(playerId)
                .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * PUT /api/player/{playerId}/location
     * 更新玩家位置。
     * 請求體範例: {"x": 10, "y": 5}
     * 響應範例: 更新後的 Player 物件
     */
    @PutMapping("/{playerId}/location")
    public ResponseEntity<Player> updatePlayerLocation(@PathVariable String playerId, @RequestBody Location newLocation) {
        return playerService.updatePlayerLocation(playerId, newLocation)
                .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST /api/player/{playerId}/addExp
     * 為玩家增加經驗值。
     * 請求體範例: {"amount": 50}
     * 響應範例: 更新後的 Player 物件
     */
    @PostMapping("/{playerId}/addExp")
    public ResponseEntity<Player> addPlayerExperience(@PathVariable String playerId, @RequestBody ExperienceRequest request) {
        return playerService.addExperience(playerId, request.getAmount())
                .map(player -> new ResponseEntity<>(player, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST /api/player/{playerId}/addResource
     * 為玩家增加特定資源。
     * 請求體範例: {"resourceType": "wood", "amount": 10}
     * 響應範例: 202 Accepted
     */
    @PostMapping("/{playerId}/addResource")
    public ResponseEntity<Void> addPlayerResource(@PathVariable String playerId, @RequestBody ResourceAddRequest request) {
        java.util.Map<String, Object> payload = java.util.Map.of(
            "playerId", playerId,
            "resourceId", request.getResourceType(),
            "amount", request.getAmount(),
            "action", "add"
        );
        eventPublisher.publish(EventType.RESOURCE_CHANGED, payload, this);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


    // 內部 DTO，用於接收創建玩家的請求
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayerCreationRequest {
        private String playerName;
    }

    // 內部 DTO，用於接收增加經驗的請求
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceRequest {
        private long amount;
    }

    // 內部 DTO，用於接收增加資源的請求
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceAddRequest {
        private String resourceType;
        private int amount;
    }
}