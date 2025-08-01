package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.CommunicationDevice;

import java.util.List;

/**
 * 通讯设备Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface CommunicationDeviceMapper {
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
     * 修改通讯设备
     *
     * @param communicationDevice 通讯设备
     * @return 结果
     */
    public int updateCommunicationDevice(CommunicationDevice communicationDevice);
}
