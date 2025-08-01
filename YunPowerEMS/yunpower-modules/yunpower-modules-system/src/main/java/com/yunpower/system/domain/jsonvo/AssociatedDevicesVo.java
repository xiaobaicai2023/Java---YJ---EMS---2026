package com.yunpower.system.domain.jsonvo;

/**
 * @title: 关联设备
 * @Author: Jiajiaglam
 * @date: 2023-12-06 8:16
 * @description: 设备和变量共用此类
 */
public class AssociatedDevicesVo {
    /** 设备ID，变量ID */
    private Long id;

    /** 设备名称，变量名称 */
    private String deviceName;

    /** 设备编码，变量编码 */
    private String deviceSn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }
}
