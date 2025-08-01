package com.yunpower.datav.enums;

import lombok.Getter;

/**
 * 设备类型列表（1 通讯状态 2 设备状态 3 市电状态）
 * */
@Getter
public enum DatavStateTypeEnum {

    /**
     * 通讯状态
     * */
    COMMUNICATION_STATUS(1, "通讯状态"),

    /**
     * 设备状态
     * */
    DEVICE_STATUS(2, "设备状态"),

    /**
     * 市电状态
     * */
    MAINS_STATUS(3, "市电状态");

    /**
     * 编码
     * */
    private final Integer key;

    /**
     * 名称
     * */
    private final String value;

    DatavStateTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DatavStateTypeEnum fromKey(Integer key) {
        for (DatavStateTypeEnum type : values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        return null;
    }
}
