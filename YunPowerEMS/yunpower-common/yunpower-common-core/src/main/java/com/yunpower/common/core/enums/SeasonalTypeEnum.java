package com.yunpower.common.core.enums;

import com.yunpower.common.core.enums.vo.EnumIOVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 峰谷平尖枚举（与字典点要一一对应）
 * @Author: Jiajiaglam
 * @date: 2023-11-04 9:23
 * @description:
 */
public enum SeasonalTypeEnum {
    JIANG(1, "尖"),
    FENG (2, "峰"),
    PING (3, "平"),
    GU   (4, "谷"),
    SHEN (5, "深谷");


    private final int key;
    private final String value;

    SeasonalTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getEnumByKey(int key) {
        String value = "";

        for (SeasonalTypeEnum temp : SeasonalTypeEnum.values()) {
            if (temp.getKey() == key) {
                return temp.getValue();
            }
        }

        return value;
    }

    public static List<EnumIOVO> getEnumList() {
        List<EnumIOVO> list = new ArrayList<>();

        for (SeasonalTypeEnum temp : SeasonalTypeEnum.values()) {
            EnumIOVO vo = new EnumIOVO();
            vo.setKey(temp.getKey());
            vo.setValue(temp.getValue());
            list.add(vo);
        }

        return list;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
