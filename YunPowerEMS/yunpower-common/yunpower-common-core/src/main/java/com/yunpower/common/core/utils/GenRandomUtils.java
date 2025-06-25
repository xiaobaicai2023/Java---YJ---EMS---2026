package com.yunpower.common.core.utils;

import java.util.Random;

/**
 * @title: 随机数生成类
 * @Author: Jiajiaglam
 * @date: 2023-10-01 10:34
 * @description:
 */
public class GenRandomUtils {

    /**
     * 随机数（数字、字母混合）
     *
     * @param length 长度
     * @return 结果
     */
    public static String GenRandom(Integer length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";

            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(random.nextInt(10));
            }
        }
        return val.toString();
    }

    /**
     * 随机数（纯字母）
     *
     * @param length 长度
     * @return 结果
     */
    public static String GenRandomLetter(Integer length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            //输出是大写字母还是小写字母
            int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
            val.append((char) (random.nextInt(26) + temp));
        }

        return val.toString();
    }

    /**
     * 随机数（纯数字）
     *
     * @param length 长度
     * @return 结果
     */
    public static String GenRandomNumber(Integer length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            val.append(String.valueOf(random.nextInt(10)));
        }

        return val.toString();
    }

    /**
     * 纳秒时间戳
     *
     * @param length 长度
     * @return 结果
     */
    public static String GenRandomNano(Integer length) {
        long currentTimeNanos = System.nanoTime();
        currentTimeNanos = currentTimeNanos / 1000L;
        String nanos = String.valueOf(currentTimeNanos);
        return nanos.substring(nanos.length() - length);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(GenRandomNano(10));
        }
    }
}
