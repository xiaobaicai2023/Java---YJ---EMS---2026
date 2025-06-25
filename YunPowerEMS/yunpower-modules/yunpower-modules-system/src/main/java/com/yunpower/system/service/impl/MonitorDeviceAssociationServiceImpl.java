package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.MonitorDeviceAssociationMapper;
import com.yunpower.system.domain.MonitorDeviceAssociation;
import com.yunpower.system.service.IMonitorDeviceAssociationService;

/**
 * 能源监控设备关联Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class MonitorDeviceAssociationServiceImpl implements IMonitorDeviceAssociationService {
    @Autowired
    private MonitorDeviceAssociationMapper monitorDeviceAssociationMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询能源监控设备关联
     *
     * @param id 能源监控设备关联主键
     * @return 能源监控设备关联
     */
    @Override
    public MonitorDeviceAssociation selectMonitorDeviceAssociationById(Long id) {
        return monitorDeviceAssociationMapper.selectMonitorDeviceAssociationById(id);
    }

    /**
     * 查询能源监控设备关联列表
     *
     * @param monitorDeviceAssociation 能源监控设备关联
     * @return 能源监控设备关联
     */
    @Override
    public List<MonitorDeviceAssociation> selectMonitorDeviceAssociationList(MonitorDeviceAssociation monitorDeviceAssociation) {
        return monitorDeviceAssociationMapper.selectMonitorDeviceAssociationList(monitorDeviceAssociation);
    }

    /**
     * 新增能源监控设备关联
     *
     * @param monitorDeviceAssociation 能源监控设备关联
     * @return 结果
     */
    @Override
    public int insertMonitorDeviceAssociation(MonitorDeviceAssociation monitorDeviceAssociation) {
        return monitorDeviceAssociationMapper.insertMonitorDeviceAssociation(monitorDeviceAssociation);
    }

    /**
     * 修改能源监控设备关联
     *
     * @param monitorDeviceAssociation 能源监控设备关联
     * @return 结果
     */
    @Override
    public int updateMonitorDeviceAssociation(MonitorDeviceAssociation monitorDeviceAssociation) {
        return monitorDeviceAssociationMapper.updateMonitorDeviceAssociation(monitorDeviceAssociation);
    }

    /**
     * 批量删除能源监控设备关联
     *
     * @param ids 需要删除的能源监控设备关联主键
     * @return 结果
     */
    @Override
    public int deleteMonitorDeviceAssociationByIds(Long[] ids) {
        return monitorDeviceAssociationMapper.deleteMonitorDeviceAssociationByIds(ids);
    }

    /**
     * 删除能源监控设备关联信息
     *
     * @param id 能源监控设备关联主键
     * @return 结果
     */
    @Override
    public int deleteMonitorDeviceAssociationById(Long id) {
        return monitorDeviceAssociationMapper.deleteMonitorDeviceAssociationById(id);
    }
}
