package com.yunpower.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 月统计数据存储对象 sharding_month
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@ApiModel("月统计数据存储对象")
public class ShardingMonth extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    private Long id;

    /**
     * 变量名称
     */
    @ApiModelProperty("变量名称")
    @Excel(name = "变量名称")
    private String variableName;

    /**
     * 日期天
     */
    @ApiModelProperty("日期天")
    @Excel(name = "日期天")
    private Integer daySign;

    /**
     * 最小值
     */
    @ApiModelProperty("最小值")
    @Excel(name = "最小值")
    private Float minValue;

    /**
     * 最小值发生时间
     */
    @ApiModelProperty("最小值发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最小值发生时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date minTime;

    /**
     * 最大值
     */
    @ApiModelProperty("最大值")
    @Excel(name = "最大值")
    private Float maxValue;

    /**
     * 最大值发生时间
     */
    @ApiModelProperty("最大值发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最大值发生时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date maxTime;

    /**
     * 平均值
     */
    @ApiModelProperty("平均值")
    @Excel(name = "平均值")
    private Float avgValue;

    /**
     * 累积标记：1时累积 2天累积
     */
    @ApiModelProperty("累积标记：1时累积 2天累积")
    @Excel(name = "累积标记：1时累积 2天累积")
    private Integer accuSign;

    /**
     * 累积值
     */
    @ApiModelProperty("累积值")
    @Excel(name = "累积值")
    private Float accuValue;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    @Excel(name = "总记录数")
    private Integer totalCount;

    /**
     * 统计时间
     */
    @ApiModelProperty("统计时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "统计时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /**
     * 年
     */
    @Transient
    private Integer year;

    /**
     * 月
     */
    @Transient
    private Integer month;

    /**
     * 日
     */
    @Transient
    private Integer day;

    /**
     * 时
     */
    @Transient
    private Integer hour;

    /**
     * 分
     */
    @Transient
    private Integer minute;

    /**
     * 统计数据
     */
    @Transient
    private Float statisticValue;

    /**
     * 环比
     */
    @Transient
    private Float chainValue;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    public Integer getDaySign() {
        return daySign;
    }

    public void setDaySign(Integer daySign) {
        this.daySign = daySign;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinTime(Date minTime) {
        this.minTime = minTime;
    }

    public Date getMinTime() {
        return minTime;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxTime(Date maxTime) {
        this.maxTime = maxTime;
    }

    public Date getMaxTime() {
        return maxTime;
    }

    public void setAvgValue(Float avgValue) {
        this.avgValue = avgValue;
    }

    public Float getAvgValue() {
        return avgValue;
    }

    public void setAccuSign(Integer accuSign) {
        this.accuSign = accuSign;
    }

    public Integer getAccuSign() {
        return accuSign;
    }

    public void setAccuValue(Float accuValue) {
        this.accuValue = accuValue;
    }

    public Float getAccuValue() {
        return accuValue;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }

    public Date getSaveTime() {
        return saveTime;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Float getStatisticValue() {
        return statisticValue;
    }

    public void setStatisticValue(Float statisticValue) {
        this.statisticValue = statisticValue;
    }

    public Float getChainValue() {
        return chainValue;
    }

    public void setChainValue(Float chainValue) {
        this.chainValue = chainValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("variableName", getVariableName())
                .append("daySign", getDaySign())
                .append("minValue", getMinValue())
                .append("minTime", getMinTime())
                .append("maxValue", getMaxValue())
                .append("maxTime", getMaxTime())
                .append("avgValue", getAvgValue())
                .append("accuSign", getAccuSign())
                .append("accuValue", getAccuValue())
                .append("totalCount", getTotalCount())
                .append("saveTime", getSaveTime())
                .toString();
    }
}
