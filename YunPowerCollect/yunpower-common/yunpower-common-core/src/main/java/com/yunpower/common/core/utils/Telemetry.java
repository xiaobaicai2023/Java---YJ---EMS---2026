package com.yunpower.common.core.utils;

/**
 * @title: 数据计算
 * @Author: Jiajiaglam
 * @date: 2023-12-12 16:39
 * @description:
 */
public class Telemetry {

    /**
     * 归一化值
     */
    public static float Bytes2Float_NVA(int low, int high) {
        float fVal;
        int nva = (high << 8) + low;

        // 符号位1位，0为正数，1为负数，后面的为补码表示，
        // 正数的补码和原码相同不需要转换。对于负数,先取反码再加1得到补码的补码，就是原码了。
        // 符号位：0表示正数，1表示负数
        int symbol = (high & 0x80);
        if (symbol == 0x80) {
            // (nva ^ 0xffff) + 1 :补码的补码  ，取出后面的15位数据乘上-1得到值
            fVal = -1 * (((nva ^ 0xffff) + 1) & 0x7fff);
        } else {
            fVal = nva;
        }

        //这里的32767值的是一个量纲，一般这个参数是主站和从站商定的
        return fVal / 32767;
    }

    /**
     * 标度化值
     */
    public static float Bytes2Float_SVA(int low, int high) {
        float fVal;
        int nva = (high << 8) + low;

        //0表示正数，1表示负数
        int symbol = (high & 0x80);
        if (symbol == 0x80) {
            fVal = -1 * (((nva ^ 0xffff) + 1) & 0x7fff);
        } else {
            fVal = nva;
        }
        return fVal;
    }

    /**
     * 十六进制转单精度
     *
     * @param hex 从低位到高位按顺序
     */
    public static float Bytes2Float_IEEE754(String hex) {
        return Float.intBitsToFloat(Integer.parseInt(hex, 16));
    }

    /**
     * 数组转单精度（可识别负数）
     */
    public static float Bytes2Float_IEEE754(byte[] bytes) {
        return Float.intBitsToFloat(byte4ToInt(bytes));
    }

    /**
     * 数组转双精度（可识别负数），并保留4位小数
     */
    public static double Bytes2Double_IEEE754(byte[] bytes) {
        float floatValue = Float.intBitsToFloat(byte4ToInt(bytes));
        return DoubleUtils.parse(floatValue);
    }

    public static int byte4ToInt(byte[] bytes) {
        int b0 = bytes[0] & 0xFF;
        int b1 = bytes[1] & 0xFF;
        int b2 = bytes[2] & 0xFF;
        int b3 = bytes[3] & 0xFF;
        return b0 << 24 & 0xFF000000 | b1 << 16 & 0xFF0000 | b2 << 8 & 0xFF00 | b3 & 0xFF;
    }
}
