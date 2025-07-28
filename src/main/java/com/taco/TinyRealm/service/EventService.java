package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameEvent;
import com.taco.TinyRealm.model.GameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    @Autowired
    private StorageService storageService;
    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public void addEvent(String playerId, String type, String message) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        GameEvent event = new GameEvent();
        event.setId(UUID.randomUUID().toString());
        event.setType(type);
        event.setMessage(message);
        event.setTimestamp(System.currentTimeMillis());
        gameState.getEvents().add(event);
        storageService.saveGameState(playerId, gameState);
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/events/" + playerId, event);
        }
    }

    public List<GameEvent> getEvents(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getEvents();
    }
}
