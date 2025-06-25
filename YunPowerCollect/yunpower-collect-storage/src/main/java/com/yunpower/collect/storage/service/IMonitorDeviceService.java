package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.MonitorDevice;

import java.util.Date;
import java.util.List;

/**
 * 能源监控设备Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IMonitorDeviceService {
    /**
     * 查询能源监控设备
     *
     * @param id 能源监控设备主键
     * @return 能源监控设备
     */
    public MonitorDevice selectMonitorDeviceById(Long id);

    /**
     * 查询能源监控设备列表
     *
     * @param monitorDevice 能源监控设备
     * @return 能源监控设备集合
     */
    public List<MonitorDevice> selectMonitorDeviceList(MonitorDevice monitorDevice);

    /**
     * 查询能源监控设备列表
     *
     * @return 能源监控设备集合
     */
    public List<MonitorDevice> selectMonitorDeviceList();

    /**
     * 更新状态
     *
     * @param id    通道ID
     * @param state 当前状态
     */
    void changeState(Long id, Integer state, Date changeTime);
}
