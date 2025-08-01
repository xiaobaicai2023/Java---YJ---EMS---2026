package com.yunpower.collect.protocols.iec104.enums;

import lombok.Getter;

/**
 * @title: U帧 基本指令
 * @Author: Jiajiaglam
 * @date: 2023-12-11 16:26
 * @description:
 */
public enum UControlEnum {
    /**
     * 启动命令
     */
    START_DT(0x07000000),

    /**
     * 启动确认命令
     */
    START_DT_YES(0x0B000000),

    /**
     * 停止指令
     */
    STOP_DT(0x13000000),

    /**
     * 停止确认指令
     */
    STOP_DT_YES(0x23000000),

    /**
     * 测试命令
     */
    TEST_FR_U(0x43000000),

    /**
     * 测试确认指令
     */
    TEST_FR_U_YES(0x83000000);


    @Getter
    private final int value;

    UControlEnum(int value) {
        this.value = value;
    }
}
