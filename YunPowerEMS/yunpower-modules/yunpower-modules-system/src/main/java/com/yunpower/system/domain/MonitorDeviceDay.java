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
 * 监控设备日实时变量对象 monitor_device_day
 *
 * @author JUNFU.WANG
 * @date 2023-12-16
 */
@ApiModel("监控设备日实时变量对象")
public class MonitorDeviceDay extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 设备ID */
    @ApiModelProperty("设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 统计时间 */
    @ApiModelProperty("统计时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "统计时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date staticTime;

    /** 设备状态 */
    @ApiModelProperty("设备状态")
    @Excel(name = "设备状态")
    private Integer status = 0;

    /** 发电功率 */
    @ApiModelProperty("发电功率")
    @Excel(name = "发电功率")
    private Float electricPower = 0F;

    /** 当日发电量 */
    @ApiModelProperty("当日发电量")
    @Excel(name = "当日发电量")
    private Float electricQuantity = 0F;

    /** 当日满发电小时 */
    @ApiModelProperty("当日满发电小时")
    @Excel(name = "当日满发电小时")
    private Float electricHours;

    /** 逆变器性能 */
    @ApiModelProperty("逆变器性能")
    @Excel(name = "逆变器性能")
    private Integer inverterPerformance = 0;

    @ApiModelProperty("逆变器性能名称")
    @Transient
    private String inverterPerformanceName = "停机";

    /** 直流离散率 */
    @ApiModelProperty("直流离散率")
    @Excel(name = "直流离散率")
    private Float dcDispersionRate = 0F;

    /** 归一化率 */
    @ApiModelProperty("归一化率")
    @Excel(name = "归一化率")
    private Float normalizedRate = 0F;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setStaticTime(Date staticTime)
    {
        this.staticTime = staticTime;
    }

    public Date getStaticTime()
    {
        return staticTime;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setElectricPower(Float electricPower)
    {
        this.electricPower = electricPower;
    }

    public Float getElectricPower()
    {
        return electricPower;
    }

    public void setElectricQuantity(Float electricQuantity)
    {
        this.electricQuantity = electricQuantity;
    }

    public Float getElectricQuantity()
    {
        return electricQuantity;
    }

    public void setElectricHours(Float electricHours)
    {
        this.electricHours = electricHours;
    }

    public Float getElectricHours()
    {
        return electricHours;
    }

    public void setInverterPerformance(Integer inverterPerformance)
    {
        this.inverterPerformance = inverterPerformance;
    }

    public Integer getInverterPerformance()
    {
        return inverterPerformance;
    }

    public String getInverterPerformanceName() {
        return inverterPerformanceName;
    }

    public void setInverterPerformanceName(String inverterPerformanceName) {
        this.inverterPerformanceName = inverterPerformanceName;
    }

    public void setDcDispersionRate(Float dcDispersionRate)
    {
        this.dcDispersionRate = dcDispersionRate;
    }

    public Float getDcDispersionRate()
    {
        return dcDispersionRate;
    }

    public void setNormalizedRate(Float normalizedRate)
    {
        this.normalizedRate = normalizedRate;
    }

    public Float getNormalizedRate()
    {
        return normalizedRate;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("staticTime", getStaticTime())
            .append("status", getStatus())
            .append("electricPower", getElectricPower())
            .append("electricQuantity", getElectricQuantity())
            .append("electricHours", getElectricHours())
            .append("inverterPerformance", getInverterPerformance())
            .append("dcDispersionRate", getDcDispersionRate())
            .append("normalizedRate", getNormalizedRate())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
