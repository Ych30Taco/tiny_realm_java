package com.taco.TinyRealm.module.storageModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taco.TinyRealm.model.Battle;
import com.taco.TinyRealm.model.GameEvent;
import com.taco.TinyRealm.model.Item;
import com.taco.TinyRealm.model.Task;
import com.taco.TinyRealm.model.Technology;
import com.taco.TinyRealm.model.Trade;
import com.taco.TinyRealm.model.Unit;
import com.taco.TinyRealm.module.buildingModule.model.PlayerBuliding;
import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.terrainModule.model.Terrain;

import java.util.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * 遊戲狀態主物件
 * 
 * 這是整個遊戲的核心數據模型，儲存單一玩家的所有遊戲資料。
 * 包含玩家信息、資源、建築、單位、任務等所有遊戲元素。
 * 
 * 數據結構：
 * - 玩家基本信息（等級、經驗、狀態等）
 * - 資源數據（當前數量、儲存上限、生產速率）
 * - 建築數據（建築列表、等級、狀態）
 * - 遊戲內容（單位、任務、科技、事件等）
 * 
 * 持久化：
 * - 每個玩家的GameState存儲為獨立的JSON文件
 * - 文件名格式：{playerId}.json 或 test_{playerId}.json
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
public class GameState {
    
    /** 玩家基本信息 */
    @JsonProperty("player")
    private Player player;
    
    /** 玩家資源數據 */
    @JsonProperty("resources")
    private PlayerResource resources;
    
    /** 玩家建築列表 */
    @JsonProperty("buildings")
    private Map<String, PlayerBuliding> buildings;
    
    /** 玩家物品背包 */
    @JsonProperty("inventory")
    private List<Item> inventory = new ArrayList<>();
    
    /** 玩家事件紀錄 */
    @JsonProperty("events")
    private List<GameEvent> events = new ArrayList<>();
    
    /** 玩家地形資訊 */
    @JsonProperty("terrains")
    private List<Terrain> terrains = new ArrayList<>();
    
    /** 玩家單位（兵種）列表 */
    @JsonProperty("units")
    private List<Unit> units = new ArrayList<>();
    
    /** 玩家交易紀錄 */
    @JsonProperty("trades")
    private List<Trade> trades = new ArrayList<>();
    
    /** 遊戲版本 */
    @JsonProperty("version")
    private String version = "1.0";
    
    /** 玩家戰鬥紀錄 */
    @JsonProperty("battles")
    private List<Battle> battles = new ArrayList<>();
    
    /** 玩家已研發科技 */
    @JsonProperty("technologies")
    private List<Technology> technologies = new ArrayList<>();
    
    /** 玩家所屬聯盟 ID */
    @JsonProperty("allianceId")
    private String allianceId;
    
    /** 玩家任務清單 */
    @JsonProperty("tasks")
    private List<Task> tasks = new ArrayList<>();

    /** 取得玩家資訊 */
    public Player getPlayer() { return player; }
    /** 設定玩家資訊 */
    public void setPlayer(Player player) { this.player = player; }
    /** 取得玩家資源 */
    public PlayerResource getResources() { return resources; }
    /** 設定玩家資源 */
    public void setResources(PlayerResource resources) { this.resources = resources; }
    /** 取得遊戲版本 */
    public String getVersion() { return version; }
    /** 設定遊戲版本 */
    public void setVersion(String version) { this.version = version; }
    /** 取得建築列表 */
    public Map<String, PlayerBuliding> getBuildings() { return buildings; }
    /** 設定建築列表 */
    public void setBuildings(Map<String, PlayerBuliding> buildings) { this.buildings = buildings; }
    /** 取得背包物品 */
    public List<Item> getInventory() { return inventory; }
    /** 設定背包物品 */
    public void setInventory(List<Item> inventory) { this.inventory = inventory; }
    /** 取得事件紀錄 */
    public List<GameEvent> getEvents() { return events; }
    /** 設定事件紀錄 */
    public void setEvents(List<GameEvent> events) { this.events = events; }
    /** 取得地形資訊 */
    public List<Terrain> getTerrains() { return terrains; }
    /** 設定地形資訊 */
    public void setTerrains(List<Terrain> terrains) { this.terrains = terrains; }
    /** 取得單位列表 */
    public List<Unit> getUnits() { return units; }
    /** 設定單位列表 */
    public void setUnits(List<Unit> units) { this.units = units; }
    /** 取得交易紀錄 */
    public List<Trade> getTrades() { return trades; }
    /** 設定交易紀錄 */
    public void setTrades(List<Trade> trades) { this.trades = trades; }
    /** 取得戰鬥紀錄 */
    public List<Battle> getBattles() { return battles; }
    /** 設定戰鬥紀錄 */
    public void setBattles(List<Battle> battles) { this.battles = battles; }
    /** 取得科技列表 */
    public List<Technology> getTechnologies() { return technologies; }
    /** 設定科技列表 */
    public void setTechnologies(List<Technology> technologies) { this.technologies = technologies; }
    /** 取得聯盟 ID */
    public String getAllianceId() { return allianceId; }
    /** 設定聯盟 ID */
    public void setAllianceId(String allianceId) { this.allianceId = allianceId; }
    /** 取得任務清單 */
    public List<Task> getTasks() { return tasks; }
    /** 設定任務清單 */
    public void setTasks(List<Task> tasks) { this.tasks = tasks; }
}
