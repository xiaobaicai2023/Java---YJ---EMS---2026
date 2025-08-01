package com.yunpower.common.core.entity.amqp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * mq-设备变量采集消息实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStateMessage implements Serializable {

	/**
	 * 通道编码
	 * */
	private String channelSn;

	/**
	 * 设备编码
	 */
	private String deviceSn;

	/**
	 * 类型 channel:通道 channel_device:通道设备 monitor_device:监控设备
	 * */
	private String type;

	/**
	 * 状态 1上线 0下线
	 */
	private Integer state;

	/**
	 * 变更时间
	 */
	private String changeTime;
}
