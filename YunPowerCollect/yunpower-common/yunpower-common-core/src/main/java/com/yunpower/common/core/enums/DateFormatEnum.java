package com.yunpower.common.core.enums;

/**
 * @title: 日期格式化格式
 * @Author: Jiajiaglam
 * @date: 2023-10-18 16:42
 * @description:
 */
public enum DateFormatEnum {
    YYYY("yyyy"),
    YYYYMM("yyyyMM"),
    YYYYMMDD("yyyyMMdd"),
    YYYYMMDDHHMM("yyyyMMddHHmm"),
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    YYYY_MM("yyyy-MM"),
    YYYY_MM_DD("yyyy-MM-dd"),
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");

    private final String value;

    DateFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}