package com.taco.TinyRealm.module.SaveSystemModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.BaseEvent;
import com.taco.TinyRealm.module.EventSystemModule.model.EventType;
import com.taco.TinyRealm.module.EventSystemModule.service.EventSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 儲存模組專屬事件監聽器。
 * 處理遊戲存檔相關事件。
 */
@Component
public class SaveEventListener {
    @Autowired
    private EventSystemService eventNotificationService;

    @EventListener
    public void onGameSaved(BaseEvent event) {
        if (event.getEventType() == EventType.GAME_SAVED) {
            System.out.println("[儲存事件] 遊戲已儲存: " + event.getPayload());
            eventNotificationService.addEvent(event);
        }
    }
}
