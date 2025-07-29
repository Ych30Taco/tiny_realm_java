package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TestService {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TerrainService terrainService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private BattleService battleService;
    @Autowired
    private TechnologyService technologyService;
    @Autowired
    private AllianceService allianceService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public String initializeTestData(String playerId, int playerCount) throws IOException {
        if (playerCount > 10) throw new IllegalArgumentException("Player count cannot exceed 10");
        clearTestData();
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            playerService.createPlayer(pid, "TestPlayer" + i, true);
            resourceService.addResources(pid, 1000, 500, true);
            inventoryService.addItem(pid, "sword", 2, true);
            result.append("Created player: ").append(pid).append("\n");
        }
        Alliance alliance = allianceService.createAlliance(playerId + "1", "TestAlliance", true);
        result.append("Created alliance: ").append(alliance.getId()).append("\n");
        MarketListing listing = marketService.listResource(playerId + "1", 100, 50, new Resource(), true);
        result.append("Listed resource in market: ").append(listing.getId()).append("\n");
        messagingTemplate.convertAndSend("/topic/test", "Initialized test data for " + playerCount + " players");
        return result.toString();
    }

    public String runScenario(String scenario, String playerId, int playerCount) throws IOException {
        if (playerCount > 10) throw new IllegalArgumentException("Player count cannot exceed 10");
        StringBuilder result = new StringBuilder();
        switch (scenario) {
            case "single_player":
                result.append(runSinglePlayerScenario(playerId));
                break;
            case "multi_alliance":
                result.append(runMultiAllianceScenario(playerId, playerCount));
                break;
            case "market_trade":
                result.append(runMarketTradeScenario(playerId, playerCount));
                break;
            case "alliance_war_prep":
                result.append(runAllianceWarPrepScenario(playerId, playerCount));
                break;
            case "task_tech_progress":
                result.append(runTaskTechProgressScenario(playerId, playerCount));
                break;
            default:
                result.append("Unknown scenario: ").append(scenario);
        }
        messagingTemplate.convertAndSend("/topic/test", "Ran scenario: " + scenario);
        return result.toString();
    }

    private String runSinglePlayerScenario(String playerId) throws IOException {
        StringBuilder result = new StringBuilder();
        playerService.createPlayer(playerId, "TestPlayer", true);
        result.append("Created player: ").append(playerId).append("\n");
        resourceService.addResources(playerId, 1000, 500, true);
        result.append("Added resources: 1000 gold, 500 wood\n");
        buildingService.createBuilding(playerId, "laboratory", 0, 0, true);
        technologyService.researchTechnology(playerId, "basic_military", true);
        buildingService.createBuilding(playerId, "barracks", 1, 1, true);
        result.append("Built laboratory and barracks\n");
        Unit unit = unitService.createUnit(playerId, "soldier", 10, 2, 2, true);
        result.append("Created unit: ").append(unit.getId()).append("\n");
        Resource rewards = new Resource();
        rewards.setGold(50); rewards.setWood(20);
        Task task = taskService.assignTask(playerId, "defeat_bandit", "Defeat a bandit", 1, rewards, true);
        result.append("Assigned task: ").append(task.getId()).append("\n");
        List<String> unitIds = new ArrayList<>();
        unitIds.add(unit.getId());
        Battle battle = battleService.startBattle(playerId, unitIds, "bandit", true);
        result.append("Battle result: ").append(battle.getResult()).append("\n");
        return result.toString();
    }

    private String runMultiAllianceScenario(String playerId, int playerCount) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            playerService.createPlayer(pid, "TestPlayer" + i, true);
            resourceService.addResources(pid, 1000, 500, true);
            result.append("Created player: ").append(pid).append("\n");
        }
        Alliance alliance = allianceService.createAlliance(playerId + "1", "TestAlliance", true);
        result.append("Created alliance: ").append(alliance.getId()).append("\n");
        for (int i = 2; i <= playerCount; i++) {
            allianceService.joinAlliance(playerId + i, alliance.getId(), true);
            result.append("Player ").append(playerId + i).append(" joined alliance\n");
        }
        allianceService.donateResources(playerId + "1", alliance.getId(), 100, 50, true);
        result.append("Player ").append(playerId + "1 donated resources\n");
        allianceService.sendAllianceMessage(playerId + "1", alliance.getId(), "Hello team!", true);
        result.append("Sent alliance message\n");
        return result.toString();
    }

    private String runMarketTradeScenario(String playerId, int playerCount) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            playerService.createPlayer(pid, "TestPlayer" + i, true);
            resourceService.addResources(pid, 1000, 500, true);
            if (i == 1) inventoryService.addItem(pid, "sword", 2, true);
            result.append("Created player: ").append(pid).append("\n");
        }
        MarketListing resourceListing = marketService.listResource(playerId + "1", 100, 50, new Resource(), true);
        result.append("Listed resource: ").append(resourceListing.getId()).append("\n");
        String itemId = storageService.loadGameState(playerId + "1", true).getInventory().get(0).getId();
        MarketListing itemListing = marketService.listItem(playerId + "1", itemId, 1, new Resource(), true);
        result.append("Listed item: ").append(itemListing.getId()).append("\n");
        for (int i = 2; i <= Math.min(playerCount, 3); i++) {
            marketService.buyListing(playerId + i, resourceListing.getId(), true);
            marketService.buyListing(playerId + i, itemListing.getId(), true);
            result.append("Player ").append(playerId + i).append(" bought resource and item\n");
        }
        return result.toString();
    }

    private String runAllianceWarPrepScenario(String playerId, int playerCount) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            playerService.createPlayer(pid, "TestPlayer" + i, true);
            resourceService.addResources(pid, 1000, 500, true);
            buildingService.createBuilding(pid, "laboratory", 0, 0, true);
            technologyService.researchTechnology(pid, "basic_military", true);
            buildingService.createBuilding(pid, "barracks", i, i, true);
            unitService.createUnit(pid, "soldier", 10, i + 1, i + 1, true);
            result.append("Created player ").append(pid).append(" with barracks and units\n");
        }
        Alliance alliance1 = allianceService.createAlliance(playerId + "1", "Alliance1", true);
        Alliance alliance2 = allianceService.createAlliance(playerId + "6", "Alliance2", true);
        result.append("Created alliances: ").append(alliance1.getId()).append(", ").append(alliance2.getId()).append("\n");
        for (int i = 2; i <= 5; i++) {
            allianceService.joinAlliance(playerId + i, alliance1.getId(), true);
            result.append("Player ").append(playerId + i).append(" joined Alliance1\n");
        }
        for (int i = 7; i <= playerCount; i++) {
            allianceService.joinAlliance(playerId + i, alliance2.getId(), true);
            result.append("Player ").append(playerId + i).append(" joined Alliance2\n");
        }
        return result.toString();
    }

    private String runTaskTechProgressScenario(String playerId, int playerCount) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            playerService.createPlayer(pid, "TestPlayer" + i, true);
            resourceService.addResources(pid, 1000, 500, true);
            buildingService.createBuilding(pid, "laboratory", 0, 0, true);
            technologyService.researchTechnology(pid, "basic_military", true);
            technologyService.researchTechnology(pid, "archery", true);
            buildingService.createBuilding(pid, "barracks", i, i, true);
            Resource rewards = new Resource();
            rewards.setGold(50); rewards.setWood(20);
            Task task = taskService.assignTask(pid, "build_barracks", "Build a barracks", 1, rewards, true);
            taskService.updateTaskProgress(pid, task.getId(), 1, true);
            result.append("Player ").append(pid).append(" researched tech and completed task\n");
        }
        return result.toString();
    }

    public String batchApiTest(String api, String playerId, int playerCount, String params) throws IOException {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            try {
                switch (api) {
                    case "add_resources":
                        resourceService.addResources(pid, 100, 50, true);
                        result.append("Added resources for ").append(pid).append("\n");
                        break;
                    case "create_building":
                        buildingService.createBuilding(pid, "barracks", i, i, true);
                        result.append("Created building for ").append(pid).append("\n");
                        break;
                    default:
                        result.append("Unknown API: ").append(api).append("\n");
                }
            } catch (Exception e) {
                result.append("Error for ").append(pid).append(": ").append(e.getMessage()).append("\n");
            }
        }
        messagingTemplate.convertAndSend("/topic/test", "Batch API test: " + api);
        return result.toString();
    }

    public String stressTest(String api, String playerId, int playerCount) throws InterruptedException {
        StringBuilder result = new StringBuilder();
        ExecutorService executor = Executors.newFixedThreadPool(playerCount);
        long startTime = System.currentTimeMillis();
        int[] failCount = {0};
        for (int i = 1; i <= playerCount; i++) {
            String pid = playerId + i;
            executor.submit(() -> {
                try {
                    switch (api) {
                        case "battle":
                            List<String> unitIds = new ArrayList<>();
                            unitIds.add(UUID.randomUUID().toString());
                            try {
                                battleService.startBattle(pid, unitIds, "bandit", true);
                            } catch (Exception e) { synchronized (failCount) { failCount[0]++; } }
                            break;
                        case "market_buy":
                            try {
                                List<MarketListing> listings = marketService.getMarketListings(true);
                                if (!listings.isEmpty()) {
                                    marketService.buyListing(pid, listings.get(0).getId(), true);
                                }
                            } catch (Exception e) { synchronized (failCount) { failCount[0]++; } }
                            break;
                    }
                } catch (Exception e) {
                    synchronized (result) {
                        result.append("Stress test error for ").append(pid).append(": ").append(e.getMessage()).append("\n");
                    }
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.currentTimeMillis();
        result.append("Stress test completed in ").append(endTime - startTime).append("ms\n");
        result.append("Failure: ").append(failCount[0]).append("\n");
        messagingTemplate.convertAndSend("/topic/test", "Stress test: " + api);
        return result.toString();
    }

    public void clearTestData() throws IOException {
        storageService.clearTestData();
        messagingTemplate.convertAndSend("/topic/test", "Cleared test data");
    }
}
