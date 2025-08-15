/* package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Item;
import com.taco.TinyRealm.model.MarketListing;
import com.taco.TinyRealm.module.ResourceModule.model.Resource;
import com.taco.TinyRealm.module.ResourceModule.service.ResourceService;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MarketService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private EventService eventService;

    public MarketListing listResource(String playerId, int gold, int wood, Resource price, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        Resource playerResources = gameState.getResources();
        if (playerResources.getGold() < gold || playerResources.getWood() < wood)
            throw new IllegalArgumentException("Insufficient resources");

        resourceService.addResources(playerId, -gold, -wood, isTest);

        MarketListing listing = new MarketListing();
        listing.setId(UUID.randomUUID().toString());
        listing.setSellerId(playerId);
        listing.setType("resource");
        Resource res = new Resource();
        res.setGold(gold);
        res.setWood(wood);
        listing.setResource(res);
        listing.setPrice(price);
        listing.setStatus("ACTIVE");

        List<MarketListing> market = storageService.loadMarket(isTest);
        if (market == null) market = new ArrayList<>();
        market.add(listing);
        storageService.saveMarket(market, isTest);

        eventService.addEvent(playerId, "market_listed", "Listed resources: " + gold + " gold, " + wood + " wood",false);
        return listing;
    }

    public MarketListing listItem(String playerId, String itemId, int quantity, Resource price, boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        Item item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (item.getQuantity() < quantity) throw new IllegalArgumentException("Insufficient item quantity");

        inventoryService.removeItem(playerId, itemId, quantity, isTest);

        MarketListing listing = new MarketListing();
        listing.setId(UUID.randomUUID().toString());
        listing.setSellerId(playerId);
        listing.setType("item");
        Item listedItem = new Item();
        listedItem.setId(itemId);
        listedItem.setType(item.getType());
        listedItem.setQuantity(quantity);
        listing.setItem(listedItem);
        listing.setPrice(price);
        listing.setStatus("ACTIVE");

        List<MarketListing> market = storageService.loadMarket(isTest);
        if (market == null) market = new ArrayList<>();
        market.add(listing);
        storageService.saveMarket(market, isTest);

        eventService.addEvent(playerId, "market_listed", "Listed item: " + quantity + " " + item.getType(),false);
        return listing;
    }

    public MarketListing buyListing(String buyerId, String listingId, boolean isTest) throws IOException {
        GameState buyerState = storageService.loadGameState(buyerId, isTest);
        if (buyerState == null) throw new IllegalArgumentException("Buyer not found");

        List<MarketListing> market = storageService.loadMarket(isTest);
        if (market == null) throw new IllegalArgumentException("Market is empty");

        MarketListing listing = market.stream()
                .filter(l -> l.getId().equals(listingId) && "ACTIVE".equals(l.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Listing not found or sold"));

        if (listing.getSellerId().equals(buyerId)) throw new IllegalArgumentException("Cannot buy own listing");

        Resource buyerResources = buyerState.getResources();
        if (buyerResources.getGold() < listing.getPrice().getGold() || buyerResources.getWood() < listing.getPrice().getWood())
            throw new IllegalArgumentException("Insufficient resources to buy");

        GameState sellerState = storageService.loadGameState(listing.getSellerId(), isTest);
        if (sellerState == null) throw new IllegalArgumentException("Seller not found");

        resourceService.addResources(buyerId, -listing.getPrice().getGold(), -listing.getPrice().getWood(), isTest);
        resourceService.addResources(listing.getSellerId(), listing.getPrice().getGold(), listing.getPrice().getWood(), isTest);

        if ("resource".equals(listing.getType())) {
            resourceService.addResources(buyerId, listing.getResource().getGold(), listing.getResource().getWood(),false);
        } else if ("item".equals(listing.getType())) {
            inventoryService.addItem(buyerId, listing.getItem().getType(), listing.getItem().getQuantity(),false);
        }

        listing.setStatus("SOLD");
        storageService.saveMarket(market, isTest);
        storageService.saveGameState(buyerId, buyerState, isTest);
        storageService.saveGameState(listing.getSellerId(), sellerState, isTest);

        eventService.addEvent(buyerId, "market_bought", "Bought " + listing.getType() + " from " + listing.getSellerId(),false);
        eventService.addEvent(listing.getSellerId(), "market_sold", "Sold " + listing.getType() + " to " + buyerId,false);
        return listing;
    }

    public List<MarketListing> getMarketListings(boolean isTest) throws IOException {
        List<MarketListing> market = storageService.loadMarket(isTest);
        if (market == null) return new ArrayList<>();
        return market;
    }
}
 */