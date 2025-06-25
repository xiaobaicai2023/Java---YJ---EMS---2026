package com.yunpower.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;

/**
 * 用户充值卡对象 sys_user_card
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("用户充值卡对象")
public class SysUserCard extends BaseEntity
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

    /** 逻辑代码 */
    @ApiModelProperty("逻辑代码")
    @Excel(name = "逻辑代码")
    private String logicCode;

    /** 用户ID */
    @ApiModelProperty("用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 卡号 */
    @ApiModelProperty("卡号")
    @Excel(name = "卡号")
    private String cardNo;

    /** 卡类型（1充值卡 2次卡） */
    @ApiModelProperty("卡类型（1充值卡 2次卡）")
    @Excel(name = "卡类型", readConverterExp = "1=充值卡,2=次卡")
    private Integer cardType;

    /** 充值卡余额 */
    @ApiModelProperty("充值卡余额")
    @Excel(name = "充值卡余额")
    private BigDecimal cardValue;

    /** 可提现金额 */
    @ApiModelProperty("可提现金额")
    @Excel(name = "可提现金额")
    private BigDecimal cashValue;

    /** 累计消费金额 */
    @ApiModelProperty("累计消费金额")
    @Excel(name = "累计消费金额")
    private BigDecimal addUpValue;

    /** 次卡有效次数 */
    @ApiModelProperty("次卡有效次数")
    @Excel(name = "次卡有效次数")
    private Integer validTimes;

    /** 次卡剩余次数 */
    @ApiModelProperty("次卡剩余次数")
    @Excel(name = "次卡剩余次数")
    private Integer surplusTimes;

    /** 次卡赠送次数 */
    @ApiModelProperty("次卡赠送次数")
    @Excel(name = "次卡赠送次数")
    private Integer giveTimes;

    /** 累计使用次数 */
    @ApiModelProperty("累计使用次数")
    @Excel(name = "累计使用次数")
    private Integer addUpTimes;

    /** 有效期开始 */
    @ApiModelProperty("有效期开始")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期开始", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 有效期结束 */
    @ApiModelProperty("有效期结束")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期结束", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 是否停用（0正常1停用） */
    @ApiModelProperty("是否停用（0正常1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常1停用")
    private Integer stopFlag;

    /** 是否删除（0正常1删除） */
    @ApiModelProperty("是否删除（0正常1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常1删除")
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

    public void setLogicCode(String logicCode)
    {
        this.logicCode = logicCode;
    }

    public String getLogicCode()
    {
        return logicCode;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setCardNo(String cardNo)
    {
        this.cardNo = cardNo;
    }

    public String getCardNo()
    {
        return cardNo;
    }

    public void setCardType(Integer cardType)
    {
        this.cardType = cardType;
    }

    public Integer getCardType()
    {
        return cardType;
    }

    public void setCardValue(BigDecimal cardValue)
    {
        this.cardValue = cardValue;
    }

    public BigDecimal getCardValue()
    {
        return cardValue;
    }

    public void setCashValue(BigDecimal cashValue)
    {
        this.cashValue = cashValue;
    }

    public BigDecimal getCashValue()
    {
        return cashValue;
    }

    public void setAddUpValue(BigDecimal addUpValue)
    {
        this.addUpValue = addUpValue;
    }

    public BigDecimal getAddUpValue()
    {
        return addUpValue;
    }

    public void setValidTimes(Integer validTimes)
    {
        this.validTimes = validTimes;
    }

    public Integer getValidTimes()
    {
        return validTimes;
    }

    public void setSurplusTimes(Integer surplusTimes)
    {
        this.surplusTimes = surplusTimes;
    }

    public Integer getSurplusTimes()
    {
        return surplusTimes;
    }

    public void setGiveTimes(Integer giveTimes)
    {
        this.giveTimes = giveTimes;
    }

    public Integer getGiveTimes()
    {
        return giveTimes;
    }

    public void setAddUpTimes(Integer addUpTimes)
    {
        this.addUpTimes = addUpTimes;
    }

    public Integer getAddUpTimes()
    {
        return addUpTimes;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
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
                .append("logicCode", getLogicCode())
                .append("userId", getUserId())
                .append("cardNo", getCardNo())
                .append("cardType", getCardType())
                .append("cardValue", getCardValue())
                .append("cashValue", getCashValue())
                .append("addUpValue", getAddUpValue())
                .append("validTimes", getValidTimes())
                .append("surplusTimes", getSurplusTimes())
                .append("giveTimes", getGiveTimes())
                .append("addUpTimes", getAddUpTimes())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
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
