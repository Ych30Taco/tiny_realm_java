package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Technology;
import com.taco.TinyRealm.module.ResourceModule.model.Resource;
import com.taco.TinyRealm.module.ResourceModule.service.ResourceService;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TechnologyService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EventService eventService;

    private static final int TECH_GOLD_COST = 100;
    private static final int TECH_WOOD_COST = 50;

    public Technology researchTechnology(String playerId, String type ,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        boolean hasLab = gameState.getBuildings().stream()
                .anyMatch(b -> b.getType().equals("laboratory"));
        if (!hasLab) throw new IllegalArgumentException("Laboratory required to research technology");

        Resource resources = gameState.getResources();
        if (resources == null || resources.getGold() < TECH_GOLD_COST || resources.getWood() < TECH_WOOD_COST)
            throw new IllegalArgumentException("Insufficient resources");

        Technology existingTech = gameState.getTechnologies().stream()
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElse(null);
        if (existingTech != null) throw new IllegalArgumentException("Technology already researched");

        resourceService.addResources(playerId, -TECH_GOLD_COST, -TECH_WOOD_COST, isTest);

        Technology technology = new Technology();
        technology.setId(UUID.randomUUID().toString());
        technology.setType(type);
        technology.setLevel(1);

        if (gameState.getTechnologies() == null) gameState.setTechnologies(new java.util.ArrayList<>());
        gameState.getTechnologies().add(technology);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "technology_researched", "Researched " + type ,isTest);
        return technology;
    }

    public List<Technology> getTechnologies(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getTechnologies();
    }
}
