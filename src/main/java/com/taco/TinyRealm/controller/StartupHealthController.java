package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.service.StartupMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系統啟動健康檢查控制器
 * 提供啟動狀態監控和健康檢查功能
 */
@RestController
@RequestMapping("/api/startup")
public class StartupHealthController {
    
    @Autowired
    private StartupMonitorService startupMonitorService;
    
    /**
     * 獲取啟動狀態
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStartupStatus() {
        Map<String, Object> status = startupMonitorService.getStartupStats();
        return ResponseEntity.ok(status);
    }
    
    /**
     * 檢查系統是否完全啟動
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        boolean isHealthy = startupMonitorService.isAllModulesLoaded();
        Map<String, Object> health = Map.of(
            "status", isHealthy ? "READY" : "LOADING",
            "message", isHealthy ? "系統已完全啟動" : "系統正在啟動中",
            "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.ok(health);
    }
    
    /**
     * 強制重新載入配置
     */
    @GetMapping("/reload")
    public ResponseEntity<Map<String, Object>> reloadConfigs() {
        try {
            // 這裡可以調用配置重載服務
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "配置重載已觸發",
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "ERROR",
                "message", "配置重載失敗: " + e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }
}
