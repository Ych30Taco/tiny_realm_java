package com.taco.TinyRealm.module.EventSystemModule.model;

import org.springframework.context.ApplicationEvent;

/**
 * 自訂事件基底類別，所有事件皆繼承此類。
 * 可攜帶事件型別與自訂資料。
 */
public class BaseEvent extends ApplicationEvent {
    private final EventType eventType;
    private final Object payload; // 可攜帶任意資料

    /**
     * @param source 事件來源
     * @param eventType 事件型別
     * @param payload 事件攜帶資料（可為 null）
     */
    public BaseEvent(Object source, EventType eventType, Object payload) {
        super(source);
        this.eventType = eventType;
        this.payload = payload;
    }

    /**
     * 取得事件型別
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * 取得事件攜帶資料
     */
    public Object getPayload() {
        return payload;
    }
}
