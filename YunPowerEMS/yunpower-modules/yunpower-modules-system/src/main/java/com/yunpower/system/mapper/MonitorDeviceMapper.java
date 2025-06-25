package com.yunpower.system.mapper;

import com.yunpower.system.domain.MonitorDevice;

import java.util.List;

/**
 * 能源监控设备Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface MonitorDeviceMapper
{
    /**
     * 查询能源监控设备
     *
     * @param id 能源监控设备主键
     * @return 能源监控设备
     */
    public MonitorDevice selectMonitorDeviceById(Long id);

    /**
     * 查询能源监控设备
     *
     * @param deviseSn 能源监控设备编码
     * @return 能源监控设备
     */
    public MonitorDevice selectMonitorDeviceBySn(String deviseSn);

    /**
     * 查询最后一条记录
     */
    public MonitorDevice selectLastMonitorDevice(MonitorDevice monitorDevice);

    /**
     * 查询能源监控设备列表
     *
     * @param monitorDevice 能源监控设备
     * @return 能源监控设备集合
     */
    public List<MonitorDevice> selectMonitorDeviceList(MonitorDevice monitorDevice);

    /**
     * 新增能源监控设备
     *
     * @param monitorDevice 能源监控设备
     * @return 结果
     */
    public int insertMonitorDevice(MonitorDevice monitorDevice);

    /**
     * 修改能源监控设备
     *
     * @param monitorDevice 能源监控设备
     * @return 结果
     */
    public int updateMonitorDevice(MonitorDevice monitorDevice);

    /**
     * 查询该[设备]下是否有[变量]
     *
     * @param id 通讯设备ID
     * @return 结果
     */
    public int hasChildrenById(Long id);

    /**
     * 删除能源监控设备
     *
     * @param id 能源监控设备主键
     * @return 结果
     */
    public int deleteMonitorDeviceById(Long id);

    /**
     * 批量删除能源监控设备
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceByIds(Long[] ids);

    /**
     * 删除上传之外的设备
     */
    public int deleteMonitorDeviceBySns(MonitorDevice monitorDevice);

    /**
     * 根据部门ID查询所有离线的通讯设备
     * */
    public int statusAllByDeptId(Long deptId);

    /**
     * 根据部门ID查询所有通讯设备
     */
    public int selectDeviceCountByDeptId(Long deptId);
}
