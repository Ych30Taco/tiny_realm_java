package com.taco.TinyRealm.module.EventSystemModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.GameEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * 事件消費者服務，負責監聽 Kafka topic 並處理收到的遊戲事件。
 */
@Service
public class EventConsumerService {
    /**
     * 監聽 game-events topic，收到事件時進行處理。
     * @param event 遊戲事件
     */
    @KafkaListener(topics = "game-events", groupId = "game-event-group")
    public void consume(GameEvent event) {
        // TODO: 根據 event.getEventType() 分派給對應模組處理
        System.out.println("Received event: event.getEventType() , player:  event.getPlayerId()");
        // ...可呼叫 SaveSystemModule 或其他模組...
    }
}