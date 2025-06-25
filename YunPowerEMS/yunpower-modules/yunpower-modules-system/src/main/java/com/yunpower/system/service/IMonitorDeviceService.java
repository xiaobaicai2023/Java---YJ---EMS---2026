package com.yunpower.system.service;

import com.yunpower.system.domain.MonitorDevice;
import com.yunpower.system.api.domain.SysGroup;

import java.util.List;

/**
 * 能源监控设备Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IMonitorDeviceService {
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
     * 获取最后一条记录
     */
    public MonitorDevice selectLastMonitorDevice(MonitorDevice monitorDevice);

    /**
     * 查询能源监控设备列表（融合分组）
     *
     * @param monitorDevice 能源监控设备
     * @return 能源监控设备集合
     */
    public List<MonitorDevice> selectMonitorDeviceListFusionGroup(MonitorDevice monitorDevice);

    /**
     * 查询能源监控设备列表
     *
     * @param monitorDevice 能源监控设备
     * @return 能源监控设备集合
     */
    public List<SysGroup> selectMonitorDeviceListForGroup(MonitorDevice monitorDevice);

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
     * 修改能源监控设备状态
     *
     * @param id    能源监控设备主键
     * @param state 状态
     * @return 结果
     */
    public int updateMonitorDeviceState(MonitorDevice monitorDevice, Long id, Integer state);

    /**
     * 查询该[设备]下是否有[变量]
     *
     * @param id 设备ID
     * @return 结果
     */
    public boolean hasChildrenById(Long id);

    /**
     * 批量删除能源监控设备
     *
     * @param ids 需要删除的能源监控设备主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceByIds(Long[] ids);

    /**
     * 删除能源监控设备信息
     *
     * @param id 能源监控设备主键
     * @return 结果
     */
    public int deleteMonitorDeviceById(Long id);

    /**
     * 导入能源监控设备
     *
     * @param deviceList      监控设备数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importDevice(List<MonitorDevice> deviceList, Boolean isUpdateSupport, String operName);

    /**
     * 通过部门ID查询所有监控状态
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public boolean statusAllByDeptId(Long deptId);

    /**
     * 通过部门ID查询设备数量
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public Integer selectDeviceCountByDeptId(Long deptId);
}
