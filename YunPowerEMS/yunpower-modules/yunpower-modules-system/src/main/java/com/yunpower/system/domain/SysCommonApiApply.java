package com.yunpower.system.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 第三方接入申请对象 sys_common_api_apply
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("第三方接入申请对象")
public class SysCommonApiApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    @ApiModelProperty("编号ID")
    private Long id;

    /** 接入公司 */
    @ApiModelProperty("接入公司")
    @Excel(name = "接入公司")
    private String companyName;

    /** 联系人员 */
    @ApiModelProperty("联系人员")
    @Excel(name = "联系人员")
    private String linkName;

    /** 联系电话 */
    @ApiModelProperty("联系电话")
    @Excel(name = "联系电话")
    private String linkMobile;

    /** 唯一标识 */
    @ApiModelProperty("唯一标识")
    @Excel(name = "唯一标识")
    private String appId;

    /** 帐号 */
    @ApiModelProperty("帐号")
    @Excel(name = "帐号")
    private String appKey;

    /** 密钥 */
    @ApiModelProperty("密钥")
    @Excel(name = "密钥")
    private String appSecret;

    /** 令牌 */
    @ApiModelProperty("令牌")
    @Excel(name = "令牌")
    private String token;

    /** 令牌有效期 */
    @ApiModelProperty("令牌有效期")
    @Excel(name = "令牌有效期")
    private Integer effectMinute;

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

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setLinkName(String linkName)
    {
        this.linkName = linkName;
    }

    public String getLinkName()
    {
        return linkName;
    }

    public void setLinkMobile(String linkMobile)
    {
        this.linkMobile = linkMobile;
    }

    public String getLinkMobile()
    {
        return linkMobile;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getAppKey()
    {
        return appKey;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public void setEffectMinute(Integer effectMinute)
    {
        this.effectMinute = effectMinute;
    }

    public Integer getEffectMinute()
    {
        return effectMinute;
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
                .append("companyName", getCompanyName())
                .append("linkName", getLinkName())
                .append("linkMobile", getLinkMobile())
                .append("appId", getAppId())
                .append("appKey", getAppKey())
                .append("appSecret", getAppSecret())
                .append("token", getToken())
                .append("effectMinute", getEffectMinute())
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
