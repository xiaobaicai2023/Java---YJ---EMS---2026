package com.yunpower.collect.protocols.iec104.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * IEC104的异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IEC104Exception extends Exception {

	/**
	 * Iec exception
	 */
	public IEC104Exception(int code, String msg) {
		super(msg);
		this.code=code;
		this.msg = msg;
	}

	/**
	 * Iec exception
	 */
	public IEC104Exception(String msg) {
		super(msg);
		this.msg = msg;
	}

	private int code;
	private String msg;
}
