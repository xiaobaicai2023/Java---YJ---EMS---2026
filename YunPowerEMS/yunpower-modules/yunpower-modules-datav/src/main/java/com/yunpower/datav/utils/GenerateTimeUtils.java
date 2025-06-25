package com.yunpower.datav.utils;

import com.yunpower.common.core.utils.LocalDateTimeUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.datav.domain.vo.ChartQueryVo;
import com.yunpower.datav.enums.DatavDateDimEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.yunpower.common.core.utils.DateUtils.dateTimeFormat;


/**
 * 生成时间集合
 */
public class GenerateTimeUtils {

    private static final Logger log = LoggerFactory.getLogger(GenerateTimeUtils.class);

    /**
     * 生成时间时段-(分钟、小时)
     * 5分钟 10分钟 15分钟 30分钟 1时
     * @param beginTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @param intervalMinutes 间隔分钟(5,10,15,30,60)
     * @return 时间段
     **/
    public static List<ChartQueryVo.TimeInfo> generateMinuteHourSlots(String beginTimeStr, String endTimeStr, Integer intervalMinutes, boolean isFullDate) {
        List<ChartQueryVo.TimeInfo> timeSlots = new ArrayList<>();
        LocalDateTime beginDate = LocalDateTimeUtils.strToLocalDateTime(beginTimeStr);
        LocalDateTime endDate =LocalDateTimeUtils.strToLocalDateTime(endTimeStr);
        LocalDateTime now = LocalDateTime.now();
        while (beginDate.isBefore(endDate)) {
            timeSlots.add(new ChartQueryVo.TimeInfo(
                    beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    beginDate.format(DateTimeFormatter.ofPattern("HH:mm")),
                    beginDate.isAfter(now)? null : 0D,beginDate.getHour())
            );
            beginDate = beginDate.plusMinutes(intervalMinutes);
        }
        return timeSlots;
    }

    /**
     * 生成时间时段-月
     * @param beginTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @param isFullDate 是否全日期
     * @return 时间段
     **/
    public static List<ChartQueryVo.TimeInfo> generateMonthlySlots(String beginTimeStr,String endTimeStr, boolean isFullDate) {
        List<ChartQueryVo.TimeInfo> timeSlots = new ArrayList<>();

        LocalDateTime beginDate = LocalDateTimeUtils.strToLocalDateTime(beginTimeStr);
        LocalDateTime endDate = LocalDateTimeUtils.strToLocalDateTime(endTimeStr);
        LocalDateTime now = LocalDateTime.now();
        while (beginDate.isBefore(endDate)) {
            timeSlots.add(new ChartQueryVo.TimeInfo(
                    beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    beginDate.format(DateTimeFormatter.ofPattern("d号")),
                    beginDate.isAfter(now)? null : 0D)
            );
            beginDate = beginDate.plusDays(1);
        }
        return timeSlots;
    }

    /**
     * 生成时间时段-年
     *
     * @param beginTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @param isFullDate  是否全日期
     * @return 时间段
     **/
    public static List<ChartQueryVo.TimeInfo> generateYearlySlots(String beginTimeStr,String endTimeStr, boolean isFullDate) {
        List<ChartQueryVo.TimeInfo> timeSlots = new ArrayList<>();

        LocalDateTime beginDate = LocalDateTimeUtils.strToLocalDateTime(beginTimeStr);
        LocalDateTime endDate = LocalDateTimeUtils.strToLocalDateTime(endTimeStr);
        LocalDateTime now = LocalDateTime.now();

        //如果当前时间在所选时间之后则说明向前查找数据
        while (beginDate.isBefore(endDate)) {
            timeSlots.add(new ChartQueryVo.TimeInfo(
                    beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    beginDate.format(DateTimeFormatter.ofPattern("M月")),
                    beginDate.isAfter(now) ? null : 0D)
            );
            beginDate = beginDate.plusMonths(1);
        }
        return timeSlots;
    }


    /**
     * 生成环比时间
     *
     * @param time 时间 (格式: yyyy-MM-dd HH:mm:ss)
     * @return 结果
     */
    public static String generateChainTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        // 将字符串转换为 LocalDateTime
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(time, dateTimeFormat);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format, expected format: yyyy-MM-dd HH:mm:ss");
        }
        // 向前推一天
        dateTime = dateTime.minusDays(1);

        // 返回格式化后的字符串
        return dateTime.format(dateTimeFormat);
    }

    /**
     * 生成环比时间
     *
     * @param time 时间 (格式: yyyy-MM-dd HH:mm:ss)
     * @param datavDateDimEnum 日期维度 DatavDateDimEnum
     * @return 结果
     */
    public static String generateChainTime(String time, DatavDateDimEnum datavDateDimEnum) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        // 将字符串转换为 LocalDateTime
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(time, dateTimeFormat);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format, expected format: yyyy-MM-dd HH:mm:ss");
        }
        if(DatavDateDimEnum.YEAR == datavDateDimEnum){
            // 向前推一个月
            dateTime = dateTime.minusMonths(1);
        }else{
            // 向前推一天
            dateTime = dateTime.minusDays(1);
        }
        // 返回格式化后的字符串
        return dateTime.format(dateTimeFormat);
    }

    /**
     * 生成同比时间
     *
     * @param time 时间 (格式: yyyy-MM-dd HH:mm:ss)
     * @return 结果
     */
    public static String generateYoyTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }

        // 将字符串转换为 LocalDateTime
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(time, dateTimeFormat);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format, expected format: yyyy-MM-dd HH:mm:ss");
        }
        // 向前推一年
        dateTime = dateTime.minusYears(1);
        // 返回格式化后的字符串
        return dateTime.format(dateTimeFormat);
    }


    /**
     * 生成同比时间
     *
     * @param time 时间 (格式: yyyy-MM-dd HH:mm:ss)
     * @return 结果
     */
    public static String generateYoyTime(String time, DatavDateDimEnum datavDateDimEnum) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }

        // 将字符串转换为 LocalDateTime
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(time, dateTimeFormat);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time format, expected format: yyyy-MM-dd HH:mm:ss");
        }
        if(DatavDateDimEnum.DAY == datavDateDimEnum){
            // 向前推一个月
            dateTime = dateTime.minusMonths(1);
        }else{
            // 向前推一年
            dateTime = dateTime.minusYears(1);
        }
        // 返回格式化后的字符串
        return dateTime.format(dateTimeFormat);
    }


    public static void main(String[] args) {
//        Integer[] arr = new Integer[]{5,10,15,60};
//        List<ChartQueryVo.TimeInfo> list = new ArrayList<>();
//        for (Integer item : arr) {
//            list = generateMinuteHourSlots("2024-06-20 00:00:00","2024-06-20 23:59:59",item,true);
//            System.out.println("间隔:"+item+"分钟");
//            list.forEach(i->{
//                System.out.print(i.getKey()+" ");
//            });
//            System.out.println(" ");
//        }

        System.out.println(String.valueOf("水滴-Ua.b.c".hashCode()));
    }
}
