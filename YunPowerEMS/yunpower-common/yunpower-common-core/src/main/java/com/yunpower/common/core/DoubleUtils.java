package com.yunpower.common.core;

import com.yunpower.common.core.utils.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


/**
 * 浮点工具类
 */
public class DoubleUtils {

    /**
     * stringToDouble
     * */
    public static double stringToDouble(String str) {
        if (StringUtils.isEmpty(str)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(str);
        return bd.doubleValue();
    }

    /**
     * floatToDouble
     * 保留2位小数
     */
    public static double floatToDouble(float floatValue) {
        BigDecimal bd = new BigDecimal(Float.toString(floatValue));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * floatToDouble
     * 保留N位小数
     */
    public static double floatToDoubleNPoint(float floatValue, int newScale) {
        BigDecimal bd = new BigDecimal(Float.toString(floatValue));
        bd = bd.setScale(newScale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 保留2位小数
     */
    public static double fmt2Point(double doubleValue) {
        BigDecimal bd = new BigDecimal(doubleValue);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 保留N位小数
     */
    public static double fmtNPoint(double doubleValue, int newScale) {
        BigDecimal bd = new BigDecimal(doubleValue);
        bd = bd.setScale(newScale, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 计算百分比，并返回百分比
     *
     * @param front 被减数
     * @param back  减数
     * @return 结果
     */
    public static Double computePercent(Double front, Double back) {
        if (front == null) {
            return null;
        }
        if (back == 0) {
            return front == 0 ? 0D : 100D;
        }
        double result = (front - back) / back;
        return fmtPercent(result, 2);
    }

    /**
     * 计算数组的比值（同比或环比）
     *
     * @param front 当前值数组
     * @param back  比较值数组（同比或环比的值）
     * @return 比值数组
     */
    public static List<Double> computePercents(List<Double> front, List<Double> back) {
        List<Double> ratios = new ArrayList<>();
        for (int i = 0; i < front.size(); i++) {
            Double ratio = null;
            if (i < back.size()) {
                ratio = computePercent(front.get(i), back.get(i));
            }
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
    public static double calculateRatio(Double value, Double baseValue) {
        if (value == null || value == 0) {
            return 0;
        }
        //基准值为0直接返回100
        if (baseValue == 0) {
            return 100;
        }
        // 计算比值
        return fmtNPoint(value / baseValue, 2);
    }

    /**
     * 计算两个浮点数的比值，并保留两位小数
     *
     * @param value     浮点数值
     * @param baseValue 基准浮点数值
     * @return 两个浮点数的比值，保留两位小数
     * @throws IllegalArgumentException 如果基准值为0
     */
    public static double calculateRatioPercent(Double value, Double baseValue) {
        if (value == null || value == 0) {
            return 0;
        }
        //基准值为0直接返回100
        if (baseValue == 0) {
            return 100;
        }
        // 计算比值
        double ratio = value / baseValue;
        return fmtPercent(ratio, 2);
    }

    /**
     * 计算两个浮点数的比值，并向上取整
     *
     * @param value     浮点数值
     * @param baseValue 基准浮点数值
     * @return 两个浮点数的比值，向上取整
     * @throws IllegalArgumentException 如果基准值为0
     */
    public static double calculateRatioCeil(Double value, Double baseValue) {
        if (value == null || value == 0) {
            return 0;
        }
        //基准值为0直接返回100
        if (baseValue == 0) {
            return 100;
        }
        // 计算比值并向上取整
        return fmtNPoint(value / baseValue, 2);
    }

    /**
     * 百分比
     *
     * @param f      数字
     * @param nPoint 保留N位小数
     * @return 56.62%
     */
    public static double fmtPercent(double f, int nPoint) {
        return fmtNPoint(f * 100, nPoint);
    }


    /**
     * 原值输出为字符串
     * */
    public static String toPlainString(double value){
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toPlainString();
    }
}
