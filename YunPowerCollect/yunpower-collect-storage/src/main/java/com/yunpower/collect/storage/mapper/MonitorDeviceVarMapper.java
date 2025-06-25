package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.MonitorDeviceVar;

import java.util.List;

/**
 * 监控设备变量Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface MonitorDeviceVarMapper {
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
     * 通过通讯设备ID获取变量列表
     * @param comDeviceId 通讯设备ID
     * @return 结果
     */
    public List<MonitorDeviceVar> selectMonitorDeviceVarListByChannelDevice(Long comDeviceId);
}
