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
 * 工单管理对象 devops_order
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@ApiModel("工单管理对象")
public class DevopsOrder extends BaseEntity {
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
     * 工单名称
     */
    @ApiModelProperty("工单名称")
    @Excel(name = "工单名称")
    private String orderTitle;

    /**
     * 电站类型
     */
    @ApiModelProperty("电站类型")
    @Excel(name = "电站类型")
    private Integer stationType;

    /**
     * 维修站点
     */
    @ApiModelProperty("维修站点")
    @Excel(name = "维修站点")
    private Long stationId;

    /**
     * 维修设备
     */
    @ApiModelProperty("维修设备")
    @Excel(name = "维修设备")
    private Long deviceId;

    /**
     * 关联报警
     */
    @ApiModelProperty("关联报警")
    @Excel(name = "关联报警")
    private Long alarmId;

    /**
     * 优先级
     */
    @ApiModelProperty("优先级")
    @Excel(name = "优先级")
    private String priorityLevel;

    /**
     * 工单类型（1普通 2抢修）
     */
    @ApiModelProperty("工单类型（1普通 2抢修）")
    @Excel(name = "工单类型", readConverterExp = "1=普通,2=抢修")
    private Integer orderType;

    /**
     * 计划开始时间
     */
    @ApiModelProperty("计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;

    /**
     * 计划结束时间
     */
    @ApiModelProperty("计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;

    /**
     * 工单描述
     */
    @ApiModelProperty("工单描述")
    @Excel(name = "工单描述")
    private String orderContent;

    /**
     * 图片描述（JSON格式）
     */
    @ApiModelProperty("图片描述（JSON格式）")
    @Excel(name = "图片描述", readConverterExp = "JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> orderFiles;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    @Excel(name = "负责人")
    private String chargeBy;

    /**
     * 协作人（多人，JSON格式）
     */
    @ApiModelProperty("协作人（多人，JSON格式）")
    @Excel(name = "协作人", readConverterExp = "多人，JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> collaborateFor;

    /**
     * 抄送人（多人，JSON格式）
     */
    @ApiModelProperty("抄送人（多人，JSON格式）")
    @Excel(name = "抄送人", readConverterExp = "多人，JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> copyFor;

    /**
     * 验证人（多人，JSON格式）
     */
    @ApiModelProperty("验证人（多人，JSON格式）")
    @Excel(name = "验证人", readConverterExp = "多人，JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> verifyFor;

    /**
     * 接单时间
     */
    @ApiModelProperty("接单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "接单时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 完成时间
     */
    @ApiModelProperty("完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;

    /**
     * 实时用时（分钟）
     */
    @ApiModelProperty("实时用时（分钟）")
    @Excel(name = "实时用时", readConverterExp = "分=钟")
    private Integer useMinutes;

    /**
     * 处理过程
     */
    @ApiModelProperty("处理过程")
    @Excel(name = "处理过程")
    private String handleProcess;

    /**
     * 现场照片（JSON格式）
     */
    @ApiModelProperty("现场照片（JSON格式）")
    @Excel(name = "现场照片", readConverterExp = "JSON格式")
    @TableField(typeHandler = JsonCommonTypeHandler.class)
    private List<JsonCommonVo> handleFiles;

    /**
     * 验证时间
     */
    @ApiModelProperty("验证时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "验证时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date verifyTime;

    /**
     * 验证意见
     */
    @ApiModelProperty("验证意见")
    @Excel(name = "验证意见")
    private String verifyAdvice;

    /**
     * 超时状态（0正常 1已超时）
     */
    @ApiModelProperty("超时状态（0正常 1已超时）")
    @Excel(name = "超时状态", readConverterExp = "0=正常,1=已超时")
    private Integer timeoutStatus;

    /**
     * 工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）
     */
    @ApiModelProperty("工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）")
    @Excel(name = "工单状态", readConverterExp = "-=1已取消,0=新工单,1=已接单,2=进行中,3=已完成,1=0已验证,2=0延迟处理")
    private Integer status;

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
     * 站点名称
     */
    @Transient
    private String stationName;

    /**
     * 订单类型名称
     */
    @Transient
    private String orderTypeName;

    /**
     * 订单状态名称
     */
    @Transient
    private String statusName;

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

    /**
     * 关联报警-名称
     */
    @ApiModelProperty("关联报警名称")
    @Excel(name = "关联报警名称")
    @Transient
    private String alarmName;

    /**
     * 关联报警-报警内容
     */
    @ApiModelProperty("报警内容")
    @Excel(name = "报警内容")
    private String alarmContent;

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

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public Integer getStationType() {
        return stationType;
    }

    public void setStationType(Integer stationType) {
        this.stationType = stationType;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setPriorityLevel(String priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPriorityLevel() {
        return priorityLevel;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public List<JsonCommonVo> getOrderFiles() {
        return orderFiles;
    }

    public void setOrderFiles(List<JsonCommonVo> orderFiles) {
        this.orderFiles = orderFiles;
    }

    public void setChargeBy(String chargeBy) {
        this.chargeBy = chargeBy;
    }

    public String getChargeBy() {
        return chargeBy;
    }

    public List<JsonCommonVo> getCollaborateFor() {
        return collaborateFor;
    }

    public void setCollaborateFor(List<JsonCommonVo> collaborateFor) {
        this.collaborateFor = collaborateFor;
    }

    public List<JsonCommonVo> getCopyFor() {
        return copyFor;
    }

    public void setCopyFor(List<JsonCommonVo> copyFor) {
        this.copyFor = copyFor;
    }

    public List<JsonCommonVo> getVerifyFor() {
        return verifyFor;
    }

    public void setVerifyFor(List<JsonCommonVo> verifyFor) {
        this.verifyFor = verifyFor;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setUseMinutes(Integer useMinutes) {
        this.useMinutes = useMinutes;
    }

    public Integer getUseMinutes() {
        return useMinutes;
    }

    public void setHandleProcess(String handleProcess) {
        this.handleProcess = handleProcess;
    }

    public String getHandleProcess() {
        return handleProcess;
    }

    public List<JsonCommonVo> getHandleFiles() {
        return handleFiles;
    }

    public void setHandleFiles(List<JsonCommonVo> handleFiles) {
        this.handleFiles = handleFiles;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyAdvice(String verifyAdvice) {
        this.verifyAdvice = verifyAdvice;
    }

    public String getVerifyAdvice() {
        return verifyAdvice;
    }

    public void setTimeoutStatus(Integer timeoutStatus) {
        this.timeoutStatus = timeoutStatus;
    }

    public Integer getTimeoutStatus() {
        return timeoutStatus;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
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

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("orderTitle", getOrderTitle())
                .append("stationType", getStationType())
                .append("stationId", getStationId())
                .append("deviceId", getDeviceId())
                .append("alarmId", getAlarmId())
                .append("priorityLevel", getPriorityLevel())
                .append("planStartTime", getPlanStartTime())
                .append("planEndTime", getPlanEndTime())
                .append("orderContent", getOrderContent())
                .append("orderFiles", getOrderFiles())
                .append("chargeBy", getChargeBy())
                .append("collaborateFor", getCollaborateFor())
                .append("copyFor", getCopyFor())
                .append("verifyFor", getVerifyFor())
                .append("receiveTime", getReceiveTime())
                .append("startTime", getStartTime())
                .append("completeTime", getCompleteTime())
                .append("useMinutes", getUseMinutes())
                .append("handleProcess", getHandleProcess())
                .append("handleFiles", getHandleFiles())
                .append("verifyTime", getVerifyTime())
                .append("verifyAdvice", getVerifyAdvice())
                .append("timeoutStatus", getTimeoutStatus())
                .append("status", getStatus())
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
