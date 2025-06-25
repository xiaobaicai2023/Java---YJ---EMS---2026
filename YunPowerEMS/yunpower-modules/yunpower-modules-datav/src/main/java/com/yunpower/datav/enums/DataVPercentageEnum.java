package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 显示类型列表（1 数值 2 百分比 3 比值）
 */
@Getter
public enum DataVPercentageEnum {

    NUMBER(1, "数值"),
    PERCENTAGE(2, "百分比"),
    RATIO(3, "比值");
    /**
     * 编码
     */
    private Integer key;

    /**
     * 名称
     */
    private String value;

    DataVPercentageEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
