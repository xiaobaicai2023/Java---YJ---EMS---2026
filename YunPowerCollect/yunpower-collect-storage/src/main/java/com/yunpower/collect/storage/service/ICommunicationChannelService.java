package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.CommunicationChannel;

import java.util.Date;
import java.util.List;

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
	CommunicationChannel selectCommunicationChannelById(Long id);

	/**
	 * 查询通讯通道列表
	 *
	 * @param communicationChannel 通讯通道
	 * @return 通讯通道集合
	 */
	List<CommunicationChannel> selectCommunicationChannelList(CommunicationChannel communicationChannel);

	/**
	 * 查询通讯通道列表
	 *
	 * @return 通讯通道集合
	 */
	List<CommunicationChannel> selectCommunicationChannelList();

	/**
	 * 更新状态
	 *
	 * @param id    通道ID
	 * @param state 当前状态
	 */
	void changeState(Long id, Integer state, Date changeTime);
}
