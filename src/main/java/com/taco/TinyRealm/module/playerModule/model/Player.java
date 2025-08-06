package com.taco.TinyRealm.module.playerModule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 玩家數據模型
 * 
 * 定義遊戲中玩家的基本屬性，包括身份信息、等級、狀態、時間戳等。
 * 這個模型用於存儲和管理玩家的核心數據。
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    
    /**
     * 玩家唯一識別碼
     * 
     * 使用UUID格式，確保全局唯一性
     * 例如：9f917eb2-de3a-420b-ae3b-0a493264072a
     */
    private String id;
    
    /**
     * 玩家名稱
     * 
     * 玩家在遊戲中顯示的名稱
     * 例如：玩家123、城主001
     */
    private String name;
    
    /**
     * 玩家等級
     * 
     * 從1開始，表示玩家的遊戲進度
     * 等級影響可建造的建築類型和數量
     */
    private int level;
    
    /**
     * 玩家經驗值
     * 
     * 累積的經驗值，用於升級
     * 通過完成任務、建造建築等方式獲得
     */
    private int experience;
    
    /**
     * 玩家狀態
     * 
     * 0: 離線
     * 1: 在線
     * 2: 禁止（被封禁）
     */
    private int status;
    
    /**
     * 玩家最後登出時間
     * 
     * 時間戳（毫秒）
     * 用於計算離線時間和資源生產
     */
    private long lastLogoutTime;
    
    /**
     * 玩家最後登入時間
     * 
     * 時間戳（毫秒）
     * 用於記錄玩家活躍度
     */
    private long lastLoginTime;
    
    /**
     * 玩家創建時間
     * 
     * 時間戳（毫秒）
     * 記錄玩家帳號的創建時間
     */
    private long foundingTime;
    
    /**
     * 玩家最後更新時間
     * 
     * 時間戳（毫秒）
     * 用於數據同步和緩存管理
     */
    private long lastUpdatedTime;
}
