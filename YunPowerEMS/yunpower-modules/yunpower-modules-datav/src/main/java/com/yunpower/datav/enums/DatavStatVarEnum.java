package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 大屏统计类变量枚举
 * */
@Getter
public enum DatavStatVarEnum {

	/**
	 * 用电量
	 * */
	ELECTRIC_USE("electric_use","用电量"),

	/**
	 * 发电量
	 * */
	PV_POWER("pv_power","发电量"),

	/**
	 * 用水量
	 * */
	WATER_USE("water_use","用水量"),

	/**
	 * 储能量
	 * */
	STORAGE_POWER("storage_power","储能量"),

	/**
	 * 充电量
	 * */
	CHARGE_POWER("charge_power","充电量"),

	/**
	 * 减排量
	 * */
	EMISSION_REDUCTION("emission_reduction","减排量");

	/**
	 * 编码
	 * */
	private String key;

	/**
	 * 名称
	 * */
	private String value;

	DatavStatVarEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
