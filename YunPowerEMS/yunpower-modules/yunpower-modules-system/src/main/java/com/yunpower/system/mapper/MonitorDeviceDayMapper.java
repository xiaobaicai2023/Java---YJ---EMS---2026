package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.MonitorDeviceDay;

/**
 * 监控设备日实时变量Mapper接口
 * 
 * @author JUNFU.WANG
 * @date 2023-12-16
 */
public interface MonitorDeviceDayMapper 
{
    /**
     * 查询监控设备日实时变量
     * 
     * @param id 监控设备日实时变量主键
     * @return 监控设备日实时变量
     */
    public MonitorDeviceDay selectMonitorDeviceDayById(Long id);

    /**
     * 查询监控设备日实时变量列表
     * 
     * @param monitorDeviceDay 监控设备日实时变量
     * @return 监控设备日实时变量集合
     */
    public List<MonitorDeviceDay> selectMonitorDeviceDayList(MonitorDeviceDay monitorDeviceDay);

    /**
     * 新增监控设备日实时变量
     * 
     * @param monitorDeviceDay 监控设备日实时变量
     * @return 结果
     */
    public int insertMonitorDeviceDay(MonitorDeviceDay monitorDeviceDay);

    /**
     * 修改监控设备日实时变量
     * 
     * @param monitorDeviceDay 监控设备日实时变量
     * @return 结果
     */
    public int updateMonitorDeviceDay(MonitorDeviceDay monitorDeviceDay);

    /**
     * 删除监控设备日实时变量
     * 
     * @param id 监控设备日实时变量主键
     * @return 结果
     */
    public int deleteMonitorDeviceDayById(Long id);

    /**
     * 批量删除监控设备日实时变量
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceDayByIds(Long[] ids);
}
