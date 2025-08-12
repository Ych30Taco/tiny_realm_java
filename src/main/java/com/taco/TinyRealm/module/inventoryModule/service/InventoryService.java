package com.taco.TinyRealm.module.inventoryModule.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.inventoryModule.model.PlayerItem;
import com.taco.TinyRealm.module.inventoryModule.model.ItemType;
import com.taco.TinyRealm.module.soldierModule.model.SoldierType;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 背包服務
 * 處理玩家背包的所有相關操作
 */
@Service
public class InventoryService {
    @Autowired
    private StorageService storageService;
        
    @Value("${app.data.item-path}")
    private org.springframework.core.io.Resource itemPath;
    
    private List<ItemType> itemTypeList = Collections.emptyList();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 初始化服務，載入物品類型配置
     */
    @PostConstruct
    public void init() {
        System.out.println("---- 應用程式啟動中，載入背包模組 ----");
        try {
            try (InputStream is = itemPath.getInputStream()) {
                itemTypeList = objectMapper.readValue(is, new TypeReference<List<ItemType>>() {});
                String itemNames = getItemName();
                System.out.println("---- 應用程式啟動中，已載入" + itemNames + " ----");

            }
        } catch (Exception e) {
            System.out.println("---- 應用程式啟動中，載入背包模組失敗 ----");
            e.printStackTrace(); // 印出詳細錯誤
            throw new RuntimeException("Failed to load items.json: " + e.getMessage(), e);
        }
        System.out.println("---- 應用程式啟動中，載入背包模組完成 ----");
    }
    public String getItemName() {
        String names = "";
        for (ItemType type : itemTypeList) {
            names += type.getName() + ", ";
        }
        names+= "共"+itemTypeList.size()+"種物品";
        return names.length() > 0 ? names : "";
    }
    /**
     * 獲取所有物品類型
     */
    public List<ItemType> getAllItemTypes() {
        return new ArrayList<>(itemTypeList);
    }
    
    /**
     * 獲取特定物品類型
     */
    public ItemType getItemTypeByType(String itemType) {
        return itemTypeList.stream()
        .filter(r -> r.getType().equals(itemType))
        .findFirst()
        .orElse(null);
    }
    /*public void reloadItemTypes(String overridePath) throws IOException {
        String path = (overridePath != null && !overridePath.isBlank()) ? overridePath : itemPath;
        loadItemTypes(path);
    }*/

    /**
     * 創建物品類型
     */
    private PlayerItem createItemFromType(ItemType itemType, int quantity) {
        PlayerItem item = new PlayerItem();
        item.setId(UUID.randomUUID().toString());
        item.setType(itemType.getType());
        item.setName(itemType.getName());
        item.setDescription(itemType.getDescription());
        item.setQuantity(quantity);
        item.setRarity(itemType.getRarity());
        item.setCategory(itemType.getCategory());
        item.setLevel(itemType.getLevel());
        item.setDurability(itemType.getMaxDurability());
        item.setAttributes(itemType.getAttributes() != null ? new HashMap<>(itemType.getAttributes()) : new HashMap<>());
        item.setStackable(itemType.isStackable());
        item.setTradeable(itemType.isTradeable());
        item.setDroppable(itemType.isDroppable());
        item.setConsumable(itemType.isConsumable());
        item.setEquippable(itemType.isEquippable());
        item.setEquipmentSlot(itemType.getEquipmentSlot());
        item.setSellPrice(itemType.getSellPrice());
        item.setBuyPrice(itemType.getBuyPrice());
        return item;
    }
    

    
    /**
     * 添加物品到背包
     */
    public GameState addItem(String playerId, String itemType, int quantity, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (itemType == null || itemType.trim().isEmpty()) {
            throw new IllegalArgumentException("Item type cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        ItemType Type = getItemTypeByType(itemType);
        if (Type == null) {
            throw new IllegalArgumentException("Unknown item type: " + itemType);
        }

        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        if (gameState.getInventory() == null) {
            gameState.setInventory(new ArrayList<>());
        }
        
        PlayerItem item;
        if (Type.isStackable()) {
            item = gameState.getInventory().stream()
                    .filter(i -> i.getType().equals(itemType))
                    .findFirst()
                    .orElse(null);
            if (item != null) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity > Type.getMaxQuantity()) {
                    throw new IllegalArgumentException("Cannot add more items: exceeds max quantity");
                }
                item.setQuantity(newQuantity);
                storageService.saveGameState(playerId, gameState, "堆疊到現有物品", isTest);
                return gameState;
            }
        }

        // 創建新物品
        item = createItemFromType(Type, quantity);
        gameState.getInventory().add(item);
        storageService.saveGameState(playerId, gameState, "創建新物品", isTest);
        return gameState;
    }
    
    /**
     * 從背包移除物品
     */
    public GameState removeItem(String playerId, String playerItemId, int quantity, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (playerItemId == null || playerItemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        if (gameState.getInventory() == null) {
            throw new IllegalArgumentException("Inventory is empty");
        }
        
        PlayerItem item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(playerItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + playerItemId));
        
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient item quantity");
        }
        
        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) {
            gameState.getInventory().remove(item);
        }
        storageService.saveGameState(playerId, gameState,"從背包移除物品", isTest);
        return gameState;
    }
    
    /**
     * 獲取玩家背包
     */
    /*public List<Item> getInventory(String playerId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        return gameState.getInventory() != null ? gameState.getInventory() : new ArrayList<>();
    }*/
    
    /**
     * 使用物品
     */
    public Map<String, Object> useItem(String playerId, String playerItemId,int quantity, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (playerItemId == null || playerItemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        
        GameState gameState = storageService.getGameStateListById(playerId);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        PlayerItem item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(playerItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + playerItemId));
        ItemType itemType = getItemTypeByType(item.getType());
        
        if (itemType == null) {
            throw new IllegalArgumentException("Unknown item type: " + item.getType());
        }
        
        
        // 使用效果
        Map<String, Object> effects = new HashMap<>();
        if (itemType.getUseEffect() != null) {
            effects.putAll(itemType.getUseEffect());
        }
                
        item.setQuantity(item.getQuantity() - quantity);
        // 如果物品數量為0，從背包移除
        if (item.getQuantity() == 0) {
            gameState.getInventory().remove(item);
        }
        
        // 使用效果邏輯


        //eventService.addEvent(playerId, "item_used", "Used " + item.getName(), isTest);
        storageService.saveGameState(playerId, gameState,"使用物品", isTest);
        
        return effects;
    }
    
    /**
     * 修復物品
     */
    /*public Item repairItem(String playerId, String itemId, int repairAmount, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (repairAmount <= 0) {
            throw new IllegalArgumentException("Repair amount must be positive");
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        Item item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
        
        if (item.getDurability() >= item.getMaxDurability()) {
            throw new IllegalArgumentException("Item is already at full durability");
        }
        
        item.repair(repairAmount);
        eventService.addEvent(playerId, "item_repaired", "Repaired " + item.getName(), isTest);
        storageService.saveGameState(playerId, gameState,"修復物品", isTest);
        
        return item;
    }*/
    
    /**
     * 獲取背包統計信息
     */
    /*public Map<String, Object> getInventoryStats(String playerId, boolean isTest) throws IOException {
        List<Item> inventory = getInventory(playerId, isTest);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalItems", inventory.size());
        stats.put("totalQuantity", inventory.stream().mapToInt(Item::getQuantity).sum());
        
        // 按類別統計
        Map<String, Long> categoryStats = inventory.stream()
                .collect(Collectors.groupingBy(Item::getCategory, Collectors.counting()));
        stats.put("categoryStats", categoryStats);
        
        // 按稀有度統計
        Map<String, Long> rarityStats = inventory.stream()
                .collect(Collectors.groupingBy(Item::getRarity, Collectors.counting()));
        stats.put("rarityStats", rarityStats);
        
        // 裝備統計
        long equipmentCount = inventory.stream().filter(Item::isEquipment).count();
        stats.put("equipmentCount", equipmentCount);
        
        // 消耗品統計
        long consumableCount = inventory.stream().filter(Item::isConsumable).count();
        stats.put("consumableCount", consumableCount);
        
        return stats;
    }*/
    
    /**
     * 搜索背包物品
     */
    /*public List<Item> searchInventory(String playerId, String searchTerm, String category, String rarity, boolean isTest) throws IOException {
        List<Item> inventory = getInventory(playerId, isTest);
        
        return inventory.stream()
                .filter(item -> searchTerm == null || item.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                .filter(item -> category == null || item.isCategory(category))
                .filter(item -> rarity == null || item.isRarity(rarity))
                .collect(Collectors.toList());
    }*/
}
