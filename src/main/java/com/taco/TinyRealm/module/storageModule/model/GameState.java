package com.taco.TinyRealm.module.storageModule.model;

import com.taco.TinyRealm.module.battleModule.model.Battle;
import com.taco.TinyRealm.model.GameEvent;
import com.taco.TinyRealm.module.inventoryModule.model.PlayerItem;
import com.taco.TinyRealm.model.Task;
import com.taco.TinyRealm.model.Technology;
import com.taco.TinyRealm.model.Trade;
import com.taco.TinyRealm.module.buildingModule.model.PlayerBuliding;
import com.taco.TinyRealm.module.playerModule.model.Player;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.soldierModule.model.PlayerSoldier;
import com.taco.TinyRealm.module.terrainMapModule.model.Terrain;

import java.util.Map;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private Player player = new Player();

    /** 玩家資源數據 */
    private PlayerResource resources = new PlayerResource();

    /** 玩家建築列表 */
    private Map<String, PlayerBuliding> buildings = new java.util.HashMap<>();

    /** 玩家物品背包 */
    private List<PlayerItem> inventory = new ArrayList<>();
    /** 玩家事件紀錄 */
    private List<GameEvent> events = new ArrayList<>();
    /** 玩家地形資訊 */
    private List<Terrain> terrains = new ArrayList<>();
    
    /** 玩家單位（兵種）列表 */
    private Map<String,PlayerSoldier> soldiers = new java.util.HashMap<>();
    
    /** 玩家交易紀錄 */
    private List<Trade> trades = new ArrayList<>();
    
    /** 遊戲版本 */
    private String version = "1.0";
    
    /** 玩家戰鬥紀錄 */
    private List<Battle> battles = new ArrayList<>();
    
    /** 玩家已研發科技 */
    private List<Technology> technologies = new ArrayList<>();
    
    /** 玩家所屬聯盟 ID */
    private String allianceId = "";
    
    /** 玩家任務清單 */
    private List<Task> tasks = new ArrayList<>();
}
