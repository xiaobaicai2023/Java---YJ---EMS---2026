package com.yunpower.system.controller.vo;

/**
 * @title: 创建有序列表使用
 * @Author: Jiajiaglam
 * @date: 2023-09-25 10:08
 * @description:
 */
public class SvgRuntimeVO {
    private String key;
    private Object value;
    private String time;
    private String svgNodeId;



    private String varSn;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSvgNodeId() {
        return svgNodeId;
    }

    public void setSvgNodeId(String svgNodeId) {
        this.svgNodeId = svgNodeId;
    }
    public String getVarSn() {
        return varSn;
    }

    public void setVarSn(String varSn) {
        this.varSn = varSn;
    }
}
