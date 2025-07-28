package com.taco.TinyRealm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.model.GameState;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class StorageService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String storagePath = "game_data/";

    public void saveGameState(String playerId, GameState gameState) throws IOException {
        File dir = new File(storagePath);
        if (!dir.exists()) dir.mkdirs();
        objectMapper.writeValue(new File(storagePath + playerId + ".json"), gameState);
    }

    public GameState loadGameState(String playerId) throws IOException {
        File file = new File(storagePath + playerId + ".json");
        if (!file.exists()) return null;
        return objectMapper.readValue(file, GameState.class);
    }
}
