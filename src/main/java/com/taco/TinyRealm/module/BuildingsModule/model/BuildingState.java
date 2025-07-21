package com.taco.TinyRealm.module.BuildingsModule.model;

/**
 * 定義建築物在遊戲中的各種可能狀態。
 * 這些狀態會影響建築物的行為和可執行操作。
 */
public enum BuildingState {
    UNDER_CONSTRUCTION, // 建築物正在建造或升級中
    ACTIVE,             // 建築物已完成建造/升級，正常運作並提供其功能
    IDLE,               // 建築物處於閒置狀態 (例如：生產建築沒有任務在生產，或者資源建築儲存已滿)
    DAMAGED,            // 建築物已受損，可能無法提供全部功能或效率降低
    DESTROYED           // 建築物已被摧毀，不再可用
}