package com.yunpower.common.core.utils;

/**
 * @title: 单位换算
 * @Author: Jiajiaglam
 * @date: 2024-02-23 17:37
 * @description: 已使用字典配置数据，这里已不使用，保留待查
 */
public class ConversionUtils {
    // 1MWh=1000KWh
    // 1度电转标准煤（吨）
    public static final float CONVERSION_ELECTRIC_COAL = 0.0001229f;

    // 1度电（KWh）转化为 二氧化碳 CO2（千克）
    public static final float CONVERSION_ELECTRIC_CO2 = 0.785f;

    // 1吨水 转化为 二氧化碳 CO2（千克）
    public static final float CONVERSION_WATER_CO2 = 0.91f;

    // 1升汽油 转化为 二氧化碳 CO2（千克）
    public static final float CONVERSION_OIL = 2.7f;

    // 1立方米天然气 转化为 二氧化碳 CO2（千克）
    public static final float CONVERSION_GAS = 0.19f;

    // 一棵树每天吸收二氧化碳 CO2（千克）
    public static final float CONVERSION_TREE_CO2 = 5.023f;

    // 一棵树每天产生氧气 O2（千克）
    public static final float CONVERSION_TREE_O2 = 3.653f;

    // 1吨标煤燃烧 产生 碳（C）（千克）
    public static final float CONVERSION_COAL_C = 0.67f;

    // 1吨标煤燃烧 产生 二氧化碳 CO2（千克）
    public static final float CONVERSION_COAL_CO2 = 2.46f;

    /**
     * 电度 转化 标准煤
     *
     * @param electric 电量（kwh）
     * @return 标准煤（t）
     */
    public static float electricConserveCoal(float electric) {
        if (electric <= 0) {
            return 0f;
        }
        return electric * CONVERSION_ELECTRIC_COAL;
    }

    /**
     * 电度 转化 二氧化碳
     *
     * @param electric 电量（kwh）
     * @return 二氧化碳（千克）
     */
    public static float electricConserveCO2(float electric) {
        if (electric <= 0) {
            return 0f;
        }
        return electric * CONVERSION_ELECTRIC_CO2;
    }

    /**
     * 电度 等效 植树量
     *
     * @param electric 电量（kwh）
     * @return 二植树量
     */
    public static float electricConserveTree(float electric) {
        if (electric <= 0) {
            return 0f;
        }
        return electric * CONVERSION_ELECTRIC_CO2 / CONVERSION_TREE_CO2;
    }
}
