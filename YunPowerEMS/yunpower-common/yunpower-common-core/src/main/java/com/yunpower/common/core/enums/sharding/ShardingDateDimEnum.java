package com.yunpower.common.core.enums.sharding;

/**
 * 图表枚举-日期维度（新）
 */
public enum ShardingDateDimEnum {
    /**
     * 日
     */
    DAY(70, "日"),

    /**
     * 月
     */
    MONTH(80, "月"),

    /**
     * 年
     */
    YEAR(90, "年");

    /**
     * 编码
     */
    private final Integer key;

    /**
     * 名称
     */
    private final String value;

    ShardingDateDimEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
