package com.yunpower.system.service;

import com.yunpower.system.domain.CommunicationDevice;

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
     * 根据设备sn查询设备
     *
     * @param deviceSn 设备sn
     * @return 设备
     */
    public CommunicationDevice selectCommunicationDeviceBySn(String deviceSn);

    /**
     * 查询通讯设备列表
     *
     * @param communicationDevice 通讯设备
     * @return 通讯设备集合
     */
    public List<CommunicationDevice> selectCommunicationDeviceList(CommunicationDevice communicationDevice);

    /**
     * 新增通讯设备
     *
     * @param communicationDevice 通讯设备
     * @return 结果
     */
    public int insertCommunicationDevice(CommunicationDevice communicationDevice);

    /**
     * 修改通讯设备
     *
     * @param communicationDevice 通讯设备
     * @return 结果
     */
    public int updateCommunicationDevice(CommunicationDevice communicationDevice);

    /**
     * 修改通讯设备状态
     *
     * @param id    通讯设备主键
     * @param state 状态
     * @return 结果
     */
    public int updateCommunicationDeviceState(CommunicationDevice communicationDevice, Long id, Integer state);

    /**
     * 查询该[通讯设备]下是否有[用电设备]
     *
     * @param id 通讯设备ID
     * @return 结果
     */
    public boolean hasChildrenById(Long id);

    /**
     * 批量删除通讯设备
     *
     * @param ids 需要删除的通讯设备主键集合
     * @return 结果
     */
    public int deleteCommunicationDeviceByIds(CommunicationDevice communicationDevice, Long[] ids);

    /**
     * 删除通讯设备信息
     *
     * @param id 通讯设备主键
     * @return 结果
     */
    public int deleteCommunicationDeviceById(CommunicationDevice communicationDevice, Long id);

    /**
     * 通过部门ID查询所有通讯机器状态
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean statusAllByDeptId(Long deptId);
}
