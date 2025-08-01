package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.ShardingDay;
import com.yunpower.collect.storage.domain.ShardingQuery;

import java.util.Date;

public interface IShardingCommonService {

	/**
	 * 获取day表最新一条数据
	 * */
	public ShardingDay selectDayValue(ShardingQuery shardingQuery);

	/**
	 * 获取近24小时内最新一条数据
	 * */
	public ShardingDay selectLastDayValue24H(String deviceVarSn);

	/**
	 * 获取近24小时内最新一条数据
	 * */
	public ShardingDay selectLastDayValue24H(Date saveTime, String deviceVarSn);

	/**
	 * 获取上次存盘数据
	 * */
	public double selectLastTimeMonthMaxValue(Date saveTime, String varSn, double value);

	/**
	 * 根据本次存盘时间获取上一个小时的数据
	 * */
	public double selectLastTimeMonthAccuRuntimeValue(Date saveTime, String varSn, double value);
}
