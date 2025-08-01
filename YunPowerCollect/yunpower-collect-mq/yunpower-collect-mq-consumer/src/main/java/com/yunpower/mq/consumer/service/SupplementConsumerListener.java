package com.yunpower.mq.consumer.service;

import com.yunpower.collect.storage.service.StorageCommonService;
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

	/**
	 * 数据补充监听队列（0）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "supplement.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "supplement0"
	))
	public void supplementQueue0(DeviceVarSupplementMessage message) {
		doSupplement(0,message);

	}

	/**
	 * 数据补充监听队列（1）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "supplement1.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "supplement1"
	))
	public void supplementQueue1(DeviceVarSupplementMessage message) {
		doSupplement(1,message);
	}

	/**
	 * 数据补充监听队列（2）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "supplement2.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "supplement2"
	))
	public void supplementQueue2(DeviceVarSupplementMessage message) {
		doSupplement(2,message);
	}

	/**
	 * 数据补充监听队列（4）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "supplement3.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "supplement3"
	))
	public void supplementQueue3(DeviceVarSupplementMessage message) {
		doSupplement(3,message);
	}


	private void doSupplement(Integer index, DeviceVarSupplementMessage message) {
		try{
			MDC.put(CHANNEL_SN, message.getChannelSn());
			log.debug("数据补充消费者{}接收到消息：{}", index, message);
			StorageCommonService.doSupplement(message);
		}finally {
			MDC.remove(CHANNEL_SN);
		}
	}
}
