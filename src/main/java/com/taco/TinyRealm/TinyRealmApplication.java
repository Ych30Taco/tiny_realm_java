package com.taco.TinyRealm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.taco.TinyRealm.config.GameConfig;

/**
 * TinyRealm 遊戲主應用程式類別
 * 
 * 這是一個基於 Spring Boot 的策略遊戲應用程式，採用模組化架構設計。
 * 遊戲包含資源管理、建築系統、玩家管理、戰鬥系統等多個模組。
 * 
 * 主要功能：
 * - 資源生產與管理
 * - 建築建造與升級
 * - 玩家狀態管理
 * - 定時任務處理
 * - 遊戲數據持久化
 * 
 * @author TinyRealm Team
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling // 啟用定時任務功能，用於自動資源生產等週期性任務
public class TinyRealmApplication {

	/**
	 * 應用程式主入口點
	 * 
	 * 啟動 Spring Boot 應用程式，初始化所有模組和服務。
	 * 應用程式啟動後會自動載入遊戲配置、初始化各個模組服務。
	 * 
	 * @param args 命令列參數
	 */
	public static void main(String[] args) {
		// 啟動 Spring Boot 應用程式
		SpringApplication.run(TinyRealmApplication.class, args);
		System.out.println("System startup successful");
	}

}
