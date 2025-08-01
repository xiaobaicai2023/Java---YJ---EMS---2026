package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.MonitorDevice;
import com.yunpower.collect.storage.mapper.MonitorDeviceMapper;
import com.yunpower.collect.storage.service.IMonitorDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 能源监控设备Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class MonitorDeviceServiceImpl implements IMonitorDeviceService {
    @Autowired
    private MonitorDeviceMapper monitorDeviceMapper;

    /**
     * 查询能源监控设备
     *
     * @param id 能源监控设备主键
     * @return 能源监控设备
     */
    @Override
    public MonitorDevice selectMonitorDeviceById(Long id) {
        return monitorDeviceMapper.selectMonitorDeviceById(id);
    }

    /**
     * 查询能源监控设备列表
     *
     * @param monitorDevice 能源监控设备
     * @return 能源监控设备
     */
    @Override
    public List<MonitorDevice> selectMonitorDeviceList(MonitorDevice monitorDevice) {
        return monitorDeviceMapper.selectMonitorDeviceList(monitorDevice);
    }

    /**
     * 查询能源监控设备列表
     *
     * @return 能源监控设备
     */
    @Override
    public List<MonitorDevice> selectMonitorDeviceList() {
        MonitorDevice monitorDevice = new MonitorDevice();
        return monitorDeviceMapper.selectMonitorDeviceList(monitorDevice);
    }


    /**
     * 更新状态
     *
     * @param id    通道ID
     * @param state 当前状态
     */
    @Override
    public void changeState(Long id, Integer state, Date changeTime){
        MonitorDevice monitorDevice = new MonitorDevice();
        monitorDevice.setId(id);
        monitorDevice.setIsActive(state);
        if(state == 0){
            monitorDevice.setOfflineTime(changeTime);
        }else{
            monitorDevice.setOnlineTime(changeTime);
            monitorDevice.setActiveUpdateTime(changeTime);
        }
        monitorDevice.setUpdateBy("MQ_SYSTEM");
        monitorDevice.setUpdateTime(new Date());
        monitorDeviceMapper.updateMonitorDevice(monitorDevice);
    }
}
