package com.yunpower.common.core.utils;

import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * 浮点工具类
 * */
public class DoubleUtils {

    /**
     * stringToDouble
     * */
    public static double stringToDouble(String str) {
        if (StrUtil.isEmpty(str)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(str);
        return bd.doubleValue();
    }

    /**
     * 保留4位小数
     * */
    public static double parse(float floatValue) {
        BigDecimal bd = new BigDecimal(Float.toString(floatValue));
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 保留2位小数
     * */
    public static double parse2(float floatValue) {
        BigDecimal bd = new BigDecimal(Float.toString(floatValue));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 默认 保留4位小数
     * */
    public static double parse(double doubleValue) {
        BigDecimal bd = new BigDecimal(doubleValue);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    /**
     * 保留2位小数
     * */
    public static double parse2(double doubleValue) {
        BigDecimal bd = new BigDecimal(doubleValue);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 原值输出为字符串
     * */
    public static String toPlainString(double value){
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(4, RoundingMode.HALF_UP);
        return bd.toPlainString();
    }
}
