package com.yunpower.common.core.vo;

import lombok.Data;

/**
 * @title: 创建有序列表使用
 * @Author: Jiajiaglam
 * @date: 2023-09-25 10:08
 * @description:
 */
@Data
public class RedisDeviceStateVO {
    private String deviceSn;
    private Integer state;
    private String time;

    public RedisDeviceStateVO(){

    }

    public RedisDeviceStateVO(String deviceSn, Integer state, String time) {
        this.deviceSn = deviceSn;
        this.state = state;
        this.time = time;
    }
}
