package com.yunpower.system.domain.jsonvo;

/**
 * @title: 通讯设备数据区域
 * @Author: Jiajiaglam
 * @date: 2024-01-08 13:59
 * @description:
 */
public class DevicesDataAreaVo {
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 起始地址
     */
    private Integer addStart;

    /**
     * 长度
     */
    private Integer addLength;

    /**
     * 寄存器
     */
    private String area;

    /**
     * 读写（1只读 2只写 3读写）
     */
    private Integer rw;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAddStart() {
        return addStart;
    }

    public void setAddStart(Integer addStart) {
        this.addStart = addStart;
    }

    public Integer getAddLength() {
        return addLength;
    }

    public void setAddLength(Integer addLength) {
        this.addLength = addLength;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getRw() {
        return rw;
    }

    public void setRw(Integer rw) {
        this.rw = rw;
    }
}
