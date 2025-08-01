package com.yunpower.common.core.enums;

/**
 * @title: 站点类型与设备分组一一对应表
 * @Author: Jiajiaglam
 * @date: 2024-01-23 14:23
 * @description: 站点类型：sys_station_type 枚举：1用电、2光伏、3用水、4流量、5储能、6计碳
 *               分组类型：sys_group_map 枚举：2用电设备分组、3光伏设备分组、6用水设备分组、7流量设备分组、8储能设备分组、9计碳设备分组
 *               //字典表 dict_sn 站点类型：sys_station_type，分组类型：sys_group_map
 *               //SELECT * FROM sys_common_dict_data WHERE dict_sn='sys_station_type'
 *               //SELECT * FROM sys_common_dict_data WHERE dict_sn='sys_group_map';
 */
public enum StationTypeGroupEnum {
    YD(1, 2L),
    GF(2, 3L),
    YS(5, 6L),
    LL(6, 7L),
    CN(3, 8L),
    JT(7, 9L);


    private final int key;
    private final Long value;

    StationTypeGroupEnum(int key, Long value) {
        this.key = key;
        this.value = value;
    }

    public static Long getEnumByKey(int key) {
        for (StationTypeGroupEnum temp : StationTypeGroupEnum.values()) {
            if (temp.getKey() == key) {
                return temp.getValue();
            }
        }

        return 0L;
    }

    public int getKey() {
        return key;
    }

    public Long getValue() {
        return value;
    }
}
