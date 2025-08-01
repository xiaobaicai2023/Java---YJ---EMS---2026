package com.yunpower.system.domain;

import com.yunpower.common.core.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 通知公告对象 sys_notice
 * 
 * @author yunpower
 * @date 2023-09-14
 */
@ApiModel("通知公告对象")
public class SysNotice extends BaseEntity
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

    /** 公告标题 */
    @ApiModelProperty("公告标题")
    @Excel(name = "公告标题")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @ApiModelProperty("公告类型（1通知 2公告）")
    @Excel(name = "公告类型", readConverterExp = "1=通知,2=公告")
    private Integer noticeType;

    /** 公告分组 */
    @ApiModelProperty("公告分组")
    @Excel(name = "公告分组")
    private Integer groupId;

    /** 公告内容 */
    @ApiModelProperty("公告内容")
    @Excel(name = "公告内容")
    private String noticeContent;

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
    public void setNoticeTitle(String noticeTitle) 
    {
        this.noticeTitle = noticeTitle;
    }

    @Xss(message = "公告标题不能包含脚本字符")
    @NotBlank(message = "公告标题不能为空")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    public String getNoticeTitle() 
    {
        return noticeTitle;
    }
    public void setNoticeType(Integer noticeType) 
    {
        this.noticeType = noticeType;
    }

    public Integer getNoticeType() 
    {
        return noticeType;
    }
    public void setGroupId(Integer groupId) 
    {
        this.groupId = groupId;
    }

    public Integer getGroupId() 
    {
        return groupId;
    }
    public void setNoticeContent(String noticeContent) 
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent() 
    {
        return noticeContent;
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
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("groupId", getGroupId())
            .append("noticeContent", getNoticeContent())
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
