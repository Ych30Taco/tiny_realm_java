package com.taco.TinyRealm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Map;

@Component
public class GameConfig {
	private static List<Map<String, Object>> buildings;
	private static List<Map<String, Object>> resources;
	private static List<Map<String, Object>> technologies;
	private static List<Map<String, Object>> units;
	private static Map<String, Object> terrain;
	private static List<Map<String, Object>> tasks;
	private static Map<String, Object> market;
	private static List<Map<String, Object>> events;

	public static void initStatic() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		buildings = mapper.readValue(new File("src/main/resources/config/buildings.json"), List.class);
		resources = mapper.readValue(new File("src/main/resources/config/resources.json"), List.class);
		technologies = mapper.readValue(new File("src/main/resources/config/technologies.json"), List.class);
		units = mapper.readValue(new File("src/main/resources/config/units.json"), List.class);
		terrain = mapper.readValue(new File("src/main/resources/config/terrain.json"), Map.class);
		tasks = mapper.readValue(new File("src/main/resources/config/tasks.json"), List.class);
		market = mapper.readValue(new File("src/main/resources/config/market.json"), Map.class);
		events = mapper.readValue(new File("src/main/resources/config/events.json"), List.class);
	}

	@PostConstruct
	public void init() {
		try {
			initStatic();
		} catch (Exception e) {
			System.err.println("GameConfig @PostConstruct load failed: " + e.getMessage());
		}
	}

	// ...可加 getter 方法...
}
