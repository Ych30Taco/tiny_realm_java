package com.taco.TinyRealm.module.resourceModule.service;

import com.taco.TinyRealm.module.storageModule.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 資源生產排程器
 * 
 * 負責定時執行資源生產任務，確保所有玩家的資源能夠自動更新。
 * 使用Spring的@Scheduled注解來實現定時任務功能。
 * 
 * 主要功能：
 * - 定期更新所有玩家的資源
 * - 處理批量資源生產計算
 * - 提供錯誤處理和日誌記錄
 * 
 * 排程設定：
 * - 每5分鐘執行一次主要更新
 * - 每小時執行一次備用更新
 * 
 * @author TinyRealm Team
 * @version 1.0
 */

@Service
public class ResourceScheduler {
    
    /** 資源生產服務，用於執行資源更新邏輯 */
    @Autowired
    private ResourceProductionService productionService;
    
    /** 儲存服務，用於獲取所有玩家ID列表 */
    @Autowired
    private StorageService storageService;

    /**
     * 每 5 分鐘自動更新所有玩家的資源
     * 
     * 這是主要的資源更新排程，會：
     * 1. 獲取所有在線玩家的ID列表
     * 2. 逐個更新每個玩家的資源
     * 3. 處理個別玩家的更新錯誤，不影響其他玩家
     * 4. 記錄更新過程的日誌
     * 
     * 使用@Scheduled注解設定每5分鐘執行一次（300000毫秒）
     */
    @Scheduled(fixedRate = 300000) // 300000 毫秒 = 5 分鐘
    public void updateAllPlayerResources() {
        try {
            // 獲取所有在線玩家的ID列表
            List<String> playerIds = storageService.getOnlineGameStateIdList();
            System.out.println("開始更新 " + playerIds.size() + " 個玩家的資源...");
            
            // 逐個更新每個玩家的資源
            for (String playerId : playerIds) {
                try {
                    productionService.updatePlayerResources(playerId, false);
                } catch (Exception e) {
                    // 個別玩家的錯誤不影響其他玩家，只記錄錯誤日誌
                    System.err.println("更新玩家 " + playerId + " 資源時發生錯誤: " + e.getMessage());
                }
            }
            
            System.out.println("完成更新所有玩家資源");
        } catch (Exception e) {
            // 記錄整體排程錯誤
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
            List<String> playerIds = storageService.getOnlineGameStateIdList();
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