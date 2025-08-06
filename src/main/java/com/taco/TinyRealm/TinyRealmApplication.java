package com.taco.TinyRealm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.taco.TinyRealm.config.GameConfig;

@SpringBootApplication
@EnableScheduling
public class TinyRealmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyRealmApplication.class, args);
		System.out.println("System startup successful");
	}

}
