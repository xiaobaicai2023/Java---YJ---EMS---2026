package com.yunpower.collect.storage.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.yunpower.collect.protocols.websocket.WebSocketPushService;
import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.collect.storage.domain.*;
import com.yunpower.collect.storage.domain.jsonvo.SeasonalRangeVo;
import com.yunpower.common.core.constant.Constants;
import com.yunpower.common.core.constant.RabbitConstants;
import com.yunpower.common.core.entity.amqp.DeviceStateMessage;
import com.yunpower.common.core.entity.amqp.DeviceVarCollectMessage;
import com.yunpower.common.core.entity.amqp.DeviceVarSupplementMessage;
import com.yunpower.common.core.enums.WebSocketMessageEnum;
import com.yunpower.common.core.utils.*;
import com.yunpower.common.core.vo.DeviceVarAlarmMessage;
import com.yunpower.common.core.vo.EnumSOTVO;
import com.yunpower.common.core.vo.RedisDeviceStateVO;
import com.yunpower.common.core.vo.WebSocketBaseMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.PURE_DATE_FORMAT;
import static cn.hutool.core.date.DatePattern.SIMPLE_MONTH_FORMAT;

/**
 * 公共-存储服务
 */
@Slf4j
public class StorageCommonService {

	//region 存储入口

	/**
	 * 存储入口
	 *
	 * @param message 设备变量采集消息
	 */
	public static void doStorage(DeviceVarCollectMessage message, boolean check) {
		MonitorDeviceVar deviceVar = getDeviceVar(message.getDeviceVarSn());
		if (deviceVar == null) {
			log.error("doStorage 设备变量不存在：{},amqp-message:{}", message.getDeviceVarSn(), message);
			return;
		}

		//报警处理
		alarm(deviceVar, message);

		//检查是否需要补充数据
		if (check) {
			checkData(deviceVar, message);
		}

		Date saveTime = DateUtil.parse(message.getSaveTime());
		int year = DateUtil.year(saveTime);
		//默认从0开始 所以要+1
		int month = DateUtil.month(saveTime) + 1;
		int day = DateUtil.dayOfMonth(saveTime);
		int hour = DateUtil.hour(saveTime, true);
		// 日数据表名后缀
		String daySuffix = PURE_DATE_FORMAT.format(saveTime);
		// 月数据表名后缀
		String monthSuffix = SIMPLE_MONTH_FORMAT.format(saveTime);

		//校验是否 需要修复数据 开启修复数据开关 DataFix = 1 && 上报的值 = 0
		if (ObjectUtil.isNotNull(deviceVar.getDataFix()) && deviceVar.getDataFix().equals(1) && message.getValue() == 0) {
			ShardingDay lastData = StorageVariables.shardingCommonService.selectLastDayValue24H(saveTime,deviceVar.getVarSn());
			log.debug("设备变量:{} 开启数据修复开关 当前上报值：{},数据库中近24小时数据：{}", deviceVar.getVarSn(), message.getValue(),lastData);
			if (ObjectUtil.isNotNull(lastData)) {
				message.setValue(lastData.getDataValue());
			}
		}

		//region // 保存到数据库

		// A.存储日数据
		StorageVariables.dayDataService.saveToDatabase(message.getDeviceVarSn(), message.getValue(), saveTime, daySuffix);

		// B.存储月统计数据
		saveMonthData(message.getDeviceVarSn(), message.getValue(), saveTime, deviceVar, monthSuffix, day);

		// C.存储月累计数据
		if (deviceVar.getIsAccumulation() == 1) {
			saveMonthAccumulateData(message.getDeviceVarSn(), message.getValue(), saveTime, deviceVar, monthSuffix, year, month, day, hour);
		}
		//endregion

		if (check) {
			//获取变量在redis中的存盘时间
			String redisKey = Constants.MONITOR_DEVICE_VAR_SN_SAVE_TIME_KEY + message.getDeviceVarSn();
			EnumSOTVO enumSOTVO = new EnumSOTVO(message.getDeviceVarSn(), DoubleUtils.toPlainString(message.getValue()), message.getSaveTime());
			//写入存盘时间 为后续补数据做准备
			StorageVariables.redisService.setCacheObject(redisKey, enumSOTVO);
		}
	}

	//endregion

	//region 数据过滤

	/**
	 * 数据过滤
	 * <pre>
	 *     ● month：判断 accu_value 值，
	 *      （1）不符合条件时，保持原逻辑；
	 *      （2-1）满足条件，不做处理（value=9.999）时直接return；
	 *      （2-2）满足条件，替换时，替换现有值。
	 *      ● month_accu表：跟随month表处理逻辑
	 * </pre>
	 */
	private static Double handleDataFilter(MonitorDeviceVar deviceVar, Double value) {
		if (deviceVar.getIsDataFilter() == 0) {
			return value;
		}

		double absValue = Math.abs(value);

		//绝对值小于
		if (ObjectUtil.isNotNull(deviceVar.getLessAbs()) && absValue < deviceVar.getLessAbs()) {
			double valueLower = DoubleUtils.parse(deviceVar.getReplaceValueLower());
			//不做处理（value=9.999）时直接return
			return valueLower == 9.999 ? null : valueLower;
		}

		//绝对值大于
		if (ObjectUtil.isNotNull(deviceVar.getMoreAbs()) && absValue > deviceVar.getMoreAbs()) {
			double valueUpper = DoubleUtils.parse(deviceVar.getReplaceValueUpper());
			//不做处理（value=9.999）时直接return
			return valueUpper == 9.999 ? null : valueUpper;
		}
		return value;
	}
	//endregion

	//region 保存月统计表数据

	/**
	 * 保存月统计表数据
	 *
	 * @param varSn       变量编码
	 * @param value       值
	 * @param saveTime    存储时间
	 * @param deviceVar   设备变量信息
	 * @param monthSuffix 月数据表日期后缀，如：202312
	 * @param day         当前时间：天
	 */
	private static void saveMonthData(String varSn, Double value, Date saveTime, MonitorDeviceVar deviceVar, String monthSuffix, Integer day) {
		Integer accuSign = deviceVar.getAccumulationType();
		try {
			//region 1.获取今日数据，条件：variable_name + day_sign
			String tableName = "month_" + monthSuffix;
			MonthDataEntity monthDataEntity = StorageVariables.monthDataService.selectMonthDataByVarSnAndDay(tableName, varSn, day);
			if (monthDataEntity != null) {

				//数据修正
				handleMonthDataFix(deviceVar, monthDataEntity, value);

				MonthDataEntity update = new MonthDataEntity();
				update.setId(monthDataEntity.getId());
				update.setMinValue(monthDataEntity.getMinValue());
				update.setMaxValue(monthDataEntity.getMaxValue());

				if (value < monthDataEntity.getMinValue()) {
					update.setMinValue(value);
					update.setMinTime(saveTime);
				}
				if (value > monthDataEntity.getMaxValue()) {
					update.setMaxValue(value);
					update.setMaxTime(saveTime);
				}

				double accuValue = DoubleUtils.parse(update.getMaxValue() - update.getMinValue());
				//数据过滤
				Double _accuValue = handleDataFilter(deviceVar, accuValue);
				if (_accuValue == null) {
					return;
				}
				update.setAvgValue(DoubleUtils.parse((update.getMinValue() + update.getMaxValue()) / 2));
				update.setAccuValue(_accuValue);
				update.setTotalCount(monthDataEntity.getTotalCount() + 1);
				update.setSaveTime(saveTime);
				update.setTableName(tableName);
				update.setRunTimeValue(value);
				StorageVariables.monthDataService.updateMonthData(update);
				return;
			}
			//endregion

			//region 2.如果没有，那就新增一条
			monthDataEntity = new MonthDataEntity();

			// 起因：【用电量】两天衔接处少算一个5分钟间隔的数据
			// 因此，要取前一天的的maxValue数据，给新记录的minValue值。这样可以无缝衔接
			// 但是【发电量】会有问题（走新逻辑，有一张数据流程图）
			double lastValue = StorageVariables.shardingCommonService.selectLastTimeMonthMaxValue(saveTime, varSn, value);
			if (value < lastValue) {
				if (deviceVar.getDataFix() != null && deviceVar.getDataFix() == 1) {
					//情形：发电掉线
					monthDataEntity.setMinValue(0D);
					monthDataEntity.setMaxValue(value);
				} else {
					//情形：电流、电压、功率等
					monthDataEntity.setMinValue(value);
					monthDataEntity.setMaxValue(lastValue);
				}
			} else {
				//情形：用电和发电不掉线
				monthDataEntity.setMinValue(lastValue);
				monthDataEntity.setMaxValue(value);
			}
			double accuValue = DoubleUtils.parse(monthDataEntity.getMaxValue() - monthDataEntity.getMinValue());

			//数据过滤
			Double _accuValue = handleDataFilter(deviceVar, accuValue);
			if (_accuValue == null) {
				return;
			}
			monthDataEntity.setVariableName(varSn);
			monthDataEntity.setDaySign(day);
			monthDataEntity.setMinTime(saveTime);
			monthDataEntity.setMaxTime(saveTime);
			monthDataEntity.setAvgValue(value);
			monthDataEntity.setAccuSign(accuSign == null ? 1 : accuSign);
			monthDataEntity.setAccuValue(_accuValue);
			monthDataEntity.setRunTimeValue(value);
			monthDataEntity.setTotalCount(1);
			monthDataEntity.setSaveTime(saveTime);
			monthDataEntity.setTableName(tableName);
			StorageVariables.monthDataService.insertMonthData(monthDataEntity);
			//endregion
		} catch (Exception e) {
			log.error("saveMonthData error", e);
		}
	}

	//endregion

	//region 保存月表数据

	/**
	 * 保存月累计表数据
	 */
	private static void saveMonthAccumulateData(String varSn, Double value, Date saveTime, MonitorDeviceVar deviceVar, String monthSuffix, Integer year, Integer month, Integer day, Integer hour) {
		Long deptId = deviceVar.getDeptId();
		try {
			//region 1.获取当前小时数据，条件：variable_name + record_year + record_month + record_day + record_hour
			String tableName = "month_accumulate_" + monthSuffix;
			MonthAccumulateDataEntity accumulateDataEntity = StorageVariables.accumulateDataService.selectMonthDataByVarSnAndHour(tableName, varSn, year, month, day, hour);
			if (accumulateDataEntity != null) {

				handleMonthAccuDataFix(deviceVar, accumulateDataEntity, value);

				//累计值
				double accuValue = DoubleUtils.parse(value - accumulateDataEntity.getDataValue());
				if (accuValue < 0) { //相减的值
					return;
				}

				//数据过滤
				Double _accuValue = handleDataFilter(deviceVar, accuValue);
				if (_accuValue == null) {
					return;
				}
				MonthAccumulateDataEntity update = new MonthAccumulateDataEntity();
				update.setId(accumulateDataEntity.getId());
				update.setRuntimeValue(value);
				update.setAccuData(_accuValue); //只更新累计值
				update.setTableName(tableName);
				StorageVariables.accumulateDataService.updateMonthAccumulateData(update);
				return;
			}
			//endregion

			//region 2.如果没有就新增一条

			// 起因：【用电量】两天衔接处少算一个5分钟间隔的数据
			// 因此，要取前一天的的maxValue数据，给新记录的minValue值。这样可以无缝衔接
			// 但是【发电量】会有问题（走新逻辑，有一张数据流程图）
			double dataValue = StorageVariables.shardingCommonService.selectLastTimeMonthAccuRuntimeValue(saveTime, varSn, value);
			double accuValue = DoubleUtils.parse(value - dataValue);

			//数据过滤
			Double _accuValue = handleDataFilter(deviceVar, accuValue);
			if (_accuValue == null) {
				return;
			}

			accumulateDataEntity = new MonthAccumulateDataEntity();
			accumulateDataEntity.setVariableName(varSn);
			accumulateDataEntity.setSaveTime(saveTime);
			accumulateDataEntity.setDataValue(dataValue);
			accumulateDataEntity.setRuntimeValue(value);
			accumulateDataEntity.setRecordYear(year);
			accumulateDataEntity.setRecordMonth(month);
			accumulateDataEntity.setRecordDay(day);
			accumulateDataEntity.setRecordWeek(EltDateUtils.getCurrWeek());
			accumulateDataEntity.setRecordHour(hour);
			accumulateDataEntity.setAccuData(_accuValue);
			accumulateDataEntity.setSeasonalTypeName("");
			accumulateDataEntity.setChargePrice(0F);
			accumulateDataEntity.setIsComplete(0);
			accumulateDataEntity.setTableName(tableName);

			Long schemeId = StorageVariables.GLOBAL_DEPTID_SCHEMEID_MAP.get(deptId);
			if (schemeId != null) {
				//格式==> 电价标准ID-YearMonth：<时：（seasonalName, price）>
				String key = schemeId + "-" + year + StringUtils.padl(month, 2);
				Map<Integer, SeasonalRangeVo> hourSeasonMap = StorageVariables.GLOBAL_SEASONAL_RANGE_MAP.get(key);
				if (hourSeasonMap != null) {
					SeasonalRangeVo vo = hourSeasonMap.get(hour);
					if (vo != null) {
						accumulateDataEntity.setSeasonalTypeName(vo.getSeasonalName());
						accumulateDataEntity.setChargePrice(vo.getChargePrice());
					}
				}
			}
			StorageVariables.accumulateDataService.insertMonthAccumulateData(accumulateDataEntity);
			//endregion

			//region 3.此时，要更新上一小时的累积量
			String nowDateTime = year + "-" + StringUtils.padl(month, 2) + "-" + StringUtils.padl(day, 2) + " " + StringUtils.padl(hour, 2) + ":00:00";
			String prevHourDateTime = EltDateUtils.getPastHour(nowDateTime, 1); //返回格式：20221231230000
			int newYear = Integer.parseInt(prevHourDateTime.substring(0, 4));
			int newMonth = Integer.parseInt(prevHourDateTime.substring(4, 6));
			int newDay = Integer.parseInt(prevHourDateTime.substring(6, 8));
			int newHour = Integer.parseInt(prevHourDateTime.substring(8, 10));
			MonthAccumulateDataEntity prevHourDataEntity = StorageVariables.accumulateDataService.selectMonthDataByVarSnAndHour(tableName, varSn, newYear, newMonth, newDay, newHour);
			if (prevHourDataEntity != null) {
				//todo 修改了存储逻辑，加入了runtimeValue字段，因此不需要更新AccuData值
				//double newAccuValue = DoubleUtils.parse(value - prevHourDataEntity.getDataValue());
				//数据过滤
				//Double _newAccuValue  = handleDataFilter(deviceVar, newAccuValue);
				//if(_newAccuValue == null){
				//	return;
				//}
				//prevHourDataEntity.setAccuData(_newAccuValue);
				prevHourDataEntity.setIsComplete(1);
				prevHourDataEntity.setTableName(tableName);
				StorageVariables.accumulateDataService.updateMonthAccumulateData(prevHourDataEntity);
			}
			//endregion
		} catch (Exception e) {
			log.error("保存月累计表数据异常！", e);
		}
	}

	//endregion

	//region 检查是否需要补数据
	private static void checkData(MonitorDeviceVar deviceVar, DeviceVarCollectMessage message) {
		//存盘周期
		int saveCycle = deviceVar.getSaveCycle();
		//本次实际存盘时间
		Date saveTime = DateUtil.parse(message.getSaveTime());

		//获取上次存盘数据
		EnumSOTVO redisVO = lastSaveRedisData(message.getDeviceVarSn());

		//有数据说明之前已经存盘过数据
		if (ObjectUtil.isNull(redisVO)) {
			return;
		}

		String lastSaveTimeStr = redisVO.getTime();

		//校验是否要补充数据（上次存盘时间+存盘周期 != 本次存盘时间）
		Date lastSaveTime = EltDateUtils.dateTime(EltDateUtils.YYYY_MM_DD_HH_MM_SS, lastSaveTimeStr);

		//上次存盘时间+存盘周期=本次存盘时间
		Date currentSaveTime = DateUtil.offsetMinute(lastSaveTime, saveCycle);

		//间隔大于1小时不需要补充数据了
		long betweenHour = DateUtil.between(currentSaveTime, saveTime, DateUnit.HOUR);
		if (betweenHour > 1) {
			log.debug("设备变量:{} 距上次存盘时间已超1小时不需要补充数据", message.getDeviceVarSn());
			return;
		}

		//补充-开始时间
		String beginSaveTime = DateUtil.formatDateTime(currentSaveTime);
		boolean isNeedSupplement = currentSaveTime.compareTo(saveTime) != 0 && saveTime.after(currentSaveTime);
		log.debug("设备变量:{} 上次存盘时间:{} 上次存盘数据:{}, 上次存盘时间+存盘周期=本次存盘时间:{},本次实际存盘时间：{}，是否需要补充数据:{}", message.getDeviceVarSn(), lastSaveTimeStr,redisVO.getValue(), beginSaveTime, message.getSaveTime(), isNeedSupplement);
		if (!isNeedSupplement) {
			return;
		}

		//补充-结束时间
		String endSaveTime = DateUtil.formatDateTime(DateUtil.offsetMinute(saveTime, -saveCycle));
		Double value = (Double) redisVO.getValue();

		//写入到MQ
		DeviceVarSupplementMessage supplementMessage = DeviceVarSupplementMessage
				.builder()
				.channelSn(message.getChannelSn())
				.deviceVarSn(message.getDeviceVarSn())
				.beginTime(beginSaveTime)
				.lastSaveTime(lastSaveTimeStr)
				.lastValue(value)
				.endTime(endSaveTime).build();
		log.debug("设备变量:{}  需要补数据:{}", message.getDeviceVarSn(), supplementMessage);
		StorageVariables.publisherService.sendMessage(RabbitConstants.SUPPLEMENT_CONSUMER_KEY + deviceVar.getMessageAddress() % 4, supplementMessage);
	}

	/**
	 * 获取上次存盘数据
	 */
	public static EnumSOTVO lastSaveRedisData(String deviceVarSn) {
		//获取变量在redis中的存盘时间
		String redisKey = Constants.MONITOR_DEVICE_VAR_SN_SAVE_TIME_KEY + deviceVarSn;
		//获取上次存盘数据
		return StorageVariables.redisService.getCacheObject(redisKey);
	}

	/**
	 * 获取上次存盘数据
	 *
	 * @param deviceVarSn 设备变量
	 * @param redDB       是否从redis中获取
	 */
	public static EnumSOTVO lastSaveRedisData(String deviceVarSn, boolean redDB) {
		EnumSOTVO redisVO = lastSaveRedisData(deviceVarSn);
		if (redisVO == null && redDB) {
			ShardingDay dayData = StorageVariables.shardingCommonService.selectLastDayValue24H(deviceVarSn);
			if (dayData != null) {
				redisVO = new EnumSOTVO(deviceVarSn, dayData.getDataValue(), DateUtil.formatDateTime(dayData.getSaveTime()));
			}
		}
		return redisVO;
	}
	//endregion

	//region 数据补充
	public static void doSupplement(DeviceVarSupplementMessage message) {
		MonitorDeviceVar deviceVar = getDeviceVar(message.getDeviceVarSn());
		if (deviceVar == null) {
			log.error("doSupplement 设备变量不存在：{},amqp-message:{}", message.getDeviceVarSn(), message);
			return;
		}

		Double value = message.getLastValue();
		if (ObjectUtil.isNull(value)) {
			//从数据库读取上次数据
			ShardingQuery query = new ShardingQuery();
			query.setSaveTime(EltDateUtils.dateTime(EltDateUtils.YYYY_MM_DD_HH_MM_SS, message.getLastSaveTime()));
			query.setVariableName(message.getDeviceVarSn());
			ShardingDay dataEntity = StorageVariables.shardingCommonService.selectDayValue(query);
			if (ObjectUtil.isNotNull(dataEntity)) {
				value = dataEntity.getDataValue();
			}
		}

		//计算出要补数据的时间段
		List<String> timeSlots = LocalDateTimeUtils.getMinuteSlots(message.getBeginTime(), message.getEndTime(), deviceVar.getSaveCycle());
		DeviceVarCollectMessage collectMessage;
		for (String timeSlot : timeSlots) {
			collectMessage = DeviceVarCollectMessage.builder()
					.deviceVarSn(deviceVar.getVarSn())
					.saveTime(timeSlot)
					.value(value)
					.collectTime(timeSlot)
					.build();
			doStorage(collectMessage, false);
		}
	}
	//endregion

	//region 从内存中获取变量信息

	/**
	 * 从内存中获取变量信息
	 */
	private static MonitorDeviceVar getDeviceVar(String deviceVarSn) {
		return StorageVariables.GLOBAL_MONITOR_DEVICE_VAR_LIST_MAP.get(deviceVarSn);
	}
	//endregion

	//region 变更设备状态

	public static void changeState(DeviceStateMessage message, boolean setRedis) {
		switch (message.getType()) {
			case Constants.CHANNEL_SUFFIX:
				//修改通道状态
				changeChannelState(message);
				break;
			case Constants.CHANNEL_DEVICE_SUFFIX:
				//修改通道设备状态
				changeChannelDeviceState(message, setRedis);
				break;
			case Constants.MONITOR_DEVICE_SUFFIX:
				changeMonitorDeviceState(message, setRedis);
				break;
			default:
				break;
		}
	}

	/**
	 * 修改通道状态
	 */
	private static void changeChannelState(DeviceStateMessage message) {
		//检查通道是否存在
		CommunicationChannel channel = StorageVariables.GLOBAL_CHANNEL_LIST.stream().filter(item -> item.getChannelSn().equals(message.getDeviceSn())).findFirst().orElse(null);
		if (channel == null) {
			return;
		}
		//时间转换
		Date changeTime = EltDateUtils.dateTime(EltDateUtils.YYYY_MM_DD_HH_MM_SS, message.getChangeTime());
		//更新状态
		StorageVariables.communicationChannelService.changeState(channel.getId(), message.getState(), changeTime);
		//redis key
		String redisKey = Constants.DEVICE_STATE_CHANNEL_KEY + message.getDeviceSn();
		//写入到redis
		RedisDeviceStateVO enumSOTVO = new RedisDeviceStateVO(channel.getChannelSn(), message.getState(), message.getChangeTime());
		StorageVariables.redisService.setCacheObject(redisKey, enumSOTVO);
	}

	/**
	 * 修改通道设备状态
	 *
	 * @param message  信息
	 * @param setRedis 从redis监听中过来的不需要写入redis
	 */
	private static void changeChannelDeviceState(DeviceStateMessage message, boolean setRedis) {
		//检查通道设备是否存在
		CommunicationDevice channelDevice = StorageVariables.GLOBAL_CHANNEL_DEVICE_LIST.stream().filter(item -> item.getDeviceSn().equals(message.getDeviceSn())).findFirst().orElse(null);
		if (channelDevice == null) {
			return;
		}
		//redis key
		String redisKey = Constants.DEVICE_STATE_CHANNEL_DEVICE_KEY + message.getDeviceSn();
		//读取redis中的数据
		RedisDeviceStateVO redisData = StorageVariables.redisService.getCacheObject(redisKey);
		//如果redis有并且是上线的状态 那么只更新redis过期时间 || 5分钟节点更新一次库
		if (redisData == null || !message.getState().equals(redisData.getState()) || DateUtil.thisMinute() % 5 == 0) {
			//时间转换
			Date changeTime = EltDateUtils.dateTime(EltDateUtils.YYYY_MM_DD_HH_MM_SS, message.getChangeTime());
			//更新状态
			StorageVariables.communicationDeviceService.changeState(channelDevice.getId(), message.getState(), changeTime);
			redisData = new RedisDeviceStateVO(channelDevice.getDeviceSn(), message.getState(), message.getChangeTime());
		}
		stateSetToRedis(setRedis, redisKey, redisData, message, channelDevice.getTimeout());
	}

	/**
	 * 修改监控设备状态
	 *
	 * @param message  信息
	 * @param setRedis 从redis监听中过来的不需要写入redis
	 */
	private static void changeMonitorDeviceState(DeviceStateMessage message, boolean setRedis) {
		//检查监控设备是否存在
		MonitorDevice monitorDevice = StorageVariables.GLOBAL_MONITOR_DEVICE_MAP.getOrDefault(message.getDeviceSn(), null);
		if (monitorDevice == null) {
			return;
		}
		//redis key
		String redisKey = Constants.DEVICE_STATE_MONITOR_DEVICE_KEY + message.getDeviceSn();
		//读取redis中的数据
		RedisDeviceStateVO redisData = StorageVariables.redisService.getCacheObject(redisKey);
		//如果redis有并且是上线的状态 那么只更新redis过期时间 || 5分钟节点更新一次库
		if (redisData == null || !message.getState().equals(redisData.getState()) || DateUtil.thisMinute() % 5 == 0) {
			//时间转换
			Date changeTime = EltDateUtils.dateTime(EltDateUtils.YYYY_MM_DD_HH_MM_SS, message.getChangeTime());
			//更新状态
			StorageVariables.monitorDeviceService.changeState(monitorDevice.getId(), message.getState(), changeTime);
			redisData = new RedisDeviceStateVO(monitorDevice.getDeviceSn(), message.getState(), message.getChangeTime());
		}
		stateSetToRedis(setRedis, redisKey, redisData, message, monitorDevice.getTimeout());
	}

	/**
	 * 状态数据写入到redis
	 */
	private static void stateSetToRedis(boolean setRedis, String redisKey, RedisDeviceStateVO redisData, DeviceStateMessage message, Integer deviceTimeout) {
		if (setRedis) {
			redisData.setTime(message.getChangeTime());
			redisData.setState(message.getState());
			//写入到redis
			long timeout = 300;
			if (ObjectUtil.isNotNull(deviceTimeout) && deviceTimeout > 0) {
				timeout = deviceTimeout;
			}
			StorageVariables.redisService.setCacheObject(redisKey, redisData, timeout, TimeUnit.SECONDS);
		}
	}

	//endregion

	//region 变量报警

	/**
	 * 报警入口
	 */
	public static void alarm(MonitorDeviceVar deviceVar, DeviceVarCollectMessage message) {
		//检查变量是否有报警规则
		List<AlarmTriggerConfig> list = StorageVariables.GLOBAL_DEVICE_VAR_ALARM_CONFIG_MAP.get(message.getDeviceVarSn());
		if (ObjectUtil.isEmpty(list)) {
			return;
		}
		log.debug("设备变量:{} 报警规则: {} 条", message.getDeviceVarSn(), list.size());
		for (AlarmTriggerConfig item : list) {
			doAlarm(item, message, deviceVar);
		}
	}

	/**
	 * 处理报警
	 */
	private static void doAlarm(AlarmTriggerConfig config, DeviceVarCollectMessage message, MonitorDeviceVar deviceVar) {
		try {
			log.debug("设备变量:{} 实时值：{} 报警类型: {},表达式:{} 阈值:{}", message.getDeviceVarSn(), message.getValue(), config.getCategory().getTriggerName(), config.getAviator(), config.getThreshold());
			if (config.getCategory().getTriggerType().contains("to")) {
				handleAlarmStateVar(config, message, deviceVar);
			} else if (config.getCategory().getTriggerType().contains("_a")) {
				handleAlarmAnalogVar(config, message, deviceVar);
			}
		} catch (Exception ex) {
			log.error("设备变量:{} 处理报警异常！", message.getDeviceVarSn(), ex);
		}
	}

	/**
	 * 处理报警-状态量
	 *
	 * @param config    报警配置
	 * @param message   上报信息
	 * @param deviceVar 变量信息
	 */
	private static void handleAlarmStateVar(AlarmTriggerConfig config, DeviceVarCollectMessage message, MonitorDeviceVar deviceVar) {
		//查询上次存盘数据
		EnumSOTVO redisVO = lastSaveRedisData(message.getDeviceVarSn());
		if (redisVO == null) {
			return;
		}
		//旧值
		int old_value = NumberUtil.parseInt(redisVO.getValue().toString());
		//新值
		int new_value = NumberUtil.parseInt(message.getValue().toString());
		Map<String, Object> env = new HashMap<>();
		env.put("old_value", old_value);
		env.put("new_value", new_value);
		//公式成立 触发报警
		if (AviatorUtils.customExecute(config.getAviator(), env)) {
			//触发报警
			saveAlarm(config, message, deviceVar);
		} else {
			//解除报警
			restoreAlarm(config, message, deviceVar);
		}
	}

	/**
	 * 处理报警-模拟量
	 *
	 * @param config    报警配置
	 * @param message   上报信息
	 * @param deviceVar 变量信息
	 */
	private static void handleAlarmAnalogVar(AlarmTriggerConfig config, DeviceVarCollectMessage message, MonitorDeviceVar deviceVar) {
		Map<String, Object> env = new HashMap<>();
		env.put("A", config.getThreshold());
		env.put("new_value", message.getValue());
		//公式成立 触发报警
		if (AviatorUtils.customExecute(config.getAviator(), env)) {
			//触发报警
			saveAlarm(config, message, deviceVar);
		} else {
			//解除报警
			restoreAlarm(config, message, deviceVar);
		}
	}

	/**
	 * 写入报警->触发报警
	 */
	private static void saveAlarm(AlarmTriggerConfig config, DeviceVarCollectMessage message, MonitorDeviceVar deviceVar) {
		//判断是否在报警事件段内
		boolean isWithinRange = LocalDateTimeUtils.isWithinTimeRange(config.getStartTime(), config.getEndTime());
		if (!isWithinRange) {
			log.debug("设备变量:{} 报警时间 {} ~ {} 当前时间 {} 不在报警事件段内，忽略！", message.getDeviceVarSn(), config.getStartTime(), config.getEndTime(), DateTime.now());
			return;
		}
		String redisKey = Constants.MONITOR_DEVICE_VAR_ALARM_KEY + message.getDeviceVarSn();
		//判断一分钟内是否有报警 有的话直接忽略
		if (StorageVariables.redisService.hasKey(redisKey)) {
			log.debug("设备变量:{} 一分钟内有报警，忽略！", message.getDeviceVarSn());
			return;
		}
		//写入到redis
		StorageVariables.redisService.setCacheObject(redisKey, 1, 1L, TimeUnit.MINUTES);

		//站点信息
		SysStation station = StorageVariables.GLOBAL_DEPT_MAP.get(deviceVar.getDeptId());
		//设备信息
		MonitorDevice monitorDevice = StorageVariables.GLOBAL_MONITOR_DEVICE_MAP.get(deviceVar.getDeviceSn());
		//报警内容 设备名称+变量名称+实时值+报警类型名称+阈值
		String triggerContent = packageAlarmTriggerContent(config, message, deviceVar, monitorDevice);
		//报警数据入库
		AlarmTrigger alarmTrigger = new AlarmTrigger();
		alarmTrigger.setEntId(deviceVar.getEntId());
		alarmTrigger.setDeptId(deviceVar.getDeptId());
		alarmTrigger.setStationType(deviceVar.getStationType());
		alarmTrigger.setDeviceId(deviceVar.getDeviceId());
		alarmTrigger.setDeviceSn(deviceVar.getDeviceSn());
		alarmTrigger.setDeviceName(monitorDevice.getDeviceName());
		alarmTrigger.setVarId(deviceVar.getId());
		alarmTrigger.setVarSn(deviceVar.getVarSn());
		alarmTrigger.setVarName(deviceVar.getVarName());
		alarmTrigger.setVarType(deviceVar.getVarType());
		alarmTrigger.setCategoryId(config.getCategoryId());
		alarmTrigger.setCategoryName(config.getCategory().getTriggerName());
		alarmTrigger.setTriggerLevel(config.getCategory().getTriggerLevel());
		alarmTrigger.setTriggerLevelName(config.getCategory().getTriggerLevelName());
		alarmTrigger.setTriggerConditionName(config.getTriggerConditionName());
		alarmTrigger.setTriggerConditionSn(config.getTriggerConditionSn());
		alarmTrigger.setThreshold(config.getThreshold());
		alarmTrigger.setRealValue(message.getValue());
		alarmTrigger.setIsSendSms(config.getIsSendSms());
		alarmTrigger.setReceiveType(config.getReceiveType());
		alarmTrigger.setReceiveConcrete(config.getReceiveConcrete());
		alarmTrigger.setHappenTime(DateUtil.parseDateTime(message.getCollectTime()));
		alarmTrigger.setTriggerContent(triggerContent);
		alarmTrigger.setTriggerStatus(0);
		alarmTrigger.setCreateBy("system-alarm");
		alarmTrigger.setCreateTime(new Date());
		alarmTrigger.setStopFlag(0);
		alarmTrigger.setDeleteFlag(0);

		//写入报警事件表
		int result = StorageVariables.alarmTriggerService.insertAlarmTrigger(alarmTrigger);
		if (result <= 0) {
			log.error("设备变量:{} 写入报警数据异常！", message.getDeviceVarSn());
			return;
		}

		//写入redis 24小时 记录该变量已有报警数据 增量+1  设备变量报警数量:报警类型:变量sn
		String redisKey2 = StrUtil.format("{}{}:{}", Constants.MONITOR_DEVICE_VAR_ALARM_COUNT_KEY, config.getCategoryId(), deviceVar.getVarSn());
		StorageVariables.redisService.incrementCacheValueWithExpire(redisKey2, 1, 1L, TimeUnit.DAYS);

		//推送websocket
		sendAlarmWebSocketMessage(station, monitorDevice, config, deviceVar, message, alarmTrigger.getTriggerStatus());
	}

	/**
	 * 解除报警
	 */
	private static void restoreAlarm(AlarmTriggerConfig config, DeviceVarCollectMessage message, MonitorDeviceVar deviceVar) {
		//获取24小时内变量的报警数量  设备变量报警数量:报警类型:变量sn
		String redisKey = StrUtil.format("{}{}:{}", Constants.MONITOR_DEVICE_VAR_ALARM_COUNT_KEY, config.getCategoryId(), deviceVar.getVarSn());
		Integer count = StorageVariables.redisService.getCacheObject(redisKey);

		if (count == null || count <= 0) {
			log.debug("设备变量:{} 24小时内暂无报警数据 无需执行", deviceVar.getVarSn());
			return;
		}
		log.debug("设备变量:{} 24小时内已有{}条报警 需要接触报警", deviceVar.getVarSn(), count);

		AlarmTrigger updateAlarmTrigger = new AlarmTrigger();
		updateAlarmTrigger.setVarSn(deviceVar.getVarSn());
		updateAlarmTrigger.setCategoryId(config.getCategoryId());
		updateAlarmTrigger.setCreateBy("system-alarm");
		updateAlarmTrigger.setTriggerStatus(0);
		Map<String, Object> dateToParamForDay = EltDateUtils.dateToParamForDay(DateUtil.formatDateTime(DateUtil.offsetDay(new Date(), -1)), DateUtil.now());
		updateAlarmTrigger.setParams(dateToParamForDay);
		int result = StorageVariables.alarmTriggerService.batchRestoreAlarm(updateAlarmTrigger);
		log.debug("设备变量:{} 解除报警 操作{}条", deviceVar.getVarSn(), result);
		StorageVariables.redisService.deleteObject(redisKey);

		//站点信息
		SysStation station = StorageVariables.GLOBAL_DEPT_MAP.get(deviceVar.getDeptId());
		//设备信息
		MonitorDevice monitorDevice = StorageVariables.GLOBAL_MONITOR_DEVICE_MAP.get(deviceVar.getDeviceSn());
		//推送消息
		sendAlarmWebSocketMessage(station, monitorDevice, config, deviceVar, message, 10);
	}

	/**
	 * 组装报警内容
	 */
	private static String packageAlarmTriggerContent(AlarmTriggerConfig config, DeviceVarCollectMessage message, MonitorDeviceVar deviceVar, MonitorDevice monitorDevice) {
		String triggerContent;
		if (config.getCategory().getSuitType() == 1) {
			//内容格式 ： [设备+变量] 实时值 报警类型名称 阈值
			triggerContent = StrUtil.format("[{} {}] {} {} {}",
					monitorDevice.getDeviceName(), deviceVar.getVarName(),
					message.getValue(), config.getCategory().getTriggerName(), config.getThreshold());
		} else {
			//内容格式 ： [设备+变量] 报警类型名称
			triggerContent = StrUtil.format("[{} {}] {}", monitorDevice.getDeviceName(), deviceVar.getVarName(), config.getCategory().getTriggerName());
		}
		return triggerContent;
	}

	/**
	 * 推送websocket
	 */
	public static void sendAlarmWebSocketMessage(SysStation station, MonitorDevice monitorDevice, AlarmTriggerConfig config, MonitorDeviceVar deviceVar, DeviceVarCollectMessage message, Integer triggerStatus) {

		//判断设备对应的站点是否开启报警功能
		boolean openAlarm = StorageVariables.GLOBAL_DEPTID_ALARM_SWITCH_MAP.getOrDefault(deviceVar.getDeptId(), false);
		if (!openAlarm) {
			log.debug("设备变量所在的站点:{} 未开启报警 不推送数据！", station.getStationName());
			return;
		}
		//报警内容
		String triggerContent = packageAlarmTriggerContent(config, message, deviceVar, monitorDevice);
		//推送websocket-恢复
		DeviceVarAlarmMessage alarmMessage = new DeviceVarAlarmMessage(
				station.getStationSn(),
				station.getStationName(),
				station.getStationType(),
				monitorDevice.getDeviceName(),
				deviceVar.getVarName(),
				config.getCategory().getTriggerName(),
				triggerContent,
				DateUtil.parseDateTime(message.getCollectTime()),
				config.getCategory().getTriggerLevelName(),
				config.getCategory().getTriggerLevel(),
				triggerStatus
		);
		WebSocketBaseMessage socketMessage = new WebSocketBaseMessage(WebSocketMessageEnum.DEVICE_ALARM_MESSAGE, alarmMessage);
		String messageStr = JSON.toJSONString(socketMessage);
		WebSocketPushService.pushMsgToOne(station.getStationSn(), messageStr);
	}

	//endregion

	//region 逆变器数据修正

	/**
	 * 数据修正=>处理月表
	 * <pre>
	 *  逆变器，由于厂家、型号的问题。第二天发电前上传的数据，有的会清0，有的会延续前一天的数据。
	 *  dataFix:（0不操作 1上报原值 2上报0值）
	 * </pre>
	 */
	public static void handleMonthDataFix(MonitorDeviceVar deviceVar, MonthDataEntity monthDataEntity, double value) {
		//没有配置 或者 0 时 直接返回
		if (ObjectUtil.isNull(deviceVar.getDataFix()) || deviceVar.getDataFix().equals(0)) {
			return;
		}
		//数据修正
		handleMonthDataFixReportOriginal(monthDataEntity, value);
	}

	/**
	 * 数据修正=>处理月累计表
	 * <pre>
	 *  逆变器，由于厂家、型号的问题。第二天发电前上传的数据，有的会清0，有的会延续前一天的数据。
	 *  dataFix:（0不操作 1上报原值 2上报0值）
	 * </pre>
	 */
	public static void handleMonthAccuDataFix(MonitorDeviceVar deviceVar, MonthAccumulateDataEntity accumulateDataEntity, double value) {
		//没有配置 或者 0 时 直接返回
		if (ObjectUtil.isNull(deviceVar.getDataFix()) || deviceVar.getDataFix().equals(0)) {
			return;
		}
		//数据修正
		handleMonthAccuDataFixReportOriginal(accumulateDataEntity, value);
	}

	/**
	 * 数据修正-上报原值(月表)
	 * <pre>
	 *   当前value<表中的 runtime_value 实时值 则 minValue = 0 maxValue = value
	 *   00:00:00 => 500
	 *   00:05:00 => 500
	 *   ...
	 *   05:00:00 => 500
	 *   05:05:00 => 500
	 *   05:10:00 => 0.1 开始发电
	 *   05:15:00 => 10  开始发电
	 *  </pre>
	 */
	public static void handleMonthDataFixReportOriginal(MonthDataEntity monthDataEntity, double value) {
		//当前value<表中的 runtime_value 实时值
		if (value < monthDataEntity.getRunTimeValue()) {
			monthDataEntity.setMinValue(0D);
			monthDataEntity.setMaxValue(value);
			monthDataEntity.setRunTimeValue(value);
		}
	}

	/**
	 * 数据修正-上报原值(月累计表)
	 * <pre>
	 *   当前value<表中的 dataValue 实时值 则 dataValue = 0 runtimeValue = value
	 * </pre>
	 */
	public static void handleMonthAccuDataFixReportOriginal(MonthAccumulateDataEntity accumulateDataEntity, double value) {
		//当前value<表中的 runtime_value 实时值
		if (value < accumulateDataEntity.getDataValue()) {
			accumulateDataEntity.setDataValue(0D);
			accumulateDataEntity.setRuntimeValue(value);
			accumulateDataEntity.setAccuData(value);
		}
	}

	/**
	 * 数据修正-上报0值(月表)
	 * <pre>
	 *  上报0则月表则不存储
	 *  2024-08-29 17:00:00 => 300
	 *  2024-08-29 18:00:00 => 400
	 *  ...
	 *  2024-08-29 19:00:00 => 500
	 *  2024-08-29 19:05:00 => 505
	 *  2024-08-29 19:10:00 => 0   停止发电
	 *  2024-08-29 19:15:00 => 0   停止发电
	 *  ...
	 *  2024-08-29 23:55:00 => 0   停止发电
	 *  2024-08-30 00:00:00 => 0   停止发电
	 *  2024-08-30 05:00:00 => 0   停止发电
	 *  2024-08-30 05:05:00 => 506 开始发电
	 *  2024-08-30 05:10:00 => 515 开始发电
	 *  </pre>
	 */
	public static void handleMonthDataFixReportZero(MonthDataEntity monthDataEntity, double value) {
		if (value <= 0) {
			monthDataEntity = null;
		}
	}

	/**
	 * 数据修正-上报0值(月累计表)
	 * <pre>
	 *  上报0则 dataValue = 0 runtimeValue = 0 accuData = 0
	 *  </pre>
	 */
	public static void handleMonthAccDataFixReportZero(MonthAccumulateDataEntity accumulateDataEntity, double value) {
		if (value == 0) {
			accumulateDataEntity.setDataValue(0D);
			accumulateDataEntity.setRuntimeValue(0D);
			accumulateDataEntity.setAccuData(0D);
		}
	}

	//endregion
}

