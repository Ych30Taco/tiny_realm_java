package com.taco.TinyRealm.module.AIModule.service;

import com.taco.TinyRealm.module.AIModule.model.AiModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiService {

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro-002:generateContent";
    private static final String GEMINI_API_KEY = "AIzaSyBqsQg_scpAbqeO42koyk6gI6mHZBSI2sQ";

    public AiModel processInput(String input) {
        RestTemplate restTemplate = new RestTemplate();

        // Gemini API 需要的請求格式
        String url = GEMINI_API_URL + "?key=" + GEMINI_API_KEY;

        // 建立請求 body
        String requestBody = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + input + "\" } ] } ] }";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // 簡單的速率限制：延遲 1 秒
        try {
            Thread.sleep(1000); // 1 秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 發送請求到 Gemini API
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 你可以根據 response.getBody() 解析回傳內容
        AiModel result = new AiModel();
        result.setInput(input);
        result.setResponse(response.getBody());
        return result;
    }

    public String listModels() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://generativelanguage.googleapis.com/v1/models?key=" + GEMINI_API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}