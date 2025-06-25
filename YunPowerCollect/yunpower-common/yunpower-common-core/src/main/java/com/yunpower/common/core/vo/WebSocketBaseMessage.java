package com.yunpower.common.core.vo;

import com.yunpower.common.core.enums.WebSocketMessageEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * websocket 信息内容
 */
@Data
public class WebSocketBaseMessage implements Serializable {

	/**
	 * 类型 -> WebSocketMessageEnum
	 */
	private String bizType;

	/**
	 * 业务数据
	 */
	private Object bizData;

	public WebSocketBaseMessage() {

	}

	public WebSocketBaseMessage(WebSocketMessageEnum messageEnum, Object bizData) {
		this.bizType = messageEnum.getKey();
		this.bizData = bizData;
	}
}
