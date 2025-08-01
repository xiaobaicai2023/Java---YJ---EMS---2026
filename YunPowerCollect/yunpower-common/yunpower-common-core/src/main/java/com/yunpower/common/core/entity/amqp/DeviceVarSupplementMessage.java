package com.yunpower.common.core.entity.amqp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * mq-设备变量补充消息-实体
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceVarSupplementMessage implements Serializable {

	/**
	 * 通道编码
	 * */
	private String channelSn;

	/**
	 * 设备变量编码
	 * */
	private String deviceVarSn;

	/**
	 * 补充开始时间
	 * */
	private String beginTime;

	/**
	 * 补充结束时间
	 * */
	private String endTime;

	/**
	 * 上次存盘时间
	 * */
	private String lastSaveTime;

	/**
	 * 上次值
	 * */
	private Double lastValue;
}
