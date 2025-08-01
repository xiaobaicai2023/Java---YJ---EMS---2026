package com.yunpower.system.mapper;

import com.yunpower.system.domain.CommunicationDevice;

import java.util.List;

/**
 * 通讯设备Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface CommunicationDeviceMapper
{
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
    public int hasChildrenById(Long id);

    /**
     * 根据部门ID查询所有离线的通讯设备
     * */
    public int statusAllByDeptId(Long deptId);
}
