package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 电价标准列表（1 跟随日期 2 指定电价）
 * */
@Getter
public enum DatavElectricityPriceTypeEnum {

    /**
     * 通讯设备
     * */
    COMMUNICATION_DEVICE(1,"跟随日期"),

    /**
     * 设备相关
     * */
    MONITOR_DEVICE(2,"指定电价");


    /**
     * 编码
     * */
    private Integer key;

    /**
     * 名称
     * */
    private String value;

    DatavElectricityPriceTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
