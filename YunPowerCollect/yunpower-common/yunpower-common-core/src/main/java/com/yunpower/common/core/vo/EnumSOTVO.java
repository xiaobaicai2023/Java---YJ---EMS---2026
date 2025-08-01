package com.yunpower.common.core.vo;

import lombok.Data;

/**
 * @title: 创建有序列表使用
 * @Author: Jiajiaglam
 * @date: 2023-09-25 10:08
 * @description:
 */
@Data
public class EnumSOTVO {
    private String key;
    private Object value;
    private String time;

    public EnumSOTVO(String key, Double value, String time) {
        this.key = key;
        this.value = value;
        this.time = time;
    }
    public EnumSOTVO(String key, String value, String time) {
        this.key = key;
        this.value = value;
        this.time = time;
    }
}
