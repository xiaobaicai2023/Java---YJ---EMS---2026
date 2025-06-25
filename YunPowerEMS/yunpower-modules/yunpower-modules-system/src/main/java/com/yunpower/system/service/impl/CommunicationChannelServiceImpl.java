package com.yunpower.system.service.impl;

import java.util.List;

import com.yunpower.system.domain.CommunicationChannel;
import com.yunpower.system.mapper.CommunicationChannelMapper;
import com.yunpower.system.service.ICommunicationChannelService;
import com.yunpower.system.service.IPublicService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通讯通道Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Service
public class CommunicationChannelServiceImpl implements ICommunicationChannelService {
    @Autowired
    private CommunicationChannelMapper communicationChannelMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询通讯通道
     *
     * @param id 通讯通道主键
     * @return 通讯通道
     */
    @Override
    public CommunicationChannel selectCommunicationChannelById(Long id) {
        return communicationChannelMapper.selectCommunicationChannelById(id);
    }

    /**
     * 通过注册码获取通道信息
     *
     * @param channelSn 注册码
     * @return 通讯通道
     */
    @Override
    public CommunicationChannel selectCommunicationChannelBySn(String channelSn) {
        return communicationChannelMapper.selectCommunicationChannelBySn(channelSn);
    }

    /**
     * 通过注册码获取通道信息
     *
     * @param registrationCode 注册码
     * @return 通讯通道
     */
    @Override
    public CommunicationChannel selectCommunicationChannelByRegisterCode(String registrationCode) {
        return communicationChannelMapper.selectCommunicationChannelByRegisterCode(registrationCode);
    }

    /**
     * 查询通讯通道列表
     *
     * @param communicationChannel 通讯通道
     * @return 通讯通道
     */
    @Override
    public List<CommunicationChannel> selectCommunicationChannelList(CommunicationChannel communicationChannel) {
        //获取数据加上 deptid==当前站点ID
        //解释：数据权限控制用户能看哪些数据（需要DataScope注解），而deptid=?是控制页面显示哪些数据（需要在where里加）
        if (communicationChannel.getDeptId() == null || communicationChannel.getDeptId() <= 0) {
            communicationChannel.setDeptId(publicService.getCurrentStation());
        }
        return communicationChannelMapper.selectCommunicationChannelList(communicationChannel);
    }

    /**
     * 新增通讯通道
     *
     * @param communicationChannel 通讯通道
     * @return 结果
     */
    @Override
    public int insertCommunicationChannel(CommunicationChannel communicationChannel) {
        if (communicationChannel.getEntId() == null || communicationChannel.getEntId() <= 0) {
            communicationChannel.setEntId(publicService.getCurrentEnterprise());
        }
        if (communicationChannel.getDeptId() == null || communicationChannel.getDeptId() <= 0) {
            communicationChannel.setDeptId(publicService.getCurrentStation());
        }
        communicationChannel.setCreateBy(SecurityUtils.getNickName());
        communicationChannel.setCreateTime(DateUtils.getNowDate());
        if (communicationChannel.getStopFlag() == null) {
            communicationChannel.setStopFlag(0);
        }
        communicationChannel.setDeleteFlag(0);
        return communicationChannelMapper.insertCommunicationChannel(communicationChannel);
    }

    /**
     * 修改通讯通道
     *
     * @param communicationChannel 通讯通道
     * @return 结果
     */
    @Override
    public int updateCommunicationChannel(CommunicationChannel communicationChannel) {
        communicationChannel.setUpdateBy(SecurityUtils.getNickName());
        communicationChannel.setUpdateTime(DateUtils.getNowDate());
        return communicationChannelMapper.updateCommunicationChannel(communicationChannel);
    }

    /**
     * 修改通讯通道状态
     *
     * @param id    通讯通道主键
     * @param state 状态
     * @return 结果
     */
    @Override
    public int updateCommunicationChannelState(Long id, Integer state) {
        CommunicationChannel communicationChannel = new CommunicationChannel();
        communicationChannel.setId(id);
        communicationChannel.setStopFlag(state);
        communicationChannel.setUpdateBy(SecurityUtils.getNickName());
        communicationChannel.setUpdateTime(DateUtils.getNowDate());
        return communicationChannelMapper.updateCommunicationChannel(communicationChannel);
    }

    /**
     * 查询该[通道]下是否有[通讯设备]
     *
     * @param id 通讯通道ID
     * @return 结果
     */
    @Override
    public boolean hasChildrenById(Long id) {
        return communicationChannelMapper.hasChildrenById(id) > 0;
    }

    /**
     * 批量删除通讯通道
     *
     * @param ids 需要删除的通讯通道主键
     * @return 结果
     */
    @Override
    public int deleteCommunicationChannelByIds(Long[] ids) {
        return communicationChannelMapper.deleteCommunicationChannelByIds(ids);
    }

    /**
     * 删除通讯通道信息
     *
     * @param id 通讯通道主键
     * @return 结果
     */
    @Override
    public int deleteCommunicationChannelById(Long id) {
        return communicationChannelMapper.deleteCommunicationChannelById(id);
    }
}
