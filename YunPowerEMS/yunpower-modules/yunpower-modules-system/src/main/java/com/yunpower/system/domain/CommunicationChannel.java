package com.yunpower.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunpower.common.core.annotation.Excel;
import com.yunpower.common.core.web.domain.BaseEntity;
import org.springframework.data.annotation.Transient;

/**
 * 通讯通道对象 communication_channel
 *
 * @author JUNFU.WANG
 * @date 2023-10-11
 */
@ApiModel("通讯通道对象")
public class CommunicationChannel extends BaseEntity
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

    /** 电站类型（1配电 2光伏） */
    @ApiModelProperty("电站类型（1配电 2光伏）")
    @Excel(name = "电站类型", readConverterExp = "1=配电,2=光伏")
    private Integer stationType;

    /** 通道名称 */
    @ApiModelProperty("通道名称")
    @Excel(name = "通道名称")
    private String channelName;

    /** 通道编码 */
    @ApiModelProperty("通道编码")
    @Excel(name = "通道编码")
    private String channelSn;

    /** 接入方式（1TCP 2MQTT） */
    @ApiModelProperty("接入方式（1TCP 2MQTT）")
    @Excel(name = "接入方式", readConverterExp = "1=TCP,2=MQTT")
    private Integer accessType;

    /** IP地址 */
    @ApiModelProperty("IP地址")
    @Excel(name = "IP地址")
    private String ipAddress;

    /** 端口 */
    @ApiModelProperty("端口")
    @Excel(name = "端口")
    private Integer port;

    /** 通讯注册码 */
    @ApiModelProperty("通讯注册码")
    @Excel(name = "通讯注册码")
    private String registrationCode;

    /** 注册码起始位置 */
    @ApiModelProperty("注册码起始位置")
    @Excel(name = "注册码起始位置")
    private Integer codeStart;

    /** 注册码长度 */
    @ApiModelProperty("注册码长度")
    @Excel(name = "注册码长度")
    private Integer codeLength;

    /** 注册回应码 */
    @ApiModelProperty("注册回应码")
    @Excel(name = "注册回应码")
    private String registrationRsp;

    /** 连接地址 */
    @ApiModelProperty("连接地址")
    @Excel(name = "连接地址")
    private String connIp;

    /** 连接端口 */
    @ApiModelProperty("连接端口")
    @Excel(name = "连接端口")
    private Integer connPort;

    /** 连接帐号 */
    @ApiModelProperty("连接帐号")
    @Excel(name = "连接帐号")
    private String connUsername;

    /** 连接密码 */
    @ApiModelProperty("连接密码")
    @Excel(name = "连接密码")
    private String connPwd;

    /** 订阅主题 */
    @ApiModelProperty("订阅主题")
    @Excel(name = "订阅主题")
    private String subscribeTopic;

    /** 发布主题 */
    @ApiModelProperty("发布主题")
    @Excel(name = "发布主题")
    private String publishTopic;

    /** 连接超时（s） */
    @ApiModelProperty("连接超时（s）")
    @Excel(name = "连接超时", readConverterExp = "s")
    private Integer timeout;

    /** 调度周期（ms） */
    @ApiModelProperty("调度周期（ms）")
    @Excel(name = "调度周期", readConverterExp = "ms")
    private Integer scheduleInterval;

    /** 上线时间 */
    @ApiModelProperty("上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "上线时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    /** 下线时间 */
    @ApiModelProperty("下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下线时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date offlineTime;

    /** 是否激活 */
    @ApiModelProperty("是否激活")
    @Excel(name = "是否激活")
    private Integer isActive;

    /** 激活时间 */
    @ApiModelProperty("激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "激活时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date activeUpdateTime;

    /** 通道连接报警 */
    @ApiModelProperty("通道连接报警")
    @Excel(name = "通道连接报警")
    private Integer connectAlarm;

    /** 是否停用（0正常 1停用） */
    @ApiModelProperty("是否停用（0正常 1停用）")
    @Excel(name = "是否停用", readConverterExp = "0=正常,1=停用")
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    @ApiModelProperty("是否删除（0正常 1删除）")
    @Excel(name = "是否删除", readConverterExp = "0=正常,1=删除")
    private Integer deleteFlag;

    /** 站点名称（部门名称） */
    @Transient
    private String stationName;

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

    public void setStationType(Integer stationType)
    {
        this.stationType = stationType;
    }

    public Integer getStationType()
    {
        return stationType;
    }

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelName()
    {
        return channelName;
    }

    public void setChannelSn(String channelSn)
    {
        this.channelSn = channelSn;
    }

    public String getChannelSn()
    {
        return channelSn;
    }

    public void setAccessType(Integer accessType)
    {
        this.accessType = accessType;
    }

    public Integer getAccessType()
    {
        return accessType;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setRegistrationCode(String registrationCode)
    {
        this.registrationCode = registrationCode;
    }

    public String getRegistrationCode()
    {
        return registrationCode;
    }

    public void setCodeStart(Integer codeStart)
    {
        this.codeStart = codeStart;
    }

    public Integer getCodeStart()
    {
        return codeStart;
    }

    public void setCodeLength(Integer codeLength)
    {
        this.codeLength = codeLength;
    }

    public Integer getCodeLength()
    {
        return codeLength;
    }

    public void setRegistrationRsp(String registrationRsp)
    {
        this.registrationRsp = registrationRsp;
    }

    public String getRegistrationRsp()
    {
        return registrationRsp;
    }

    public void setConnIp(String connIp)
    {
        this.connIp = connIp;
    }

    public String getConnIp()
    {
        return connIp;
    }

    public void setConnPort(Integer connPort)
    {
        this.connPort = connPort;
    }

    public Integer getConnPort()
    {
        return connPort;
    }

    public void setConnUsername(String connUsername)
    {
        this.connUsername = connUsername;
    }

    public String getConnUsername()
    {
        return connUsername;
    }

    public void setConnPwd(String connPwd)
    {
        this.connPwd = connPwd;
    }

    public String getConnPwd()
    {
        return connPwd;
    }

    public void setSubscribeTopic(String subscribeTopic)
    {
        this.subscribeTopic = subscribeTopic;
    }

    public String getSubscribeTopic()
    {
        return subscribeTopic;
    }

    public void setPublishTopic(String publishTopic)
    {
        this.publishTopic = publishTopic;
    }

    public String getPublishTopic()
    {
        return publishTopic;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setScheduleInterval(Integer scheduleInterval)
    {
        this.scheduleInterval = scheduleInterval;
    }

    public Integer getScheduleInterval()
    {
        return scheduleInterval;
    }

    public void setOnlineTime(Date onlineTime)
    {
        this.onlineTime = onlineTime;
    }

    public Date getOnlineTime()
    {
        return onlineTime;
    }

    public void setOfflineTime(Date offlineTime)
    {
        this.offlineTime = offlineTime;
    }

    public Date getOfflineTime()
    {
        return offlineTime;
    }

    public void setIsActive(Integer isActive)
    {
        this.isActive = isActive;
    }

    public Integer getIsActive()
    {
        return isActive;
    }

    public void setActiveUpdateTime(Date activeUpdateTime)
    {
        this.activeUpdateTime = activeUpdateTime;
    }

    public Date getActiveUpdateTime()
    {
        return activeUpdateTime;
    }

    public void setConnectAlarm(Integer connectAlarm)
    {
        this.connectAlarm = connectAlarm;
    }

    public Integer getConnectAlarm()
    {
        return connectAlarm;
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("entId", getEntId())
                .append("deptId", getDeptId())
                .append("stationType", getStationType())
                .append("channelName", getChannelName())
                .append("channelSn", getChannelSn())
                .append("accessType", getAccessType())
                .append("ipAddress", getIpAddress())
                .append("port", getPort())
                .append("registrationCode", getRegistrationCode())
                .append("codeStart", getCodeStart())
                .append("codeLength", getCodeLength())
                .append("registrationRsp", getRegistrationRsp())
                .append("connIp", getConnIp())
                .append("connPort", getConnPort())
                .append("connUsername", getConnUsername())
                .append("connPwd", getConnPwd())
                .append("subscribeTopic", getSubscribeTopic())
                .append("publishTopic", getPublishTopic())
                .append("timeout", getTimeout())
                .append("scheduleInterval", getScheduleInterval())
                .append("onlineTime", getOnlineTime())
                .append("offlineTime", getOfflineTime())
                .append("isActive", getIsActive())
                .append("activeUpdateTime", getActiveUpdateTime())
                .append("connectAlarm", getConnectAlarm())
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
