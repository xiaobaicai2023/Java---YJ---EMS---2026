package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 设备类型列表（1 通讯相关 2 设备相关）
 * */
@Getter
public enum DatavEquipTypeEnum {

    /**
     * 通讯设备
     * */
    COMMUNICATION_DEVICE(1,"通讯相关"),

    /**
     * 设备相关
     * */
    MONITOR_DEVICE(2,"设备相关");


    /**
     * 编码
     * */
    private Integer key;

    /**
     * 名称
     * */
    private String value;

    DatavEquipTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
