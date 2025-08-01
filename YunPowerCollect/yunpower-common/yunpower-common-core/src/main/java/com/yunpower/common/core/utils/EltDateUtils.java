package com.yunpower.common.core.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: 时间工具类
 * @Author: Jiajiaglam
 * @date: 2023-07-18 08:30
 * @description:
 */
public class EltDateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYYMM = "yyyyMM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, YYYYMMDD);
    }

    /**
     * 日期路径 即年/月 如201808
     */
    public static String dateTimeMontrh() {
        Date now = new Date();
        return DateFormatUtils.format(now, YYYYMM);
    }

    /**
     * 获取过去第几天的日期（YYYYMMDD）
     * 未来用负数
     */
    public static String getPastDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMMDD);
        return format.format(today);
    }

    /**
     * 获取过去第几个月的日期（YYYYMM）
     * 未来用负数
     */
    public static String getPastMonth(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - months);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(YYYYMM);
        return format.format(today);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 拼接成存储日期
     */
    public static Date packageSaveDate(String date, Integer hour, Integer minute) {
        String strDatetime = date + " " + StringUtils.padl(hour, 2) + ":" + StringUtils.padl(minute, 2) + ":00";
        return parseDate(strDatetime);
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate   最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 获取今年年份
     *
     * @return 年
     */
    public static Integer getCurrYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取现在是当年第几周
     *
     * @return 第几周
     */
    public static Integer getCurrWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取现在的小时
     *
     * @return hour
     */
    public static Integer getCurrHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取现在的分
     *
     * @return minute
     */
    public static Integer getCurrMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取指定日期过去几个小时的日期（YYYYMMDDHHMMSS）
     *
     * @param dateTime 指定日期
     * @param hours    过去几个小时
     * @return 结果
     */
    public static String getPastHour(String dateTime, Integer hours) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        localDateTime = localDateTime.minusHours(hours); //-N天，往前推
        return localDateTime.format(DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS));
    }

    /**
     * 将日期格式：YYYYMMDDHHMMSS 转换为 yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateLong(String dateTime){
        if (dateTime.length() == 14) {
            dateTime = dateTime.substring(0, 4) + "-" + dateTime.substring(4, 6) + "-" + dateTime.substring(6, 8) + " "
                    + dateTime.substring(8, 10) + ":" + dateTime.substring(10, 12) + ":" + dateTime.substring(12, 14);
        }

        return dateTime;
    }

    /**
     * 日期转搜索参数（日存储表，原汁原味）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForDay(String beginTime, String endTime) {
        if (StrUtil.isEmpty(beginTime) || StrUtil.isEmpty(endTime)) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return map;
    }
}
