package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.CommunicationChannel;

/**
 * 通讯通道Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface ICommunicationChannelService {
    /**
     * 查询通讯通道
     *
     * @param id 通讯通道主键
     * @return 通讯通道
     */
    public CommunicationChannel selectCommunicationChannelById(Long id);

    /**
     * 通过通道sn获取通道信息
     *
     * @param channelSn 通道sn
     * @return 通讯通道
     */
    public CommunicationChannel selectCommunicationChannelBySn(String channelSn);

    /**
     * 通过注册码获取通道信息
     *
     * @param registrationCode 注册码
     * @return 通讯通道
     */
    public CommunicationChannel selectCommunicationChannelByRegisterCode(String registrationCode);

    /**
     * 查询通讯通道列表
     *
     * @param communicationChannel 通讯通道
     * @return 通讯通道集合
     */
    public List<CommunicationChannel> selectCommunicationChannelList(CommunicationChannel communicationChannel);

    /**
     * 查询该[通道]下是否有[通讯设备]
     *
     * @param id 通讯通道ID
     * @return 结果
     */
    public boolean hasChildrenById(Long id);

}
