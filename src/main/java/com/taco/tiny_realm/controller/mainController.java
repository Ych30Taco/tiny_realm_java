package com.taco.tiny_realm.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class mainController {
    @PostMapping("/main")

    public Map<String, Object> getmain(@RequestBody Map<String, Object> body) {
        // Logic to process the request and return equity data
        // This is a placeholder implementation
        return Map.of("status", "success", "data", body);
    }
    
}