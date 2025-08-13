package com.taco.TinyRealm.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 系統啟動監控服務
 * 追蹤各模組的啟動狀態和性能指標
 */
@Service
public class StartupMonitorService {
    
    private final Map<String, Long> moduleStartTimes = new ConcurrentHashMap<>();
    private final Map<String, Long> moduleLoadTimes = new ConcurrentHashMap<>();
    private final AtomicInteger totalModules = new AtomicInteger(0);
    private final AtomicInteger loadedModules = new AtomicInteger(0);
    
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * 記錄模組開始載入
     */
    public void recordModuleStart(String moduleName) {
        moduleStartTimes.put(moduleName, System.currentTimeMillis());
        totalModules.incrementAndGet();
    }
    
    /**
     * 記錄模組載入完成
     */
    public void recordModuleLoaded(String moduleName) {
        Long startTime = moduleStartTimes.get(moduleName);
        if (startTime != null) {
            long loadTime = System.currentTimeMillis() - startTime;
            moduleLoadTimes.put(moduleName, loadTime);
            loadedModules.incrementAndGet();
            
            System.out.println("模組 " + moduleName + " 載入完成，耗時: " + loadTime + "ms");
        }
    }
    
    /**
     * 獲取啟動統計信息
     */
    public Map<String, Object> getStartupStats() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("totalModules", totalModules.get());
        stats.put("loadedModules", loadedModules.get());
        stats.put("moduleLoadTimes", new ConcurrentHashMap<>(moduleLoadTimes));
        
        // 計算總啟動時間
        long totalStartupTime = moduleLoadTimes.values().stream()
            .mapToLong(Long::longValue)
            .sum();
        stats.put("totalStartupTime", totalStartupTime);
        
        return stats;
    }
    
    /**
     * 檢查是否所有模組都已載入
     */
    public boolean isAllModulesLoaded() {
        return loadedModules.get() >= totalModules.get();
    }
    
    /**
     * 應用程式啟動完成事件監聽
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("=== 系統啟動完成 ===");
        System.out.println("總模組數: " + totalModules.get());
        System.out.println("已載入模組數: " + loadedModules.get());
        
        // 輸出各模組載入時間
        moduleLoadTimes.forEach((module, time) -> 
            System.out.println(module + ": " + time + "ms"));
        
        long totalTime = moduleLoadTimes.values().stream()
            .mapToLong(Long::longValue)
            .sum();
        System.out.println("總啟動時間: " + totalTime + "ms");
    }
}
