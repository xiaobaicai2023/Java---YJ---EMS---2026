package com.yunpower.system.domain.jsonvo;

import com.yunpower.common.core.enums.sharding.ShardingDataTypeEnum;
import com.yunpower.common.core.enums.sharding.XAxisTypeEnum;

/**
 * @title: 页面配置表[variable_config] - 字段（预设）
 * @Author: Jiajiaglam
 * @date: 2023-11-11 14:27
 * @description: 因为按日、月、年获取数据的配置是固定的，因此预设几个
 */
public class VariableConfigPresetsUtils {
    // 预设（普通图表）
    public static VariableConfigVo config(String configSign, Integer storageType) {

        VariableConfigVo presetsVo = new VariableConfigVo();

        //region 组合1（单日期 - 变化值）普通图表
        if (configSign.equalsIgnoreCase("day-s-1")) {
            presetsVo.setxAxis(XAxisTypeEnum.HOUR.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.DAY_BH_H.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("month-s-1")) {
            presetsVo.setxAxis(XAxisTypeEnum.DAY.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_BH_D.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("year-s-1")) {
            presetsVo.setxAxis(XAxisTypeEnum.MONTH.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_BH_M.getKey());
            presetsVo.setStorageType(storageType);
        }
        //endregion

        //region 组合2（单日期 - 累计值）普通图表
        if (configSign.equalsIgnoreCase("day-s-2")) {
            presetsVo.setxAxis(XAxisTypeEnum.HOUR.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.DAY_LJ_H.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("month-s-2")) {
            presetsVo.setxAxis(XAxisTypeEnum.DAY.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_D.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("year-s-2")) {
            presetsVo.setxAxis(XAxisTypeEnum.MONTH.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_M.getKey());
            presetsVo.setStorageType(storageType);
        }
        //endregion

        //region 组合3（双日期 - 累计值）尖峰平谷
        if (configSign.equalsIgnoreCase("day-d-ppfv")) {
            presetsVo.setxAxis(XAxisTypeEnum.DAY.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_D.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("month-d-ppfv")) {
            presetsVo.setxAxis(XAxisTypeEnum.MONTH.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_M.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("year-d-ppfv")) {
            presetsVo.setxAxis(XAxisTypeEnum.YEAR.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_Y.getKey());
            presetsVo.setStorageType(storageType);
        }
        //endregion

        //region 组合4（双日期 - 变化值）普通图表
        if (configSign.equalsIgnoreCase("day-d-1")) {
            presetsVo.setxAxis(XAxisTypeEnum.DAY.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_BH_D.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("month-d-1")) {
            presetsVo.setxAxis(XAxisTypeEnum.MONTH.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_BH_M.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("year-d-1")) {
            presetsVo.setxAxis(XAxisTypeEnum.YEAR.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_BH_Y.getKey());
            presetsVo.setStorageType(storageType);
        }
        //endregion

        //region 组合5（双日期 - 累计值）普通图表
        if (configSign.equalsIgnoreCase("day-d-2")) {
            presetsVo.setxAxis(XAxisTypeEnum.DAY.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_D.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("month-d-2")) {
            presetsVo.setxAxis(XAxisTypeEnum.MONTH.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_M.getKey());
            presetsVo.setStorageType(storageType);
        }
        if (configSign.equalsIgnoreCase("year-d-2")) {
            presetsVo.setxAxis(XAxisTypeEnum.YEAR.getValue());
            presetsVo.setDataType(ShardingDataTypeEnum.MONTH_LJ_Y.getKey());
            presetsVo.setStorageType(storageType);
        }
        //endregion
        return presetsVo;
    }
}
