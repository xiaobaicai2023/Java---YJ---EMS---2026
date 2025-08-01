package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 图表枚举-日期维度
 * */
@Getter
public enum DatavDateDimEnum {
    /**
     * 日
     * */
    DAY(70,"日"),

    /**
     * 月
     * */
    MONTH(80,"月"),

    /**
     * 年
     * */
    YEAR(90,"年"),

    /**
     * 汇总
     * */
    TOTAL(100,"总");

    /**
     * 编码
     * */
    private Integer key;

    /**
     * 名称
     * */
    private String value;

    DatavDateDimEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DatavDateDimEnum fromKey(Integer key) {
        for (DatavDateDimEnum type : values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        return null;
    }
}
