package com.yunpower.mq.consumer.service;

import com.yunpower.collect.storage.service.StorageCommonService;
import com.yunpower.common.core.entity.amqp.DeviceVarCollectMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import static com.yunpower.common.core.constant.Constants.CHANNEL_SN;

/**
 * 采集数据存储-消费者
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/3 15:42
 */
@Component
@Slf4j
public class CollectConsumerListener {

	/**
	 * 数据存储监听队列（0）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage0"
	))
	public void storageDirectQueue0(DeviceVarCollectMessage message) {
		doStorage(0,message);
	}

	/**
	 * 数据存储监听队列（1）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect1.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage1"
	))
	public void storageDirectQueue1(DeviceVarCollectMessage message) {
		doStorage(1,message);
	}


	/**
	 * 数据存储监听队列（2）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect2.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage2"
	))
	public void storageDirectQueue2(DeviceVarCollectMessage message) {
		doStorage(2,message);

	}

	/**
	 * 数据存储监听队列（3）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect3.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage3"
	))
	public void storageDirectQueue3(DeviceVarCollectMessage message) {
		doStorage(3,message);

	}

	/**
	 * 数据存储监听队列（4）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect4.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage4"
	))
	public void storageDirectQueue4(DeviceVarCollectMessage message) {
		doStorage(4,message);

	}

	/**
	 * 数据存储监听队列（5）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect5.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage5"
	))
	public void storageDirectQueue5(DeviceVarCollectMessage message) {
		doStorage(5,message);

	}

	/**
	 * 数据存储监听队列（6）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect6.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage6"
	))
	public void storageDirectQueue6(DeviceVarCollectMessage message) {
		doStorage(6,message);

	}

	/**
	 * 数据存储监听队列（7）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect7.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage7"
	))
	public void storageDirectQueue7(DeviceVarCollectMessage message) {
		doStorage(7,message);

	}

	/**
	 * 数据存储监听队列（8）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect8.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage8"
	))
	public void storageDirectQueue8(DeviceVarCollectMessage message) {
		doStorage(8,message);
	}

	/**
	 * 数据存储监听队列（9）
	 */
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(name = "collect9.queue", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
			exchange = @Exchange(name = "yunpower.direct", type = ExchangeTypes.DIRECT),
			key = "storage9"
	))
	public void storageDirectQueue9(DeviceVarCollectMessage message) {
		doStorage(9,message);
	}

	private void doStorage(Integer index, DeviceVarCollectMessage message) {
		try{
			MDC.put(CHANNEL_SN, message.getChannelSn());
			log.debug("采集数据存储消费者{}接收到消息：{}", index, message);
			StorageCommonService.doStorage(message, true);
		}finally {
			MDC.remove(CHANNEL_SN);
		}
	}
}
