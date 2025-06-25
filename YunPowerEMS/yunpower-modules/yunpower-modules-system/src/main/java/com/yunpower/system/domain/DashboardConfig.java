package com.yunpower.system.domain;

import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Transient;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 仪表盘配置对象 dashboard_config
 *
 * @author yunpower
 * @date 2024-05-30
 */
@ApiModel("仪表盘配置对象")
public class DashboardConfig extends BaseEntity {
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
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String pageName;

    /**
     * 页面key 唯一
     */
    @ApiModelProperty("页面key 唯一")
    @Excel(name = "页面key 唯一")
    @NotEmpty(message = "payKey不能为空")
    private String pageKey;

    /**
     * 页面类型 1:大屏页面 2:普通图表
     */
    @ApiModelProperty("页面类型 1:大屏页面 2:普通图表")
    @Excel(name = "页面类型 1:大屏页面 2:普通图表")
    @NotNull(message = "pageType不能为空")
    private Long pageType;

    /**
     * 页面配置json
     */
    @ApiModelProperty("页面配置json")
    @Excel(name = "页面配置json")
    private String pageConfig;

    /**
     * 页面配置json 用于预览页面
     */
    @ApiModelProperty("页面配置json 用于预览页面")
    @Excel(name = "页面配置json 用于预览页面")
    private String pageConfigPre;

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

    /**
     * 企业名称（企业名称）
     */
    @Transient
    private String entName;

    public String getEntName() {
        return entName;
    }

    /**
     * 分组名称（分组名称）
     */
    @Transient
    private String groupName;

    /**
     * 站点名称（部门名称）
     */
    @Transient
    private String stationName;

    /**
     * 站点类型
     */
    @Transient
    private Integer stationType;

    /**
     * 大屏-模版类型
     */
    @Transient
    private String pageTemplate;

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

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    public String getPageKey() {
        return pageKey;
    }

    public void setPageType(Long pageType) {
        this.pageType = pageType;
    }

    public Long getPageType() {
        return pageType;
    }

    public void setPageConfig(String pageConfig) {
        this.pageConfig = pageConfig;
    }

    public String getPageConfig() {
        return pageConfig;
    }

    public void setPageConfigPre(String pageConfigPre) {
        this.pageConfigPre = pageConfigPre;
    }

    public String getPageConfigPre() {
        return pageConfigPre;
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

    public void setEntName(String entName) {
        this.entName = entName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public String getPageTemplate() {
        return pageTemplate;
    }

    public void setPageTemplate(String pageTemplate) {
        this.pageTemplate = pageTemplate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("pageName", getPageName())
                .append("pageKey", getPageKey())
                .append("pageType", getPageType())
                .append("pageConfig", getPageConfig())
                .append("pageConfigPre", getPageConfigPre())
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
