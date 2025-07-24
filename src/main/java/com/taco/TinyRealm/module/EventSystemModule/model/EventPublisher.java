package com.taco.TinyRealm.module.EventSystemModule.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 事件發佈工具類。
 * 封裝 Spring ApplicationEventPublisher，統一發佈事件。
 */
@Component
public class EventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 發佈自訂事件
     * @param eventType 事件型別
     * @param payload 攜帶資料（可為 null）
     * @param source 事件來源
     */
    public void publish(EventType eventType, Object payload, Object source) {
        BaseEvent event = new BaseEvent(source, eventType, payload);
        applicationEventPublisher.publishEvent(event);
    }
}
