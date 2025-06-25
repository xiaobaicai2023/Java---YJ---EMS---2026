package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 参数配置对象 sys_common_config
 * 
 * @author yunpower
 * @date 2023-09-14
 */
@ApiModel("参数配置对象")
public class SysCommonConfig extends BaseEntity
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

    /** 参数Key */
    @ApiModelProperty("参数Key")
    @Excel(name = "参数Key")
    private String paramKey;

    /** 参数值 */
    @ApiModelProperty("参数值")
    @Excel(name = "参数值")
    private String paramValue;

    /** 系统内置（0否 1是） */
    @ApiModelProperty("系统内置（0否 1是）")
    @Excel(name = "系统内置", readConverterExp = "0=否,1=是")
    private Integer isSystem;

    /** 参数说明 */
    @ApiModelProperty("参数说明")
    @Excel(name = "参数说明")
    private String paramInfo;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public void setParamKey(String paramKey)
    {
        this.paramKey = paramKey;
    }

    @NotBlank(message = "参数键名长度不能为空")
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符")
    public String getParamKey() 
    {
        return paramKey;
    }
    public void setParamValue(String paramValue) 
    {
        this.paramValue = paramValue;
    }

    @NotBlank(message = "参数键值不能为空")
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符")
    public String getParamValue() 
    {
        return paramValue;
    }
    public void setIsSystem(Integer isSystem) 
    {
        this.isSystem = isSystem;
    }

    public Integer getIsSystem() 
    {
        return isSystem;
    }
    public void setParamInfo(String paramInfo) 
    {
        this.paramInfo = paramInfo;
    }

    public String getParamInfo() 
    {
        return paramInfo;
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
            .append("paramKey", getParamKey())
            .append("paramValue", getParamValue())
            .append("isSystem", getIsSystem())
            .append("paramInfo", getParamInfo())
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
