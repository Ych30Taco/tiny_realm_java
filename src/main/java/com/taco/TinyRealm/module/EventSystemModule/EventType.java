package com.taco.TinyRealm.module.EventSystemModule;

/**
 * 定義所有事件型別的列舉。
 * 可依需求擴充事件種類，方便統一管理。
 */
public enum EventType {
    RESOURCE_CHANGED, // 資源變動事件
    PLAYER_LEVEL_UP,  // 玩家升級事件
    BUILDING_COMPLETE, // 建築完成事件
    GAME_SAVED
    // ...可依需求新增其他事件型別
}
