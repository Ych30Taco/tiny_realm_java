package com.taco.TinyRealm.module.EventSystemModule;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 事件系統模組設定類。
 * 啟用本模組下的元件自動掃描，讓事件發佈器與監聽器能被 Spring 管理。
 */
@Configuration
@ComponentScan(basePackages = "com.taco.TinyRealm.module.EventSystemModule")
public class EventSystemModuleConfig {
    // 此類僅作為模組設定入口，無需額外程式碼。
}
