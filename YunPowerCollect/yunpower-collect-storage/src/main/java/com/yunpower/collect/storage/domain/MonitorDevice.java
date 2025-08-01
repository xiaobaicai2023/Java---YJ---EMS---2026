package com.yunpower.collect.storage.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yunpower.collect.storage.domain.handler.AssociatedDevicesTypeHandler;
import com.yunpower.collect.storage.domain.jsonvo.AssociatedDevicesVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 能源监控设备对象 monitor_device
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class MonitorDevice extends BaseEntity
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

    /** 电能类型（1用电 2发电 3并网 4购电） */
    private Integer electricType;

    /** 所属通道ID */
    private Long channelId;

    /** 所属通道编码 */
    private String channelSn;

    /** 所属通信设备ID */
    private Long comDeviceId;

    /** 所属通信设备编码 */
    private String comDeviceSn;

    /** 设备名称 */
    private String deviceName;

    /** 设备编号 */
    private String deviceSn;

    /** 设备地址 */
    private Integer sbdz;

    /** 电表表号 */
    private String meterNumber;

    /** 设备分组 */
    private Long groupId;

    /** 是否关口表 */
    private Integer isPass;

    /** 生产厂家 */
    private Long proFactory;

    /** 设备类型 */
    private Long proType;

    /** 产品版本 */
    private String proVer;

    /** 购买价格 */
    private Float buyPrice;

    /** 产品SN号 */
    private String proSn;

    /** 产品型号 */
    private String proModel;

    /** 安规国家 */
    private String agStandard;

    /** 额定电压 */
    private Float ratedVol;

    /** 额定电流 */
    private Float ratedEle;

    /** 额定功率 */
    private Float ratedPower;

    /** 电流负载率 */
    private Float eleLoadRate;

    /** 电能质量分析 */
    private Integer isAnalysisEnergy;

    /** 关联监控 */
    private Integer monitorId;

    /** 发电属性 */
    private String electricAttribute;

    /** 温度属性 */
    private String temperatureAttribute;

    /** 其它属性 */
    private String ortherAttribute;

    /** 是否抄表 */
    private Integer isReading;

    /** 组件容量 */
    private Float ratedVolume;

    /** 方位角度 */
    private Float azimuth;

    /** 倾角角度 */
    private Float dipAngle;

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;

    /** 使用年限 */
    private Integer useLife;

    /** 性能衰减 */
    private Float attenuationRate;

    /** 抄表变量 */
    private Integer readingVar;

    /** DTU端口 */
    private Integer dtuCmd;

    /** 绑定用户 */
    private Integer bindUserId;

    /** 付费模式 */
    private Integer chargingType;

    /** 付费规则 */
    private Integer ruleId;

    /** 付费周期 */
    private Integer checkoutType;

    /** 关联设备（JSON格式） */
    @TableField(typeHandler = AssociatedDevicesTypeHandler.class)
    private List<AssociatedDevicesVo> associatedDevices;

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

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;
}
