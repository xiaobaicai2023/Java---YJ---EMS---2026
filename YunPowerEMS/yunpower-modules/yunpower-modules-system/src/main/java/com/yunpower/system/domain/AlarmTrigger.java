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
import org.springframework.data.annotation.Transient;

/**
 * 报警管理对象 alarm_trigger
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("报警管理对象")
public class AlarmTrigger extends BaseEntity {
    private static final long serialVersionUID = 1L;

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
     * 电站类型
     */
    @ApiModelProperty("电站类型")
    @Excel(name = "电站类型")
    private Integer stationType;

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    @Excel(name = "设备ID")
    private Long deviceId;

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    @Excel(name = "设备编码")
    private String deviceSn;

    /**
     * 变量ID
     */
    @ApiModelProperty("变量ID")
    @Excel(name = "变量ID")
    private Long varId;

    /**
     * 变量编码
     */
    @ApiModelProperty("变量编码")
    @Excel(name = "变量编码")
    private String varSn;

    /**
     * 变量类型（1模拟量 2状态量）
     */
    @ApiModelProperty("变量类型（1模拟量 2状态量）")
    @Excel(name = "变量类型", readConverterExp = "1=模拟量,2=状态量")
    private Integer varType;

    /**
     * 报警类型
     */
    @ApiModelProperty("报警类型")
    @Excel(name = "报警类型")
    private Long categoryId;

    /**
     * 报警级别
     */
    @ApiModelProperty("报警级别")
    @Excel(name = "报警级别")
    private Integer triggerLevel;

    /**
     * 触发条件
     */
    @ApiModelProperty("触发条件")
    @Excel(name = "触发条件")
    private String triggerConditionName;

    /**
     * 触发条件编码
     */
    @ApiModelProperty("触发条件编码")
    @Excel(name = "触发条件编码")
    private String triggerConditionSn;

    /**
     * 阈值
     */
    @ApiModelProperty("阈值")
    @Excel(name = "阈值")
    private Float threshold;

    /**
     * 实际值
     */
    @ApiModelProperty("实际值")
    @Excel(name = "实际值")
    private Float realValue;

    /**
     * 报警内容
     */
    @ApiModelProperty("报警内容")
    @Excel(name = "报警内容")
    private String triggerContent;

    /**
     * 是否发送短信（0否 1是）
     */
    @ApiModelProperty("是否发送短信（0否 1是）")
    @Excel(name = "是否发送短信", readConverterExp = "0=否,1=是")
    private Integer isSendSms;

    /**
     * 接收人员类型（1按岗位发送 1指定具体人员）
     */
    @ApiModelProperty("接收人员类型（1按岗位发送 1指定具体人员）")
    @Excel(name = "接收人员类型", readConverterExp = "1=按岗位发送,1=指定具体人员")
    private Integer receiveType;

    /**
     * 具体岗位或人员（JSON格式）
     */
    @ApiModelProperty("具体岗位或人员（JSON格式）")
    @Excel(name = "具体岗位或人员", readConverterExp = "JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> receiveConcrete;

    /**
     * 发生时间
     */
    @ApiModelProperty("发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发生时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date happenTime;

    /**
     * 恢复时间
     */
    @ApiModelProperty("恢复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "恢复时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recoverTime;

    /**
     * 确认时间
     */
    @ApiModelProperty("确认时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "确认时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;

    /**
     * 确认人员
     */
    @ApiModelProperty("确认人员")
    @Excel(name = "确认人员")
    private String confirmBy;

    /**
     * 确认过程
     */
    @ApiModelProperty("确认过程")
    @Excel(name = "确认过程")
    private String confirmContent;

    /**
     * 是否自动恢复（0否 1是）
     */
    @ApiModelProperty("是否自动恢复（0否 1是）")
    @Excel(name = "是否自动恢复", readConverterExp = "0=否,1=是")
    private Integer isAuto;

    /**
     * 报警状态
     */
    @ApiModelProperty("报警状态")
    @Excel(name = "报警状态")
    private Integer triggerStatus;

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

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 变量名称
     */
    private String varName;

    /**
     * 变量类型（1模拟量 2状态量）
     */
    @Transient
    private String varTypeName;

    /**
     * 报警类型
     */
    private String categoryName;

    /**
     * 报警状态
     */
    @Transient
    private String triggerStatusName;

    /**
     * 报警级别
     */
    private String triggerLevelName;

    /**
     * 报警名称
     * 报警名称为拼接：[设备名称]触发条件
     */
    @Transient
    private String triggerName;

    /**
     * 站点名称
     */
    @Transient
    private String stationName;

    /**
     * 统计数据
     */
    @Transient
    private Integer statisticValue;

    /**
     * 查询返回日期格式
     */
    @Transient
    private String formattedDatetime;

    /**
     * 日期维度
     */
    @Transient
    private Integer dateDim;

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

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setVarId(Long varId) {
        this.varId = varId;
    }

    public Long getVarId() {
        return varId;
    }

    public void setVarSn(String varSn) {
        this.varSn = varSn;
    }

    public String getVarSn() {
        return varSn;
    }

    public void setVarType(Integer varType) {
        this.varType = varType;
    }

    public Integer getVarType() {
        return varType;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setTriggerLevel(Integer triggerLevel) {
        this.triggerLevel = triggerLevel;
    }

    public Integer getTriggerLevel() {
        return triggerLevel;
    }

    public void setTriggerConditionName(String triggerConditionName) {
        this.triggerConditionName = triggerConditionName;
    }

    public String getTriggerConditionName() {
        return triggerConditionName;
    }

    public void setTriggerConditionSn(String triggerConditionSn) {
        this.triggerConditionSn = triggerConditionSn;
    }

    public String getTriggerConditionSn() {
        return triggerConditionSn;
    }

    public void setThreshold(Float threshold) {
        this.threshold = threshold;
    }

    public Float getThreshold() {
        return threshold;
    }

    public void setRealValue(Float realValue) {
        this.realValue = realValue;
    }

    public Float getRealValue() {
        return realValue;
    }

    public void setTriggerContent(String triggerContent) {
        this.triggerContent = triggerContent;
    }

    public String getTriggerContent() {
        return triggerContent;
    }

    public void setIsSendSms(Integer isSendSms) {
        this.isSendSms = isSendSms;
    }

    public Integer getIsSendSms() {
        return isSendSms;
    }

    public void setReceiveType(Integer receiveType) {
        this.receiveType = receiveType;
    }

    public Integer getReceiveType() {
        return receiveType;
    }

    public List<JsonCommonVo> getReceiveConcrete() {
        return receiveConcrete;
    }

    public void setReceiveConcrete(List<JsonCommonVo> receiveConcrete) {
        this.receiveConcrete = receiveConcrete;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setRecoverTime(Date recoverTime) {
        this.recoverTime = recoverTime;
    }

    public Date getRecoverTime() {
        return recoverTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public String getConfirmContent() {
        return confirmContent;
    }

    public void setConfirmContent(String confirmContent) {
        this.confirmContent = confirmContent;
    }

    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }

    public Integer getIsAuto() {
        return isAuto;
    }

    public Integer getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(Integer triggerStatus) {
        this.triggerStatus = triggerStatus;
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

    public String getTriggerStatusName() {
        return triggerStatusName;
    }

    public void setTriggerStatusName(String triggerStatusName) {
        this.triggerStatusName = triggerStatusName;
    }

    public String getTriggerLevelName() {
        return triggerLevelName;
    }

    public void setTriggerLevelName(String triggerLevelName) {
        this.triggerLevelName = triggerLevelName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getStatisticValue() {
        return statisticValue;
    }

    public void setStatisticValue(Integer statisticValue) {
        this.statisticValue = statisticValue;
    }

    public String getFormattedDatetime() {
        return formattedDatetime;
    }

    public void setFormattedDatetime(String formattedDatetime) {
        this.formattedDatetime = formattedDatetime;
    }

    public Integer getDateDim() {
        return dateDim;
    }

    public void setDateDim(Integer dateDim) {
        this.dateDim = dateDim;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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
                .append("triggerLevel", getTriggerLevel())
                .append("triggerConditionName", getTriggerConditionName())
                .append("triggerConditionSn", getTriggerConditionSn())
                .append("threshold", getThreshold())
                .append("realValue", getRealValue())
                .append("triggerContent", getTriggerContent())
                .append("isSendSms", getIsSendSms())
                .append("receiveType", getReceiveType())
                .append("receiveConcrete", getReceiveConcrete())
                .append("happenTime", getHappenTime())
                .append("recoverTime", getRecoverTime())
                .append("confirmTime", getConfirmTime())
                .append("confirmBy", getConfirmBy())
                .append("confirmContent", getConfirmContent())
                .append("isAuto", getIsAuto())
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
