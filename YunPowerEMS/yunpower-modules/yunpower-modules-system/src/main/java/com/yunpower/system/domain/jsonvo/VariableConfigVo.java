package com.yunpower.system.domain.jsonvo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * @title: 页面配置表[variable_config] - 字段（JSON格式）
 * @Author: Jiajiaglam
 * @date: 2023-10-24 13:24
 * @description:
 */
public class VariableConfigVo implements Serializable {
    /**
     * 变量名称
     */
    private String varName;

    /**
     * 变量编码
     */
    private String varSn;

    /**
     * 索引地图变量编码
     */
    private String varMapSn;

    /**
     * 变量单位
     */
    private String unit;

    /**
     * 数据存储类型：1变化值 2累计值 3环比
     */
    private Integer storageType;

    /**
     * 是否堆叠：0否 1是
     */
    private Integer isStack;

    /**
     * 是否环比：0否 1是
     */
    private Integer isChain;

    /**
     * 取值类型：枚举 ShardingDataTypeEnum
     */
    private String dataType;

    /**
     * 图表类型：bar/line
     */
    private String chartType;

    /**
     * X轴类型（临时）
     */
    private Integer xAxis;

    /**
     * 子列表
     */
    List<VariableConfigVo> children;

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarSn() {
        return varSn;
    }

    public void setVarSn(String varSn) {
        this.varSn = varSn;
    }

    public String getVarMapSn() {
        return varMapSn;
    }

    public void setVarMapSn(String varMapSn) {
        this.varMapSn = varMapSn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStorageType() {
        return storageType;
    }

    public void setStorageType(Integer storageType) {
        this.storageType = storageType;
    }

    public Integer getIsStack() {
        return isStack;
    }

    public void setIsStack(Integer isStack) {
        this.isStack = isStack;
    }

    public Integer getIsChain() {
        return isChain;
    }

    public void setIsChain(Integer isChain) {
        this.isChain = isChain;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public List<VariableConfigVo> getChildren() {
        return children;
    }

    public void setChildren(List<VariableConfigVo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("varName", getVarName())
                .append("varSn", getVarSn())
                .append("varMapSn", getVarMapSn())
                .append("unit", getUnit())
                .append("storageType", getStorageType())
                .append("isChain", getIsChain())
                .append("dataType", getDataType())
                .append("chartType", getChartType())
                .append("xAxis", getxAxis())
                .toString();
    }
}
