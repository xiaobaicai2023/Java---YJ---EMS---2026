package com.yunpower.common.core.enums;

import com.yunpower.common.core.enums.vo.EnumIOVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: 菜单权限选择列表
 * @Author: Jiajiaglam
 * @date: 2023-09-25 9:56
 * @description:
 */
public enum MenuAuthEnum {
    EDIT     (1, "全部菜单"),
    VISITOR  (2, "仅浏览"),
    CUSTOMIZE(3, "自定义");

    private final int key;
    private final String value;

    MenuAuthEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getEnumByKey(int key) {
        String value = "";

        for (MenuAuthEnum temp : MenuAuthEnum.values()) {
            if (temp.getKey() == key) {
                return temp.getValue();
            }
        }

        return value;
    }

    public static List<EnumIOVO> getEnumList(Integer isSupper) {
        List<EnumIOVO> list = new ArrayList<>();

        for (MenuAuthEnum temp : MenuAuthEnum.values()) {
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
