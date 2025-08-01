package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.CommunicationDevice;

import java.util.Date;
import java.util.List;

/**
 * 通讯设备Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ICommunicationDeviceService {
    /**
     * 查询通讯设备
     *
     * @param id 通讯设备主键
     * @return 通讯设备
     */
    public CommunicationDevice selectCommunicationDeviceById(Long id);

    /**
     * 查询通讯设备列表
     *
     * @param communicationDevice 通讯设备
     * @return 通讯设备集合
     */
    public List<CommunicationDevice> selectCommunicationDeviceList(CommunicationDevice communicationDevice);

    /**
     * 查询通讯设备列表
     *
     * @return 通讯设备集合
     */
    public List<CommunicationDevice> selectCommunicationDeviceList();

    /**
     * 更新状态
     *
     * @param id    通道ID
     * @param state 当前状态
     */
    void changeState(Long id, Integer state, Date changeTime);
}
