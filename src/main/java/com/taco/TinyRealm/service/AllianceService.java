package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Alliance;
import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class AllianceService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EventService eventService;
    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public Alliance createAlliance(String playerId, String allianceName, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (gameState.getAllianceId() != null) throw new IllegalArgumentException("Player already in an alliance");

        Alliance alliance = new Alliance();
        alliance.setId(UUID.randomUUID().toString());
        alliance.setName(allianceName);
        alliance.setLeaderId(playerId);
        alliance.setLevel(1);
        alliance.setResources(new Resource());
        if (alliance.getMembers() == null) alliance.setMembers(new java.util.ArrayList<>());
        alliance.getMembers().add(playerId);

        gameState.setAllianceId(alliance.getId());
        storageService.saveGameState(playerId, gameState, isTest);
        storageService.saveAlliance(alliance.getId(), alliance, isTest);

        eventService.addEvent(playerId, "alliance_created", "Created alliance: " + allianceName, isTest);
        return alliance;
    }

    public Alliance joinAlliance(String playerId, String allianceId, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (gameState.getAllianceId() != null) throw new IllegalArgumentException("Player already in an alliance");

        Alliance alliance = storageService.loadAlliance(allianceId, isTest);
        if (alliance == null) throw new IllegalArgumentException("Alliance not found");

        if (alliance.getMembers() == null) alliance.setMembers(new java.util.ArrayList<>());
        alliance.getMembers().add(playerId);
        gameState.setAllianceId(allianceId);
        storageService.saveGameState(playerId, gameState, isTest);
        storageService.saveAlliance(allianceId, alliance, isTest);

        eventService.addEvent(playerId, "alliance_joined", "Joined alliance: " + alliance.getName(), isTest);
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/alliance/" + allianceId, "Player " + playerId + " joined alliance");
        return alliance;
    }

    public void leaveAlliance(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        String allianceId = gameState.getAllianceId();
        if (allianceId == null) throw new IllegalArgumentException("Player not in an alliance");

        Alliance alliance = storageService.loadAlliance(allianceId);
        if (alliance == null) throw new IllegalArgumentException("Alliance not found");
        if (alliance.getLeaderId().equals(playerId)) throw new IllegalArgumentException("Leader cannot leave alliance");

        alliance.getMembers().remove(playerId);
        gameState.setAllianceId(null);
        storageService.saveGameState(playerId, gameState);
        storageService.saveAlliance(allianceId, alliance);

        eventService.addEvent(playerId, "alliance_left", "Left alliance: " + alliance.getName(),false);
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/alliance/" + allianceId, "Player " + playerId + " left alliance");
    }

    public void donateResources(String playerId, String allianceId, int gold, int wood, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (!allianceId.equals(gameState.getAllianceId())) throw new IllegalArgumentException("Player not in this alliance");

        Alliance alliance = storageService.loadAlliance(allianceId, isTest);
        if (alliance == null) throw new IllegalArgumentException("Alliance not found");

        Resource playerResources = gameState.getResources();
        if (playerResources.getGold() < gold || playerResources.getWood() < wood)
            throw new IllegalArgumentException("Insufficient resources");

        resourceService.addResources(playerId, -gold, -wood, isTest);
        alliance.getResources().setGold(alliance.getResources().getGold() + gold);
        alliance.getResources().setWood(alliance.getResources().getWood() + wood);

        storageService.saveAlliance(allianceId, alliance, isTest);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "alliance_donated", "Donated " + gold + " gold and " + wood + " wood to alliance", isTest);
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/alliance/" + allianceId, "Player " + playerId + " donated resources");
    }

    public void sendAllianceMessage(String playerId, String allianceId, String message, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        if (!allianceId.equals(gameState.getAllianceId())) throw new IllegalArgumentException("Player not in this alliance");

        eventService.addEvent(playerId, "alliance_message", "Alliance message: " + message, isTest);
        if (messagingTemplate != null)
            messagingTemplate.convertAndSend("/topic/alliance/" + allianceId, "Player " + playerId + ": " + message);
    }

    public Alliance getAlliance(String allianceId, boolean isTest) throws IOException {
        Alliance alliance = storageService.loadAlliance(allianceId, isTest);
        if (alliance == null) throw new IllegalArgumentException("Alliance not found");
        return alliance;
    }
}
