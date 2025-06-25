package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.CommunicationDeviceAreaMap;

/**
 * 通讯设备数据区域类型（公共）Service接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ICommunicationDeviceAreaMapService 
{
    /**
     * 查询通讯设备数据区域类型（公共）
     * 
     * @param id 通讯设备数据区域类型（公共）主键
     * @return 通讯设备数据区域类型（公共）
     */
    public CommunicationDeviceAreaMap selectCommunicationDeviceAreaMapById(Long id);

    /**
     * 查询通讯设备数据区域类型（公共）列表
     * 
     * @param communicationDeviceAreaMap 通讯设备数据区域类型（公共）
     * @return 通讯设备数据区域类型（公共）集合
     */
    public List<CommunicationDeviceAreaMap> selectCommunicationDeviceAreaMapList(CommunicationDeviceAreaMap communicationDeviceAreaMap);

    /**
     * 新增通讯设备数据区域类型（公共）
     * 
     * @param communicationDeviceAreaMap 通讯设备数据区域类型（公共）
     * @return 结果
     */
    public int insertCommunicationDeviceAreaMap(CommunicationDeviceAreaMap communicationDeviceAreaMap);

    /**
     * 修改通讯设备数据区域类型（公共）
     * 
     * @param communicationDeviceAreaMap 通讯设备数据区域类型（公共）
     * @return 结果
     */
    public int updateCommunicationDeviceAreaMap(CommunicationDeviceAreaMap communicationDeviceAreaMap);

    /**
     * 批量删除通讯设备数据区域类型（公共）
     * 
     * @param ids 需要删除的通讯设备数据区域类型（公共）主键集合
     * @return 结果
     */
    public int deleteCommunicationDeviceAreaMapByIds(Long[] ids);

    /**
     * 删除通讯设备数据区域类型（公共）信息
     * 
     * @param id 通讯设备数据区域类型（公共）主键
     * @return 结果
     */
    public int deleteCommunicationDeviceAreaMapById(Long id);
}
