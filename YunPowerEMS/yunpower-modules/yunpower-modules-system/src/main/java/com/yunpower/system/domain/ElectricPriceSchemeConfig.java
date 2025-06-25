package com.yunpower.system.domain;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.system.domain.handler.SeasonalRangeTypeHandler;
import com.yunpower.system.domain.jsonvo.SeasonalRangeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 电度电价配置对象 electric_price_scheme_config
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@ApiModel("电度电价配置对象")
public class ElectricPriceSchemeConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 电价标准ID */
    @ApiModelProperty("电价标准ID")
    @Excel(name = "电价标准ID")
    private Long schemeId;

    /** 生效年份 */
    @ApiModelProperty("生效年份")
    @Excel(name = "生效年份")
    private Integer effectYear;

    /** 生效月份集合 */
    @ApiModelProperty("生效月份集合")
    @Excel(name = "生效月份集合")
    private String effectMonth;

    /** 电价类型：1分时电价 2统一电价 */
    @ApiModelProperty("电价类型：1分时电价 2统一电价")
    @Excel(name = "电价类型：1分时电价 2统一电价")
    private Integer priceType;

    /** 统一电价 */
    @ApiModelProperty("统一电价")
    @Excel(name = "统一电价")
    private Float uniformPrice;

    /** 尖峰电价 */
    @ApiModelProperty("尖峰电价")
    @Excel(name = "尖峰电价")
    private Float jianPrice;

    /** 峰段电价 */
    @ApiModelProperty("峰段电价")
    @Excel(name = "峰段电价")
    private Float fengPrice;

    /** 平段电价 */
    @ApiModelProperty("平段电价")
    @Excel(name = "平段电价")
    private Float pingPrice;

    /** 谷段电价 */
    @ApiModelProperty("谷段电价")
    @Excel(name = "谷段电价")
    private Float guPrice;

    /** 深谷电价 */
    @ApiModelProperty("深谷电价")
    @Excel(name = "深谷电价")
    private Float shenPrice;

    /** 全天电价 */
    @ApiModelProperty("全天电价")
    @Excel(name = "全天电价")
    private Float wholePrice;

    /** 时间段（JSON格式） */
    @ApiModelProperty("时间段（JSON格式）")
    @Excel(name = "时间段")
    @TableField(typeHandler = SeasonalRangeTypeHandler.class)
    private List<SeasonalRangeVo> seasonalRange;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSchemeId(Long schemeId) 
    {
        this.schemeId = schemeId;
    }

    public Long getSchemeId() 
    {
        return schemeId;
    }

    public Integer getEffectYear() {
        return effectYear;
    }

    public void setEffectYear(Integer effectYear) {
        this.effectYear = effectYear;
    }

    public void setEffectMonth(String effectMonth)
    {
        this.effectMonth = effectMonth;
    }

    public String getEffectMonth() 
    {
        return effectMonth;
    }

    public void setPriceType(Integer priceType) 
    {
        this.priceType = priceType;
    }

    public Integer getPriceType() 
    {
        return priceType;
    }

    public Float getUniformPrice() {
        return uniformPrice;
    }

    public void setUniformPrice(Float uniformPrice) {
        this.uniformPrice = uniformPrice;
    }

    public void setJianPrice(Float jianPrice)
    {
        this.jianPrice = jianPrice;
    }

    public Float getJianPrice() 
    {
        return jianPrice;
    }

    public void setFengPrice(Float fengPrice) 
    {
        this.fengPrice = fengPrice;
    }

    public Float getFengPrice() 
    {
        return fengPrice;
    }

    public void setPingPrice(Float pingPrice) 
    {
        this.pingPrice = pingPrice;
    }

    public Float getPingPrice() 
    {
        return pingPrice;
    }

    public void setGuPrice(Float guPrice) 
    {
        this.guPrice = guPrice;
    }

    public Float getGuPrice() 
    {
        return guPrice;
    }

    public void setShenPrice(Float shenPrice) 
    {
        this.shenPrice = shenPrice;
    }

    public Float getShenPrice() 
    {
        return shenPrice;
    }

    public void setWholePrice(Float wholePrice) 
    {
        this.wholePrice = wholePrice;
    }

    public Float getWholePrice() 
    {
        return wholePrice;
    }

    public List<SeasonalRangeVo> getSeasonalRange() {
        return seasonalRange;
    }

    public void setSeasonalRange(List<SeasonalRangeVo> seasonalRange) {
        this.seasonalRange = seasonalRange;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("schemeId", getSchemeId())
            .append("effectYear", getEffectYear())
            .append("effectMonth", getEffectMonth())
            .append("priceType", getPriceType())
            .append("uniformPrice", getUniformPrice())
            .append("jianPrice", getJianPrice())
            .append("fengPrice", getFengPrice())
            .append("pingPrice", getPingPrice())
            .append("guPrice", getGuPrice())
            .append("shenPrice", getShenPrice())
            .append("wholePrice", getWholePrice())
            .append("seasonalRange", getSeasonalRange())
            .toString();
    }
}
