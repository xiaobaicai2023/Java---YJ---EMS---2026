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
 * 通用天气数据对象 sys_common_weather
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("通用天气数据对象")
public class SysCommonWeather extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 国家 */
    @ApiModelProperty("国家")
    @Excel(name = "国家")
    private Integer country;

    /** 省 */
    @ApiModelProperty("省")
    @Excel(name = "省")
    private Integer province;

    /** 市 */
    @ApiModelProperty("市")
    @Excel(name = "市")
    private Integer city;

    /** 县（区） */
    @ApiModelProperty("县（区）")
    @Excel(name = "县", readConverterExp = "区=")
    private Integer county;

    /** 镇（街道） */
    @ApiModelProperty("镇（街道）")
    @Excel(name = "镇", readConverterExp = "街=道")
    private Integer town;

    /** 村 */
    @ApiModelProperty("村")
    @Excel(name = "村")
    private Integer village;

    /** 天气日期 */
    @ApiModelProperty("天气日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "天气日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date weatherDate;

    /** 天气时 */
    @ApiModelProperty("天气时")
    @Excel(name = "天气时")
    private Integer weatherHour;

    /** 日出日落 */
    @ApiModelProperty("日出日落")
    @Excel(name = "日出日落")
    private String sunrise;

    /** 天气状况 */
    @ApiModelProperty("天气状况")
    @Excel(name = "天气状况")
    private String weatherBase;

    /** 空气质量 */
    @ApiModelProperty("空气质量")
    @Excel(name = "空气质量")
    private String airQuality;

    /** 风向 */
    @ApiModelProperty("风向")
    @Excel(name = "风向")
    private String windDirection;

    /** 风力 */
    @ApiModelProperty("风力")
    @Excel(name = "风力")
    private Float windForce;

    /** 风速 */
    @ApiModelProperty("风速")
    @Excel(name = "风速")
    private Float windSpeed;

    /** 气温 */
    @ApiModelProperty("气温")
    @Excel(name = "气温")
    private Float temperature;

    /** 体感温度 */
    @ApiModelProperty("体感温度")
    @Excel(name = "体感温度")
    private Float sendibleTemperature;

    /** 湿度 */
    @ApiModelProperty("湿度")
    @Excel(name = "湿度")
    private Float humidity;

    /** 降水量 */
    @ApiModelProperty("降水量")
    @Excel(name = "降水量")
    private Float precipitation;

    /** 光辐射 */
    @ApiModelProperty("光辐射")
    @Excel(name = "光辐射")
    private Float lightRadiation;

    /** 紫外线 */
    @ApiModelProperty("紫外线")
    @Excel(name = "紫外线")
    private String ultravioletRays;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setCountry(Integer country)
    {
        this.country = country;
    }

    public Integer getCountry()
    {
        return country;
    }

    public void setProvince(Integer province)
    {
        this.province = province;
    }

    public Integer getProvince()
    {
        return province;
    }

    public void setCity(Integer city)
    {
        this.city = city;
    }

    public Integer getCity()
    {
        return city;
    }

    public void setCounty(Integer county)
    {
        this.county = county;
    }

    public Integer getCounty()
    {
        return county;
    }

    public void setTown(Integer town)
    {
        this.town = town;
    }

    public Integer getTown()
    {
        return town;
    }

    public void setVillage(Integer village)
    {
        this.village = village;
    }

    public Integer getVillage()
    {
        return village;
    }

    public void setWeatherDate(Date weatherDate)
    {
        this.weatherDate = weatherDate;
    }

    public Date getWeatherDate()
    {
        return weatherDate;
    }

    public void setWeatherHour(Integer weatherHour)
    {
        this.weatherHour = weatherHour;
    }

    public Integer getWeatherHour()
    {
        return weatherHour;
    }

    public void setSunrise(String sunrise)
    {
        this.sunrise = sunrise;
    }

    public String getSunrise()
    {
        return sunrise;
    }

    public void setWeatherBase(String weatherBase)
    {
        this.weatherBase = weatherBase;
    }

    public String getWeatherBase()
    {
        return weatherBase;
    }

    public void setAirQuality(String airQuality)
    {
        this.airQuality = airQuality;
    }

    public String getAirQuality()
    {
        return airQuality;
    }

    public void setWindDirection(String windDirection)
    {
        this.windDirection = windDirection;
    }

    public String getWindDirection()
    {
        return windDirection;
    }

    public void setWindForce(Float windForce)
    {
        this.windForce = windForce;
    }

    public Float getWindForce()
    {
        return windForce;
    }

    public void setWindSpeed(Float windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public Float getWindSpeed()
    {
        return windSpeed;
    }

    public void setTemperature(Float temperature)
    {
        this.temperature = temperature;
    }

    public Float getTemperature()
    {
        return temperature;
    }

    public void setSendibleTemperature(Float sendibleTemperature)
    {
        this.sendibleTemperature = sendibleTemperature;
    }

    public Float getSendibleTemperature()
    {
        return sendibleTemperature;
    }

    public void setHumidity(Float humidity)
    {
        this.humidity = humidity;
    }

    public Float getHumidity()
    {
        return humidity;
    }

    public void setPrecipitation(Float precipitation)
    {
        this.precipitation = precipitation;
    }

    public Float getPrecipitation()
    {
        return precipitation;
    }

    public void setLightRadiation(Float lightRadiation)
    {
        this.lightRadiation = lightRadiation;
    }

    public Float getLightRadiation()
    {
        return lightRadiation;
    }

    public void setUltravioletRays(String ultravioletRays)
    {
        this.ultravioletRays = ultravioletRays;
    }

    public String getUltravioletRays()
    {
        return ultravioletRays;
    }

    public void setStopFlag(Integer stopFlag)
    {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag()
    {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag)
    {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag()
    {
        return deleteFlag;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("country", getCountry())
                .append("province", getProvince())
                .append("city", getCity())
                .append("county", getCounty())
                .append("town", getTown())
                .append("village", getVillage())
                .append("weatherDate", getWeatherDate())
                .append("weatherHour", getWeatherHour())
                .append("sunrise", getSunrise())
                .append("weatherBase", getWeatherBase())
                .append("airQuality", getAirQuality())
                .append("windDirection", getWindDirection())
                .append("windForce", getWindForce())
                .append("windSpeed", getWindSpeed())
                .append("temperature", getTemperature())
                .append("sendibleTemperature", getSendibleTemperature())
                .append("humidity", getHumidity())
                .append("precipitation", getPrecipitation())
                .append("lightRadiation", getLightRadiation())
                .append("ultravioletRays", getUltravioletRays())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}
