package com.taco.TinyRealm.controller;

import com.taco.TinyRealm.server.DataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataServer dataServer;

    // 取得資料
    @GetMapping("/{functionName}")
    public String getData(@PathVariable String functionName) throws Exception {
        // 這裡可以根據 functionName 給不同預設值
        String defaultJson = "{}"; // 你可以根據需求調整
        return dataServer.readData(functionName, defaultJson);
    }

    // 存入資料
    @PostMapping("/{functionName}")
    public void saveData(@PathVariable String functionName, @RequestBody String json) throws Exception {
        dataServer.writeData(functionName, json);
    }
} 