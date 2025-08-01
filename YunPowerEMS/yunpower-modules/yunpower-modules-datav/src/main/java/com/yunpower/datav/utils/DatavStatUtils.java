package com.yunpower.datav.utils;

import com.yunpower.datav.enums.DatavStatVarEnum;
import com.yunpower.datav.enums.DatavStatVarSecondEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计工具类
 * <pre>
 *    建立映射关系
 *    total_electric_use => electric_use
 *    year_electric_use => electric_use
 *    ...
 * </pre>
 */
public class DatavStatUtils {


	private static final Map<DatavStatVarSecondEnum, DatavStatVarEnum> DatavStatVarSecondEnumMap = new HashMap<>();

	static {
		// 用电量映射
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.TOTAL_ELECTRIC_USE, DatavStatVarEnum.ELECTRIC_USE);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_ELECTRIC_USE, DatavStatVarEnum.ELECTRIC_USE);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.MONTH_ELECTRIC_USE, DatavStatVarEnum.ELECTRIC_USE);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.DAY_ELECTRIC_USE, DatavStatVarEnum.ELECTRIC_USE);

		// 发电量映射
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.TOTAL_PV_POWER, DatavStatVarEnum.PV_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_PV_POWER, DatavStatVarEnum.PV_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.MONTH_PV_POWER, DatavStatVarEnum.PV_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.DAY_PV_POWER, DatavStatVarEnum.PV_POWER);

		// 用水量映射
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.TOTAL_WATER_USE, DatavStatVarEnum.WATER_USE);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_WATER_USE, DatavStatVarEnum.WATER_USE);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.MONTH_WATER_USE, DatavStatVarEnum.WATER_USE);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.DAY_WATER_USE, DatavStatVarEnum.WATER_USE);

		// 储能量映射
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.TOTAL_STORAGE_POWER, DatavStatVarEnum.STORAGE_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_STORAGE_POWER, DatavStatVarEnum.STORAGE_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.MONTH_STORAGE_POWER, DatavStatVarEnum.STORAGE_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.DAY_STORAGE_POWER, DatavStatVarEnum.STORAGE_POWER);

		// 充电量映射
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.TOTAL_CHARGE_POWER, DatavStatVarEnum.CHARGE_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_CHARGE_POWER, DatavStatVarEnum.CHARGE_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.MONTH_CHARGE_POWER, DatavStatVarEnum.CHARGE_POWER);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.DAY_CHARGE_POWER, DatavStatVarEnum.CHARGE_POWER);

		// 减排量映射
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.TOTAL_EMISSION_REDUCTION, DatavStatVarEnum.EMISSION_REDUCTION);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_EMISSION_REDUCTION, DatavStatVarEnum.EMISSION_REDUCTION);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.MONTH_EMISSION_REDUCTION, DatavStatVarEnum.EMISSION_REDUCTION);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.DAY_EMISSION_REDUCTION, DatavStatVarEnum.EMISSION_REDUCTION);
		DatavStatVarSecondEnumMap.put(DatavStatVarSecondEnum.YEAR_EMISSION_REDUCTION_CONVERT, DatavStatVarEnum.EMISSION_REDUCTION);
	}

	/**
	 * 获取对应的一级统计枚举
	 * */
	public static DatavStatVarEnum getFirstLevelEnum(DatavStatVarSecondEnum secondLevelEnum) {
		return DatavStatVarSecondEnumMap.get(secondLevelEnum);
	}
}
