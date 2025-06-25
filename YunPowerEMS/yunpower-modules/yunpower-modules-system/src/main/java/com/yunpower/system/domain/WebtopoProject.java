package com.yunpower.system.domain;

import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 项目列对象
 */
public class WebtopoProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目id */
    private Long id;

    /** 企业ID */
    @Excel(name = "企业ID")
    private Long entId;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 电站类型 */
    @Excel(name = "电站类型")
    private Integer stationType;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 缩略图 */
    @Excel(name = "缩略图")
    private String thumbnail;

    /** 数据模型 */
    @Excel(name = "数据模型")
    private String dataModel;

    /** 是否停用（0正常 1停用） */
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /** 项目关联设备信息 */
    private List<WebtopoProjectDevice> deviceList;

    public List<WebtopoProjectDevice> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<WebtopoProjectDevice> deviceList) {
        this.deviceList = deviceList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEntId() {
        return entId;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setThumbnail(String thumbnail) 
    {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() 
    {
        return thumbnail;
    }

    public void setDataModel(String dataModel) 
    {
        this.dataModel = dataModel;
    }

    public String getDataModel() 
    {
        return dataModel;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("entId", getEntId())
            .append("deptId", getDeptId())
            .append("stationType", getStationType())
            .append("projectName", getProjectName())
            .append("thumbnail", getThumbnail())
            .append("dataModel", getDataModel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("deviceList", getDeviceList())
            .append("stopFlag", getStopFlag())
            .append("deleteFlag", getDeleteFlag())
            .toString();
    }
}
