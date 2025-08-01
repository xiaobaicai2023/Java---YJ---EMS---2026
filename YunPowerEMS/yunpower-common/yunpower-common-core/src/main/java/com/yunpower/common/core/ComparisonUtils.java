package com.yunpower.common.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 计算比值
 *
 * @author: XIAOTONG.CAO
 */
public class ComparisonUtils {

    /**
     * 计算比值（同比或环比）
     *
     * @param currentValue    当前值
     * @param comparisonValue 比较值（同比或环比的值）
     * @return 比值
     */
    public static Float calculateComparisonRatio(Float currentValue, Float comparisonValue) {
        if (comparisonValue == 0) {
            return currentValue == 0 ? 0.0f : 100.0f;
        }
        return (currentValue - comparisonValue) / comparisonValue * 100.0f;
    }

    /**
     * 计算数组的比值（同比或环比）
     *
     * @param currentValues    当前值数组
     * @param comparisonValues 比较值数组（同比或环比的值）
     * @return 比值数组
     */
    public static List<Float> calculateComparisonRatios(List<Float> currentValues, List<Float> comparisonValues) {
        if (currentValues.size() != comparisonValues.size()) {
            throw new IllegalArgumentException("Both arrays must have the same length.");
        }

        List<Float> ratios = new ArrayList<>();
        for (int i = 0; i < currentValues.size(); i++) {
            Float ratio = calculateComparisonRatio(currentValues.get(i), comparisonValues.get(i));
            ratios.add(ratio);
        }
        return ratios;
    }


    /**
     * 计算两个浮点数的比值，并保留两位小数
     *
     * @param value     浮点数值
     * @param baseValue 基准浮点数值
     * @return 两个浮点数的比值，保留两位小数
     * @throws IllegalArgumentException 如果基准值为0
     */
    public static float calculateRatio(Float value, Float baseValue) {
        // 检查基准值是否为0，避免除以0
        if (baseValue == 0) {
            throw new IllegalArgumentException("基准值不能为0");
        }

        // 计算比值
        float ratio = value / baseValue;

        // 使用 BigDecimal 保留两位小数
        BigDecimal bd = new BigDecimal(Float.toString(ratio));
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        // 返回保留两位小数的比值
        return bd.floatValue();
    }


    public static void main(String[] args) {
//        Float currentValue = 0f;
//        Float yoyValue = 100f;  // 去年同期值
//        Float momValue = 110.0f;  // 上月值
//
//        try {
//            Float yoyRatio = calculateComparisonRatio(currentValue, yoyValue);
//            System.out.println("同比值: " + yoyRatio + "%");
//
//            Float momRatio = calculateComparisonRatio(currentValue, momValue);
//            System.out.println("环比值: " + momRatio + "%");
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }
//
//
//        List<Float> currentValues = Arrays.asList(120.0f, 150.0f, 180.0f);
//        List<Float> yoyValues = Arrays.asList(100.0f, 140.0f, 160.0f);  // 去年同期值
//        List<Float> momValues = Arrays.asList(110.0f, 145.0f, 175.0f);  // 上月值
//
//
//        try {
//            List<Float> yoyRatios = calculateComparisonRatios(currentValues, yoyValues);
//            System.out.println("同比值: " + yoyRatios);
//
//            List<Float> momRatios = calculateComparisonRatios(currentValues, momValues);
//            System.out.println("环比值: " + momRatios);
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }


//        Float currentValue = 551221.50F;
//        Float chainValue = 550576.19F;
//        Float momRatio =  FormatUtils.computePercent(currentValue,chainValue);
//        System.out.println("环比值: " + momRatio);

        String name = "车间/单元1/区域1";
        System.out.println(name.substring(name.indexOf("/") + 1));
    }
}
