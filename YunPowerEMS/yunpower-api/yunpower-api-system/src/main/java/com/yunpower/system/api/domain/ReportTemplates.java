package com.yunpower.system.api.domain;

import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 报表模版对象 report_templates
 *
 * @author yunpower
 * @date 2024-06-06
 */
@ApiModel("报表模版对象")
public class ReportTemplates extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号ID
     */
    @ApiModelProperty("编号ID")
    private Long id;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    @Excel(name = "企业ID")
    private Long entId;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    @Excel(name = "部门ID")
    private Long deptId;


    /**
     * 电站类型
     */
    @ApiModelProperty("电站类型")
    @Excel(name = "电站类型")
    private Integer stationType;

    /**
     * 报表模版归属人
     */
    @ApiModelProperty("报表模版归属人")
    @Excel(name = "报表模版归属人")
    private Long userId;

    /**
     * 报表名称
     */
    @ApiModelProperty("报表名称")
    @Excel(name = "报表名称")
    @NotEmpty(message = "报表名称 不能为空")
    private String reportName;

    /**
     * 报表类型（1普通 2峰谷）
     */
    @ApiModelProperty("报表类型（1普通 2峰谷）")
    @Excel(name = "报表类型", readConverterExp = "1=普通,2=峰谷")
    @NotNull(message = "报表类型 不能为空")
    private Integer reportType;

    /**
     * 可见类型（1公有 2私有）
     */
    @ApiModelProperty("可见类型（0默认 1公有 2私有）")
    @Excel(name = "可见类型", readConverterExp = "0默认 1=公有,2=私有")
    private Integer visibilityType;

    /**
     * 多级表头 （1是 0否）
     */
    @ApiModelProperty("多级表头 （1是 0否）")
    @Excel(name = "多级表头 ", readConverterExp = "1=是,0=否")
    private Integer multiLevelHeader;

    /**
     * 表头配置
     */
    @ApiModelProperty("表头配置，json格式 [{deviceId:xxx,deviceVarId:xxx,deviceVarName:xxx}]")
    @Excel(name = "表头配置")
    private String headConfig;


    /**
     * 系统默认（0系统 1自定义）
     */
    @ApiModelProperty("系统默认（0系统 1自定义）")
    @Excel(name = "系统默认", readConverterExp = "0=系统,1=自定义")
    private Integer systemFlag;

    /**
     * 是否停用（0正常 1停用）
     */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /**
     * 是否删除（0正常 1删除）
     */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEntId(Long entId) {
        this.entId = entId;
    }

    public Long getEntId() {
        return entId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setVisibilityType(Integer visibilityType) {
        this.visibilityType = visibilityType;
    }

    public Integer getVisibilityType() {
        return visibilityType;
    }

    public void setMultiLevelHeader(Integer multiLevelHeader) {
        this.multiLevelHeader = multiLevelHeader;
    }

    public Integer getMultiLevelHeader() {
        return multiLevelHeader;
    }

    public void setHeadConfig(String headConfig) {
        this.headConfig = headConfig;
    }

    public String getHeadConfig() {
        return headConfig;
    }

    public void setStopFlag(Integer stopFlag) {
        this.stopFlag = stopFlag;
    }

    public Integer getStopFlag() {
        return stopFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public Integer getSystemFlag() {
        return systemFlag;
    }

    public void setSystemFlag(Integer systemFlag) {
        this.systemFlag = systemFlag;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("userId", getUserId())
                .append("reportName", getReportName())
                .append("visibilityType", getVisibilityType())
                .append("multiLevelHeader", getMultiLevelHeader())
                .append("headConfig", getHeadConfig())
                .append("systemFlag", getSystemFlag())
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
