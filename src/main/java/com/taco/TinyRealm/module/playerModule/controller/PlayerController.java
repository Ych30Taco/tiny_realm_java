package com.taco.TinyRealm.module.playerModule.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;

@RestController // 標記為 REST Controller
@RequestMapping("/api/player") // 所有玩家相關的 API 都以 /api/player 開頭
public class PlayerController {
    @Autowired
    PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    /**
     * POST /api/player/create
     * 創建一個新玩家。
     * 請求體範例: {}
     * 響應範例: 新創建的 Player 物件
     */
    @PostMapping("/create")
    public ResponseEntity<Player> createPlayer(@RequestBody Map<String, Object> body) {
        Player newPlayer = playerService.createPlayer();
        if (newPlayer != null) {
            return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
}
