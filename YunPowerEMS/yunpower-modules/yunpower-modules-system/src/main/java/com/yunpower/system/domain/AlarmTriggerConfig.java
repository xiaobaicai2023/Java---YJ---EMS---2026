package com.yunpower.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.system.domain.handler.JsonCommonTypeHandler;
import com.yunpower.system.domain.jsonvo.JsonCommonVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * 报警配置对象 alarm_trigger_config
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("报警配置对象")
public class AlarmTriggerConfig extends BaseEntity
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

    /**
     * 电站类型
     */
    @ApiModelProperty("电站类型")
    @Excel(name = "电站类型")
    private Integer stationType;

    /** 设备ID */
    @ApiModelProperty("设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    /** 设备编码 */
    @ApiModelProperty("设备编码")
    @Excel(name = "设备编码")
    private String deviceSn;

    /** 变量ID */
    @ApiModelProperty("变量ID")
    @Excel(name = "变量ID")
    private Long varId;

    /** 变量编码 */
    @ApiModelProperty("变量编码")
    @Excel(name = "变量编码")
    private String varSn;

    /** 变量类型（1模拟量 2状态量） */
    @ApiModelProperty("变量类型（1模拟量 2状态量）")
    @Excel(name = "变量类型", readConverterExp = "1=模拟量,2=状态量")
    private Integer varType;

    /** 报警类型 */
    @ApiModelProperty("报警类型")
    @Excel(name = "报警类型")
    private Long categoryId;

    /** 阈值 */
    @ApiModelProperty("阈值")
    @Excel(name = "阈值")
    private Float threshold;

    /** 是否发送短信（0否 1是） */
    @ApiModelProperty("是否发送短信（0否 1是）")
    @Excel(name = "是否发送短信", readConverterExp = "0=否,1=是")
    private Integer isSendSms;

    /** 接收人员类型（1按岗位发送 1指定具体人员） */
    @ApiModelProperty("接收人员类型（1按岗位发送 1指定具体人员）")
    @Excel(name = "接收人员类型", readConverterExp = "1=按岗位发送,1=指定具体人员")
    private Integer receiveType;

    /** 具体岗位或人员（JSON格式） */
    @ApiModelProperty("具体岗位或人员（JSON格式）")
    @Excel(name = "具体岗位或人员", readConverterExp = "JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> receiveConcrete;

    /** 报警开始时间 */
    @ApiModelProperty("报警开始时间")
    @Excel(name = "报警开始时间")
    private String startTime;

    /** 报警结束时间 */
    @ApiModelProperty("报警结束时间")
    @Excel(name = "报警结束时间")
    private String endTime;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /** 设备名称 */
    @Transient
    private String deviceName;

    /** 变量名称 */
    @Transient
    private String varName;

    /** 变量类型（1模拟量 2状态量） */
    @Transient
    private String varTypeName;

    /** 报警类型 */
    @Transient
    private String categoryName;

    /** 批量新增时用到 */
    @Transient
    private String[] varIds;

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

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public void setDeviceId(Long deviceId)
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceSn(String deviceSn)
    {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn()
    {
        return deviceSn;
    }

    public void setVarId(Long varId)
    {
        this.varId = varId;
    }

    public Long getVarId()
    {
        return varId;
    }

    public void setVarSn(String varSn)
    {
        this.varSn = varSn;
    }

    public String getVarSn()
    {
        return varSn;
    }

    public void setVarType(Integer varType)
    {
        this.varType = varType;
    }

    public Integer getVarType()
    {
        return varType;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setThreshold(Float threshold)
    {
        this.threshold = threshold;
    }

    public Float getThreshold()
    {
        return threshold;
    }

    public void setIsSendSms(Integer isSendSms)
    {
        this.isSendSms = isSendSms;
    }

    public Integer getIsSendSms()
    {
        return isSendSms;
    }

    public void setReceiveType(Integer receiveType)
    {
        this.receiveType = receiveType;
    }

    public Integer getReceiveType()
    {
        return receiveType;
    }

    public List<JsonCommonVo> getReceiveConcrete() {
        return receiveConcrete;
    }

    public void setReceiveConcrete(List<JsonCommonVo> receiveConcrete) {
        this.receiveConcrete = receiveConcrete;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarTypeName() {
        return varTypeName;
    }

    public void setVarTypeName(String varTypeName) {
        this.varTypeName = varTypeName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String[] getVarIds() {
        return varIds;
    }

    public void setVarIds(String[] varIds) {
        this.varIds = varIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("stationType", getStationType())
                .append("deviceId", getDeviceId())
                .append("deviceSn", getDeviceSn())
                .append("varId", getVarId())
                .append("varSn", getVarSn())
                .append("varType", getVarType())
                .append("categoryId", getCategoryId())
                .append("threshold", getThreshold())
                .append("isSendSms", getIsSendSms())
                .append("receiveType", getReceiveType())
                .append("receiveConcrete", getReceiveConcrete())
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
