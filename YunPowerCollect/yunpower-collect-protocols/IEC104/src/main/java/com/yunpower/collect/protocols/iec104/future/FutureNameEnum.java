package com.yunpower.collect.protocols.iec104.future;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 线程名称列举
 * @Author: Jiajiaglam
 * @date: 2023-12-27 8:22
 * @description:
 */
@Getter
public enum FutureNameEnum {
    /**
     * 启动帧 线程名称
     */
    START_FRAME_NAME("startFrame_"),

    /**
     * 测试帧 线程名称
     */
    TEST_FRAME_NAME("testFrame_"),

    /**
     * 总召唤帧
     */
    TOTAL_SUMMON_FRAME("totalFrame_");

    private final String value;

    FutureNameEnum(String value) {
        this.value = value;
    }

    /**
     * 获取所有枚举列表
     */
    public static List<String> getEnumsList() {
        List<String> list = new ArrayList<>();
        for (FutureNameEnum item : FutureNameEnum.values()) {
            list.add(item.getValue());
        }
        return list;
    }
}
