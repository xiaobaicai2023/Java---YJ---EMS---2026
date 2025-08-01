package com.yunpower.datav.utils;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.vo.AssignDataVo;
import com.yunpower.datav.domain.vo.ChartCardInfoVo;

import java.util.HashMap;
import java.util.Map;

/**
 * 电力单位准换
 * */
public class EnergyConverter {
    private static final Map<String, String[]> UNIT_MAP = new HashMap<>();

    static {
        UNIT_MAP.put("kw", new String[]{"MW", "GW"});
        UNIT_MAP.put("kwh", new String[]{"MWh", "GWh"});
        UNIT_MAP.put("kvar", new String[]{"MVar", "GVar"});
        UNIT_MAP.put("kvarh", new String[]{"MVarh", "GVarh"});
        UNIT_MAP.put("t", new String[]{"kt", "Mt"});
    }

    //region 通用图表-单值

    public static void convertEnergy(ChartCardInfoVo.SingleInfo singleInfo) {
        String unit = singleInfo.getUnit().toLowerCase();
        if (UNIT_MAP.containsKey(unit)) {
            updateSingleInfo(singleInfo, UNIT_MAP.get(unit));
        }
    }

    private static void updateSingleInfo(ChartCardInfoVo.SingleInfo singleInfo, String[] units) {
        double value = singleInfo.getSingleValue();
        if (value >= 1000000) {
            singleInfo.setSingleValue(DoubleUtils.calculateRatioCeil(value, 1000000D));
            singleInfo.setUnit(units[1]);
        } else if (value >= 1000) {
            singleInfo.setSingleValue(DoubleUtils.calculateRatioCeil(value, 1000D));
            singleInfo.setUnit(units[0]);
        }
    }

    //endregion





    //region 大屏-统计变量

    public static void convertEnergy(AssignDataVo dataVo) {
        String unit = dataVo.getUnit().toLowerCase();
        if (UNIT_MAP.containsKey(unit)) {
            updateSingleInfo(dataVo, UNIT_MAP.get(unit));
        }
    }

    private static void updateSingleInfo(AssignDataVo dataVo, String[] units) {
        double value = DoubleUtils.stringToDouble(dataVo.getValue().toString());
        if (value >= 1000000) {
            dataVo.setValue(DoubleUtils.calculateRatioCeil(value, 1000000D));
            dataVo.setUnit(units[1]);
        } else if (value >= 1000) {
            dataVo.setValue(DoubleUtils.calculateRatioCeil(value, 1000D));
            dataVo.setUnit(units[0]);
        }
    }

    //endregion
}
