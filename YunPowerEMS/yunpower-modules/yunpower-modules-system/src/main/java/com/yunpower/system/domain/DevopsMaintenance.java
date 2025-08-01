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
import org.springframework.data.annotation.Transient;

/**
 * 维保记录对象 devops_maintenance
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("维保记录对象")
public class DevopsMaintenance extends BaseEntity
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

    /** 维保名称 */
    @ApiModelProperty("维保名称")
    @Excel(name = "维保名称")
    private String maintainTitle;

    /** 电站类型 */
    @ApiModelProperty("电站类型")
    @Excel(name = "电站类型")
    private Integer stationType;

    /** 维保站点 */
    @ApiModelProperty("维保站点")
    @Excel(name = "维保站点")
    private Long stationId;

    /** 维保设备（多个，JSON格式） */
    @ApiModelProperty("维保设备（多个，JSON格式）")
    @Excel(name = "维保设备", readConverterExp = "多个，JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> deviceIdFor;

    /** 维保人员（多个，JSON格式） */
    @ApiModelProperty("维保人员（多个，JSON格式）")
    @Excel(name = "维保人员", readConverterExp = "多个，JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> userNameFor;

    /** 维保时间 */
    @ApiModelProperty("维保时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "维保时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date maintainTime;

    /** 维保内容 */
    @ApiModelProperty("维保内容")
    @Excel(name = "维保内容")
    private String maintainContent;

    /** 图片描述 */
    @ApiModelProperty("图片描述")
    @Excel(name = "图片描述")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> maintainFiles;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;


    /**
     * 站点名称
     * */
    @Transient
    private String stationName;

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

    public void setMaintainTitle(String maintainTitle)
    {
        this.maintainTitle = maintainTitle;
    }

    public String getMaintainTitle()
    {
        return maintainTitle;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public void setStationId(Long stationId)
    {
        this.stationId = stationId;
    }

    public Long getStationId()
    {
        return stationId;
    }

    public List<JsonCommonVo> getDeviceIdFor() {
        return deviceIdFor;
    }

    public void setDeviceIdFor(List<JsonCommonVo> deviceIdFor) {
        this.deviceIdFor = deviceIdFor;
    }

    public List<JsonCommonVo> getUserNameFor() {
        return userNameFor;
    }

    public void setUserNameFor(List<JsonCommonVo> userNameFor) {
        this.userNameFor = userNameFor;
    }

    public void setMaintainTime(Date maintainTime)
    {
        this.maintainTime = maintainTime;
    }

    public Date getMaintainTime()
    {
        return maintainTime;
    }

    public void setMaintainContent(String maintainContent)
    {
        this.maintainContent = maintainContent;
    }

    public String getMaintainContent()
    {
        return maintainContent;
    }

    public List<JsonCommonVo> getMaintainFiles() {
        return maintainFiles;
    }

    public void setMaintainFiles(List<JsonCommonVo> maintainFiles) {
        this.maintainFiles = maintainFiles;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("maintainTitle", getMaintainTitle())
                .append("stationId", getStationId())
                .append("deviceIdFor", getDeviceIdFor())
                .append("userNameFor", getUserNameFor())
                .append("maintainTime", getMaintainTime())
                .append("maintainContent", getMaintainContent())
                .append("maintainFiles", getMaintainFiles())
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
