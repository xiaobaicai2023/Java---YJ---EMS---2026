package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.service.IPublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunpower.system.mapper.CommunicationDeviceAreaMapMapper;
import com.yunpower.system.domain.CommunicationDeviceAreaMap;
import com.yunpower.system.service.ICommunicationDeviceAreaMapService;

/**
 * 通讯设备数据区域类型（公共）Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class CommunicationDeviceAreaMapServiceImpl implements ICommunicationDeviceAreaMapService {
    @Autowired
    private CommunicationDeviceAreaMapMapper communicationDeviceAreaMapMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通讯设备数据区域类型（公共）
     *
     * @param id 通讯设备数据区域类型（公共）主键
     * @return 通讯设备数据区域类型（公共）
     */
    @Override
    public CommunicationDeviceAreaMap selectCommunicationDeviceAreaMapById(Long id) {
        return communicationDeviceAreaMapMapper.selectCommunicationDeviceAreaMapById(id);
    }

    /**
     * 查询通讯设备数据区域类型（公共）列表
     *
     * @param communicationDeviceAreaMap 通讯设备数据区域类型（公共）
     * @return 通讯设备数据区域类型（公共）
     */
    @Override
    public List<CommunicationDeviceAreaMap> selectCommunicationDeviceAreaMapList(CommunicationDeviceAreaMap communicationDeviceAreaMap) {
        return communicationDeviceAreaMapMapper.selectCommunicationDeviceAreaMapList(communicationDeviceAreaMap);
    }

    /**
     * 新增通讯设备数据区域类型（公共）
     *
     * @param communicationDeviceAreaMap 通讯设备数据区域类型（公共）
     * @return 结果
     */
    @Override
    public int insertCommunicationDeviceAreaMap(CommunicationDeviceAreaMap communicationDeviceAreaMap) {
        return communicationDeviceAreaMapMapper.insertCommunicationDeviceAreaMap(communicationDeviceAreaMap);
    }

    /**
     * 修改通讯设备数据区域类型（公共）
     *
     * @param communicationDeviceAreaMap 通讯设备数据区域类型（公共）
     * @return 结果
     */
    @Override
    public int updateCommunicationDeviceAreaMap(CommunicationDeviceAreaMap communicationDeviceAreaMap) {
        return communicationDeviceAreaMapMapper.updateCommunicationDeviceAreaMap(communicationDeviceAreaMap);
    }

    /**
     * 批量删除通讯设备数据区域类型（公共）
     *
     * @param ids 需要删除的通讯设备数据区域类型（公共）主键
     * @return 结果
     */
    @Override
    public int deleteCommunicationDeviceAreaMapByIds(Long[] ids) {
        return communicationDeviceAreaMapMapper.deleteCommunicationDeviceAreaMapByIds(ids);
    }

    /**
     * 删除通讯设备数据区域类型（公共）信息
     *
     * @param id 通讯设备数据区域类型（公共）主键
     * @return 结果
     */
    @Override
    public int deleteCommunicationDeviceAreaMapById(Long id) {
        return communicationDeviceAreaMapMapper.deleteCommunicationDeviceAreaMapById(id);
    }
}
