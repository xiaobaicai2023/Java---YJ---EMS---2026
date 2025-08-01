package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 岗位信息对象 sys_post
 * 
 * @author yunpower
 * @date 2023-09-14
 */
@ApiModel("岗位信息对象")
public class SysPost extends BaseEntity
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

    /** 岗位名称 */
    @ApiModelProperty("岗位名称")
    @Excel(name = "岗位名称")
    private String postName;

    /** 岗位代码 */
    @ApiModelProperty("岗位代码")
    @Excel(name = "岗位代码")
    private String postSn;

    /** 显示顺序 */
    @ApiModelProperty("显示顺序")
    @Excel(name = "显示顺序")
    private Integer orderNum;

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
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setPostName(String postName) 
    {
        this.postName = postName;
    }

    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    public String getPostName() 
    {
        return postName;
    }
    public void setPostSn(String postSn) 
    {
        this.postSn = postSn;
    }

    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    public String getPostSn() 
    {
        return postSn;
    }
    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() 
    {
        return orderNum;
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
            .append("postName", getPostName())
            .append("postSn", getPostSn())
            .append("orderNum", getOrderNum())
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
