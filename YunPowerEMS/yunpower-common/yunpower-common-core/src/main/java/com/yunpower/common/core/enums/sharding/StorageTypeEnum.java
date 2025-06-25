package com.yunpower.common.core.enums.sharding;

import com.yunpower.common.core.enums.vo.EnumIOVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 数据存储类型：1变化值 2累计值
 * @Author: Jiajiaglam
 * @date: 2023-11-07 8:45
 * @description:
 */
public enum StorageTypeEnum {
    CHANGE(1, "变化值"),
    ACCUM (2, "累计值");

    private final int key;
    private final String value;

    StorageTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getEnumByKey(int key) {
        for (StorageTypeEnum temp : StorageTypeEnum.values()) {
            if (temp.getKey() == key) {
                return temp.getValue();
            }
        }
        return null;
    }

    public static List<EnumIOVO> getEnumList() {
        List<EnumIOVO> list = new ArrayList<>();

        for (StorageTypeEnum temp : StorageTypeEnum.values()) {
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
