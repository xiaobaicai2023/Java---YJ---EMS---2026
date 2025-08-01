package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.CommunicationDeviceArea;
import com.yunpower.collect.storage.mapper.CommunicationDeviceAreaMapper;
import com.yunpower.collect.storage.service.ICommunicationDeviceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通讯设备数据区域Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class CommunicationDeviceAreaServiceImpl implements ICommunicationDeviceAreaService {
    @Autowired
    private CommunicationDeviceAreaMapper communicationDeviceAreaMapper;

    /**
     * 查询通讯设备数据区域
     *
     * @param id 通讯设备数据区域主键
     * @return 通讯设备数据区域
     */
    @Override
    public CommunicationDeviceArea selectCommunicationDeviceAreaById(Long id) {
        return communicationDeviceAreaMapper.selectCommunicationDeviceAreaById(id);
    }

    /**
     * 查询通讯设备数据区域列表
     *
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 通讯设备数据区域
     */
    @Override
    public List<CommunicationDeviceArea> selectCommunicationDeviceAreaList(CommunicationDeviceArea communicationDeviceArea) {
        return communicationDeviceAreaMapper.selectCommunicationDeviceAreaList(communicationDeviceArea);
    }
}
