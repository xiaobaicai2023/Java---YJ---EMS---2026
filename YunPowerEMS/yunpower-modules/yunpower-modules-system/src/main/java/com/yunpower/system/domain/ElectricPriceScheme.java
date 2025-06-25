package com.yunpower.system.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.system.domain.handler.JsonCommonTypeHandler;
import com.yunpower.system.domain.jsonvo.JsonCommonVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 电价标准对象 electric_price_scheme
 * 
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@ApiModel("电价标准对象")
public class ElectricPriceScheme extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 企业ID */
    @ApiModelProperty("企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /** 部门ID */
    @ApiModelProperty("部门ID")
    @Excel(name = "部门ID")
    private Long deptId;

    /** 上级ID */
    @ApiModelProperty("上级ID")
    @Excel(name = "上级ID")
    private Long parentId;

    /** 电价名称 */
    @ApiModelProperty("电价名称")
    @Excel(name = "电价名称")
    private String electricName;

    /** 地址省 */
    @ApiModelProperty("地址省")
    @Excel(name = "地址省")
    private Integer province;

    /** 地址市 */
    @ApiModelProperty("地址市")
    @Excel(name = "地址市")
    private Integer city;

    /** 地址县 */
    @ApiModelProperty("地址县")
    @Excel(name = "地址县")
    private Integer county;

    /** 用电分类 */
    @ApiModelProperty("用电分类")
    @Excel(name = "用电分类")
    private Integer electricGroup;

    /** 用电标准 */
    @ApiModelProperty("用电标准")
    @Excel(name = "用电标准")
    private Integer electricStandard;

    /** 电压等级 */
    @ApiModelProperty("电压等级")
    @Excel(name = "电压等级")
    private Integer voltageLevel;

    /** 生效年份 */
    @ApiModelProperty("生效年份")
    @Excel(name = "生效年份")
    private Integer effectYear;

    /** 执行开始日期 */
    @ApiModelProperty("执行开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 执行结束日期 */
    @ApiModelProperty("执行结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 电度电价 */
    @ApiModelProperty("电度电价")
    @Excel(name = "电度电价")
    private String electricText;

    /** 容量电价 */
    @ApiModelProperty("容量电价")
    @Excel(name = "容量电价")
    private Float capacityPrice;

    /** 需量电价 */
    @ApiModelProperty("需量电价")
    @Excel(name = "需量电价")
    private Float demandPrice;

    /** 需量计费（超） */
    @ApiModelProperty("需量计费（超）")
    @Excel(name = "需量计费")
    private Float demandPercent;

    /** 需量计费（倍） */
    @ApiModelProperty("需量计费（倍）")
    @Excel(name = "需量计费")
    private Float demandRate;

    /** 电量达到（千瓦） */
    @ApiModelProperty("电量达到（千瓦）")
    @Excel(name = "电量达到")
    private Float electricUp;

    /** 电量达到（倍） */
    @ApiModelProperty("电量达到（倍）")
    @Excel(name = "电量达到")
    private Float electricUpRate;

    /** 偏差电价（JSON格式） */
    @ApiModelProperty("偏差电价（JSON格式）")
    @Excel(name = "偏差电价")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> deviationPrice;

    /** 加征电价（名称） */
    @ApiModelProperty("加征电价（名称）")
    @Excel(name = "加征电价")
    private String additiveName;

    /** 加征电价（价格） */
    @ApiModelProperty("加征电价（价格）")
    @Excel(name = "加征电价")
    private Float additivePrice;

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

    public void setEntId(Long entId) 
    {
        this.entId = entId;
    }

    public Long getEntId() 
    {
        return entId;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setElectricName(String electricName) 
    {
        this.electricName = electricName;
    }

    public String getElectricName() 
    {
        return electricName;
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

    public void setElectricGroup(Integer electricGroup) 
    {
        this.electricGroup = electricGroup;
    }

    public Integer getElectricGroup() 
    {
        return electricGroup;
    }

    public void setElectricStandard(Integer electricStandard) 
    {
        this.electricStandard = electricStandard;
    }

    public Integer getElectricStandard() 
    {
        return electricStandard;
    }

    public void setVoltageLevel(Integer voltageLevel) 
    {
        this.voltageLevel = voltageLevel;
    }

    public Integer getVoltageLevel() 
    {
        return voltageLevel;
    }

    public Integer getEffectYear() {
        return effectYear;
    }

    public void setEffectYear(Integer effectYear) {
        this.effectYear = effectYear;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setElectricText(String electricText) 
    {
        this.electricText = electricText;
    }

    public String getElectricText() 
    {
        return electricText;
    }

    public void setCapacityPrice(Float capacityPrice) 
    {
        this.capacityPrice = capacityPrice;
    }

    public Float getCapacityPrice() 
    {
        return capacityPrice;
    }

    public void setDemandPrice(Float demandPrice) 
    {
        this.demandPrice = demandPrice;
    }

    public Float getDemandPrice() 
    {
        return demandPrice;
    }

    public void setDemandPercent(Float demandPercent) 
    {
        this.demandPercent = demandPercent;
    }

    public Float getDemandPercent() 
    {
        return demandPercent;
    }

    public void setDemandRate(Float demandRate) 
    {
        this.demandRate = demandRate;
    }

    public Float getDemandRate() 
    {
        return demandRate;
    }

    public void setElectricUp(Float electricUp) 
    {
        this.electricUp = electricUp;
    }

    public Float getElectricUp() 
    {
        return electricUp;
    }

    public void setElectricUpRate(Float electricUpRate) 
    {
        this.electricUpRate = electricUpRate;
    }

    public Float getElectricUpRate() 
    {
        return electricUpRate;
    }

    public List<JsonCommonVo> getDeviationPrice() {
        return deviationPrice;
    }

    public void setDeviationPrice(List<JsonCommonVo> deviationPrice) {
        this.deviationPrice = deviationPrice;
    }

    public void setAdditiveName(String additiveName)
    {
        this.additiveName = additiveName;
    }

    public String getAdditiveName() 
    {
        return additiveName;
    }

    public void setAdditivePrice(Float additivePrice) 
    {
        this.additivePrice = additivePrice;
    }

    public Float getAdditivePrice() 
    {
        return additivePrice;
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
            .append("entId", getEntId())
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("electricName", getElectricName())
            .append("province", getProvince())
            .append("city", getCity())
            .append("county", getCounty())
            .append("electricGroup", getElectricGroup())
            .append("electricStandard", getElectricStandard())
            .append("voltageLevel", getVoltageLevel())
            .append("effectYear", getEffectYear())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("electricText", getElectricText())
            .append("capacityPrice", getCapacityPrice())
            .append("demandPrice", getDemandPrice())
            .append("demandPercent", getDemandPercent())
            .append("demandRate", getDemandRate())
            .append("electricUp", getElectricUp())
            .append("electricUpRate", getElectricUpRate())
            .append("deviationPrice", getDeviationPrice())
            .append("additiveName", getAdditiveName())
            .append("additivePrice", getAdditivePrice())
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
