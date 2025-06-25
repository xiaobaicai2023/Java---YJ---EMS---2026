package com.yunpower.mq.consumer.service;

import com.yunpower.common.core.entity.amqp.DeviceStateMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;

/**
 * 设备状态变更-消费者
 * <pre>
 *     1、通道
 *     2、通道设备
 *     3、监控设备
 * </pre>
 * @Author: Jiajiaglam
 * @Date: 2024/8/13 15:42
 *
 */
@Component
@Slf4j
public class StateChangeConsumerListener {

	private void changeState(Integer index,DeviceStateMessage message){
		try{
			MDC.put(CHANNEL_SN, message.getChannelSn());
			log.debug("设备状态变更消费者{}接收到消息：{}",index, message);
		}finally {
			MDC.remove(CHANNEL_SN);
		}
	}
}
