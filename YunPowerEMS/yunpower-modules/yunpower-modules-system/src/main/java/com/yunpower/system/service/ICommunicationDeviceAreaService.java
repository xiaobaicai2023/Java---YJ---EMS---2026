package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.CommunicationDeviceArea;

/**
 * 通讯设备数据区域Service接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ICommunicationDeviceAreaService 
{
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

    /**
     * 新增通讯设备数据区域
     * 
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 结果
     */
    public int insertCommunicationDeviceArea(CommunicationDeviceArea communicationDeviceArea);

    /**
     * 修改通讯设备数据区域
     * 
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 结果
     */
    public int updateCommunicationDeviceArea(CommunicationDeviceArea communicationDeviceArea);

    /**
     * 修改通讯设备数据区域状态
     *
     * @param id 通讯设备数据区域主键
     * @param state 状态
     * @return 结果
     */
    public int updateCommunicationDeviceAreaState(Long id, Integer state);

    /**
     * 批量删除通讯设备数据区域
     * 
     * @param ids 需要删除的通讯设备数据区域主键集合
     * @return 结果
     */
    public int deleteCommunicationDeviceAreaByIds(Long[] ids);

    /**
     * 删除通讯设备数据区域信息
     * 
     * @param id 通讯设备数据区域主键
     * @return 结果
     */
    public int deleteCommunicationDeviceAreaById(Long id);
}
