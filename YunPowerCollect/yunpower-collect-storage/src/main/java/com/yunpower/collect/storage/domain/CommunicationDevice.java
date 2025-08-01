package com.yunpower.collect.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.collect.storage.domain.handler.DevicesDataAreaTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.DevicesDataAreaVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 通讯设备对象 communication_device
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class CommunicationDevice extends BaseEntity
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

    /** 设备名称 */
    private String deviceName;

    /** 设备编码 */
    private String deviceSn;

    /** 所属通道ID */
    private Long channelId;

    /** 所属通道编码 */
    private String channelSn;

    /** 连接协议 */
    private String connectModel;

    /** 连接超时（s） */
    private Integer timeout;

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

    /** 设备地址（Modbus） */
    private Integer sbdz;

    /** 传送原因长度 */
    private Integer csyyLength;

    /** 公共地址（104） */
    private Integer ggdz;

    /** 公共地址长度 */
    private Integer ggdzLength;

    /** 信息体地址长度 */
    private Integer infoLength;

    /** 超时T0（s） */
    private Integer timeoutT0;

    /** 超时T1（s） */
    private Integer timeoutT1;

    /** 超时T2（s） */
    private Integer timeoutT2;

    /** 超时T3（s） */
    private Integer timeoutT3;

    /** 总召采集周期 */
    private Integer assCallCycle;

    /** 电度总召周期 */
    private Integer elcCallCycle;

    /** 数据区域 */
    @TableField(typeHandler = DevicesDataAreaTypeHandler.class)
    private List<DevicesDataAreaVo> dataArea;

    /** 产品类型（1通讯管理机 2DTU） */
    private Integer proType;

    /** 生产厂家 */
    private Integer proFactory;

    /** 产品版本 */
    private String proVer;

    /** 产品SN号 */
    private String proSn;

    /** 产品型号 */
    private String proModel;

    /** 运营商 */
    private Integer cardOperator;

    /** 物联卡号 */
    private String cardNo;

    /** IP地址 */
    private String cardIp;

    /** 流量标准 */
    private Integer cardFlow;

    /** 资费标准 */
    private Float cardPostage;

    /** 资费到期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date cardExpireDate;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;

}
