package com.taco.TinyRealm.service;

import com.taco.TinyRealm.model.GameState;
import com.taco.TinyRealm.model.Resource;
import com.taco.TinyRealm.model.Task;
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
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setType(type);
        task.setDescription(description);
        task.setProgress(0);
        task.setTarget(target);
        task.setRewards(rewards);
        task.setStatus("ACTIVE");

        gameState.getTasks().add(task);
        storageService.saveGameState(playerId, gameState);

        eventService.addEvent(playerId, "task_assigned", "Assigned task: " + description,false);
        return task;
    }

    public Task updateTaskProgress(String playerId, String taskId, int progressIncrement,boolean isTest) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");

        Task task = gameState.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if ("COMPLETED".equals(task.getStatus())) throw new IllegalArgumentException("Task already completed");

        task.setProgress(task.getProgress() + progressIncrement);
        if (task.getProgress() >= task.getTarget()) {
            task.setStatus("COMPLETED");
            resourceService.addResources(playerId, task.getRewards().getGold(), task.getRewards().getWood(),false);
            eventService.addEvent(playerId, "task_completed", "Completed task: " + task.getDescription(),false);
        }

        storageService.saveGameState(playerId, gameState);
        return task;
    }

    public List<Task> getTasks(String playerId) throws IOException {
        GameState gameState = storageService.loadGameState(playerId);
        if (gameState == null) throw new IllegalArgumentException("Player not found");
        return gameState.getTasks();
    }
}
