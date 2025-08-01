package com.yunpower.common.core.enums;

import com.yunpower.common.core.enums.vo.EnumIOVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 数据权限选择列表
 * @Author: Jiajiaglam
 * @date: 2023-09-25 10:19
 * @description:
 */
public enum DataAuthEnum {
    //1全部数据权限 2自定数据权限 3本部门数据权限 4本部门及以下数据权限 5仅本人数据权限
    ALL             (1, "全部数据权限"),
    CUSTOMIZE       (2, "自定数据权限"),
    DEPT            (3, "本部门数据权限"),
    DEPT_ADN_BELOW  (4, "本部门及以下数据权限");

    private final int key;
    private final String value;

    DataAuthEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getEnumByKey(int key) {
        String value = "";

        for (DataAuthEnum temp : DataAuthEnum.values()) {
            if (temp.getKey() == key) {
                return temp.getValue();
            }
        }

        return value;
    }

    public static List<EnumIOVO> getEnumList(Integer isSupper) {
        List<EnumIOVO> list = new ArrayList<>();

        for (DataAuthEnum temp : DataAuthEnum.values()) {
            // 非管理员不显示第1个权限
            if (isSupper != 1 && temp.getKey() == 1) {
                continue;
            }

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
