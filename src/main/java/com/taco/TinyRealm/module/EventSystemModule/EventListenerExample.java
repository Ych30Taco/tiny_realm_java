package com.taco.TinyRealm.module.EventSystemModule;

import com.taco.TinyRealm.module.EventSystemModule.service.EventSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 事件監聽範例。
 * 僅處理未被其他監聽器處理的事件。
 */
@Component
public class EventListenerExample {
    @Autowired
    private EventSystemService eventNotificationService;

    /**
     * 監聽自訂事件，並根據事件型別處理。
     * @param event 收到的事件
     */
    @EventListener
    public void onEvent(BaseEvent event) {
        // 根據事件型別分流處理
        switch (event.getEventType()) {
            default:
                System.out.println("[事件通知] 其他事件: " + event.getEventType());
        }
        eventNotificationService.addEvent(event);
    }
}
