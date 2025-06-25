package com.yunpower.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 消息对象 sys_message
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("消息对象")
public class SysMessage extends BaseEntity
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

    /** 用户姓名 */
    @ApiModelProperty("用户姓名")
    @Excel(name = "用户姓名")
    private String userName;

    /** 手机号码 */
    @ApiModelProperty("手机号码")
    @Excel(name = "手机号码")
    private String mobile;

    /** 消息类型（1短信 2微信 3小程序） */
    @ApiModelProperty("消息类型（1短信 2微信 3小程序）")
    @Excel(name = "消息类型", readConverterExp = "1=短信,2=微信,3=小程序")
    private Integer templateType;

    /** 消息主题 */
    @ApiModelProperty("消息主题")
    @Excel(name = "消息主题")
    private String topic;

    /** 验证码 */
    @ApiModelProperty("验证码")
    @Excel(name = "验证码")
    private String code;

    /** 有效期 */
    @ApiModelProperty("有效期")
    @Excel(name = "有效期")
    private Integer effectMinute;

    /** 提交信息 */
    @ApiModelProperty("提交信息")
    @Excel(name = "提交信息")
    private String returnResult;

    /** 短信渠道（1腾讯云 2阿里云） */
    @ApiModelProperty("短信渠道（1腾讯云 2阿里云）")
    @Excel(name = "短信渠道", readConverterExp = "1=腾讯云,2=阿里云")
    private Integer smsChannel;

    /** 模板ID */
    @ApiModelProperty("模板ID")
    @Excel(name = "模板ID")
    private Long templateId;

    /** 发送人员 */
    @ApiModelProperty("发送人员")
    @Excel(name = "发送人员")
    private String sendBy;

    /** 发送时间 */
    @ApiModelProperty("发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 发送状态（-10已使用 0发送成功 1发送失败） */
    @ApiModelProperty("发送状态（-10已使用 0发送成功 1发送失败）")
    @Excel(name = "发送状态", readConverterExp = "-=10已使用,0=发送成功,1=发送失败")
    private Integer sendStatus;

    /** 发送内容 */
    @ApiModelProperty("发送内容")
    @Excel(name = "发送内容")
    private String sendContent;

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

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile()
    {
        return mobile;
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

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public void setEffectMinute(Integer effectMinute)
    {
        this.effectMinute = effectMinute;
    }

    public Integer getEffectMinute()
    {
        return effectMinute;
    }

    public void setReturnResult(String returnResult)
    {
        this.returnResult = returnResult;
    }

    public String getReturnResult()
    {
        return returnResult;
    }

    public void setSmsChannel(Integer smsChannel)
    {
        this.smsChannel = smsChannel;
    }

    public Integer getSmsChannel()
    {
        return smsChannel;
    }

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setSendBy(String sendBy)
    {
        this.sendBy = sendBy;
    }

    public String getSendBy()
    {
        return sendBy;
    }

    public void setSendTime(Date sendTime)
    {
        this.sendTime = sendTime;
    }

    public Date getSendTime()
    {
        return sendTime;
    }

    public void setSendStatus(Integer sendStatus)
    {
        this.sendStatus = sendStatus;
    }

    public Integer getSendStatus()
    {
        return sendStatus;
    }

    public void setSendContent(String sendContent)
    {
        this.sendContent = sendContent;
    }

    public String getSendContent()
    {
        return sendContent;
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
                .append("userName", getUserName())
                .append("remark", getRemark())
                .append("mobile", getMobile())
                .append("templateType", getTemplateType())
                .append("topic", getTopic())
                .append("code", getCode())
                .append("effectMinute", getEffectMinute())
                .append("returnResult", getReturnResult())
                .append("smsChannel", getSmsChannel())
                .append("templateId", getTemplateId())
                .append("sendBy", getSendBy())
                .append("sendTime", getSendTime())
                .append("sendStatus", getSendStatus())
                .append("sendContent", getSendContent())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("stopFlag", getStopFlag())
                .append("deleteFlag", getDeleteFlag())
                .toString();
    }
}
