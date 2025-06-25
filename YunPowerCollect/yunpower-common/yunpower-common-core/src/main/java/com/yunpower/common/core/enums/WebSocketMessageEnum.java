package com.yunpower.common.core.enums;

import lombok.Getter;

/**
 * webSocket 消息枚举
 */
@Getter
public enum WebSocketMessageEnum {

	/**
	 * 订阅后应答消息
	 * */
	SUBSCRIBE_RESPONSE_MESSAGE("subscribe_response", "订阅后应答消息"),

	/**
	 * 设备变量实时数据
	 * */
	DEVICE_RUNTIME_MESSAGE("runtime_msg", "设备变量实时数据"),

	/**
	 * 设备变量报警数据
	 * */
	DEVICE_ALARM_MESSAGE("alarm_msg", "设备变量报警数据");

	private final String key;
	private final String name;

	WebSocketMessageEnum(String key, String name) {
		this.key = key;
		this.name = name;
	}

}
