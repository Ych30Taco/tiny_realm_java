package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Player;
import com.taco.TinyRealm.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        Optional<Player> player = playerRepository.findById(id);
        return player.orElse(null);
    }

    @PostMapping("/update")
    public Player updatePlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }
}
