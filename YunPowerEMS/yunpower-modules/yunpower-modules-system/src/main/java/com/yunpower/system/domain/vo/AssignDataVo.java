package com.yunpower.system.domain.vo;

/**
 * 指定接口数据 返回结果
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/26 15:13
 */
public class AssignDataVo {
    /**
     * 例如:"network"
     */
    private String key;

    /**
     * 例如:true, 2356.4, 8630
     */
    private Object value;

    /**
     * 例如:"网络通断"
     */
    private String label;

    /**
     * 例如:"kwh"
     */
    private String unit;

    public AssignDataVo() {

    }

    public AssignDataVo(Object value) {
        this.value = value;
    }

    public AssignDataVo(String key, Object value, String label, String unit) {
        this.key = key;
        this.value = value;
        this.label = label;
        this.unit = unit;
    }

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
