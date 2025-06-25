package com.yunpower.system.domain;

import com.yunpower.common.core.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * svg与设备绑定对象
 */
public class WebtopoDeviceSvgnode
{
    private static final long serialVersionUID = 1L;

    /** 项目id */
    @Excel(name = "主键")
    private Long id;

    /** 项目id */
    @Excel(name = "项目ID")
    private Long projectId;

    /** 设备id */
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 节点id */
    @Excel(name = "节点ID")
    private String svgNodeId;

    /** 节点属性 */
    @Excel(name = "节点属性")
    private String svgNodeProp;

    /** 设备属性 */
    @Excel(name = "设备属性")
    private String deviceProp;

    /** 变量类型 */
    @Excel(name = "变量类型", readConverterExp = "1=模拟量,2=状态量")
    private Integer devicePropType;

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }
    public void setSvgNodeId(String svgNodeId) 
    {
        this.svgNodeId = svgNodeId;
    }

    public String getSvgNodeId() 
    {
        return svgNodeId;
    }
    public void setSvgNodeProp(String svgNodeProp) 
    {
        this.svgNodeProp = svgNodeProp;
    }

    public String getSvgNodeProp() 
    {
        return svgNodeProp;
    }
    public void setDeviceProp(String deviceProp) 
    {
        this.deviceProp = deviceProp;
    }

    public String getDeviceProp() 
    {
        return deviceProp;
    }

    public Integer getDevicePropType() {
        return devicePropType;
    }

    public void setDevicePropType(Integer devicePropType) {
        this.devicePropType = devicePropType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("deviceId", getDeviceId())
            .append("svgNodeId", getSvgNodeId())
            .append("svgNodeProp", getSvgNodeProp())
            .append("deviceProp", getDeviceProp())
            .append("devicePropType", getDevicePropType())
            .toString();
    }
}
