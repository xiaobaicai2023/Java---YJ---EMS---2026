package com.yunpower.common.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备变量报警信息-websocket
 * */
@Data
public class DeviceVarAlarmMessage implements Serializable {

	/**
	 * 站点sn
	 */
	private String stationSn;

	/**
	 * 站点名称
	 */
	private String stationName;

	/**
	 * 电站类型（1配电 2光伏）
	 */
	private Integer stationType;

	/**
	 * 设备名称
	 */
	private String deviceName;

	/**
	 * 设备变量名称
	 */
	private String deviceVarName;

	/**
	 * 类型名称
	 */
	private String categoryName;

	/**
	 * 报警内容
	 */
	private String content;

	/**
	 * 发生时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date happenTime;

	/**
	 * 报警级别名称
	 */
	private String triggerLevelName;

	/** 报警级别 */
	private Integer triggerLevel;

	/**
	 * 报警状态
	 */
	private Integer triggerStatus;

	/**
	 * 报警状态-名称
	 */
	private String triggerStatusName;

	/**
	 * 报警级别
	 */
	private Long alarmId;

	public DeviceVarAlarmMessage() {
	}


	/**
	 * 推送报警信息
	 */
	public DeviceVarAlarmMessage(String stationSn, String stationName, Integer stationType, String deviceName, String deviceVarName, String categoryName, String content, Date happenTime, String triggerLevelName, Integer triggerLevel, Integer triggerStatus) {
		this.stationSn = stationSn;
		this.stationName = stationName;
		this.stationType = stationType;
		this.deviceName = deviceName;
		this.deviceVarName = deviceVarName;
		this.categoryName = categoryName;
		this.content = content;
		this.happenTime = happenTime;
		this.triggerLevelName = triggerLevelName;
		this.triggerLevel = triggerLevel;
		this.triggerStatus = triggerStatus;
		if (triggerStatus == 0) {
			this.triggerStatusName = "发生中";
		}else if(triggerStatus == 10){
			this.triggerStatusName = "已恢复";
		}
	}
}
