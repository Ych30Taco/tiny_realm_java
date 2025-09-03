package com.taco.TinyRealm.module.AIModule.controller;

import com.taco.TinyRealm.module.AIModule.model.AiModel;
import com.taco.TinyRealm.module.AIModule.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/process")
    public ResponseEntity<?> processInput(@RequestBody AiModel aiModel) {
        try {
            AiModel response = aiService.processInput(aiModel.getInput());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing input: " + e.getMessage());
        }
    }
}