/* package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping
    public String showTestPage(Model model) {
        return "test";
    }

    @GetMapping("/events")
    public String showEventsPage(Model model) {
        return "events";
    }

    @PostMapping("/init")
    public String initializeTestData(@RequestParam String playerId, @RequestParam(defaultValue = "10") int playerCount, Model model) {
        try {
            String result = testService.initializeTestData(playerId, playerCount);
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to initialize: " + e.getMessage());
        }
        return "test";
    }

    @PostMapping("/run")
    public String runScenario(@RequestParam String scenario, @RequestParam String playerId, @RequestParam(defaultValue = "10") int playerCount, Model model) {
        try {
            String result = testService.runScenario(scenario, playerId, playerCount);
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to run scenario: " + e.getMessage());
        }
        return "test";
    }

    @PostMapping("/batch")
    public String batchApiTest(@RequestParam String api, @RequestParam String playerId, @RequestParam(defaultValue = "10") int playerCount, @RequestParam String params, Model model) {
        try {
            String result = testService.batchApiTest(api, playerId, playerCount, params);
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to run batch test: " + e.getMessage());
        }
        return "test";
    }

    @PostMapping("/stress")
    public String stressTest(@RequestParam String api, @RequestParam String playerId, @RequestParam(defaultValue = "10") int playerCount, Model model) {
        try {
            String result = testService.stressTest(api, playerId, playerCount);
            model.addAttribute("result", result);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to run stress test: " + e.getMessage());
        }
        return "test";
    }

    @PostMapping("/reset")
    public String resetTestData(Model model) {
        try {
            testService.clearTestData();
            model.addAttribute("result", "Test data cleared");
        } catch (Exception e) {
            model.addAttribute("error", "Failed to clear data: " + e.getMessage());
        }
        return "test";
    }
}
 */