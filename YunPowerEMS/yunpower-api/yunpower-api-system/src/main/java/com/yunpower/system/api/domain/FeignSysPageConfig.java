package com.yunpower.system.api.domain;

import com.yunpower.system.api.domain.vo.FeignVariableConfigVo;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面配置对象 sys_page_config
 */
public class FeignSysPageConfig extends BaseEntity {
    private static final long serialVersionUID = 4966359225917000886L;

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
     * 站点类型（1用电 2光伏 3用水 4流量）
     */
    @ApiModelProperty("站点类型（1用电 2光伏 3用水 4流量）")
    @Excel(name = "电站类型", readConverterExp = "1=配电,2=光伏,3=用水,4=流量")
    private Integer stationType;

    /**
     * 模块ID
     */
    @ApiModelProperty("模块ID")
    @Excel(name = "模块ID")
    private Long groupId;

    /**
     * 模块名称
     */
    @ApiModelProperty("模块名称")
    @Excel(name = "模块名称")
    private String groupName;

    /**
     * 访问方法
     */
    @ApiModelProperty("访问方法")
    @Excel(name = "访问方法")
    private String methodValue;

    /**
     * 页面标识
     */
    @ApiModelProperty("页面标识")
    @Excel(name = "页面标识")
    private String pageValue;

    /**
     * 设备
     */
    @ApiModelProperty("设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    /**
     * X轴类型
     */
    @ApiModelProperty("X轴类型")
    @Excel(name = "X轴类型")
    private Integer xAxis;

    /**
     * 变量（Json：变量/单位/存储类型/取值类型）
     */
    @ApiModelProperty("变量（Json：变量/单位/存储类型/取值类型）")
    @Excel(name = "变量", readConverterExp = "Json：变量/单位/存储类型/取值类型")
    private List<FeignVariableConfigVo> variableConfig =new ArrayList<>();

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

    /** 分组父名称 */
    private String groupParentName;

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

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMethodValue() {
        return methodValue;
    }

    public void setMethodValue(String methodValue) {
        this.methodValue = methodValue;
    }

    public void setPageValue(String pageValue) {
        this.pageValue = pageValue;
    }

    public String getPageValue() {
        return pageValue;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setxAxis(Integer xAxis) {
        this.xAxis = xAxis;
    }

    public Integer getxAxis() {
        return xAxis;
    }

    public List<FeignVariableConfigVo> getVariableConfig() {
        return variableConfig;
    }

    public void setVariableConfig(List<FeignVariableConfigVo> variableConfig) {
        this.variableConfig = variableConfig;
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

    public String getGroupParentName() {
        return groupParentName;
    }

    public void setGroupParentName(String groupParentName) {
        this.groupParentName = groupParentName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("stationType", getStationType())
                .append("mapId", getGroupId())
                .append("methodValue", getMethodValue())
                .append("pageValue", getPageValue())
                .append("deviceId", getDeviceId())
                .append("xAxis", getxAxis())
                .append("variableConfig", getVariableConfig())
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
