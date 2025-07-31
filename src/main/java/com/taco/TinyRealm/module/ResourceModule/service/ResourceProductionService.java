package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.storageModule.model.GameState;
import com.taco.TinyRealm.module.storageModule.service.StorageService;
import com.taco.TinyRealm.module.resourceModule.model.PlayerResource;
import com.taco.TinyRealm.module.playerModule.service.PlayerService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@EnableScheduling
public class ResourceProductionService {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    StorageService storageService;

    //@Scheduled(fixedRate = 300000) // 每5分鐘
    public void updatePlayerResources(String playerId) {
        GameState playdata = storageService.getGameStateList(playerId);
        PlayerResource playerResource = playdata.getResources();
        if (playerResource == null) {
            throw new IllegalArgumentException("Player not found");
        }

        // 計算時間間隔（小時）
        long currentTime = System.currentTimeMillis();
        double hoursPassed = (currentTime - playerResource.getLastUpdatedTime()) / (1000.0 * 3600.0);

        // 更新每種資源
        resourceService.getAllResourceTypes().forEach(type -> {
            String resourceId = type.getResourceID();
            int rate = type.getBaseProductionRate();
            int produced =  (int)(rate * hoursPassed);
            int nowAmount = playerResource.getNowAmount().getOrDefault(resourceId, 0);
            int maxAmount = playerResource.getMaxAmount().get(resourceId);
            int newAmount = Math.min(nowAmount + produced, maxAmount);

            playerResource.getNowAmount().put(resourceId, newAmount);
        });

        // 更新時間戳
        playerResource.setLastUpdatedTime(currentTime);

    }
}
