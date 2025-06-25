package com.yunpower.collect.storage.domain;


import lombok.Data;

/**
 * 监控设备变量对象 monitor_device_var
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class MonitorDeviceVar extends BaseEntity
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

    /** 所属通道ID */
    private Long channelId;

    /** 所属通道编码 */
    private String channelSn;

    /** 所属通信设备ID */
    private Long comDeviceId;

    /** 所属通信设备编码 */
    private String comDeviceSn;

    /** 监控设备ID */
    private Long deviceId;

    /** 监控设备编码 */
    private String deviceSn;

    /** 变量名称 */
    private String varName;

    /** 变量编码 */
    private String varSn;

    /** 索引地图变量编码 */
    private String varMapSn;

    /** 变量单位 */
    private String unit;

    /** 是否监控 */
    private Integer isMonitor;

    /** 变量类型（1模拟量 2状态量） */
    private Integer varType;

    /** 数据来源（1IO型 2内存型） */
    private Integer origin;

    /** 数据区域 */
    private String registerName;

    /** 数据地址 */
    private Integer registerIndex;

    /** 信息体地址 */
    private Integer messageAddress;

    /** 读写类型（1只读 2只写 3读写） */
    private Integer rw;

    /** 计算公式 */
    private String computeFormula;

    /** 缺失值处理（1不计算 2使用最近值 3使用0值 4使用1值） */
    private Integer deletionHandle;

    /** 零值计算（0不计算 1计算） */
    private Integer zeroCompute;

    /** 补当前数据（0不补 1补） */
    private Integer repairData;

    /** 数据类型 */
    private Integer dataType;

    /** 初始赋值 */
    private Float initValue;

    /** 数据基值 */
    private Float baseValue;

    /** 数据系数 */
    private Float coefficient;

    /** 存盘周期（m）0不存盘 */
    private Integer saveCycle;

    /** 数据修正（0不操作 1上报原值 2上报0值） */
    private Integer dataFix;

    /** 是否累积量 */
    private Integer isAccumulation;

    /** 累积类型（1小时累积 2天累积） */
    private Integer accumulationType;

    /** 是否数据过滤（0否 1是） */
    private Integer isDataFilter;

    /** 绝对值大于 */
    private Float moreAbs;

    /** 替换值1 */
    private Float replaceValueUpper;

    /** 绝对值小于 */
    private Float lessAbs;

    /** 替换值2 */
    private Float replaceValueLower;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;

    /**
     * 变量值
     * */
    private Double dataValue;
}
