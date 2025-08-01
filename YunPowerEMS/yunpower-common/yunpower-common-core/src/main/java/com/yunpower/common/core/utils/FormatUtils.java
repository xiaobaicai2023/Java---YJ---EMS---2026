package com.yunpower.common.core.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: 数字格式化
 * @Author: Jiajiaglam
 * @date: 2023-11-06 17:06
 * @description:
 */
public class FormatUtils {

    /**
     * 数字千分位符
     *
     * @param text       要格式化的字符串
     * @param isTowPoint 是否保留两位小数
     * @return 格式化结果
     */
    public static String fmtMicrometer(String text, int isTowPoint) {

        DecimalFormat df = null;

        if (isTowPoint == 1) {
            df = new DecimalFormat("###,##0.00");
        } else {
            df = new DecimalFormat("###,##0");
        }

        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }

        return df.format(number);
    }

    /**
     * 保留两位小数 Float
     *
     * @param f 数字
     * @return 格式化结果
     */
    public static String fmt2point(float f) {
        if (f == 0) {
            return "0.00";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String res = df.format(f);
        return fmt2PointStartwithZero(res);
    }

    /**
     * 保留N位小数 Float
     *
     * @param f      数字
     * @param length 位数
     * @return 格式化结果
     */
    public static String fmtNPoint(float f, int length) {
        StringBuilder zero = new StringBuilder();
        for (int i = 0; i < length; i++) {
            zero.append("0");
        }

        DecimalFormat df = new DecimalFormat("#");
        if (zero.length() > 0) {
            df = new DecimalFormat("#." + zero);
        }

        String res = df.format(f);
        return fmt2PointStartwithZero(res);
    }

    /**
     * 把 .00结果包装成 0.00
     *
     * @param res 数字
     * @return 格式化结果
     */
    private static String fmt2PointStartwithZero(String res) {
        if (res.startsWith(".")) {
            res = "0" + res;
        }
        if (res.startsWith("-.")) {
            res = "-0" + res.substring(1);
        }

        return res;
    }

    /**
     * 百分比
     *
     * @param f      数字
     * @param nPoint 保留N位小数
     * @return 56.62%
     */
    public static String fmtPercentSuffix(Float f, int nPoint) {
        f = f * 100;
        String fmt = fmtNPoint(f, nPoint);
        return fmt + "%";
    }

    /**
     * 百分比
     *
     * @param f      数字
     * @param nPoint 保留N位小数
     * @return 56.62%
     */
    public static Float fmtPercent(Float f, int nPoint) {
        f = f * 100;
        String fmt = fmtNPoint(f, nPoint);
        return Float.parseFloat(fmt);
    }

    /**
     * 计算百分比，并返回百分比
     *
     * @param front 被减数
     * @param back  减数
     * @return 结果
     */
    public static Float computePercent(Float front, Float back) {
        if (front == null) {
            return null;
        }

        if (back == 0) {
            return front == 0 ? 0.0f : 100.0f;
        }
        float result = (front - back) / back;
        return fmtPercent(result, 2);
    }


    /**
     * 计算百分比，并返回百分比
     *
     * @param front 被减数
     * @param back  减数
     * @return 结果
     */
    public static Float computePercent(Object front, Object back) {
        float front_float = Float.parseFloat(front.toString());
        float back_float = Float.parseFloat(back.toString());
        float result = (front_float - back_float) / back_float;
        return fmtPercent(result, 2);
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
        if (value == null || value == 0) {
            return 0;
        }
        //基准值为0直接返回100
        if (baseValue == 0) {
            return 100;
        }
        // 计算比值
        return Math.round(value / baseValue * 100) / 100.0f;
    }

    /**
     * 计算两个浮点数的比值，并向上取整
     *
     * @param value     浮点数值
     * @param baseValue 基准浮点数值
     * @return 两个浮点数的比值，向上取整
     * @throws IllegalArgumentException 如果基准值为0
     */
    public static float calculateRatioCeil(Float value, Float baseValue) {
        if (value == null || value == 0) {
            return 0;
        }
        //基准值为0直接返回100
        if (baseValue == 0) {
            return 100;
        }
        // 计算比值并向上取整
        return (float) Math.ceil(value / baseValue);
    }

    /**
     * 计算两个浮点数的比值，并保留两位小数
     *
     * @param value     浮点数值
     * @param baseValue 基准浮点数值
     * @return 两个浮点数的比值，保留两位小数
     * @throws IllegalArgumentException 如果基准值为0
     */
    public static float calculateRatioPercent(Float value, Float baseValue) {
        if (value == null || value == 0) {
            return 0;
        }
        //基准值为0直接返回100
        if (baseValue == 0) {
            return 100;
        }
        // 计算比值
        float ratio = value / baseValue;
        return fmtPercent(ratio, 2);
    }

    /**
     * 计算数组的比值（同比或环比）
     *
     * @param front 当前值数组
     * @param back  比较值数组（同比或环比的值）
     * @return 比值数组
     */
    public static List<Float> computePercents(List<Float> front, List<Float> back) {
        List<Float> ratios = new ArrayList<>();
        for (int i = 0; i < front.size(); i++) {
            Float ratio = null;
            if (i < back.size()) {
                ratio = computePercent(front.get(i), back.get(i));
            }
            ratios.add(ratio);
        }
        return ratios;
    }



    /**
     * 进位
     * @param value 值
     * @param exponent 1000
     * */
    public static float carryBit(Float value,Float exponent){
        if(value == null || value == 0){
            return 0;
        }
        if(value < exponent){
            return value;
        }
        value = calculateRatio(value,exponent);
        if(value > exponent){
            carryBit(value,exponent);
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.print(computePercent(-59.48,-2.13));
    }
}
