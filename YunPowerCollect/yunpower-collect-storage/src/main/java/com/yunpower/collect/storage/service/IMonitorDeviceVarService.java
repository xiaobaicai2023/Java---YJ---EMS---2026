package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.CommunicationDevice;
import com.yunpower.collect.storage.domain.MonitorDeviceVar;

import java.util.List;
import java.util.Map;

/**
 * 监控设备变量Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IMonitorDeviceVarService {
    /**
     * 查询监控设备变量
     *
     * @param id 监控设备变量主键
     * @return 监控设备变量
     */
    public MonitorDeviceVar selectMonitorDeviceVarById(Long id);

    /**
     * 查询监控设备变量列表
     *
     * @param monitorDeviceVar 监控设备变量
     * @return 监控设备变量集合
     */
    public List<MonitorDeviceVar> selectMonitorDeviceVarList(MonitorDeviceVar monitorDeviceVar);

    /**
     * 通过通讯设备查询变量信息
     * */
    public List<MonitorDeviceVar> selectMonitorDeviceVarListByChannelDevice(Long comDeviceId);

    /**
     * 获取通讯设备下，所有变量及寄存器地址
     *
     * @param deviceList 通讯设备列表
     * @return 结果
     */
    public Map<Long, Map<Integer, MonitorDeviceVar>> selectStorageVarList(List<CommunicationDevice> deviceList);
}
