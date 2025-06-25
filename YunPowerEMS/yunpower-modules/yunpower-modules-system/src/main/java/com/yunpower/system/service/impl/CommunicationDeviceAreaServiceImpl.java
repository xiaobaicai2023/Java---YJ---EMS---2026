package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.domain.CommunicationDeviceArea;
import com.yunpower.system.mapper.CommunicationDeviceAreaMapper;
import com.yunpower.system.service.ICommunicationDeviceAreaService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通讯设备数据区域Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class CommunicationDeviceAreaServiceImpl implements ICommunicationDeviceAreaService {
    @Autowired
    private CommunicationDeviceAreaMapper communicationDeviceAreaMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通讯设备数据区域
     *
     * @param id 通讯设备数据区域主键
     * @return 通讯设备数据区域
     */
    @Override
    public CommunicationDeviceArea selectCommunicationDeviceAreaById(Long id) {
        return communicationDeviceAreaMapper.selectCommunicationDeviceAreaById(id);
    }

    /**
     * 查询通讯设备数据区域列表
     *
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 通讯设备数据区域
     */
    @Override
    public List<CommunicationDeviceArea> selectCommunicationDeviceAreaList(CommunicationDeviceArea communicationDeviceArea) {
        return communicationDeviceAreaMapper.selectCommunicationDeviceAreaList(communicationDeviceArea);
    }

    /**
     * 新增通讯设备数据区域
     *
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 结果
     */
    @Override
    public int insertCommunicationDeviceArea(CommunicationDeviceArea communicationDeviceArea) {
        communicationDeviceArea.setCreateBy(SecurityUtils.getNickName());
        communicationDeviceArea.setCreateTime(DateUtils.getNowDate());
        if (communicationDeviceArea.getStopFlag() == null) {
            communicationDeviceArea.setStopFlag(0);
        }
        communicationDeviceArea.setDeleteFlag(0);
        return communicationDeviceAreaMapper.insertCommunicationDeviceArea(communicationDeviceArea);
    }

    /**
     * 修改通讯设备数据区域
     *
     * @param communicationDeviceArea 通讯设备数据区域
     * @return 结果
     */
    @Override
    public int updateCommunicationDeviceArea(CommunicationDeviceArea communicationDeviceArea) {
        communicationDeviceArea.setUpdateBy(SecurityUtils.getNickName());
        communicationDeviceArea.setUpdateTime(DateUtils.getNowDate());
        return communicationDeviceAreaMapper.updateCommunicationDeviceArea(communicationDeviceArea);
    }

    /**
     * 修改通讯设备数据区域状态
     *
     * @param id    通讯设备数据区域主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateCommunicationDeviceAreaState(Long id, Integer state) {
        CommunicationDeviceArea communicationDeviceArea = new CommunicationDeviceArea();
        communicationDeviceArea.setId(id);
        communicationDeviceArea.setStopFlag(state);
        communicationDeviceArea.setUpdateBy(SecurityUtils.getNickName());
        communicationDeviceArea.setUpdateTime(DateUtils.getNowDate());
        return communicationDeviceAreaMapper.updateCommunicationDeviceArea(communicationDeviceArea);
    }

    /**
     * 批量删除通讯设备数据区域
     *
     * @param ids 需要删除的通讯设备数据区域主键
     * @return 结果
     */
    @Override
    public int deleteCommunicationDeviceAreaByIds(Long[] ids) {
        return communicationDeviceAreaMapper.deleteCommunicationDeviceAreaByIds(ids);
    }

    /**
     * 删除通讯设备数据区域信息
     *
     * @param id 通讯设备数据区域主键
     * @return 结果
     */
    @Override
    public int deleteCommunicationDeviceAreaById(Long id) {
        return communicationDeviceAreaMapper.deleteCommunicationDeviceAreaById(id);
    }
}
