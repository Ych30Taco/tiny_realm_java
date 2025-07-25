package com.taco.TinyRealm.module.ResourcesModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.BaseEvent;
import com.taco.TinyRealm.module.EventSystemModule.model.EventType;
import com.taco.TinyRealm.module.EventSystemModule.service.EventSystemService;
import com.taco.TinyRealm.module.playerModule.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 資源模組專屬事件監聽器。
 * 處理資源變動事件。
 */
@Component
public class ResourceEventListener {
    @Autowired
    private EventSystemService eventNotificationService;

    @Autowired
    private PlayerRepository playerRepository;

    @EventListener
    public void onResourceChanged(BaseEvent event) {
        if (event.getEventType() == EventType.RESOURCE_CHANGED) {
            handleResourceChanged(event.getPayload());
            eventNotificationService.addEvent(event);
        }
    }

    private void handleResourceChanged(Object payload) {
        if (payload instanceof java.util.Map<?, ?> map) {
            String playerId = (String) map.get("playerId");
            String resourceId = (String) map.get("resourceId");
            Integer amount = (Integer) map.get("amount");
            String action = (String) map.get("action");
            if (playerId != null && resourceId != null && amount != null && action != null) {
                playerRepository.findById(playerId).ifPresent(player -> {
                    java.util.Map<String, Integer> resources = player.getResources();
                    if ("add".equals(action)) {
                        resources.put(resourceId, resources.getOrDefault(resourceId, 0) + amount);
                    } else if ("consume".equals(action)) {
                        int current = resources.getOrDefault(resourceId, 0);
                        if (current >= amount) {
                            resources.put(resourceId, current - amount);
                        }
                    }
                    playerRepository.save(player);
                });
            }
        }
    }
}
