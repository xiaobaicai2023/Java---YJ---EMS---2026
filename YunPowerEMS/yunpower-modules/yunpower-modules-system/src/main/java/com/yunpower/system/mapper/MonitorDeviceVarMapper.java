package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.MonitorDeviceVar;

/**
 * 监控设备变量Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface MonitorDeviceVarMapper
{
    /**
     * 查询监控设备变量
     *
     * @param id 监控设备变量主键
     * @return 监控设备变量
     */
    public MonitorDeviceVar selectMonitorDeviceVarById(Long id);

    /**
     * 查询监控设备变量，查询最新一条数据
     */
    public MonitorDeviceVar selectLastMonitorDeviceVar(MonitorDeviceVar monitorDeviceVar);

    /**
     * 通过变量编码查询监控设备变量
     *
     * @param varSn 监控设备变量编码
     * @return 监控设备变量
     */
    public MonitorDeviceVar selectMonitorDeviceVarByVarSn(String varSn);

    /**
     * 查询监控设备变量列表
     *
     * @param monitorDeviceVar 监控设备变量
     * @return 监控设备变量集合
     */
    public List<MonitorDeviceVar> selectMonitorDeviceVarList(MonitorDeviceVar monitorDeviceVar);

    /**
     * 新增监控设备变量
     *
     * @param monitorDeviceVar 监控设备变量
     * @return 结果
     */
    public int insertMonitorDeviceVar(MonitorDeviceVar monitorDeviceVar);

    /**
     * 修改监控设备变量
     *
     * @param monitorDeviceVar 监控设备变量
     * @return 结果
     */
    public int updateMonitorDeviceVar(MonitorDeviceVar monitorDeviceVar);

    /**
     * 删除监控设备变量
     *
     * @param id 监控设备变量主键
     * @return 结果
     */
    public int deleteMonitorDeviceVarById(Long id);

    /**
     * 批量删除监控设备变量
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceVarByIds(Long[] ids);

    /**
     * 删除上传之外的设备
     */
    public int deleteMonitorDeviceBySns(MonitorDeviceVar monitorDeviceVar);

    /**
     * 根据部门ID查询所有点位
     */
    public int selectDeviceVarCountByDeptId(Long deptId);

    /**
     * IO型变量，同一管理机下，信息体地址唯一
     */
    public MonitorDeviceVar checkMessageAddressOnly(MonitorDeviceVar monitorDeviceVar);
}
