package com.yunpower.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 变量累积数据月存储对象 sharding_month_accumulate
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@ApiModel("变量累积数据月存储对象")
public class ShardingMonthAccumulate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

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
    @ApiModelProperty("年")
    @Excel(name = "年")
    private Integer recordYear;

    /** 月 */
    @ApiModelProperty("月")
    @Excel(name = "月")
    private Integer recordMonth;

    /** 日 */
    @ApiModelProperty("日")
    @Excel(name = "日")
    private Integer recordDay;

    /** 周 */
    @ApiModelProperty("周")
    @Excel(name = "周")
    private Integer recordWeek;

    /** 时 */
    @ApiModelProperty("时")
    @Excel(name = "时")
    private Integer recordHour;

    /** 累积数据 */
    @ApiModelProperty("累积数据")
    @Excel(name = "累积数据")
    private Float accuData;

    /** 峰谷名称 */
    @ApiModelProperty("峰谷名称")
    @Excel(name = "峰谷名称")
    private String seasonalTypeName;

    /** 计费电价 */
    @ApiModelProperty("计费电价")
    @Excel(name = "计费电价")
    private Float chargePrice;

    /** 完成标记 */
    @ApiModelProperty("完成标记")
    @Excel(name = "完成标记")
    private Integer isComplete;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

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

    public void setRecordYear(Integer recordYear) 
    {
        this.recordYear = recordYear;
    }

    public Integer getRecordYear() 
    {
        return recordYear;
    }

    public void setRecordMonth(Integer recordMonth) 
    {
        this.recordMonth = recordMonth;
    }

    public Integer getRecordMonth() 
    {
        return recordMonth;
    }

    public void setRecordDay(Integer recordDay) 
    {
        this.recordDay = recordDay;
    }

    public Integer getRecordDay() 
    {
        return recordDay;
    }

    public void setRecordWeek(Integer recordWeek) 
    {
        this.recordWeek = recordWeek;
    }

    public Integer getRecordWeek() 
    {
        return recordWeek;
    }

    public void setRecordHour(Integer recordHour) 
    {
        this.recordHour = recordHour;
    }

    public Integer getRecordHour() 
    {
        return recordHour;
    }

    public void setAccuData(Float accuData) 
    {
        this.accuData = accuData;
    }

    public Float getAccuData() 
    {
        return accuData;
    }

    public void setSeasonalTypeName(String seasonalTypeName)
    {
        this.seasonalTypeName = seasonalTypeName;
    }

    public String getSeasonalTypeName()
    {
        return seasonalTypeName;
    }

    public void setChargePrice(Float chargePrice)
    {
        this.chargePrice = chargePrice;
    }

    public Float getChargePrice()
    {
        return chargePrice;
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("variableName", getVariableName())
            .append("saveTime", getSaveTime())
            .append("dataValue", getDataValue())
            .append("recordYear", getRecordYear())
            .append("recordMonth", getRecordMonth())
            .append("recordDay", getRecordDay())
            .append("recordWeek", getRecordWeek())
            .append("recordHour", getRecordHour())
            .append("accuData", getAccuData())
            .append("seasonalTypeName", getSeasonalTypeName())
            .append("chargePrice", getChargePrice())
            .append("isComplete", getIsComplete())
            .toString();
    }
}
