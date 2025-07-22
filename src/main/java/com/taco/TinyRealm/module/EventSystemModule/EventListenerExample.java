package com.taco.TinyRealm.module.EventSystemModule;

import com.taco.TinyRealm.module.EventSystemModule.service.EventSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 事件監聽範例。
 * 監聽所有 BaseEvent 事件，並根據事件型別進行處理。
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
            case RESOURCE_CHANGED:
                // 資源變動事件處理邏輯
                System.out.println("[事件通知] 資源變動: " + event.getPayload());
                break;
            case PLAYER_LEVEL_UP:
                // 玩家升級事件處理邏輯
                System.out.println("[事件通知] 玩家升級: " + event.getPayload());
                break;
            case BUILDING_COMPLETE:
                // 建築完成事件處理邏輯
                System.out.println("[事件通知] 建築完成: " + event.getPayload());
                break;
            default:
                System.out.println("[事件通知] 其他事件: " + event.getEventType());
        }
        eventNotificationService.addEvent(event);
    }
}
