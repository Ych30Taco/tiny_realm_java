package com.taco.TinyRealm.module.EventSystemModule.controller;

import com.taco.TinyRealm.module.EventSystemModule.model.BaseEvent;
import com.taco.TinyRealm.module.EventSystemModule.service.EventSystemService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventSystemController {
    private final EventSystemService eventSystem;

    public EventSystemController(EventSystemService eventSystemService) {
        this.eventSystem = eventSystemService;
    }

    /**
     * 取得最近的事件通知
     * GET /api/event/recent?limit=20
     */
    @GetMapping("/recent")
    public ResponseEntity<List<BaseEvent>> getRecentEvents(@RequestParam(defaultValue = "20") int limit) {
        List<BaseEvent> events = eventSystem.getRecentEvents(limit);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
