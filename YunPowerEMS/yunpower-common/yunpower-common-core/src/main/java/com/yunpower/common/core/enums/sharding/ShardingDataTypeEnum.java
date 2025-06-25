package com.yunpower.common.core.enums.sharding;

import com.yunpower.common.core.enums.vo.EnumSOVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 从数据库取值类型
 * @Author: Jiajiaglam
 * @date: 2023-10-25 14:38
 * @description: 取值说明：
 * ...最小值   => 月统计表（全月数据 MIN(min_value)）
 * ...最大值   => 月统计表（全月数据 MAX(max_value)）
 * ...平均值   => 月统计表（全月数据 AVG(avg_value)）
 * ...记录数   => 月统计表（最后一条记录 total_count）
 * ...日变化值 => 月统计表（当天数据 avg_value）
 * ...日累计值 => 月统计表（当天数据 accu_value）
 * ...月累计值 => 月统计表（全月数据 SUM(accu_value)）
 * ...年累计值 => 月统计表（当年最后一条数据 max_value - 当年1月1日数据 min_value）（全年数据 SUM(accu_value)）
 * ...时累计值 ===> 日存储表（下一个小时的0点值 - 当前小时的0点值）
 */
public enum ShardingDataTypeEnum {
    MONTH_MIN ("MMIN", "最小值（单值）"),
    MONTH_MAX ("MMAX", "最大值（单值）"),
    MONTH_AVG ("MAVG", "平均值（单值）"),
    MONTH_LJZ ("MLJZ", "累计值（单值）"),
    MONTH_CONT("MCNT", "记录数（单值）"),
    MONTH_BH_D("MD1",  "日变化值（列表）"),
    MONTH_LJ_D("MD2",  "日累计值（列表）"),
    MONTH_BH_M("MM1",  "月平均值（列表）"),
    MONTH_LJ_M("MM2",  "月累计值（列表）"),
    MONTH_BH_Y("MY1",  "年平均值（列表）"),
    MONTH_LJ_Y("MY2",  "年累计值（列表）"),
    DAY_BH_H  ("DH1",  "时变化值（列表）"),
    DAY_LJ_H  ("DH2",  "时累计值（列表）"),
    DAY_BH_30 ("D301", "30分钟变化值（列表）"),
    DAY_LJ_30 ("D302", "30分钟累计值（列表）"),
    DAY_BH_15 ("D151", "15分钟变化值（列表）"),
    DAY_LJ_15 ("D152", "15分钟累计值（列表）"),
    DAY_BH_10 ("D101", "10分钟变化值（列表）"),
    DAY_LJ_10 ("D102", "10分钟累计值（列表）"),
    DAY_BH_05 ("D051", "5分钟变化值（列表）"),
    DAY_LJ_05 ("D052", "5分钟累计值（列表）"),
    ACCU_LJ_H ("AH2",  "时累计值（列表，中间表）");

    private final String key;
    private final String value;

    ShardingDataTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getEnumByKey(String key) {
        for (ShardingDataTypeEnum temp : ShardingDataTypeEnum.values()) {
            if (temp.getKey().equals(key)) {
                return temp.getValue();
            }
        }
        return null;
    }

    public static List<EnumSOVO> getEnumList() {
        List<EnumSOVO> list = new ArrayList<>();

        for (ShardingDataTypeEnum temp : ShardingDataTypeEnum.values()) {
            EnumSOVO vo = new EnumSOVO();
            vo.setKey(temp.getKey());
            vo.setValue(temp.getValue());
            list.add(vo);
        }

        return list;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
