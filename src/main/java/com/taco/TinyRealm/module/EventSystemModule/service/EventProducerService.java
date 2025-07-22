package com.taco.TinyRealm.module.EventSystemModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.GameEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * 事件生產者服務，負責將遊戲事件發送到 Kafka。
 */
@Service
public class EventProducerService {
    /** Kafka topic 名稱 */
    private static final String TOPIC = "game-events";

    @Autowired
    private KafkaTemplate<String, GameEvent> kafkaTemplate;

    /**
     * 發送事件到 Kafka topic。
     * @param event 遊戲事件
     */
    public void sendEvent(GameEvent event) {
        kafkaTemplate.send(TOPIC, "event.getEventId()", event);
    }
}