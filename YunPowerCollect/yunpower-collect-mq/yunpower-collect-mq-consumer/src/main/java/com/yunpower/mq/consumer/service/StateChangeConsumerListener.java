package com.yunpower.mq.consumer.service;

import com.yunpower.collect.storage.service.StorageCommonService;
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

	/**
	 * 数据存储监听队列（0）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "state.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "statechange0"
	))
	public void storageDirectQueue0(DeviceStateMessage message) {
		changeState(0,message);
	}

	/**
	 * 数据存储监听队列（1）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "state1.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "statechange1"
	))
	public void storageDirectQueue1(DeviceStateMessage message) {
		changeState(1,message);
	}

	/**
	 * 数据存储监听队列（2）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "state2.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "statechange2"
	))
	public void storageDirectQueue2(DeviceStateMessage message) {
		changeState(2,message);
	}

	/**
	 * 数据存储监听队列（3）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "state3.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "statechange3"
	))
	public void storageDirectQueue3(DeviceStateMessage message) {
		changeState(3,message);
	}

	private void changeState(Integer index,DeviceStateMessage message){
		try{
			MDC.put(CHANNEL_SN, message.getChannelSn());
			log.debug("设备状态变更消费者{}接收到消息：{}",index, message);
			StorageCommonService.changeState(message,true);
		}finally {
			MDC.remove(CHANNEL_SN);
		}
	}
}
