package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.Task;
import com.taco.TinyRealm.module.ResourceModule.model.Resource;
import com.taco.TinyRealm.module.ResourceModule.service.ResourceService;
import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    private StorageService storageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private EventService eventService;

    public Task assignTask(String playerId, String type, String description, int target, Resource rewards,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setType(type);
        task.setDescription(description);
        task.setProgress(0);
        task.setTarget(target);
        task.setRewards(rewards);
        task.setStatus("ACTIVE");

        if (gameState.getTasks() == null) gameState.setTasks(new java.util.ArrayList<>());
        gameState.getTasks().add(task);
        storageService.saveGameState(playerId, gameState, isTest);

        eventService.addEvent(playerId, "task_assigned", "Assigned task: " + description,isTest);
        return task;
    }

    public Task updateTaskProgress(String playerId, String taskId, int progressIncrement,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, isTest);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        Task task = gameState.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if ("COMPLETED".equals(task.getStatus())) throw new IllegalArgumentException("Task already completed");

        task.setProgress(task.getProgress() + progressIncrement);
        if (task.getProgress() >= task.getTarget()) {
            task.setStatus("COMPLETED");
            resourceService.addResources(playerId, task.getRewards().getGold(), task.getRewards().getWood(),isTest);
            eventService.addEvent(playerId, "task_completed", "Completed task: " + task.getDescription(),isTest);
        }

        storageService.saveGameState(playerId, gameState, isTest);
        return task;
    }

    public List<Task> getTasks(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId, false);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getTasks();
    }
}
