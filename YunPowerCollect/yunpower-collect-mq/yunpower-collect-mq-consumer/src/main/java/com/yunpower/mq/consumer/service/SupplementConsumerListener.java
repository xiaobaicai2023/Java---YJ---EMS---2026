package com.yunpower.mq.consumer.service;

import com.yunpower.common.core.entity.amqp.DeviceVarSupplementMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;

/**
 * 采集数据补充-消费者
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/3 15:42
 */
@Component
@Slf4j
public class SupplementConsumerListener {


	private void doSupplement(Integer index, DeviceVarSupplementMessage message) {
		try{
			MDC.put(CHANNEL_SN, message.getChannelSn());
			log.debug("数据补充消费者{}接收到消息：{}", index, message);
		}finally {
			MDC.remove(CHANNEL_SN);
		}
	}
}
