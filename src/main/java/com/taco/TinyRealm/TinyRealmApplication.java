package com.taco.TinyRealm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.taco.TinyRealm.config.GameConfig;

@SpringBootApplication
public class TinyRealmApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyRealmApplication.class, args);
		System.out.println("System startup successful");
	}

}
