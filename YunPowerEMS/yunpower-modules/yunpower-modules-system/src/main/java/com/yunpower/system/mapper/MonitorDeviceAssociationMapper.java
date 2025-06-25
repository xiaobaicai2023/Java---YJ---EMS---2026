package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.MonitorDeviceAssociation;

/**
 * 能源监控设备关联Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface MonitorDeviceAssociationMapper
{
    /**
     * 查询能源监控设备关联
     *
     * @param id 能源监控设备关联主键
     * @return 能源监控设备关联
     */
    public MonitorDeviceAssociation selectMonitorDeviceAssociationById(Long id);

    /**
     * 查询能源监控设备关联列表
     *
     * @param monitorDeviceAssociation 能源监控设备关联
     * @return 能源监控设备关联集合
     */
    public List<MonitorDeviceAssociation> selectMonitorDeviceAssociationList(MonitorDeviceAssociation monitorDeviceAssociation);

    /**
     * 新增能源监控设备关联
     *
     * @param monitorDeviceAssociation 能源监控设备关联
     * @return 结果
     */
    public int insertMonitorDeviceAssociation(MonitorDeviceAssociation monitorDeviceAssociation);

    /**
     * 修改能源监控设备关联
     *
     * @param monitorDeviceAssociation 能源监控设备关联
     * @return 结果
     */
    public int updateMonitorDeviceAssociation(MonitorDeviceAssociation monitorDeviceAssociation);

    /**
     * 删除能源监控设备关联
     *
     * @param id 能源监控设备关联主键
     * @return 结果
     */
    public int deleteMonitorDeviceAssociationById(Long id);

    /**
     * 批量删除能源监控设备关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMonitorDeviceAssociationByIds(Long[] ids);
}
