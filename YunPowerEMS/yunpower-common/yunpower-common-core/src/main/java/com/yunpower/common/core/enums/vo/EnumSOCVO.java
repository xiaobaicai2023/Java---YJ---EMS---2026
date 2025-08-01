package com.yunpower.common.core.enums.vo;

import java.util.List;

/**
 * @title: 创建有序列表使用（有子列表）
 * @Author: Jiajiaglam
 * @date: 2023-09-25 10:08
 * @description:
 */
public class EnumSOCVO {
    private String key;
    private Object value;
    private List<EnumSOCVO> children;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<EnumSOCVO> getChildren() {
        return children;
    }

    public void setChildren(List<EnumSOCVO> children) {
        this.children = children;
    }
}
