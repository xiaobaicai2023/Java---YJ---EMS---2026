package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.MonitorDevice;

import java.util.List;

/**
 * 能源监控设备Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface MonitorDeviceMapper {
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
     * 修改能源监控设备
     *
     * @param monitorDevice 能源监控设备
     * @return 结果
     */
    public int updateMonitorDevice(MonitorDevice monitorDevice);
}
