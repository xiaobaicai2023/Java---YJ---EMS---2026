package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 通讯设备数据区域对象 communication_device_area
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("通讯设备数据区域对象")
public class CommunicationDeviceArea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 设备ID */
    @ApiModelProperty("设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备编码 */
    @ApiModelProperty("设备编码")
    @Excel(name = "设备编码")
    private String deviceSn;

    /** 区域名称 */
    @ApiModelProperty("区域名称")
    @Excel(name = "区域名称")
    private String areaName;

    /** 起始地址 */
    @ApiModelProperty("起始地址")
    @Excel(name = "起始地址")
    private Integer addStart;

    /** 长度 */
    @ApiModelProperty("长度")
    @Excel(name = "长度")
    private Integer addLength;

    /** 寄存器 */
    @ApiModelProperty("寄存器")
    @Excel(name = "寄存器")
    private String area;

    /** 读写（1只读 2只写 3读写） */
    @ApiModelProperty("读写（1只读 2只写 3读写）")
    @Excel(name = "读写", readConverterExp = "1=只读,2=只写,3=读写")
    private Integer rw;

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

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceSn(String deviceSn)
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn()
    {
        return deviceSn;
    }

    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaName()
    {
        return areaName;
    }

    public void setAddStart(Integer addStart)
    {
        this.addStart = addStart;
    }

    public Integer getAddStart()
    {
        return addStart;
    }

    public void setAddLength(Integer addLength)
    {
        this.addLength = addLength;
    }

    public Integer getAddLength()
    {
        return addLength;
    }

    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea()
    {
        return area;
    }

    public void setRw(Integer rw)
    {
        this.rw = rw;
    }

    public Integer getRw()
    {
        return rw;
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
                .append("deviceId", getDeviceId())
                .append("deviceSn", getDeviceSn())
                .append("areaName", getAreaName())
                .append("addStart", getAddStart())
                .append("addLength", getAddLength())
                .append("area", getArea())
                .append("rw", getRw())
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
