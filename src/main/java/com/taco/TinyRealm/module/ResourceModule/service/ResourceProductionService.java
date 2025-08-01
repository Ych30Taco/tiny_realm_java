package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.IOException;
import java.util.List;

@Service
@EnableScheduling
public class ResourceProductionService {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    StorageService storageService;

    @Scheduled(initialDelay = 0, fixedRate = 5000) // 每5分鐘
    public void updateAllPlayersResources() {
        //獲取所有上線玩家ID
        for (String playerId : storageService.getGameStateIdList()) {
            try {
                updatePlayerResources(playerId);
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
    }

    // 原本的更新邏輯移到這個私有方法
    private void updatePlayerResources(String playerId) throws IOException {
        GameState gameState = storageService.getGameStateList(playerId);
        PlayerResource playerResource = gameState.getResources();
        if (playerResource == null) {
            throw new IllegalArgumentException("Player not found");
        }

        // 計算時間間隔（小時）
        long currentTime = System.currentTimeMillis();
        double hourPassed = (currentTime - playerResource.getLastUpdatedTime()) / (1000.0 * 3600.0);

        // 更新每種資源
        resourceService.getAllResourceTypes().forEach(type -> {
            String resourceId = type.getId();
            int rate = type.getBaseProductionRate();
            int produced =  (int)(rate * hourPassed);
            int nowAmount = playerResource.getNowAmount().getOrDefault(resourceId, 0);
            int maxAmount = playerResource.getMaxAmount().get(resourceId);
            int newAmount = Math.min(nowAmount + produced, maxAmount);

            playerResource.getNowAmount().put(resourceId, newAmount);
        });

        // 更新時間戳
        playerResource.setLastUpdatedTime(currentTime);
        gameState.setResources(playerResource);
        storageService.saveGameState(playerId, gameState, false);
    }
}
