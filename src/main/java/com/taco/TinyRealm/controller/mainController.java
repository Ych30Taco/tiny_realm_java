package com.taco.TinyRealm.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class mainController {
    @PostMapping("/main")

    public Map<String, Object> getmain(@RequestBody Map<String, Object> body) {
        return Map.of("success", true, "message", "主控制器回應成功", "data", body);
    }
    
}