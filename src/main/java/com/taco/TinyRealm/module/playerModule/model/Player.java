package com.taco.TinyRealm.module.playerModule.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String id; // 玩家唯一識別碼
    private String name; // 玩家名稱
    private int level; // 玩家等級
    private int experience; // 玩家經驗值
    private int status; // 玩家狀態，2禁止 1在線、0離線
    private long lastLogoutTime; // 玩家最後登出時間
    private long lastLoginTime; // 玩家最後登入時間
    private long foundingTime; // 玩家創建時間
    private long lastUpdatedTime; // 玩家最後更新時間
    
}
