package com.yunpower.collect.storage.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yunpower.collect.storage.domain.*;
import com.yunpower.collect.storage.mapper.DayDataMapper;
import com.yunpower.collect.storage.mapper.MonthAccumulateDataMapper;
import com.yunpower.collect.storage.mapper.MonthDataMapper;
import com.yunpower.collect.storage.service.IShardingCommonService;
import com.yunpower.common.dds.annotation.ShardingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@ShardingDataSource
@Slf4j
public class ShardingCommonServiceImpl implements IShardingCommonService {

	@Autowired
	private DayDataMapper dayDataMapper;

	@Autowired
	private MonthDataMapper monthDataMapper;

	@Autowired
	private MonthAccumulateDataMapper monthAccumulateDataMapper;

	@Override
	public ShardingDay selectDayValue(ShardingQuery shardingQuery) {
		return dayDataMapper.selectDayValue(shardingQuery);
	}

	/**
	 * 获取近24小时内最新一条数据
	 */
	@Override
	public ShardingDay selectLastDayValue24H(String deviceVarSn) {
		ShardingQuery query = new ShardingQuery();
		query.setVariableName(deviceVarSn);
		query.getParams().put("beginTime", DateUtil.formatDateTime(DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, -1)));
		query.getParams().put("endTime", DateUtil.formatDateTime(DateUtil.endOfDay(new Date())));
		return this.selectDayValue(query);
	}

	/**
	 * 获取近24小时内最新一条数据
	 * @param saveTime 存盘时间 按照这个时间为节点
	 * */
	@Override
	public ShardingDay selectLastDayValue24H(Date saveTime, String deviceVarSn){
		ShardingQuery query = new ShardingQuery();
		query.setVariableName(deviceVarSn);
		query.getParams().put("beginTime", DateUtil.formatDateTime(DateUtil.offset(saveTime, DateField.DAY_OF_YEAR, -1)));
		query.getParams().put("endTime", DateUtil.formatDateTime(saveTime));
		return this.selectDayValue(query);
	}

	/**
	 * 根据本次存盘时间获取上一天的数据
	 */
	@Override
	public double selectLastTimeMonthMaxValue(Date saveTime, String varSn, double value) {
		double lastValue = value;
		//查询7天内的数据
		ShardingQuery query = new ShardingQuery();
		query.setVariableName(varSn);
		query.getParams().put("beginTime", DateUtil.formatDateTime(DateUtil.offset(saveTime, DateField.DAY_OF_YEAR, -7)));
		query.getParams().put("endTime", DateUtil.formatDateTime(saveTime));
		ShardingMonth lastData = monthDataMapper.selectLastMonthData(query);
		if (ObjectUtil.isNotNull(lastData) && ObjectUtil.isNotNull(lastData.getMaxValue())) {
			lastValue = lastData.getMaxValue();
		}
		return lastValue;
	}

	/**
	 * 根据本次存盘时间获取上一个小时的数据
	 */
	@Override
	public double selectLastTimeMonthAccuRuntimeValue(Date saveTime, String varSn, double value) {
		double dataValue = value;
		//查询7天内的数据
		ShardingQuery query = new ShardingQuery();
		query.setVariableName(varSn);
		query.getParams().put("beginTime", DateUtil.formatDateTime(DateUtil.offset(saveTime, DateField.DAY_OF_YEAR, -7)));
		query.getParams().put("endTime", DateUtil.formatDateTime(saveTime));
		ShardingMonthAccumulate lastData = monthAccumulateDataMapper.selectLastMonthAccumulateData(query);
		if (ObjectUtil.isNotNull(lastData) && ObjectUtil.isNotNull(lastData.getRuntimeValue())) {
			dataValue = lastData.getRuntimeValue();
		}
		return dataValue;
	}
}
