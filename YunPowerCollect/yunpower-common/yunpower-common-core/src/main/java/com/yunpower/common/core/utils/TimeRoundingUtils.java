package com.yunpower.common.core.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @title: 时间向后取整
 * @Author: Jiajiaglam
 * @date: 2024-02-02 17:16
 * @description:
 */
public class TimeRoundingUtils {

    /**
     * 存储时间，向后取整
     * 比如 12:03 以5分钟为单位，那么就取 12:05
     *
     * @param dateTime 当前时间
     * @param minutes  时间刻度
     */
    public static String getNextScaleTime(String dateTime, Integer minutes) {
        if (dateTime.length() == 14) {
            dateTime = EltDateUtils.formatDateLong(dateTime);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZonedDateTime now = ZonedDateTime.parse(dateTime, formatter.withZone(ZoneId.systemDefault()));
        //System.out.println("时间1：" + now.format(formatter));

        ZonedDateTime nextMinBoundary = roundToNext5Minutes(now, minutes);
        String nextMinStr = nextMinBoundary.format(formatter);
        //System.out.println("时间2：" + nextMinStr);

        String preTime = nextMinStr.substring(0, 14);
        int minute = Integer.parseInt(nextMinStr.substring(14, 16));
        int rightMinute = (minute / minutes) * minutes;
        //System.out.println("正确3：" + preTime + StringUtils.padl(rightMinute, 2) + ":00");

        return preTime + StringUtils.padl(rightMinute, 2) + ":00";
    }

    public static ZonedDateTime roundToNext5Minutes(ZonedDateTime dateTime, Integer minutes) {
        // 获取距离下一N分钟刻度的秒数差
        long secondsUntilNext5Min = ChronoUnit.SECONDS.between(dateTime, dateTime.truncatedTo(ChronoUnit.MINUTES).plus(minutes, ChronoUnit.MINUTES));

        // 向后取整，加上剩余秒数到达下一N分钟
        return dateTime.plus(secondsUntilNext5Min, ChronoUnit.SECONDS);
    }
}
