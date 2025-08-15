package com.taco.TinyRealm.module.battleModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 戰鬥日誌條目模型
 * 記錄戰鬥過程中的詳細事件
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleLogEntry {
    
    /** 時間戳 */
    @JsonProperty("timestamp")
    private long timestamp;
    
    /** 回合數 */
    @JsonProperty("round")
    private int round;
    
    /** 事件類型 */
    @JsonProperty("eventType")
    private EventType eventType;
    
    /** 進攻方單位ID */
    @JsonProperty("attackerId")
    private String attackerId;
    
    /** 防守方單位ID */
    @JsonProperty("defenderId")
    private String defenderId;
    
    /** 事件描述 */
    @JsonProperty("description")
    private String description;
    
    /** 傷害值 */
    @JsonProperty("damage")
    private Integer damage;
    
    /** 是否暴擊 */
    @JsonProperty("isCritical")
    private Boolean isCritical;
    
    /** 位置信息 */
    @JsonProperty("position")
    private Position position;
    
    /** 額外數據 */
    @JsonProperty("extraData")
    private String extraData;

    /**
     * 事件類型枚舉
     */
    public enum EventType {
        ATTACK("攻擊"),
        MOVE("移動"),
        DEATH("死亡"),
        HEAL("治療"),
        SPECIAL("特殊事件"),
        TERRAIN_EFFECT("地形效果"),
        FORMATION_CHANGE("陣型變化");

        private final String displayName;

        EventType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * 位置信息內部類
     */
    public static class Position {
        private int x;
        private int y;
        
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 創建攻擊日誌
     */
    public static BattleLogEntry createAttackLog(int round, String attackerId, String defenderId, 
                                               int damage, boolean isCritical, Position position) {
        BattleLogEntry entry = new BattleLogEntry();
        entry.setTimestamp(System.currentTimeMillis());
        entry.setRound(round);
        entry.setEventType(EventType.ATTACK);
        entry.setAttackerId(attackerId);
        entry.setDefenderId(defenderId);
        entry.setDamage(damage);
        entry.setIsCritical(isCritical);
        entry.setPosition(position);
        
        String criticalText = isCritical ? "暴擊！" : "";
        entry.setDescription(String.format("單位 %s 攻擊 %s，造成 %d 點傷害 %s", 
                                         attackerId, defenderId, damage, criticalText));
        
        return entry;
    }

    /**
     * 創建移動日誌
     */
    public static BattleLogEntry createMoveLog(int round, String unitId, Position from, Position to) {
        BattleLogEntry entry = new BattleLogEntry();
        entry.setTimestamp(System.currentTimeMillis());
        entry.setRound(round);
        entry.setEventType(EventType.MOVE);
        entry.setAttackerId(unitId);
        entry.setPosition(to);
        
        entry.setDescription(String.format("單位 %s 從 (%d,%d) 移動到 (%d,%d)", 
                                         unitId, from.x, from.y, to.x, to.y));
        
        return entry;
    }

    /**
     * 創建死亡日誌
     */
    public static BattleLogEntry createDeathLog(int round, String unitId, Position position) {
        BattleLogEntry entry = new BattleLogEntry();
        entry.setTimestamp(System.currentTimeMillis());
        entry.setRound(round);
        entry.setEventType(EventType.DEATH);
        entry.setDefenderId(unitId);
        entry.setPosition(position);
        
        entry.setDescription(String.format("單位 %s 在位置 (%d,%d) 陣亡", 
                                         unitId, position.x, position.y));
        
        return entry;
    }

    /**
     * 創建地形效果日誌
     */
    public static BattleLogEntry createTerrainEffectLog(int round, String unitId, String effect, Position position) {
        BattleLogEntry entry = new BattleLogEntry();
        entry.setTimestamp(System.currentTimeMillis());
        entry.setRound(round);
        entry.setEventType(EventType.TERRAIN_EFFECT);
        entry.setAttackerId(unitId);
        entry.setPosition(position);
        entry.setExtraData(effect);
        
        entry.setDescription(String.format("單位 %s 受到地形效果：%s", unitId, effect));
        
        return entry;
    }

    /**
     * 獲取格式化的時間
     */
    public String getFormattedTime() {
        return String.format("%.2f秒", (System.currentTimeMillis() - timestamp) / 1000.0);
    }

    /**
     * 檢查是否為重要事件
     */
    public boolean isImportantEvent() {
        return eventType == EventType.DEATH || 
               (eventType == EventType.ATTACK && isCritical != null && isCritical) ||
               eventType == EventType.SPECIAL;
    }
}
