package com.yunpower.collect.storage.domain;


import lombok.Data;

/**
 * 通讯设备数据区域对象 communication_device_area
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Data
public class CommunicationDeviceArea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号ID */
    private Long id;

    /** 设备ID */
    private Long deviceId;

    /** 设备编码 */
    private String deviceSn;

    /** 区域名称 */
    private String areaName;

    /** 起始地址 */
    private Integer addStart;

    /** 长度 */
    private Integer addLength;

    /** 寄存器 */
    private String area;

    /** 读写（1只读 2只写 3读写） */
    private Integer rw;

    /** 是否停用（0正常 1停用） */
    private Integer stopFlag;

    /** 是否删除（0正常 1删除） */
    private Integer deleteFlag;
}
