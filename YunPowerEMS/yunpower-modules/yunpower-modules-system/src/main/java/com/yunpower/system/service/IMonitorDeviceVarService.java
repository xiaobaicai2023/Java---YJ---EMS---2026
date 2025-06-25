package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.MonitorDeviceVar;

/**
 * 监控设备变量Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IMonitorDeviceVarService {
    /**
     * 查询监控设备变量
     *
     * @param id 监控设备变量主键
     * @return 监控设备变量
     */
    public MonitorDeviceVar selectMonitorDeviceVarById(Long id);

    /**
     * 获取最后一条记录
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
     * 修改监控设备变量状态
     *
     * @param id    监控设备变量主键
     * @param state 状态
     * @return 结果
     */
    public int updateMonitorDeviceVarState(MonitorDeviceVar monitorDeviceVar, Long id, Integer state);

    /**
     * 修改监控设备变量【监控状态】
     *
     * @param id    监控设备变量主键
     * @param state 状态
     * @return 结果
     */
    public int updateDeviceVarMonitorState(MonitorDeviceVar monitorDeviceVar, Long id, Integer state);

    /**
     * 批量删除监控设备变量
     *
     * @param ids 需要删除的监控设备变量主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceVarByIds(Long[] ids);

    /**
     * 删除监控设备变量信息
     *
     * @param id 监控设备变量主键
     * @return 结果
     */
    public int deleteMonitorDeviceVarById(Long id);

    /**
     * 导入监控设备变量
     *
     * @param deviceVarList   监控设备变量列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importDevice(List<MonitorDeviceVar> deviceVarList, Boolean isUpdateSupport, String operName);

    /**
     * 根据部门ID查询所有点位
     */
    public Integer selectDeviceVarCountByDeptId(Long deptId);

    /**
     * IO型变量，同一管理机下，信息体地址唯一
     */
    public boolean checkMessageAddressOnly(MonitorDeviceVar monitorDeviceVar);
}
