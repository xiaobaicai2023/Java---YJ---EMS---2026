package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @title: 月累计数据
 * @Author: Jiajiaglam
 * @date: 2023-12-28 10:56
 * @description:
 */
public class MonthAccumulateDataEntity implements Serializable {

    /**
     * 编号ID
     */
    private Long id;

    /**
     * 变量名称
     */
    private String variableName;

    /**
     * 存盘时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /**
     * 上一个小时runtime值
     */
    private Double dataValue;

    /**
     * 当前数据
     */
    private Double runtimeValue;

    /**
     * 年
     */
    private Integer recordYear;

    /**
     * 月
     */
    private Integer recordMonth;

    /**
     * 日
     */
    private Integer recordDay;

    /**
     * 周
     */
    private Integer recordWeek;

    /**
     * 时
     */
    private Integer recordHour;

    /**
     * 累积数据
     */
    private Double accuData;

    /**
     * 峰谷名称
     */
    private String seasonalTypeName;

    /**
     * 计费电价
     */
    private Float chargePrice;

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

    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public Double getDataValue() {
        return dataValue;
    }

    public void setDataValue(Double dataValue) {
        this.dataValue = dataValue;
    }

    public Double getRuntimeValue() {
        return runtimeValue;
    }

    public void setRuntimeValue(Double runtimeValue) {
        this.runtimeValue = runtimeValue;
    }

    public Integer getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(Integer recordYear) {
        this.recordYear = recordYear;
    }

    public Integer getRecordMonth() {
        return recordMonth;
    }

    public void setRecordMonth(Integer recordMonth) {
        this.recordMonth = recordMonth;
    }

    public Integer getRecordDay() {
        return recordDay;
    }

    public void setRecordDay(Integer recordDay) {
        this.recordDay = recordDay;
    }

    public Integer getRecordWeek() {
        return recordWeek;
    }

    public void setRecordWeek(Integer recordWeek) {
        this.recordWeek = recordWeek;
    }

    public Integer getRecordHour() {
        return recordHour;
    }

    public void setRecordHour(Integer recordHour) {
        this.recordHour = recordHour;
    }

    public Double getAccuData() {
        return accuData;
    }

    public void setAccuData(Double accuData) {
        this.accuData = accuData;
    }

    public String getSeasonalTypeName() {
        return seasonalTypeName;
    }

    public void setSeasonalTypeName(String seasonalTypeName) {
        this.seasonalTypeName = seasonalTypeName;
    }

    public Float getChargePrice() {
        return chargePrice;
    }

    public void setChargePrice(Float chargePrice) {
        this.chargePrice = chargePrice;
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 完成标记
     */
    private Integer isComplete;

    /**
     * 数据库表名
     */
    private String tableName;
}
