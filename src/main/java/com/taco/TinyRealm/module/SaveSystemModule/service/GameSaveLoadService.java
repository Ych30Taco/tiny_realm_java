package com.taco.TinyRealm.module.SaveSystemModule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.taco.TinyRealm.module.SaveSystemModule.model.GameState;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service // 標記為 Spring Bean
public class GameSaveLoadService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DEFAULT_SAVE_FILE_NAME = "game_state.json";
    private static final String SAVE_DIRECTORY = "saves"; // 相對路徑，在應用程式執行目錄下

    public GameSaveLoadService() {
        // 配置 ObjectMapper，讓 JSON 輸出格式化，更易讀
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * 儲存遊戲狀態到指定檔案
     * @param gameState 要儲存的遊戲狀態物件
     * @param filename 儲存的檔案名稱 (例如 "my_game_01.json")
     */
    public void saveGame(GameState gameState, String filename) {
        try {
            Path dirPath = Paths.get(SAVE_DIRECTORY);
            // 如果儲存目錄不存在，則創建它
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
                System.out.println("DEBUG: 已建立儲存目錄: " + dirPath.toAbsolutePath());
            }
            File saveFile = dirPath.resolve(filename).toFile();
            objectMapper.writeValue(saveFile, gameState);
            System.out.println("遊戲已儲存至: " + saveFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("錯誤：儲存遊戲失敗！" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 從指定檔案載入遊戲狀態
     * @param filename 要載入的檔案名稱
     * @return 載入的遊戲狀態物件，如果檔案不存在或載入失敗則返回 null
     */
    public GameState loadGame(String filename) {
        File saveFile = Paths.get(SAVE_DIRECTORY, filename).toFile();
        if (saveFile.exists()) {
            try {
                GameState loadedState = objectMapper.readValue(saveFile, GameState.class);
                System.out.println("遊戲已從 " + saveFile.getAbsolutePath() + " 載入。");
                return loadedState;
            } catch (IOException e) {
                System.err.println("錯誤：載入遊戲失敗！檔案可能已損壞或格式不正確。" + e.getMessage());
                e.printStackTrace();
                return null; // 載入失敗
            }
        }
        System.out.println("DEBUG: 找不到存檔檔案: " + saveFile.getAbsolutePath() + "。");
        return null; // 檔案不存在
    }

    /**
     * 儲存遊戲狀態到預設檔案 (方便快捷呼叫)
     * @param gameState 要儲存的遊戲狀態物件
     */
    public void saveGame(GameState gameState) {
        saveGame(gameState, DEFAULT_SAVE_FILE_NAME);
    }

    /**
     * 從預設檔案載入遊戲狀態 (方便快捷呼叫)
     * @return 載入的遊戲狀態物件，如果檔案不存在或載入失敗則返回 null
     */
    public GameState loadGame() {
        return loadGame(DEFAULT_SAVE_FILE_NAME);
    }
}