package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 系统配置变量类型
 */
@Getter
public enum SysConfigVariableTypeEnum {

    /**
     * 不指定
     */
    UNSPECIFIED(0, "不指定"),

    /**
     * 电能（用电）
     */
    ELECTRICITY_CONSUMPTION(1, "电能（用电）"),

    /**
     * 电能（发电）
     */
    ELECTRICITY_GENERATION(2, "电能（发电）"),

    /**
     * 电流A
     */
    CURRENT_A(3, "电流A"),

    /**
     * 电流B
     */
    CURRENT_B(4, "电流B"),

    /**
     * 电流C
     */
    CURRENT_C(5, "电流C"),

    /**
     * 电压A
     */
    VOLTAGE_A(6, "电压A"),

    /**
     * 电压B
     */
    VOLTAGE_B(7, "电压B"),

    /**
     * 电压C
     */
    VOLTAGE_C(8, "电压C"),

    /**
     * 有功功率
     */
    ACTIVE_POWER(9, "有功功率"),

    /**
     * 无功功率
     */
    REACTIVE_POWER(10, "无功功率"),

    /**
     * 视在功率
     */
    APPARENT_POWER(11, "视在功率"),

    /**
     * 功率因素
     */
    POWER_FACTOR(12, "功率因素"),

    /**
     * 温度
     */
    TEMPERATURE(13, "温度"),

    /**
     * 湿度
     */
    HUMIDITY(14, "湿度"),

    /**
     * 需量
     */
    DEMAND(15, "需量"),

    /**
     * 负载率
     */
    LOAD_RATE(16, "负载率"),

    /**
     * 状态
     */
    STATUS(17, "状态");

    /**
     * 编码
     */
    private Integer key;

    /**
     * 名称
     */
    private String value;

    SysConfigVariableTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String fromKey(Integer key) {
        for (SysConfigVariableTypeEnum type : values()) {
            if (type.getKey().equals(key)) {
                return type.getValue();
            }
        }
        return null;
    }
}

