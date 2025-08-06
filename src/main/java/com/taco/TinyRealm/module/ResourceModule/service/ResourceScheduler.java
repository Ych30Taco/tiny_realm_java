package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.storageModule.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ResourceScheduler {
    @Autowired
    private ResourceProductionService productionService;
    @Autowired
    private StorageService storageService;

    /**
     * 每 5 分鐘自動更新所有玩家的資源
     */
    @Scheduled(fixedRate = 300000) // 300000 毫秒 = 5 分鐘
    public void updateAllPlayerResources() {
        try {
            List<String> playerIds = storageService.getGameStateIdList();
            System.out.println("開始更新 " + playerIds.size() + " 個玩家的資源...");
            
            for (String playerId : playerIds) {
                try {
                    productionService.updatePlayerResources(playerId, false);
                } catch (Exception e) {
                    System.err.println("更新玩家 " + playerId + " 資源時發生錯誤: " + e.getMessage());
                }
            }
            
            System.out.println("完成更新所有玩家資源");
        } catch (Exception e) {
            System.err.println("資源更新排程發生錯誤: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 每小時更新一次（可選的備用排程）
     */
    @Scheduled(fixedRate = 3600000) // 3600000 毫秒 = 1 小時
    public void hourlyResourceUpdate() {
        try {
            List<String> playerIds = storageService.getGameStateIdList();
            System.out.println("執行每小時資源更新，玩家數量: " + playerIds.size());
            
            for (String playerId : playerIds) {
                try {
                    productionService.updatePlayerResources(playerId, false);
                } catch (Exception e) {
                    System.err.println("每小時更新玩家 " + playerId + " 資源時發生錯誤: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("每小時資源更新排程發生錯誤: " + e.getMessage());
        }
    }
} 