package com.yunpower.system.service.impl;

import java.util.List;
import com.yunpower.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.MonitorDeviceDayMapper;
import com.yunpower.system.domain.MonitorDeviceDay;
import com.yunpower.system.service.IMonitorDeviceDayService;

/**
 * 监控设备日实时变量Service业务层处理
 * 
 * @author JUNFU.WANG
 * @date 2023-12-16
 */
@Service
public class MonitorDeviceDayServiceImpl implements IMonitorDeviceDayService 
{
    @Autowired
    private MonitorDeviceDayMapper monitorDeviceDayMapper;

    /**
     * 查询监控设备日实时变量
     * 
     * @param id 监控设备日实时变量主键
     * @return 监控设备日实时变量
     */
    @Override
    public MonitorDeviceDay selectMonitorDeviceDayById(Long id)
    {
        return monitorDeviceDayMapper.selectMonitorDeviceDayById(id);
    }

    /**
     * 查询监控设备日实时变量列表
     * 
     * @param monitorDeviceDay 监控设备日实时变量
     * @return 监控设备日实时变量
     */
    @Override
    public List<MonitorDeviceDay> selectMonitorDeviceDayList(MonitorDeviceDay monitorDeviceDay)
    {
        return monitorDeviceDayMapper.selectMonitorDeviceDayList(monitorDeviceDay);
    }

    /**
     * 新增监控设备日实时变量
     * 
     * @param monitorDeviceDay 监控设备日实时变量
     * @return 结果
     */
    @Override
    public int insertMonitorDeviceDay(MonitorDeviceDay monitorDeviceDay)
    {
        return monitorDeviceDayMapper.insertMonitorDeviceDay(monitorDeviceDay);
    }

    /**
     * 修改监控设备日实时变量
     * 
     * @param monitorDeviceDay 监控设备日实时变量
     * @return 结果
     */
    @Override
    public int updateMonitorDeviceDay(MonitorDeviceDay monitorDeviceDay)
    {
        monitorDeviceDay.setUpdateTime(DateUtils.getNowDate());
        return monitorDeviceDayMapper.updateMonitorDeviceDay(monitorDeviceDay);
    }

    /**
     * 批量删除监控设备日实时变量
     * 
     * @param ids 需要删除的监控设备日实时变量主键
     * @return 结果
     */
    @Override
    public int deleteMonitorDeviceDayByIds(Long[] ids)
    {
        return monitorDeviceDayMapper.deleteMonitorDeviceDayByIds(ids);
    }

    /**
     * 删除监控设备日实时变量信息
     * 
     * @param id 监控设备日实时变量主键
     * @return 结果
     */
    @Override
    public int deleteMonitorDeviceDayById(Long id)
    {
        return monitorDeviceDayMapper.deleteMonitorDeviceDayById(id);
    }
}
