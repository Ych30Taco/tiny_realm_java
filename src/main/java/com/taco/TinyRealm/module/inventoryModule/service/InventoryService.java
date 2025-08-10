package com.taco.TinyRealm.module.inventoryModule.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taco.TinyRealm.module.inventoryModule.model.Item;
import com.taco.TinyRealm.module.inventoryModule.model.ItemType;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("classpath:config/items.json")
    private String configResourcePath;
    
    private Map<String, ItemType> itemTypes = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 初始化服務，載入物品類型配置
     */
    @PostConstruct
    public void init() {
        try {
            loadItemTypes();
            System.out.println("InventoryService initialized with " + itemTypes.size() + " item types");
        } catch (Exception e) {
            System.err.println("Failed to initialize InventoryService: " + e.getMessage());
        }
    }
    
    /**
     * 載入物品類型配置
     */
    private void loadItemTypes() throws IOException {
        try {
            String content = new String(resourceLoader.getResource(configResourcePath).getInputStream().readAllBytes());
            List<ItemType> types = objectMapper.readValue(content, new TypeReference<List<ItemType>>() {});
            itemTypes.clear();
            for (ItemType type : types) {
                itemTypes.put(type.getType(), type);
            }
        } catch (IOException e) {
            System.err.println("Failed to load item types: " + e.getMessage());
            // 創建默認物品類型
            createDefaultItemTypes();
        }
    }
    
    /**
     * 創建默認物品類型
     */
    private void createDefaultItemTypes() {
        // 武器
        ItemType sword = new ItemType();
        sword.setType("sword");
        sword.setName("鐵劍");
        sword.setDescription("鋒利的鐵製長劍");
        sword.setRarity("common");
        sword.setCategory("weapon");
        sword.setMaxQuantity(1);
        sword.setMaxDurability(100);
        sword.setLevel(1);
        sword.setIsStackable(false);
        sword.setIsTradeable(true);
        sword.setIsDroppable(true);
        sword.setIsEquippable(true);
        sword.setEquipmentSlot("weapon");
        sword.setSellPrice(50);
        sword.setBuyPrice(100);
        sword.setAttributes(Map.of("attack", 10, "speed", 1.0));
        itemTypes.put("sword", sword);
        
        // 防具
        ItemType armor = new ItemType();
        armor.setType("armor");
        armor.setName("鐵甲");
        armor.setDescription("堅固的鐵製護甲");
        armor.setRarity("common");
        armor.setCategory("armor");
        armor.setMaxQuantity(1);
        armor.setMaxDurability(100);
        armor.setLevel(1);
        armor.setIsStackable(false);
        armor.setIsTradeable(true);
        armor.setIsDroppable(true);
        armor.setIsEquippable(true);
        armor.setEquipmentSlot("armor");
        armor.setSellPrice(40);
        armor.setBuyPrice(80);
        armor.setAttributes(Map.of("defense", 8, "weight", 5));
        itemTypes.put("armor", armor);
        
        // 藥水
        ItemType potion = new ItemType();
        potion.setType("health_potion");
        potion.setName("生命藥水");
        potion.setDescription("恢復生命值的藥水");
        potion.setRarity("common");
        potion.setCategory("consumable");
        potion.setMaxQuantity(99);
        potion.setMaxDurability(1);
        potion.setLevel(1);
        potion.setIsStackable(true);
        potion.setIsTradeable(true);
        potion.setIsDroppable(true);
        potion.setIsConsumable(true);
        potion.setSellPrice(10);
        potion.setBuyPrice(20);
        potion.setUseEffect(Map.of("heal", 50));
        itemTypes.put("health_potion", potion);
        
        // 材料
        ItemType wood = new ItemType();
        wood.setType("wood");
        wood.setName("木材");
        wood.setDescription("用於建造和製作的基本材料");
        wood.setRarity("common");
        wood.setCategory("material");
        wood.setMaxQuantity(999);
        wood.setMaxDurability(1);
        wood.setLevel(1);
        wood.setIsStackable(true);
        wood.setIsTradeable(true);
        wood.setIsDroppable(true);
        wood.setSellPrice(2);
        wood.setBuyPrice(5);
        itemTypes.put("wood", wood);
    }
    
    /**
     * 獲取所有物品類型
     */
    public Map<String, ItemType> getAllItemTypes() {
        return new HashMap<>(itemTypes);
    }
    
    /**
     * 獲取特定物品類型
     */
    public ItemType getItemType(String type) {
        return itemTypes.get(type);
    }
    
    /**
     * 添加物品到背包
     */
    public Item addItem(String playerId, String type, int quantity, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Item type cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        ItemType itemType = itemTypes.get(type);
        if (itemType == null) {
            throw new IllegalArgumentException("Unknown item type: " + type);
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        if (gameState.getInventory() == null) {
            gameState.setInventory(new ArrayList<>());
        }
        
        // 檢查是否可以堆疊
        Item existingItem = null;
        if (itemType.isStackable()) {
            existingItem = gameState.getInventory().stream()
                    .filter(item -> item.getType().equals(type))
                    .findFirst()
                    .orElse(null);
        }
        
        if (existingItem != null && itemType.isStackable()) {
            // 堆疊到現有物品
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > itemType.getMaxQuantity()) {
                throw new IllegalArgumentException("Cannot add more items: exceeds max quantity");
            }
            existingItem.setQuantity(newQuantity);
            eventService.addEvent(playerId, "item_added", "Added " + quantity + " " + itemType.getName(), isTest);
            storageService.saveGameState(playerId, gameState, isTest);
            return existingItem;
        } else {
            // 創建新物品
            Item newItem = createItemFromType(itemType, quantity);
            gameState.getInventory().add(newItem);
            eventService.addEvent(playerId, "item_added", "Added " + quantity + " " + itemType.getName(), isTest);
            storageService.saveGameState(playerId, gameState, isTest);
            return newItem;
        }
    }
    
    /**
     * 從背包移除物品
     */
    public Item removeItem(String playerId, String itemId, int quantity, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        if (gameState.getInventory() == null) {
            throw new IllegalArgumentException("Inventory is empty");
        }
        
        Item item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
        
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient item quantity");
        }
        
        item.setQuantity(item.getQuantity() - quantity);
        if (item.getQuantity() == 0) {
            gameState.getInventory().remove(item);
        }
        
        eventService.addEvent(playerId, "item_removed", "Removed " + quantity + " " + item.getName(), isTest);
        storageService.saveGameState(playerId, gameState, isTest);
        return item;
    }
    
    /**
     * 獲取玩家背包
     */
    public List<Item> getInventory(String playerId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        return gameState.getInventory() != null ? gameState.getInventory() : new ArrayList<>();
    }
    
    /**
     * 使用物品
     */
    public Map<String, Object> useItem(String playerId, String itemId, boolean isTest) throws IOException {
        if (playerId == null || playerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        
        Item item = gameState.getInventory().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
        
        ItemType itemType = itemTypes.get(item.getType());
        if (itemType == null) {
            throw new IllegalArgumentException("Unknown item type: " + item.getType());
        }
        
        if (!itemType.isConsumable() && !item.isUsable()) {
            throw new IllegalArgumentException("Item cannot be used");
        }
        
        // 使用物品
        item.use();
        
        // 處理使用效果
        Map<String, Object> effects = new HashMap<>();
        if (itemType.getUseEffect() != null) {
            effects.putAll(itemType.getUseEffect());
        }
        
        // 如果物品數量為0，從背包移除
        if (item.getQuantity() == 0) {
            gameState.getInventory().remove(item);
        }
        
        eventService.addEvent(playerId, "item_used", "Used " + item.getName(), isTest);
        storageService.saveGameState(playerId, gameState, isTest);
        
        return effects;
    }
    
    /**
     * 修復物品
     */
    public Item repairItem(String playerId, String itemId, int repairAmount, boolean isTest) throws IOException {
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
        storageService.saveGameState(playerId, gameState, isTest);
        
        return item;
    }
    
    /**
     * 獲取背包統計信息
     */
    public Map<String, Object> getInventoryStats(String playerId, boolean isTest) throws IOException {
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
    }
    
    /**
     * 搜索背包物品
     */
    public List<Item> searchInventory(String playerId, String searchTerm, String category, String rarity, boolean isTest) throws IOException {
        List<Item> inventory = getInventory(playerId, isTest);
        
        return inventory.stream()
                .filter(item -> searchTerm == null || item.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                .filter(item -> category == null || item.isCategory(category))
                .filter(item -> rarity == null || item.isRarity(rarity))
                .collect(Collectors.toList());
    }
    
    /**
     * 從物品類型創建物品實例
     */
    private Item createItemFromType(ItemType itemType, int quantity) {
        Item item = new Item();
        item.setId(UUID.randomUUID().toString());
        item.setType(itemType.getType());
        item.setName(itemType.getName());
        item.setDescription(itemType.getDescription());
        item.setQuantity(quantity);
        item.setMaxQuantity(itemType.getMaxQuantity());
        item.setRarity(itemType.getRarity());
        item.setCategory(itemType.getCategory());
        item.setLevel(itemType.getLevel());
        item.setDurability(itemType.getMaxDurability());
        item.setMaxDurability(itemType.getMaxDurability());
        item.setAttributes(itemType.getAttributes() != null ? new HashMap<>(itemType.getAttributes()) : new HashMap<>());
        item.setEffects(itemType.getEffects() != null ? new HashMap<>(itemType.getEffects()) : new HashMap<>());
        item.setCreatedTime(LocalDateTime.now());
        item.setLastUsedTime(null);
        item.setIsStackable(itemType.isStackable());
        item.setIsTradeable(itemType.isTradeable());
        item.setIsDroppable(itemType.isDroppable());
        item.setIsConsumable(itemType.isConsumable());
        item.setIsEquippable(itemType.isEquippable());
        item.setEquipmentSlot(itemType.getEquipmentSlot());
        item.setSellPrice(itemType.getSellPrice());
        item.setBuyPrice(itemType.getBuyPrice());
        
        return item;
    }
}
