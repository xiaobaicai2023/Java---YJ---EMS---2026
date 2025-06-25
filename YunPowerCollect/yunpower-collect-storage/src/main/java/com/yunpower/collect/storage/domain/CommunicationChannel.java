package com.yunpower.collect.storage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 通讯通道对象 communication_channel
 *
 * @author JUNFU.WANG
 * @date 2023-10-11
 */
@Data
public class CommunicationChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    private Long id;

    /** 企业ID */
    private Long entId;

    /** 部门ID */
    private Long deptId;

    /** 电站类型（1配电 2光伏） */
    private Integer stationType;

    /** 通道名称 */
    private String channelName;

    /** 通道编码 */
    private String channelSn;

    /** 接入方式（1TCP 2MQTT） */
    private Integer accessType;

    /** IP地址 */
    private String ipAddress;

    /** 端口 */
    private Integer port;

    /** 通讯注册码 */
    private String registrationCode;

    /** 注册码起始位置 */
    private Integer codeStart;

    /** 注册码长度 */
    private Integer codeLength;

    /** 注册回应码 */
    private String registrationRsp;

    /** 连接地址 */
    private String connIp;

    /** 连接端口 */
    private Integer connPort;

    /** 连接帐号 */
    private String connUsername;

    /** 连接密码 */
    private String connPwd;

    /** 订阅主题 */
    private String subscribeTopic;

    /** 发布主题 */
    private String publishTopic;

    /** 连接超时（s） */
    private Integer timeout;

    /** 调度周期（ms） */
    private Integer scheduleInterval;

    /** 上线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date onlineTime;

    /** 下线时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date offlineTime;

    /** 是否激活 */
    private Integer isActive;

    /** 激活时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activeUpdateTime;

    /** 通道连接报警 */
    private Integer connectAlarm;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;
}
