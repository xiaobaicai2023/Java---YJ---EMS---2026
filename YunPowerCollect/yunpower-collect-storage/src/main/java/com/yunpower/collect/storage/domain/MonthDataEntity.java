package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: 月统计表
 * @Author: Jiajiaglam
 * @date: 2023-12-28 10:55
 * @description:
 */
public class MonthDataEntity implements Serializable {

    /**
     * 编号ID
     */
    private Long id;

    /**
     * 变量名称
     */
    private String variableName;

    /**
     * 日期天
     */
    private Integer daySign;

    /**
     * 最小值
     */
    private Double minValue;

    /**
     * 最小值发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date minTime;

    /**
     * 最大值
     */
    private Double maxValue;

    /**
     * 最大值发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date maxTime;

    /**
     * 平均值
     */
    private Double avgValue;

    /**
     * 累积标记：1时累积 2天累积
     */
    private Integer accuSign;

    /**
     * 累积值
     */
    private Double accuValue;

    /**
     * 实时值
     */
    private Double runTimeValue;

    /**
     * 总记录数
     */
    private Integer totalCount;

    /**
     * 统计时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /**
     * 数据库表名
     */
    private String tableName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Integer getDaySign() {
        return daySign;
    }

    public void setDaySign(Integer daySign) {
        this.daySign = daySign;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Double getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(Double avgValue) {
        this.avgValue = avgValue;
    }

    public Integer getAccuSign() {
        return accuSign;
    }

    public void setAccuSign(Integer accuSign) {
        this.accuSign = accuSign;
    }

    public Double getAccuValue() {
        return accuValue;
    }

    public void setAccuValue(Double accuValue) {
        this.accuValue = accuValue;
    }

    public Double getRunTimeValue() {
        return runTimeValue;
    }

    public void setRunTimeValue(Double runTimeValue) {
        this.runTimeValue = runTimeValue;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
