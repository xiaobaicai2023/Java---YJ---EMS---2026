package com.yunpower.system.utils;

import com.yunpower.system.domain.vo.time.TimesVo;
import com.yunpower.common.core.enums.sharding.ShardingDateDimEnum;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.LocalDateTimeUtils;
import com.yunpower.common.core.utils.StringUtils;
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
     * 组装时间数据
     * <pre>
     *     beginTime 开始时间
     *     endTime   结束时间
     * </pre>
     *
     * @param timesVo 查询参数
     **/
    public static void packageTime(TimesVo timesVo) {
        String beginTime = "";
        String endTime = "";

        //格式化【日】时间 00:05～24:00 00:10～24:00 00:15～24:00 00:30～24:00 01:00～24:00
        if (StringUtils.isNotEmpty(timesVo.getDayTime())) {
            timesVo.setDateDim(ShardingDateDimEnum.DAY.getKey());
            beginTime = DateUtils.completionDayTime(timesVo.getDayTime(), true);
            endTime = DateUtils.completionDayTime(timesVo.getDayTime(), false);
            //二级维度只有：时
            timesVo.setTimeList(generateMinuteHourSlots(beginTime, 60, false));
        }

        // 格式化【月】时间 1号～30号/31号
        if (StringUtils.isNotEmpty(timesVo.getMonthTime())) {
            timesVo.setDateDim(ShardingDateDimEnum.MONTH.getKey());
            beginTime = DateUtils.completionMonthTime(timesVo.getMonthTime(), true);
            endTime = DateUtils.completionMonthTime(timesVo.getMonthTime(), false);
            timesVo.setTimeList(generateMonthlySlots(beginTime, endTime, false));
        }

        // 格式化【年】时间 1月～12月
        if (StringUtils.isNotEmpty(timesVo.getYearTime())) {
            timesVo.setDateDim(ShardingDateDimEnum.YEAR.getKey());
            beginTime = DateUtils.completionYearTime(timesVo.getYearTime(), true);
            endTime = DateUtils.completionYearTime(timesVo.getYearTime(), false);
            timesVo.setTimeList(generateYearlySlots(beginTime, endTime, false));
        }

        // 如果没传日期，默认给当月日期
        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            timesVo.setDateDim(ShardingDateDimEnum.DAY.getKey());
            beginTime = DateUtils.getDate().substring(0, 7) + "-01 00:00:00";
            endTime = DateUtils.getTime();
            timesVo.setTimeList(generateMonthlySlots(beginTime, endTime, false));
        }

        timesVo.setBeginTime(beginTime);
        timesVo.setEndTime(endTime);
    }

    /**
     * 生成时间时段-(分钟、小时)
     * 5分钟 10分钟 15分钟 30分钟 1时
     *
     * @param intervalMinutes 间隔分钟(5,10,15,30,60)
     * @param isFullDate      是否全日期
     * @return 时间段
     **/
    public static List<TimesVo.TimeInfo> generateMinuteHourSlots(String specifyTime, Integer intervalMinutes, boolean isFullDate) {
        List<TimesVo.TimeInfo> timeSlots = new ArrayList<>();
        LocalDateTime specifyLocalTime = LocalDateTimeUtils.strToLocalDateTime(specifyTime);
        LocalDateTime beginTime = specifyLocalTime.withHour(0).withMinute(0).withSecond(0).withNano(0);

        LocalDateTime now = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = beginTime.withHour(23).withMinute(59);
        boolean noDataSign = false;
        //如果当前时间在所选时间之后则说明向前查找数据
        if (now.equals(beginTime)) {
            if (!isFullDate) {
                endTime = LocalDateTime.now();
            } else {
                noDataSign = true;
            }
        } else {
            isFullDate = true;
        }
        while (beginTime.isBefore(endTime)) {
            timeSlots.add(new TimesVo.TimeInfo(
                    beginTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    beginTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    noDataSign && beginTime.isAfter(LocalDateTime.now()) ? null : 0F)
            );
            beginTime = beginTime.plusMinutes(intervalMinutes);
        }
        return timeSlots;
    }

    /**
     * 生成时间时段-月
     *
     * @param beginTimeStr 开始时间
     * @param endTimeStr   结束时间
     * @param isFullDate   是否全日期
     * @return 时间段
     **/
    public static List<TimesVo.TimeInfo> generateMonthlySlots(String beginTimeStr, String endTimeStr, boolean isFullDate) {
        List<TimesVo.TimeInfo> timeSlots = new ArrayList<>();

        LocalDateTime beginDate = LocalDateTimeUtils.strToLocalDateTime(beginTimeStr);
        LocalDateTime endDate = LocalDateTimeUtils.strToLocalDateTime(endTimeStr);
        LocalDateTime now = LocalDateTime.now();
        while (beginDate.isBefore(endDate)) {
            timeSlots.add(new TimesVo.TimeInfo(
                    beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    beginDate.format(DateTimeFormatter.ofPattern("dd号")),
                    beginDate.isAfter(now) && !isFullDate ? null : 0F)
            );
            beginDate = beginDate.plusDays(1);
        }
        return timeSlots;
    }

    /**
     * 生成时间时段-年
     *
     * @param beginTimeStr 开始时间
     * @param endTimeStr   结束时间
     * @param isFullDate   是否全日期
     * @return 时间段
     **/
    public static List<TimesVo.TimeInfo> generateYearlySlots(String beginTimeStr, String endTimeStr, boolean isFullDate) {
        List<TimesVo.TimeInfo> timeSlots = new ArrayList<>();

        LocalDateTime beginDate = LocalDateTimeUtils.strToLocalDateTime(beginTimeStr);
        LocalDateTime endDate = LocalDateTimeUtils.strToLocalDateTime(endTimeStr);
        LocalDateTime now = LocalDateTime.now();

        //如果当前时间在所选时间之后则说明向前查找数据
        while (beginDate.isBefore(endDate)) {
            timeSlots.add(new TimesVo.TimeInfo(
                    beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    beginDate.format(DateTimeFormatter.ofPattern("MM月")),
                    beginDate.isAfter(now) ? null : 0F)
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
     * @param time                时间 (格式: yyyy-MM-dd HH:mm:ss)
     * @param ShardingDateDimEnum 日期维度 ShardingDateDimEnum
     * @return 结果
     */
    public static String generateChainTime(String time, ShardingDateDimEnum ShardingDateDimEnum) {
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
        if (ShardingDateDimEnum.YEAR == ShardingDateDimEnum) {
            // 向前推一个月
            dateTime = dateTime.minusMonths(1);
        } else {
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
    public static String generateYoyTime(String time, ShardingDateDimEnum ShardingDateDimEnum) {
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
        if (ShardingDateDimEnum.DAY == ShardingDateDimEnum) {
            // 向前推一个月
            dateTime = dateTime.minusMonths(1);
        } else {
            // 向前推一年
            dateTime = dateTime.minusYears(1);
        }
        // 返回格式化后的字符串
        return dateTime.format(dateTimeFormat);
    }
}
