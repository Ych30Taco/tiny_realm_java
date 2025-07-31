/* package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.GameEvent;
import com.taco.TinyRealm.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/{playerId}/add")
    public ResponseEntity<?> addEvent(@PathVariable String playerId,
                                      @RequestParam String type,
                                      @RequestParam String message) {
        try {
            eventService.addEvent(playerId, type, message, false);
            return ResponseEntity.ok("Event added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to add event: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getEvents(@PathVariable String playerId) {
        try {
            List<GameEvent> events = eventService.getEvents(playerId, false);
            return ResponseEntity.ok(events);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
 */