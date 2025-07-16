package com.taco.TinyRealm.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class DataServer {

    @Value("${data.path}")
    private String dataPath;

    // 取得檔案完整路徑
    private String getFilePath(String functionName) {
        return dataPath + File.separator + functionName + ".json";
    }

    // 讀取資料，若不存在則建立預設值
    public String readData(String functionName, String defaultJson) throws IOException {
        String filePath = getFilePath(functionName);
        File file = new File(filePath);
        if (!file.exists()) {
            // 建立資料夾
            file.getParentFile().mkdirs();
            // 寫入預設值
            Files.write(Paths.get(filePath), defaultJson.getBytes());
            return defaultJson;
        }
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    // 寫入資料
    public void writeData(String functionName, String json) throws IOException {
        String filePath = getFilePath(functionName);
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        Files.write(Paths.get(filePath), json.getBytes());
    }
} 