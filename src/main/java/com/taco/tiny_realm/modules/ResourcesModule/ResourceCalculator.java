package com.taco.tiny_realm.modules.ResourcesModule;

/**
 * ResourceCalculator 專門負責資源相關的運算，例如產量、消耗、資源轉換等。
 * 將運算邏輯與資源管理分離，方便維護與擴充。
 */
public class ResourceCalculator {
    /**
     * 計算資源產量
     * @param base 基礎產量
     * @param rate 每單位時間產量增加率
     * @param time 經過的時間（單位可自訂）
     * @return 計算後的總產量
     */
    public int calculateProduction(int base, double rate, int time) {
        return (int) (base + rate * time);
    }

    /**
     * 計算資源消耗
     * @param base 基礎消耗量
     * @param multiplier 消耗倍率
     * @return 計算後的消耗量
     */
    public int calculateConsumption(int base, double multiplier) {
        return (int) (base * multiplier);
    }

    /**
     * 資源轉換（如兌換、合成等）
     * @param amount 原始資源數量
     * @param ratio 轉換比例
     * @return 轉換後的資源數量
     */
    public int convertResource(int amount, double ratio) {
        return (int) (amount * ratio);
    }
}
