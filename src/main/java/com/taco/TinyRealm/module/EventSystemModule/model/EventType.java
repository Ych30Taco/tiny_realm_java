package com.taco.TinyRealm.module.EventSystemModule.model;

/**
 * 遊戲事件類型枚舉，定義所有可用的事件類型。
 */
public enum EventType {
    /** 玩家資源變動 */
    PLAYER_RESOURCE_CHANGED,
    /** 玩家升級 */
    PLAYER_LEVEL_UP,
    /** 遊戲開始 */
    GAME_STARTED,
    /** 遊戲結束 */
    GAME_ENDED,
    // ...可依需求擴充...
}