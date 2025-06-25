package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.CommunicationDeviceArea;

import java.util.List;

/**
 * 通讯设备数据区域Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ICommunicationDeviceAreaService {
    /**
     * 查询通讯设备数据区域
     *
     * @param id 通讯设备数据区域主键
     * @return 通讯设备数据区域
     */
    public CommunicationDeviceArea selectCommunicationDeviceAreaById(Long id);

    /**
     * 查询通讯设备数据区域列表
     *
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 通讯设备数据区域集合
     */
    public List<CommunicationDeviceArea> selectCommunicationDeviceAreaList(CommunicationDeviceArea communicationDeviceArea);
}
