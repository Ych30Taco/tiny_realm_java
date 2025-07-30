package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Item;
import com.taco.TinyRealm.model.Resource;
import com.taco.TinyRealm.model.Trade;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TradeService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private EventService eventService;

    public Trade createTrade(String initiatorId, String receiverId, Resource offeredResources, List<Item> offeredItems,
                            Resource requestedResources, List<Item> requestedItems) throws IOException {
        GameState initiatorState = storageService.loadGameState(initiatorId, false);
        GameState receiverState = storageService.loadGameState(receiverId, false);
        if (initiatorState == null || receiverState == null) throw new IllegalArgumentException("Player not found");
        Resource initiatorResources = initiatorState.getResources();
        if (initiatorResources == null || (offeredResources.getGold() > 0 && initiatorResources.getGold() < offeredResources.getGold()) ||
                (offeredResources.getWood() > 0 && initiatorResources.getWood() < offeredResources.getWood()))
            throw new IllegalArgumentException("Insufficient resources for initiator");
        if (offeredItems == null) offeredItems = new ArrayList<>();
        if (requestedItems == null) requestedItems = new ArrayList<>();
        for (Item offeredItem : offeredItems) {
            Item item = initiatorState.getInventory().stream()
                    .filter(i -> i.getType().equals(offeredItem.getType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Offered item not found"));
            if (item.getQuantity() < offeredItem.getQuantity())
                throw new IllegalArgumentException("Insufficient item quantity for initiator");
        }
        Trade trade = new Trade();
        trade.setId(UUID.randomUUID().toString());
        trade.setInitiatorId(initiatorId);
        trade.setReceiverId(receiverId);
        trade.setOfferedResources(offeredResources);
        trade.setOfferedItems(offeredItems);
        trade.setRequestedResources(requestedResources);
        trade.setRequestedItems(requestedItems);
        trade.setStatus("PENDING");
        initiatorState.getTrades().add(trade);
        receiverState.getTrades().add(trade);
        storageService.saveGameState(initiatorId, initiatorState, false);
        storageService.saveGameState(receiverId, receiverState, false);
        eventService.addEvent(initiatorId, "trade_created", "Trade proposed to " + receiverId,false);
        eventService.addEvent(receiverId, "trade_received", "Trade received from " + initiatorId,false);
        return trade;
    }

    public Trade acceptTrade(String playerId, String tradeId) throws IOException {
        GameState receiverState = storageService.loadGameState(playerId, false);
        if (receiverState == null) throw new IllegalArgumentException("Player not found");
        Trade trade = receiverState.getTrades().stream()
                .filter(t -> t.getId().equals(tradeId) && t.getReceiverId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Trade not found or not authorized"));
        if (!"PENDING".equals(trade.getStatus())) throw new IllegalArgumentException("Trade is not pending");
        String initiatorId = trade.getInitiatorId();
        GameState initiatorState = storageService.loadGameState(initiatorId, false);
        if (initiatorState == null) throw new IllegalArgumentException("Initiator not found");
        Resource receiverResources = receiverState.getResources();
        if (receiverResources == null || (trade.getRequestedResources().getGold() > 0 && receiverResources.getGold() < trade.getRequestedResources().getGold()) ||
                (trade.getRequestedResources().getWood() > 0 && receiverResources.getWood() < trade.getRequestedResources().getWood()))
            throw new IllegalArgumentException("Insufficient resources for receiver");
        for (Item requestedItem : trade.getRequestedItems()) {
            Item item = receiverState.getInventory().stream()
                    .filter(i -> i.getType().equals(requestedItem.getType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Requested item not found"));
            if (item.getQuantity() < requestedItem.getQuantity())
                throw new IllegalArgumentException("Insufficient item quantity for receiver");
        }
        resourceService.addResources(initiatorId, -trade.getOfferedResources().getGold(), -trade.getOfferedResources().getWood(),false);
        resourceService.addResources(playerId, trade.getOfferedResources().getGold(), trade.getOfferedResources().getWood(),false);
        resourceService.addResources(playerId, -trade.getRequestedResources().getGold(), -trade.getRequestedResources().getWood(),false);
        resourceService.addResources(initiatorId, trade.getRequestedResources().getGold(), trade.getRequestedResources().getWood(),false);
        for (Item offeredItem : trade.getOfferedItems()) {
            inventoryService.removeItem(initiatorId, offeredItem.getId(), offeredItem.getQuantity(),false);
            inventoryService.addItem(playerId, offeredItem.getType(), offeredItem.getQuantity(),false);
        }
        for (Item requestedItem : trade.getRequestedItems()) {
            inventoryService.removeItem(playerId, requestedItem.getId(), requestedItem.getQuantity(),false);
            inventoryService.addItem(initiatorId, requestedItem.getType(), requestedItem.getQuantity(),false);
        }
        trade.setStatus("ACCEPTED");
        initiatorState.getTrades().stream()
                .filter(t -> t.getId().equals(tradeId))
                .findFirst()
                .ifPresent(t -> t.setStatus("ACCEPTED"));
        receiverState.getTrades().stream()
                .filter(t -> t.getId().equals(tradeId))
                .findFirst()
                .ifPresent(t -> t.setStatus("ACCEPTED"));
        storageService.saveGameState(initiatorId, initiatorState, false);
        storageService.saveGameState(playerId, receiverState, false);
        eventService.addEvent(initiatorId, "trade_accepted", "Trade with " + playerId + " accepted",false);
        eventService.addEvent(playerId, "trade_accepted", "Trade with " + initiatorId + " accepted",false);
        return trade;
    }

    public Trade rejectTrade(String playerId, String tradeId) throws IOException {
        GameState receiverState = storageService.loadGameState(playerId, false);
        if (receiverState == null) throw new IllegalArgumentException("Player not found");
        Trade trade = receiverState.getTrades().stream()
                .filter(t -> t.getId().equals(tradeId) && t.getReceiverId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Trade not found or not authorized"));
        if (!"PENDING".equals(trade.getStatus())) throw new IllegalArgumentException("Trade is not pending");
        String initiatorId = trade.getInitiatorId();
        GameState initiatorState = storageService.loadGameState(initiatorId, false);
        if (initiatorState == null) throw new IllegalArgumentException("Initiator not found");
        trade.setStatus("REJECTED");
        initiatorState.getTrades().stream()
                .filter(t -> t.getId().equals(tradeId))
                .findFirst()
                .ifPresent(t -> t.setStatus("REJECTED"));
        receiverState.getTrades().stream()
                .filter(t -> t.getId().equals(tradeId))
                .findFirst()
                .ifPresent(t -> t.setStatus("REJECTED"));
        storageService.saveGameState(initiatorId, initiatorState, false);
        storageService.saveGameState(playerId, receiverState, false);
        eventService.addEvent(initiatorId, "trade_rejected", "Trade with " + playerId + " rejected",false);
        eventService.addEvent(playerId, "trade_rejected", "Trade with " + initiatorId + " rejected",false);
        return trade;
    }

    public List<Trade> getTrades(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getTrades();
    }
}
