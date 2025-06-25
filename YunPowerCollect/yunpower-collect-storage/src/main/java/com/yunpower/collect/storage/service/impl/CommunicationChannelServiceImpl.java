package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.CommunicationChannel;
import com.yunpower.collect.storage.mapper.CommunicationChannelMapper;
import com.yunpower.collect.storage.service.ICommunicationChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
     * 查询通讯通道列表
     *
     * @param communicationChannel 通讯通道
     * @return 通讯通道
     */
    @Override
    public List<CommunicationChannel> selectCommunicationChannelList(CommunicationChannel communicationChannel) {
        return communicationChannelMapper.selectCommunicationChannelList(communicationChannel);
    }

    /**
     * 查询通讯通道列表
     *
     * @return 通讯通道
     */
    @Override
    public List<CommunicationChannel> selectCommunicationChannelList() {
        CommunicationChannel communicationChannel = new CommunicationChannel();
        return communicationChannelMapper.selectCommunicationChannelList(communicationChannel);
    }

    /**
     * 更新状态
     *
     * @param id         通道ID
     * @param state      当前状态
     * @param changeTime 变更时间
     */
    @Override
    public void changeState(Long id, Integer state, Date changeTime){
        CommunicationChannel communicationChannel = new CommunicationChannel();
        communicationChannel.setId(id);
        communicationChannel.setIsActive(state);
        if(state == 0){
            communicationChannel.setOfflineTime(changeTime);
        }else{
            communicationChannel.setOnlineTime(changeTime);
            communicationChannel.setActiveUpdateTime(changeTime);
        }
        communicationChannel.setUpdateBy("MQ_SYSTEM");
        communicationChannel.setUpdateTime(new Date());
        communicationChannelMapper.updateCommunicationChannel(communicationChannel);
    }
}
