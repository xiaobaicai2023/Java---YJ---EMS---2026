package com.yunpower.common.core.utils;

import com.yunpower.common.core.enums.DateFormatEnum;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: 时间工具类
 * @Author: Jiajiaglam
 * @date: 2023-10-18 14:49
 * @description:
 */
public class LocalDateTimeUtils {

	/**
	 * 返回日期：202310
	 */
	public static String GetYYYYMM(LocalDateTime dateTime) {
		return parseDateToStr(dateTime, DateFormatEnum.YYYYMM.getValue());
	}

	/**
	 * 返回日期：20231018
	 */
	public static String GetYYYYMMDD(LocalDateTime dateTime) {
		return parseDateToStr(dateTime, DateFormatEnum.YYYYMMDD.getValue());
	}

	/**
	 * 日期格式化
	 *
	 * @param dateTime 日期
	 * @param format   格式
	 * @return 结果
	 */
	public static String parseDateToStr(LocalDateTime dateTime, String format) {
		return dateTime.format(DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 字符串转日期格式
	 *
	 * @param time 字符串日期
	 * @return 结果
	 */
	public static LocalDateTime strToLocalDateTime(String time) {
		if (time == null) {
			return null;
		}
		return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue()));
	}

	/**
	 * 从开始日期到结束日期，封装成 yyyyMMdd 列表
	 *
	 * @param startTime 开始日期
	 * @param endTime   结束日期
	 * @return 结果
	 */
	public static List<String> GetDayListYYYYMMDD(LocalDateTime startTime, LocalDateTime endTime) {
		List<String> result = new ArrayList<>();

		// 这里先对startDate减一天，最后的结果才能包含startTime
		LocalDate newStartDate = startTime.toLocalDate().minusDays(1);
		LocalDate newEndDate = endTime.toLocalDate();

		while (!newStartDate.equals(newEndDate)) {
			newStartDate = newStartDate.plusDays(1);
			result.add(newStartDate.format(DateTimeFormatter.ofPattern(DateFormatEnum.YYYYMMDD.getValue())));
		}
		return result;
	}

	/**
	 * 从开始日期到结束日期，封装成 yyyyMM 列表
	 *
	 * @param startTime 开始日期
	 * @param endTime   结束日期
	 * @return 结果
	 */
	public static List<String> GetMonthListYYYYMM(LocalDateTime startTime, LocalDateTime endTime) {
		List<String> result = new ArrayList<>();

		// 这里先对startDate减一个月，最后的结果才能包含startTime
		startTime = startTime.minusMonths(1);

		String newStartMonth = parseDateToStr(startTime, DateFormatEnum.YYYYMM.getValue());
		String newEndMonth = parseDateToStr(endTime, DateFormatEnum.YYYYMM.getValue());

		while (!newStartMonth.equals(newEndMonth)) {
			startTime = startTime.plusMonths(1);
			newStartMonth = parseDateToStr(startTime, DateFormatEnum.YYYYMM.getValue());
			result.add(newStartMonth);
		}
		return result;
	}

	/**
	 * 将 Timestamp 转换为 LocalDateTime
	 *
	 * @param timestamp 转换日期
	 * @return 结果
	 */
	public static LocalDateTime convertTimestamp(Timestamp timestamp) {
		if (timestamp == null) {
			return null;
		}

		return timestamp.toLocalDateTime();
	}


	/**
	 * 获取两个时间段内的所有间隔数据
	 * @param beginTimeStr 开始时间
	 * @param endTimeStr 结束时间
	 * @param intervalMinutes 间隔分钟
	 * */
	public static List<String> getMinuteSlots(String beginTimeStr, String endTimeStr, Integer intervalMinutes) {
		List<String> timeSlots = new ArrayList<>();
		LocalDateTime beginDate = LocalDateTimeUtils.strToLocalDateTime(beginTimeStr);
		LocalDateTime endDate = LocalDateTimeUtils.strToLocalDateTime(endTimeStr);

		//如果时间一样就返回一条数据
		if (beginDate.equals(endDate)) {
			timeSlots.add(beginTimeStr);
			return timeSlots;
		}
		while (beginDate.isBefore(endDate)) {
			timeSlots.add(beginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			beginDate = beginDate.plusMinutes(intervalMinutes);
		}
		timeSlots.add(endTimeStr);
		return timeSlots;
	}

	/**
	 * 判断当前时间是否在指定时间段内
	 *
	 * @param startTimeStr 开始时间字符串（格式 HH:mm）
	 * @param endTimeStr 结束时间字符串（格式 HH:mm）
	 * @return 如果当前时间在时间段内，返回 true；否则返回 false
	 */
	public static boolean isWithinTimeRange(String startTimeStr, String endTimeStr) {
		// 定义时间格式化器
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

		// 将字符串解析为 LocalTime 对象
		LocalTime startTime = LocalTime.parse(startTimeStr, timeFormatter);
		LocalTime endTime = LocalTime.parse(endTimeStr, timeFormatter);

		// 获取当前时间
		LocalTime now = LocalTime.now();

		// 判断当前时间是否在时间段内
		return !now.isBefore(startTime) && !now.isAfter(endTime);
	}
}
