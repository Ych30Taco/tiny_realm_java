package com.taco.TinyRealm.module.playerModule.repository;

import com.taco.TinyRealm.module.playerModule.model.Player;
import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findById(String playerId);
    Player save(Player player); // 儲存或更新玩家數據
    boolean existsById(String playerId);
    // ... 未來如果需要，可以增加 deleteById 等方法
}
