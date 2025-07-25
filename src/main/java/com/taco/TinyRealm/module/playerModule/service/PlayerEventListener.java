package com.taco.TinyRealm.module.playerModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.BaseEvent;
import com.taco.TinyRealm.module.EventSystemModule.model.EventType;
import com.taco.TinyRealm.module.EventSystemModule.service.EventSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 玩家模組專屬事件監聽器。
 * 處理玩家升級等事件。
 */
@Component
public class PlayerEventListener {
    @Autowired
    private EventSystemService eventNotificationService;

    @EventListener
    public void onPlayerLevelUp(BaseEvent event) {
        if (event.getEventType() == EventType.PLAYER_LEVEL_UP) {
            System.out.println("[玩家事件] 玩家升級: " + event.getPayload());
            eventNotificationService.addEvent(event);
        }
    }
}
