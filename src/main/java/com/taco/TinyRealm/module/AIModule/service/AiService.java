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

    private static final String GEMINI_API_URL = "https://api.gemini.com/v1/endpoint";
    private static final String GEMINI_API_KEY = "AIzaSyBqsQg_scpAbqeO42koyk6gI6mHZBSI2sQ";

    public AiModel processInput(String input) {
        RestTemplate restTemplate = new RestTemplate();
        // 構建請求物件
        AiModel request = new AiModel();
        request.setInput(input);

        // 添加 API 金鑰到請求頭
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GEMINI_API_KEY);
        HttpEntity<AiModel> entity = new HttpEntity<>(request, headers);

        // 發送請求到 Gemini API
        ResponseEntity<AiModel> response = restTemplate.exchange(GEMINI_API_URL, HttpMethod.POST, entity, AiModel.class);

        // 返回回應
        return response.getBody();
    }
}