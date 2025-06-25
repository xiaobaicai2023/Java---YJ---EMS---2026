package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 数据类型（1单值 2连续值）
 * */
@Getter
public enum DatavValueEnum {

    /**
     * 连续值
     * */
    SINGLE_VALUE(1,"单值"),

    /**
     * 单值
     * */
    CONTINUOUS_VALUE(2,"连续值");


    /**
     * 编码
     * */
    private Integer key;

    /**
     * 名称
     * */
    private String value;

    DatavValueEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
