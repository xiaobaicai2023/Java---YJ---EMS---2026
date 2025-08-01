package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 图表枚举-日期维度
 * */
@Getter
public enum DatavDateDimSecondEnum {
    /**
     * 5分钟
     * */
    MINUTE_5(5,"5分钟"),

    /**
     * 10分钟
     * */
    MINUTE_10(10,"10分钟"),

    /**
     * 15
     * */
    MINUTE_15(15,"15分钟"),

    /**
     * 30分钟
     * */
    MINUTE_30(30,"30分钟"),

    /**
     * 1小时
     * */
    HOUR_1(60,"1小时");

    /**
     * 编码
     * */
    private Integer key;

    /**
     * 名称
     * */
    private String value;

    DatavDateDimSecondEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
