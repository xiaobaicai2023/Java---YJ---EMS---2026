package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 能源监控设备关联对象 monitor_device_association
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("能源监控设备关联对象")
public class MonitorDeviceAssociation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 设备ID */
    @ApiModelProperty("设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 关联设备ID */
    @ApiModelProperty("关联设备ID")
    @Excel(name = "关联设备ID")
    private Long correDeviceId;

    /** 关联设备名称 */
    @ApiModelProperty("关联设备名称")
    @Excel(name = "关联设备名称")
    private String correDeviceName;

    /** 关联设备编号 */
    @ApiModelProperty("关联设备编号")
    @Excel(name = "关联设备编号")
    private String correDeviceSn;

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

    public void setCorreDeviceId(Long correDeviceId)
    {
        this.correDeviceId = correDeviceId;
    }

    public Long getCorreDeviceId()
    {
        return correDeviceId;
    }

    public void setCorreDeviceName(String correDeviceName)
    {
        this.correDeviceName = correDeviceName;
    }

    public String getCorreDeviceName()
    {
        return correDeviceName;
    }

    public void setCorreDeviceSn(String correDeviceSn)
    {
        this.correDeviceSn = correDeviceSn;
    }

    public String getCorreDeviceSn()
    {
        return correDeviceSn;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("correDeviceId", getCorreDeviceId())
                .append("correDeviceName", getCorreDeviceName())
                .append("correDeviceSn", getCorreDeviceSn())
                .toString();
    }
}
