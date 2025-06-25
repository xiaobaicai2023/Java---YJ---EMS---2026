package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.CommunicationDevice;
import com.yunpower.collect.storage.mapper.CommunicationDeviceMapper;
import com.yunpower.collect.storage.service.ICommunicationDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 通讯设备Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class CommunicationDeviceServiceImpl implements ICommunicationDeviceService {
    @Autowired
    private CommunicationDeviceMapper communicationDeviceMapper;

    /**
     * 查询通讯设备
     *
     * @param id 通讯设备主键
     * @return 通讯设备
     */
    @Override
    public CommunicationDevice selectCommunicationDeviceById(Long id) {
        return communicationDeviceMapper.selectCommunicationDeviceById(id);
    }

    /**
     * 查询通讯设备列表
     *
     * @param communicationDevice 通讯设备
     * @return 通讯设备
     */
    @Override
    public List<CommunicationDevice> selectCommunicationDeviceList(CommunicationDevice communicationDevice) {
        return communicationDeviceMapper.selectCommunicationDeviceList(communicationDevice);
    }

    @Override
    public List<CommunicationDevice> selectCommunicationDeviceList() {
        CommunicationDevice communicationDevice = new CommunicationDevice();
        return communicationDeviceMapper.selectCommunicationDeviceList(communicationDevice);
    }

    /**
     * 更新状态
     *
     * @param id    通道ID
     * @param state 当前状态
     */
    @Override
    public void changeState(Long id, Integer state, Date changeTime){
        CommunicationDevice device = new CommunicationDevice();
        device.setId(id);
        device.setIsActive(state);
        if(state == 0){
            device.setOfflineTime(changeTime);
        }else{
            device.setOnlineTime(changeTime);
            device.setActiveUpdateTime(changeTime);
        }
        device.setUpdateBy("MQ_SYSTEM");
        device.setUpdateTime(new Date());
        communicationDeviceMapper.updateCommunicationDevice(device);
    }
}
