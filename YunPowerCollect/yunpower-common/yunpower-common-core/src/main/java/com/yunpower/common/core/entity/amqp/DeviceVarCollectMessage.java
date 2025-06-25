package com.yunpower.common.core.entity.amqp;

import lombok.*;

import java.io.Serializable;

/**
 * mq-设备变量采集消息实体
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceVarCollectMessage implements Serializable {

	/**
	 * 通道编码
	 * */
	private String channelSn;

	/**
	 * 设备变量编码
	 * */
	private String deviceVarSn;

	/**
	 * 变量值
	 * */
	private Double value;

	/**
	 * 存盘时间
	 * */
	private String saveTime;

	/**
	 * 消息采集时间
	 * */
	private String collectTime;
}
