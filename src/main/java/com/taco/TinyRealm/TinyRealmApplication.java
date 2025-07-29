package com.taco.TinyRealm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.taco.TinyRealm.config.GameConfig;

@SpringBootApplication
public class TinyRealmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyRealmApplication.class, args);
		// 系統啟動時載入配置
		try {
			GameConfig.initStatic();
			System.out.println("GameConfig loaded successfully");
		} catch (Exception e) {
			System.err.println("GameConfig load failed: " + e.getMessage());
		}
		System.out.println("System startup successful");
	}

}
