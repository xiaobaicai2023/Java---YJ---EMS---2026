package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 消息模板对象 sys_message_template
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("消息模板对象")
public class SysMessageTemplate extends BaseEntity
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

    /** 模板类型（1短信 2微信 3小程序） */
    @ApiModelProperty("模板类型（1短信 2微信 3小程序）")
    @Excel(name = "模板类型", readConverterExp = "1=短信,2=微信,3=小程序")
    private Integer templateType;

    /** 消息主题 */
    @ApiModelProperty("消息主题")
    @Excel(name = "消息主题")
    private String topic;

    /** 主题代码 */
    @ApiModelProperty("主题代码")
    @Excel(name = "主题代码")
    private String topicSn;

    /** 模板ID */
    @ApiModelProperty("模板ID")
    @Excel(name = "模板ID")
    private Integer templateId;

    /** 短信渠道（1腾讯云 2阿里云） */
    @ApiModelProperty("短信渠道（1腾讯云 2阿里云）")
    @Excel(name = "短信渠道", readConverterExp = "1=腾讯云,2=阿里云")
    private Integer smsChannel;

    /** 短信签名 */
    @ApiModelProperty("短信签名")
    @Excel(name = "短信签名")
    private String smsSign;

    /** 模板内容 */
    @ApiModelProperty("模板内容")
    @Excel(name = "模板内容")
    private String templateContent;

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

    public void setTemplateType(Integer templateType)
    {
        this.templateType = templateType;
    }

    public Integer getTemplateType()
    {
        return templateType;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopicSn(String topicSn)
    {
        this.topicSn = topicSn;
    }

    public String getTopicSn()
    {
        return topicSn;
    }

    public void setTemplateId(Integer templateId)
    {
        this.templateId = templateId;
    }

    public Integer getTemplateId()
    {
        return templateId;
    }

    public void setSmsChannel(Integer smsChannel)
    {
        this.smsChannel = smsChannel;
    }

    public Integer getSmsChannel()
    {
        return smsChannel;
    }

    public void setSmsSign(String smsSign)
    {
        this.smsSign = smsSign;
    }

    public String getSmsSign()
    {
        return smsSign;
    }

    public void setTemplateContent(String templateContent)
    {
        this.templateContent = templateContent;
    }

    public String getTemplateContent()
    {
        return templateContent;
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
                .append("templateType", getTemplateType())
                .append("topic", getTopic())
                .append("topicSn", getTopicSn())
                .append("templateId", getTemplateId())
                .append("smsChannel", getSmsChannel())
                .append("smsSign", getSmsSign())
                .append("templateContent", getTemplateContent())
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
