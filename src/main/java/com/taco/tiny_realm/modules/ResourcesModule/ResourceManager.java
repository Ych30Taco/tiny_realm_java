package com.taco.tiny_realm.modules.ResourcesModule;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * ResourceManager 負責管理所有資源的集合，
 * 提供資源的查詢、增加、消耗、設限等基本操作。
 */
@Component
public class ResourceManager {
    // 使用 EnumMap 儲存每種資源對應的 ResourceData
    private final Map<ResourceType, ResourceData> resources = new EnumMap<>(ResourceType.class);

    /**
     * 建構子：初始化所有資源種類，預設數量為 1000000，限制為最大值
     */
    public ResourceManager() {
        for (ResourceType type : ResourceType.values()) {
            ResourceData data = new ResourceData();
            data.Type = type;
            data.Amount = 1000000;
            data.Limit = 1000000;
            resources.put(type, data);
        }
    }

    /**
     * 取得指定資源的數量
     * @param type 資源種類
     * @return 該資源的數量
     */
    public int getAmount(ResourceType type) {
        return resources.get(type).Amount;
    }

    /**
     * 設定指定資源的數量（不超過上限）
     * @param type 資源種類
     * @param amount 欲設定的數量
     */
    public void setAmount(ResourceType type, int amount) {
        ResourceData data = resources.get(type);
        data.Amount = Math.min(amount, data.Limit);
    }

    /**
     * 增加指定資源的數量（不超過上限）
     * @param type 資源種類
     * @param amount 增加的數量
     */
    public void add(ResourceType type, int amount) {
        ResourceData data = resources.get(type);
        data.Amount = Math.min(data.Amount + amount, data.Limit);
    }

    /**
     * 消耗指定資源的數量，若數量足夠則扣除並回傳 true，否則不變回傳 false
     * @param type 資源種類
     * @param amount 消耗的數量
     * @return 是否成功消耗
     */
    public boolean consume(ResourceType type, int amount) {
        ResourceData data = resources.get(type);
        if (data.Amount >= amount) {
            data.Amount -= amount;
            return true;
        }
        return false;
    }

    /**
     * 設定指定資源的上限，若目前數量超過上限則一併調整
     * @param type 資源種類
     * @param limit 上限值
     */
    public void setLimit(ResourceType type, int limit) {
        ResourceData data = resources.get(type);
        data.Limit = limit;
        if (data.Amount > limit) {
            data.Amount = limit;
        }
    }

    /**
     * 取得指定資源的上限
     * @param type 資源種類
     * @return 上限值
     */
    public int getLimit(ResourceType type) {
        return resources.get(type).Limit;
    }

    /**
     * 取得所有資源的資料集合
     * @return Map<ResourceType, ResourceData>
     */
    public Map<ResourceType, ResourceData> getAllResources() {
        return resources;
    }
}
