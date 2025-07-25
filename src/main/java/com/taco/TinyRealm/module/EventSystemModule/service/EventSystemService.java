package com.taco.TinyRealm.module.EventSystemModule.service;

import com.taco.TinyRealm.module.EventSystemModule.model.BaseEvent;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class EventSystemService {
    private final LinkedList<BaseEvent> eventList = new LinkedList<>();
    private static final int MAX_EVENTS = 100;

    public void addEvent(BaseEvent event) {
        synchronized (eventList) {
            if (eventList.size() >= MAX_EVENTS) {
                eventList.removeFirst();
            }
            eventList.addLast(event);
        }
    }

    public List<BaseEvent> getRecentEvents(int limit) {
        synchronized (eventList) {
            int fromIndex = Math.max(0, eventList.size() - limit);
            return Collections.unmodifiableList(new LinkedList<>(eventList.subList(fromIndex, eventList.size())));
        }
    }
}
