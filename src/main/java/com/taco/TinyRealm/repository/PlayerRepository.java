package com.taco.TinyRealm.repository;

import com.taco.TinyRealm.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
