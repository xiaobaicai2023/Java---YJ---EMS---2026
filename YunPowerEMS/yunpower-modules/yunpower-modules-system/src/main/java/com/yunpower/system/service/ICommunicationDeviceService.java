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
     * 查询该[通讯设备]下是否有[用电设备]
     *
     * @param id 通讯设备ID
     * @return 结果
     */
    public boolean hasChildrenById(Long id);

    /**
     * 通过部门ID查询所有通讯机器状态
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean statusAllByDeptId(Long deptId);
}
