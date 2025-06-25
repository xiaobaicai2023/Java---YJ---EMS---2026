package com.yunpower.system.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunpower.system.domain.handler.JsonCommonTypeHandler;
import com.yunpower.system.domain.jsonvo.JsonCommonVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 留言回复管理对象 sys_leave_message
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("留言回复管理对象")
public class SysLeaveMessage extends BaseEntity
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

    /** 数据表名 */
    @ApiModelProperty("数据表名")
    @Excel(name = "数据表名")
    private String tableName;

    /** 记录ID */
    @ApiModelProperty("记录ID")
    @Excel(name = "记录ID")
    private Long recordId;

    /** 留言人ID */
    @ApiModelProperty("留言人ID")
    @Excel(name = "留言人ID")
    private Long userId;

    /** 留言人 */
    @ApiModelProperty("留言人")
    @Excel(name = "留言人")
    private String userName;

    /** 留言内容 */
    @ApiModelProperty("留言内容")
    @Excel(name = "留言内容")
    private String replyContent;

    /** 留言日期 */
    @ApiModelProperty("留言日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "留言日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date replyTime;

    /** 附件合集（JSON格式） */
    @ApiModelProperty("附件合集（JSON格式）")
    @Excel(name = "附件合集", readConverterExp = "JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> replyFiles;

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

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId()
    {
        return recordId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setReplyContent(String replyContent)
    {
        this.replyContent = replyContent;
    }

    public String getReplyContent()
    {
        return replyContent;
    }

    public void setReplyTime(Date replyTime)
    {
        this.replyTime = replyTime;
    }

    public Date getReplyTime()
    {
        return replyTime;
    }

    public List<JsonCommonVo> getReplyFiles() {
        return replyFiles;
    }

    public void setReplyFiles(List<JsonCommonVo> replyFiles) {
        this.replyFiles = replyFiles;
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
                .append("tableName", getTableName())
                .append("recordId", getRecordId())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("replyContent", getReplyContent())
                .append("replyTime", getReplyTime())
                .append("replyFiles", getReplyFiles())
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
