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
 * 日数据存储对象 sharding_day
 * 
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@ApiModel("日数据存储对象")
public class ShardingDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 变量名称 */
    @ApiModelProperty("变量名称")
    @Excel(name = "变量名称")
    private String variableName;

    /** 存盘时间 */
    @ApiModelProperty("存盘时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "存盘时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date saveTime;

    /** 数据 */
    @ApiModelProperty("数据")
    @Excel(name = "数据")
    private Float dataValue;

    /** 年 */
    @Transient
    private Integer year;

    /** 月 */
    @Transient
    private Integer month;

    /** 日 */
    @Transient
    private Integer day;

    /** 时 */
    @Transient
    private Integer hour;

    /** 分 */
    @Transient
    private Integer minute;

    public void setVariableName(String variableName) 
    {
        this.variableName = variableName;
    }

    public String getVariableName() 
    {
        return variableName;
    }

    public void setSaveTime(Date saveTime) 
    {
        this.saveTime = saveTime;
    }

    public Date getSaveTime() 
    {
        return saveTime;
    }

    public void setDataValue(Float dataValue) 
    {
        this.dataValue = dataValue;
    }

    public Float getDataValue() 
    {
        return dataValue;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("variableName", getVariableName())
            .append("saveTime", getSaveTime())
            .append("dataValue", getDataValue())
            .toString();
    }
}
