package com.taco.TinyRealm.module.EventSystemModule.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * 遊戲事件資料模型，封裝所有需傳遞的事件資訊。
 */
public class GameEvent implements Serializable {
    /** 事件唯一識別碼 */
    private String eventId;
    /** 事件類型 */
    private EventType eventType;
    /** 玩家ID（可選） */
    private String playerId;
    /** 房間ID（可選） */
    private String roomId;
    /** 事件發生時間 */
    private Instant timestamp;
    /** 事件附加資料 */
    private Map<String, Object> payload;

    /**
     * 建構子，初始化遊戲事件。
     *
     * @param eventId 事件唯一識別碼
     * @param eventType 事件類型
     * @param playerId 玩家ID
     * @param roomId 房間ID
     * @param timestamp 事件發生時間
     * @param payload 事件附加資料
     */
    public GameEvent(String eventId, EventType eventType, String playerId, String roomId, Instant timestamp, Map<String, Object> payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.playerId = playerId;
        this.roomId = roomId;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    // getter 和 setter 方法省略，便於閱讀
}