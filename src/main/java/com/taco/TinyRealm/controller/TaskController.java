package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.model.Resource;
import com.taco.TinyRealm.model.Task;
import com.taco.TinyRealm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/{playerId}/assign")
    public ResponseEntity<?> assignTask(@PathVariable String playerId,
                                        @RequestParam String type,
                                        @RequestParam String description,
                                        @RequestParam int target,
                                        @RequestBody Resource rewards) {
        try {
            Task task = taskService.assignTask(playerId, type, description, target, rewards,false);
            return ResponseEntity.ok(task);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/{playerId}/update/{taskId}")
    public ResponseEntity<?> updateTaskProgress(@PathVariable String playerId,
                                                @PathVariable String taskId,
                                                @RequestParam int progressIncrement) {
        try {
            Task task = taskService.updateTaskProgress(playerId, taskId, progressIncrement,false);
            return ResponseEntity.ok(task);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<?> getTasks(@PathVariable String playerId) {
        try {
            List<Task> tasks = taskService.getTasks(playerId);
            return ResponseEntity.ok(tasks);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}
