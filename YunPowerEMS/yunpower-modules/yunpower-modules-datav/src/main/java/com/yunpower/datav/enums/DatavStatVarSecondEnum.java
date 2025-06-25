package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 大屏统计类变量二级枚举
 * */
@Getter
public enum DatavStatVarSecondEnum {

	/**
	 * 用电量
	 * */
	TOTAL_ELECTRIC_USE("total_electric_use", "总用电量",DatavDateDimEnum.TOTAL),
	YEAR_ELECTRIC_USE("year_electric_use", "年用电量",DatavDateDimEnum.YEAR),
	MONTH_ELECTRIC_USE("month_electric_use", "月用电量",DatavDateDimEnum.MONTH),
	DAY_ELECTRIC_USE("day_electric_use", "日用电量",DatavDateDimEnum.DAY),

	/**
	 * 发电量
	 * */
	TOTAL_PV_POWER("total_pv_power", "总发电量",DatavDateDimEnum.TOTAL),
	YEAR_PV_POWER("year_pv_power", "年发电量",DatavDateDimEnum.YEAR),
	MONTH_PV_POWER("month_pv_power", "月发电量",DatavDateDimEnum.MONTH),
	DAY_PV_POWER("day_pv_power", "日发电量",DatavDateDimEnum.DAY),

	/**
	 * 用水量
	 * */
	TOTAL_WATER_USE("total_water_use", "总用水量",DatavDateDimEnum.TOTAL),
	YEAR_WATER_USE("year_water_use", "年用水量",DatavDateDimEnum.YEAR),
	MONTH_WATER_USE("month_water_use", "月用水量",DatavDateDimEnum.MONTH),
	DAY_WATER_USE("day_water_use", "日用水量",DatavDateDimEnum.DAY),

	/**
	 * 储能量
	 * */
	TOTAL_STORAGE_POWER("total_storage_power", "总储能量",DatavDateDimEnum.TOTAL),
	YEAR_STORAGE_POWER("year_storage_power", "年储能量",DatavDateDimEnum.YEAR),
	MONTH_STORAGE_POWER("month_storage_power", "月储能量",DatavDateDimEnum.MONTH),
	DAY_STORAGE_POWER("day_storage_power", "日储能量",DatavDateDimEnum.DAY),

	/**
	 * 充电量
	 * */
	TOTAL_CHARGE_POWER("total_charge_power", "总充电量",DatavDateDimEnum.TOTAL),
	YEAR_CHARGE_POWER("year_charge_power", "年充电量",DatavDateDimEnum.YEAR),
	MONTH_CHARGE_POWER("month_charge_power", "月充电量",DatavDateDimEnum.MONTH),
	DAY_CHARGE_POWER("day_charge_power", "日充电量",DatavDateDimEnum.DAY),

	/**
	 * 减排量
	 * */
	TOTAL_EMISSION_REDUCTION("total_emission_reduction", "总减排量",DatavDateDimEnum.TOTAL),
	YEAR_EMISSION_REDUCTION("year_emission_reduction", "年减排量",DatavDateDimEnum.YEAR),
	MONTH_EMISSION_REDUCTION("month_emission_reduction", "月减排量",DatavDateDimEnum.MONTH),
	DAY_EMISSION_REDUCTION("day_emission_reduction", "日减排量",DatavDateDimEnum.DAY),
	YEAR_EMISSION_REDUCTION_CONVERT("yearEmissionReduction", "年减排量",DatavDateDimEnum.YEAR);


	/**
	 * 编码
	 * */
	private String key;

	/**
	 * 名称
	 * */
	private String value;

	//获取日期
	private DatavDateDimEnum dateDimEnum;

	DatavStatVarSecondEnum(String key, String value, DatavDateDimEnum dateDimEnum) {
		this.key = key;
		this.value = value;
		this.dateDimEnum = dateDimEnum;
	}

	/**
	 *
	 * */
	public static DatavStatVarSecondEnum fromKey(String key) {
		for (DatavStatVarSecondEnum type : values()) {
			if (type.getKey().equals(key)) {
				return type;
			}
		}
		return null;
	}
}
