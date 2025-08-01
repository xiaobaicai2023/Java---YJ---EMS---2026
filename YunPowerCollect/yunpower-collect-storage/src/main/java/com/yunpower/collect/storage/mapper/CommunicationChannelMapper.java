package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.CommunicationChannel;

import java.util.List;

/**
 * 通讯通道Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface CommunicationChannelMapper {
    /**
     * 查询通讯通道
     *
     * @param id 通讯通道主键
     * @return 通讯通道
     */
    public CommunicationChannel selectCommunicationChannelById(Long id);

    /**
     * 查询通讯通道列表
     *
     * @param communicationChannel 通讯通道
     * @return 通讯通道集合
     */
    public List<CommunicationChannel> selectCommunicationChannelList(CommunicationChannel communicationChannel);

    /**
     * 修改通讯通道
     *
     * @param communicationChannel 通讯通道
     * @return 结果
     */
    public int updateCommunicationChannel(CommunicationChannel communicationChannel);
}
