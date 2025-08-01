package com.yunpower.common.core.enums.sharding;

import com.yunpower.common.core.enums.vo.EnumSOVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 图表X轴类型
 * @Author: Jiajiaglam
 * @date: 2023-10-25 14:37
 * @description: 取值说明：
 * 【取列表】
 * ...5/10/15/30/小时 => 日存储表
 * ...天 => 月统计表
 * 【取单值】
 * ...天 => 月统计表
 * ...月 => 月统计表
 * ...年 => 月统计表
 */
public enum XAxisTypeEnum {
    M5   ("5分钟",  5),
    M10  ("10分钟", 10),
    M15  ("15分钟", 15),
    M30  ("30分钟", 30),
    HOUR ("小时",   60),
    DAY  ("天",     70),
    MONTH("月",     80),
    YEAR ("年",     90);

    private final String key;
    private final int value;

    XAxisTypeEnum(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public static int getEnumByKey(String key) {
        for (XAxisTypeEnum temp : XAxisTypeEnum.values()) {
            if (temp.getKey().equals(key)) {
                return temp.getValue();
            }
        }

        return -1;
    }

    public static List<EnumSOVO> getEnumList() {
        List<EnumSOVO> list = new ArrayList<>();

        for (XAxisTypeEnum temp : XAxisTypeEnum.values()) {
            EnumSOVO vo = new EnumSOVO();
            vo.setKey(temp.getKey());
            vo.setValue(temp.getValue());
            list.add(vo);
        }

        return list;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}
