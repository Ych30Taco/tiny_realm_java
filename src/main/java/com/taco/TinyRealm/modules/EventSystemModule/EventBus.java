package com.taco.TinyRealm.modules.EventSystemModule;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 事件中心（EventBus）
 * 提供模組間註冊、取消註冊、發送事件的功能。
 * 操作流程：
 * 1. 使用 subscribe 註冊事件監聽。
 * 2. 使用 publish 發送事件，所有註冊的監聽者都會收到通知。
 * 3. 使用 unsubscribe 取消註冊，避免重複觸發或記憶體洩漏。
 * 4. 可用 subscribeOnce 註冊一次性事件，觸發後自動移除。
 */
public class EventBus {
    // 事件表，儲存每個事件名稱對應的監聽者（支援泛型）
    private static final Map<String, List<Consumer<?>>> eventTable = new ConcurrentHashMap<>();

    /**
     * 註冊事件監聽（泛型版本，型別安全）。
     */
    public static <T> void subscribe(String eventName, Consumer<T> callback) {
        eventTable.computeIfAbsent(eventName, k -> new ArrayList<>()).add(callback);
    }

    /**
     * 註冊一次性事件監聽（觸發一次後自動移除）。
     */
    public static <T> void subscribeOnce(String eventName, Consumer<T> callback) {
        Consumer<T>[] wrapper = new Consumer[1];
        wrapper[0] = param -> {
            callback.accept(param);
            unsubscribe(eventName, wrapper[0]);
        };
        subscribe(eventName, wrapper[0]);
    }

    /**
     * 取消註冊事件監聽（泛型版本）。
     */
    public static <T> void unsubscribe(String eventName, Consumer<T> callback) {
        List<Consumer<?>> listeners = eventTable.get(eventName);
        if (listeners != null) {
            listeners.remove(callback);
            if (listeners.isEmpty()) {
                eventTable.remove(eventName);
            }
        }
    }

    /**
     * 發送事件（泛型版本，型別安全，含錯誤處理）。
     */
    @SuppressWarnings("unchecked")
    public static <T> void publish(String eventName, T param) {
        List<Consumer<?>> listeners = eventTable.get(eventName);
        if (listeners != null) {
            // 建立副本，避免執行過程中監聽者被移除導致錯誤
            List<Consumer<?>> copy = new ArrayList<>(listeners);
            for (Consumer<?> consumer : copy) {
                try {
                    ((Consumer<T>) consumer).accept(param);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // 範例事件資料結構
    public static class ResourceChangedEvent {
        public String resourceType;
        public int newValue;
        public ResourceChangedEvent(String resourceType, int newValue) {
            this.resourceType = resourceType;
            this.newValue = newValue;
        }
    }

    // 範例 DataManager
    public static class DataManager {
        public static void updateResource(String resourceType, int newValue) {
            // 讀取 JSON、更新資料、寫回 JSON
            // ...請根據你的專案架構實作...
        }
    }

    // 註冊事件監聽範例（建議放在初始化流程）
    public static void registerEventListeners() {
        EventBus.subscribe(EventNames.ResourceChanged, (ResourceChangedEvent evt) -> {
            DataManager.updateResource(evt.resourceType, evt.newValue);
        });
    }
}
