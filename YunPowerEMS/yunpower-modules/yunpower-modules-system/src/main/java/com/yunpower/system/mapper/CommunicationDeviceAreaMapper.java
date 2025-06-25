package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.CommunicationDeviceArea;

/**
 * 通讯设备数据区域Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface CommunicationDeviceAreaMapper
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
     * 删除通讯设备数据区域
     *
     * @param id 通讯设备数据区域主键
     * @return 结果
     */
    public int deleteCommunicationDeviceAreaById(Long id);

    /**
     * 批量删除通讯设备数据区域
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCommunicationDeviceAreaByIds(Long[] ids);
}
