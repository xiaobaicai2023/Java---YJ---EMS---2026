package com.yunpower.common.core.utils;

import com.yunpower.common.core.enums.DateFormatEnum;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author yunpower
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue());
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM_DD.getValue());
    private static final DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM.getValue());
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD.getValue());

    public static Date getNowDate() {
        return new Date();
    }

    public static String getDate() {
        return dateTimeNow(DateFormatEnum.YYYY_MM_DD.getValue());
    }

    public static String getTime() {
        return dateTimeNow(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue());
    }

    public static String dateTimeNow() {
        return dateTimeNow(DateFormatEnum.YYYYMMDDHHMMSS.getValue());
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        if (date == null) {
            return null;
        }
        return parseDateToStr(DateFormatEnum.YYYY_MM_DD.getValue(), date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        if (ts == null) {
            return null;
        }

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
        return DateFormatUtils.format(now, DateFormatEnum.YYYYMMDD.getValue());
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
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 获取过去第几天的日期
     */
    public static String getPastDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD.getValue());
        return format.format(today);
    }

    /**
     * 获取指定日期 未来第几天的日期
     *
     * @param dateTime 日期
     * @param days     天数
     * @return 短日期
     */
    public static String getPastDate(String dateTime, int days) {
        LocalDateTime localDateTime = LocalDateTimeUtils.strToLocalDateTime(dateTime);
        localDateTime = localDateTime.plusDays(days); //+N天，往后推
        return LocalDateTimeUtils.parseDateToStr(localDateTime, DateFormatEnum.YYYY_MM_DD.getValue());
    }

    /**
     * 获取指定日期 未来第几个月的日期
     *
     * @param dateTime 日期
     * @param months   月数
     * @return 短日期
     */
    public static String getPastMonth(String dateTime, Integer months) {
        LocalDateTime localDateTime = LocalDateTimeUtils.strToLocalDateTime(dateTime);
        localDateTime = localDateTime.plusMonths(months);
        return LocalDateTimeUtils.parseDateToStr(localDateTime, DateFormatEnum.YYYY_MM_DD.getValue());
    }

    /**
     * 获取指定日期 未来第几多少分钟
     *
     * @param dateTime 日期
     * @param minutes  月数
     * @return 短日期
     */
    public static String getPastMinutes(String dateTime, Integer minutes) {
        LocalDateTime localDateTime = LocalDateTimeUtils.strToLocalDateTime(dateTime);
        localDateTime = localDateTime.plusMinutes(minutes);
        return LocalDateTimeUtils.parseDateToStr(localDateTime, DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue());
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
     * 将字符串格式化成想要的格式
     */
    public static String strdateFormat(String format, String dateStr) {
        if (dateStr == null) {
            return null;
        }
        Date date = parseDate(dateStr);
        return parseDateToStr(format, date);
    }

    /**
     * 日期转搜索参数（日存储表，闭合整天）
     *
     * @param date 日期
     * @return 结果
     */
    public static Map<String, Object> dateToParamForDay(Date date) {
        if (date == null) {
            return null;
        }

        String dateTime = dateTime(date);
        return dateToParamForDayFormat(dateTime, dateTime);
    }

    /**
     * 日期转搜索参数（日存储表，闭合整天）
     *
     * @param dateTime 日期时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForDay(String dateTime) {
        return dateToParamForDayFormat(dateTime, dateTime);
    }

    /**
     * 日期转搜索参数（日存储表，原汁原味）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForDay(String beginTime, String endTime) {
        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return map;
    }

    /**
     * 日期转搜索参数（日存储表，原汁原味）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForDayFormat(String beginTime, String endTime) {
        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime.substring(0, 10) + " 00:00:00");
        map.put("endTime", endTime.substring(0, 10) + " 23:59:59");
        return map;
    }

    /**
     * 日期转搜索参数（日存储表，往后推一天，计算累计值时用到）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForBackOneDay(String beginTime, String endTime) {
        if (StringUtils.isEmpty(beginTime)) {
            return null;
        }

        beginTime = beginTime.substring(0, 10) + " 00:00:00";

        LocalDateTime endDateTime = LocalDateTimeUtils.strToLocalDateTime(endTime);
        endDateTime = endDateTime.plusDays(1);
        String endDateStr = LocalDateTimeUtils.parseDateToStr(endDateTime, DateFormatEnum.YYYY_MM_DD.getValue());


        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endDateStr + " 00:00:00");
        return map;
    }

    /**
     * 日期转搜索参数（日存储表，往前推一天，计算环比用到）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForForwardOneDay(String beginTime, String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            return null;
        }

        LocalDateTime beginDateTime = LocalDateTimeUtils.strToLocalDateTime(beginTime);
        beginDateTime = beginDateTime.minusDays(1);
        String beginDateStr = LocalDateTimeUtils.parseDateToStr(beginDateTime, DateFormatEnum.YYYY_MM_DD.getValue());

        endTime = endTime.substring(0, 10) + " 23:59:59";

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginDateStr + " 23:00:00");
        map.put("endTime", endTime);
        return map;
    }

    /**
     * 日期转搜索参数（月统计表，闭合整月）
     *
     * @param date 日期
     * @return 结果
     */
    public static Map<String, Object> dateToParamForMonth(Date date) {
        if (date == null) {
            return null;
        }

        int year = getDateYear(date);
        int month = getDateMonth(date);
        int lastDay = getMonthLastDay(year, month);

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", year + "-" + StringUtils.padLeft(month, 2) + "-01 00:00:00");
        map.put("endTime", year + "-" + StringUtils.padLeft(month, 2) + "-" + StringUtils.padLeft(lastDay, 2) + " 23:59:59");
        return map;
    }

    /**
     * 日期转搜索参数（月统计表，范围内闭合整月）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForMonth(String beginTime, String endTime) {
        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return null;
        }

        int year = Integer.parseInt(endTime.split("-")[0]);
        int month = Integer.parseInt(endTime.split("-")[1]);
        int lastDay = getMonthLastDay(year, month);

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime.substring(0, 7) + "-01 00:00:00");
        map.put("endTime", endTime.substring(0, 7) + "-" + StringUtils.padLeft(lastDay, 2) + " 23:59:59");
        return map;
    }

    /**
     * 日期转搜索参数（月统计表，往后推一月，计算累计值时用到）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForBackOneMonth(String beginTime, String endTime) {
        if (StringUtils.isEmpty(beginTime)) {
            return null;
        }

        beginTime = beginTime.substring(0, 7) + "-01 00:00:00";

        LocalDateTime endDateTime = LocalDateTimeUtils.strToLocalDateTime(endTime);
        endDateTime = endDateTime.plusMonths(1);
        int endYear = endDateTime.getYear();
        int endMonth = endDateTime.getMonthValue();

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginTime);
        map.put("endTime", endYear + "-" + StringUtils.padLeft(endMonth, 2) + "-" + getMonthLastDay(endYear, endMonth) + " 23:59:59");
        return map;
    }

    /**
     * 日期转搜索参数（月统计表，往前推一月，计算环比用到）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForForwardOneMonth(String beginTime, String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            return null;
        }

        LocalDateTime beginDateTime = LocalDateTimeUtils.strToLocalDateTime(beginTime);
        beginDateTime = beginDateTime.minusMonths(1);

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", beginDateTime.getYear() + "-" + StringUtils.padLeft(beginDateTime.getMonthValue(), 2) + "-01 00:00:00");
        map.put("endTime", endTime.substring(0, 10) + " 23:59:59");
        return map;
    }

    /**
     * 日期转搜索参数（月统计表，闭合整年）
     *
     * @param year 年
     * @return 结果
     */
    public static Map<String, Object> dateToParamForYear(Integer year) {
        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", year + "-01-01 00:00:00");
        map.put("endTime", year + "-12-31 23:59:59");
        return map;
    }

    /**
     * 日期转搜索参数（月统计表，往前推一年，计算环比用到）
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 结果
     */
    public static Map<String, Object> dateToParamForForwardOneYear(String beginTime, String endTime) {
        if (StringUtils.isEmpty(endTime)) {
            return null;
        }

        LocalDateTime beginDateTime = LocalDateTimeUtils.strToLocalDateTime(beginTime);
        int beginYear = beginDateTime.getYear();

        LocalDateTime endDateTime = LocalDateTimeUtils.strToLocalDateTime(endTime);
        int endYear = endDateTime.getYear();

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", (beginYear - 1) + "-01-01 00:00:00");
        map.put("endTime", endYear + "-12-31 23:59:59");
        return map;
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
     * 获取本月月份
     *
     * @return 月
     */
    public static Integer getCurrMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取年月
     *
     * @return 例：2022-07
     */
    public static String getCurrYearMonth() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "-" + StringUtils.padLeft(month, 2);
    }

    /**
     * 获取今天是几号
     *
     * @return 号
     */
    public static Integer getCurrDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取【上个月】第一天
     *
     * @return 例：2023-05-01
     */
    public static String getLastMonthFirstDay() {
        Calendar lastMonthFirstDateCal = Calendar.getInstance();
        lastMonthFirstDateCal.add(Calendar.MONTH, -1);
        lastMonthFirstDateCal.set(Calendar.DAY_OF_MONTH, 1);
        return simpleDateFormat.format(lastMonthFirstDateCal.getTime());
    }

    /**
     * 获取【上个月】最后一天
     *
     * @return 例：2023-05-31
     */
    public static String getLastMonthEndDay() {
        Calendar lastMonthEndDateCal = Calendar.getInstance();
        lastMonthEndDateCal.add(Calendar.MONTH, -1);
        lastMonthEndDateCal.set(Calendar.DAY_OF_MONTH, lastMonthEndDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return simpleDateFormat.format(lastMonthEndDateCal.getTime());
    }

    /**
     * 获取【本月】第一天
     *
     * @return 例：2023-05-01
     */
    public static String getThisMonthFirstDay() {
        Calendar thisMonthFirstDateCal = Calendar.getInstance();
        thisMonthFirstDateCal.set(Calendar.DAY_OF_MONTH, 1);

        return simpleDateFormat.format(thisMonthFirstDateCal.getTime());
    }

    /**
     * 获取【本月】最后一天
     *
     * @return 例：2023-05-31
     */
    public static String getThisMonthEndDay() {
        Calendar thisMonthEndDateCal = Calendar.getInstance();
        thisMonthEndDateCal.set(Calendar.DAY_OF_MONTH, thisMonthEndDateCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return simpleDateFormat.format(thisMonthEndDateCal.getTime());
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
     * 获取现在是当年第几周
     *
     * @return 第几周
     */
    public static Integer getCurrWeeks() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定日期 年
     *
     * @param date 日期
     * @return 年
     */
    public static Integer getDateYear(Date date) {
        String year = String.format("%tY", date);
        return Integer.valueOf(year);
    }

    /**
     * 获取指定日期 年
     *
     * @param date 日期
     * @return 年
     */
    public static String getDateYear(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String year = null;
        try {
            Date dt = parseDate(date);
            year = format.format(new Date(dt.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return year;
    }

    /**
     * 获取指定日期 月
     *
     * @param date 日期
     * @return 月
     */
    public static Integer getDateMonth(Date date) {
        String mon = String.format("%tm", date);
        return Integer.valueOf(mon);
    }

    /**
     * 获取指定日期 年-月
     *
     * @param date 日期
     * @return 年-月
     */
    public static String getYearMonth(Date date) {
        return parseDateToStr(DateFormatEnum.YYYY_MM.getValue(), date);
    }

    /**
     * 获取指定日期 日
     *
     * @param date 日期
     * @return 日
     */
    public static Integer getDateDay(Date date) {
        String day = String.format("%td", date);
        return Integer.valueOf(day);
    }

    /**
     * 获取指定日期 日
     *
     * @param date 日期
     * @return 日
     */
    public static String getDateDay(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String dd = null;
        try {
            Date dt = parseDate(date);
            dd = format.format(new Date(dt.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd;
    }

    /**
     * 获取现在是当年第几周
     *
     * @param date 日期
     * @return 第几周
     */
    public static Integer getDateWeeks(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定时间 小时
     *
     * @param date 时间
     * @return 时
     */
    public static String getDateHour(String date) {
        SimpleDateFormat format = new SimpleDateFormat("HH");
        String hour = null;
        try {
            Date dt = parseDate(date);
            hour = format.format(new Date(dt.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hour;
    }

    /**
     * 获取指定时间 小时
     *
     * @param date 时间
     * @return 时
     */
    public static String getDateHour(Date date) {
        return String.format("%tH", date);
    }

    /**
     * 获取指定时间 分
     *
     * @param date 时间
     * @return 分
     */
    public static String getDateMinute(String date) {
        SimpleDateFormat format = new SimpleDateFormat("mm");
        String minute = null;
        try {
            Date dt = parseDate(date);
            minute = format.format(new Date(dt.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minute;
    }

    /**
     * 获取指定日期 分
     *
     * @param date 日期
     * @return 分
     */
    public static String getDateMinute(Date date) {
        return String.format("%tM", date);
    }

    /**
     * 获取上个月的今天
     *
     * @return date
     */
    public static Date getLastMonthToday() {
        Date date = new Date();
        return getLastMonthToday(date);
    }

    /**
     * 获取上个月的今天
     *
     * @param date 指定日期
     * @return date
     */
    public static Date getLastMonthToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); //设置为上一个月
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取某个月的最后一天
     *
     * @param month 月
     * @return 天
     */
    public static int getMonthLastDay(int year, int month) {
        int day = 0;

        if (month == 2) {
            if (year % 4 == 0) {
                day = 29;
            } else {
                day = 28;
            }
            return day;
        }

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            default:
                day = 30;
        }

        return day;
    }

    /**
     * 生成 “年-月-日” 日期序列
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期列表
     */
    public static List<String> getDayList(String startDate, String endDate) {
        List<String> res = new ArrayList<>();
        if (compareShortDate(endDate, startDate)) {
            return res;
        }

        if (startDate.length() == 10) {
            startDate += " 00:00:00";
        }
        if (endDate.length() == 10) {
            endDate += " 00:00:00";
        }

        //先减一天再计算
        LocalDate newStartDate = LocalDate.parse(startDate, dateTimeFormat).minusDays(1);
        LocalDate newEndDate = LocalDate.parse(endDate, dateTimeFormat);

        while (!newStartDate.equals(newEndDate)) {
            newStartDate = newStartDate.plusDays(1);
            String dateString = dateFormat.format(newStartDate);
            res.add(dateString);
        }
        return res;
    }

    /**
     * 生成 “年-月-日” 日期序列
     *
     * @param startDate 开始日期
     * @param gap       长度
     * @return 日期列表
     */
    public static List<String> getDayList(String startDate, int gap) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < gap; i++) {
            LocalDate newDate = LocalDate.parse(startDate, dateFormat).plusDays(i);
            String dateString = dateFormat.format(newDate);
            res.add(dateString);
        }
        return res;
    }

    /**
     * 生成 “年-月-日” 日期序列
     *
     * @param gap     长度
     * @param endDate 结束日期
     * @return 日期列表
     */
    public static List<String> getDayList(int gap, String endDate) {
        List<String> res = new ArrayList<>();
        for (int i = gap - 1; i >= 0; i--) {
            LocalDate newDate = LocalDate.parse(endDate, dateFormat).minusDays(i);
            String dateString = dateFormat.format(newDate);
            res.add(dateString);
        }
        return res;
    }

    /**
     * 生成 “年-月” 日期序列
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期列表
     */
    public static List<String> getYearList(String startDate, String endDate) {
        List<String> res = new ArrayList<>();
        int startYear = Integer.parseInt(getDateYear(startDate));
        int endYear = Integer.parseInt(getDateYear(endDate));
        for (int i = startYear; i <= endYear; i++) {
            res.add(i + "");
        }
        return res;
    }

    /**
     * 生成 “年-月” 日期序列
     *
     * @param gap     长度
     * @param endDate 结束日期
     * @return 日期列表
     */
    public static List<String> getMonthList(int gap, String endDate) {
        List<String> res = new ArrayList<>();
        for (int i = gap - 1; i >= 0; i--) {
            LocalDate newDate = LocalDate.parse(endDate, dateFormat).minusMonths(i);
            String dateString = monthFormat.format(newDate);
            res.add(dateString);
        }
        return res;
    }

    /**
     * 生成 “年-月” 日期序列
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 日期列表
     */
    public static List<String> getMonthList(String startDate, String endDate) {
        List<String> res = new ArrayList<>();

        //先减一个月再计算
        LocalDate newStartDate = LocalDate.parse(startDate, dateTimeFormat).minusMonths(1);
        String startMonthDate = monthFormat.format(newStartDate);
        String endMonthDate = endDate.substring(0, 7);

        while (!startMonthDate.equals(endMonthDate)) {
            newStartDate = newStartDate.plusMonths(1);
            startMonthDate = monthFormat.format(newStartDate);
            res.add(startMonthDate);
        }

        return res;
    }

    /**
     * 生成 “小时” 日期序列
     *
     * @return 日期列表
     */
    public static List<String> getHourList() {
        return getHourList(0, 23);
    }

    /**
     * 生成 “小时” 日期序列
     *
     * @param startHour 开始时间
     * @param endHour   结束时间
     * @return 日期列表
     */
    public static List<String> getHourList(Integer startHour, Integer endHour) {
        List<String> res = new ArrayList<>();
        for (int i = startHour; i <= endHour; i++) {
            res.add(i + "时");
        }
        return res;
    }

    /**
     * 生成 “小时” 日期序列
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 日期列表
     */
    public static List<String> getHourList(String startTime, String endTime) {
        Integer startHour = Integer.parseInt(getDateHour(startTime));
        Integer endHour = Integer.parseInt(getDateHour(endTime));
        return getHourList(startHour, endHour);
    }

    /**
     * 生成 “分” 日期序列
     *
     * @param gap 间隔
     * @return 日期列表
     */
    public static List<String> getMinuteList(Integer gap) {
        return getMinuteList(0, 23, gap);
    }

    /**
     * 生成 “分” 日期序列
     *
     * @param startHour 开始小时
     * @param endHour   结束小时
     * @param gap       间隔
     * @return 日期列表
     */
    public static List<String> getMinuteList(Integer startHour, Integer endHour, Integer gap) {
        List<String> res = new ArrayList<>();
        if (60 % gap != 0) {
            return res;
        }

        for (int i = startHour; i <= endHour; i++) {
            int n = 60 / gap;
            for (int j = 0; j < n; j++) {
                res.add(StringUtils.padLeft(i, 2) + ":" + StringUtils.padLeft(j * gap, 2));
            }
        }
        return res;
    }

    /**
     * 生成 “分” 日期序列
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param gap       间隔
     * @return 日期列表
     */
    public static List<String> getMinuteList(String startTime, String endTime, Integer gap) {
        Integer startHour = Integer.parseInt(getDateHour(startTime));
        Integer endHour = Integer.parseInt(getDateHour(endTime));
        return getMinuteList(startHour, endHour, gap);
    }

    /**
     * 比较两个日期大小（精确到秒）
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return 正值表是【beginTime】日期大
     */
    public static Integer compareDateTime(String beginTime, String endTime) {
        return beginTime.compareTo(endTime);
    }

    /**
     * 比较两个日期大小（精确到秒）
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return True表示【beginTime】日期小
     */
    public static Boolean compareDateTimeB(String beginTime, String endTime) {
        try {
            SimpleDateFormat simpleDateTimeFornat = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue());
            Date d1 = simpleDateTimeFornat.parse(beginTime);
            Date d2 = simpleDateTimeFornat.parse(endTime);
            return d1.before(d2);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 比较两个日期是否相等（精确到分）
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return True表示两个日期相等
     */
    public static Boolean compareDateEqual(String beginTime, String endTime) {
        try {
            SimpleDateFormat simpleDateMinuteFormat = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM.getValue());
            Date d1 = simpleDateMinuteFormat.parse(beginTime);
            Date d2 = simpleDateMinuteFormat.parse(endTime);
            return d1.equals(d2);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 比较两个日期大小（精确到天）
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return True表示【beginTime】日期小
     */
    public static Boolean compareShortDate(String beginTime, String endTime) {
        try {
            Date d1 = simpleDateFormat.parse(beginTime);
            Date d2 = simpleDateFormat.parse(endTime);
            return d1.before(d2);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 比较两个时间大小（只判断时间）
     *
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return True表示【beginTime】时间小
     */
    public static Boolean compareTime(String beginTime, String endTime) {
        try {
            String[] arrBegin = beginTime.split(" ");
            String[] arrEnd = endTime.split(" ");

            String newBegin = "2023-10-25 " + arrBegin[arrBegin.length - 1];
            String newEnd = "2023-10-25 " + arrEnd[arrEnd.length - 1];

            SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue());
            Date beginDate = simpleDateTimeFormat.parse(newBegin);
            Date endDate = simpleDateTimeFormat.parse(newEnd);

            return beginDate.before(endDate);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 强制补全【日】时间
     *
     * @param date  要补全的时间
     * @param begin true开始时间，false结束时间
     * @return 完整时间
     */
    public static String completionDayTime(String date, boolean begin) {
        if (null == date || date.length() == 19) {
            return date;
        }

        if (begin) {
            return date.substring(0, 10) + " 00:00:00";
        } else {
            return date.substring(0, 10) + " 23:59:59";
        }
    }

    /**
     * 强制补全【日】时间
     *
     * @param date  要补全的时间
     * @param begin true开始时间，false结束时间
     * @return 完整时间
     */
    public static String completionDayTime(String date, boolean begin, boolean isFullDate) {
        if (null == date || date.length() == 19) {
            return date;
        }

        if (begin) {
            return date.substring(0, 10) + " 00:00:00";
        } else {
            LocalDate localDate = LocalDateTimeUtils.strToLocalDateTime(date + " 00:00:00").toLocalDate();
            if (isFullDate || !localDate.equals(LocalDate.now())) {
                return date.substring(0, 10) + " 23:59:59";
            } else {
                return DateUtils.getTime();
            }
        }
    }


    /**
     * 强制补全【月】时间
     *
     * @param date  要补全的时间
     * @param begin true开始时间，false结束时间
     * @return 完整时间
     */
    public static String completionMonthTime(String date, boolean begin) {
        if (null == date || date.length() == 19) {
            return date;
        }

        if (begin) {
            return date.substring(0, 7) + "-01 00:00:00";
        } else {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            return date.substring(0, 7) + "-" + getMonthLastDay(year, month) + " 23:59:59";
        }
    }

    /**
     * 强制补全【月】时间
     *
     * @param date  要补全的时间
     * @param begin true开始时间，false结束时间
     * @return 完整时间
     */
    public static String completionMonthTime(String date, boolean begin, boolean isFullDate) {
        if (null == date || date.length() == 19) {
            return date;
        }
        if (begin) {
            return date.substring(0, 7) + "-01 00:00:00";
        } else {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int currYear = getCurrYear();
            int currMonth = getCurrMonth();
            if (year == currYear && currMonth == month && !isFullDate) {
                return date.substring(0, 7) + "-" + StringUtils.padLeft(getCurrDay(), 2) + " 23:59:59";
            } else {
                return date.substring(0, 7) + "-" + getMonthLastDay(year, month) + " 23:59:59";
            }
        }
    }


    /**
     * 强制补全【年】时间
     *
     * @param date  要补全的时间
     * @param begin true开始时间，false结束时间
     * @return 完整时间
     */
    public static String completionYearTime(String date, boolean begin) {
        if (null == date || date.length() == 19) {
            return date;
        }

        if (begin) {
            return date.substring(0, 4) + "-01-01 00:00:00";
        } else {
            int year = Integer.parseInt(date.substring(0, 4));
            int currYear = getCurrYear();
            if (year == currYear) {
                return getDate() + " 23:59:59";
            } else {
                return year + "-12-31 23:59:59";
            }
        }
    }

    /**
     * 强制补全【年】时间
     *
     * @param date       要补全的时间
     * @param begin      true开始时间，false结束时间
     * @param isFullDate 是否补全日期
     * @return 完整时间
     */
    public static String completionYearTime(String date, boolean begin, boolean isFullDate) {
        if (null == date || date.length() == 19) {
            return date;
        }

        if (begin) {
            return date.substring(0, 4) + "-01-01 00:00:00";
        } else {
            int year = Integer.parseInt(date.substring(0, 4));
            int currYear = getCurrYear();
            if (year == currYear && !isFullDate) {
                return getDate() + " 23:59:59";
            } else {
                return year + "-12-31 23:59:59";
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("------ 打印结果 开始 ------");

        //生成 “年-月” 日期序列
        //List<String> months = getMonthList("2023-02-03", "2023-09-02");
        //System.out.println(String.join(",", months));

        //生成 “年-月-日” 日期序列
        //List<String> days = getDayList("2023-02-03", "2023-02-13");
        //System.out.println(String.join(",", days));

        //String begintime = "2020-10-01 04:05:00";
        //String endtime = "2023-10-01 20:00:00";
        //System.out.println("length:" + begintime.length());

        //List<String> res = getYearList(begintime, endtime);
        //System.out.println(String.join(",", res));

        //yyyy-MM-dd HH:mm:ss
        //String m = getDateMinute(begintime);
        //System.out.println(m);

        //Date myDate = new Date();
        //System.out.println("当前时间：" + myDate);
        //int year = Integer.parseInt(String.format("%tY", myDate));
        //System.out.println("年：" + year);
        //int month = Integer.parseInt(String.format("%tm", myDate));
        //System.out.println("月：" + month);
        //int day = Integer.parseInt(String.format("%td", myDate));
        //System.out.println("日：" + day);
        //int hour = Integer.parseInt(String.format("%tH", myDate));
        //System.out.println("时：" + hour);
        //int minute = Integer.parseInt(String.format("%tM", myDate));
        //System.out.println("分：" + minute);
        //int second = Integer.parseInt(String.format("%tS", myDate));
        //System.out.println("秒：" + second);

        //String beginTime = DateUtils.getCurrYearMonth() + "-01 00:00:00";
        //String endTime = DateUtils.getCurrYearMonth() + "-" + DateUtils.getMonthLastDay(DateUtils.getCurrYear(), DateUtils.getCurrMonth()) + " 23:59:59";
        //System.out.println(beginTime);
        //System.out.println(endTime);

        //String dd = getDateDay(begintime);
        //System.out.println(dd);

//        String dayTime = "2023-08-08";
//        System.out.println(completionDayTime(dayTime, true));
//        System.out.println(completionDayTime(dayTime, false));
//
//        String monthTime = "2023-08";
//        System.out.println(completionMonthTime(monthTime, true));
//        System.out.println(completionMonthTime(monthTime, false));
//
//        String yearTime = "2023";
//        System.out.println(completionYearTime(yearTime, true));
//        System.out.println(completionYearTime(yearTime, false));
//
//        System.out.println("------ 打印结果 结束 ------");


//        boolean[] fullDayOptions = {false, true};
//        for (boolean fullDay : fullDayOptions) {
//
//            System.out.println("时间维度: 5分钟 | 全日期: " + fullDay);
//            List<String> timeSlots = generateMinuteHourSlots(5, fullDay);
//            for (String timeSlot : timeSlots) {
//                System.out.print(timeSlot + " ");
//            }
//            System.out.println("\n");
//
//            System.out.println("时间维度: 月 | 全日期: " + fullDay);
//            timeSlots = generateMonthlySlots(fullDay);
//            for (String timeSlot : timeSlots) {
//                System.out.print(timeSlot + " ");
//            }
//            System.out.println("\n");
//
//            System.out.println("时间维度: 年 | 全日期: " + fullDay);
//            timeSlots = generateYearlySlots(fullDay);
//            for (String timeSlot : timeSlots) {
//                System.out.print(timeSlot + " ");
//            }
//            System.out.println("\n");
//        }
    }


}
