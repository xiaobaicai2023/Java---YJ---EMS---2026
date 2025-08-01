package com.yunpower.common.core.constant;

/**
 * @title: MQ常量信息
 * @Author: Jiajiaglam
 * @date: 2024-08-14 8:31
 * @description:
 */
public class RabbitConstants {

	/**
	 * 数据存储消费者-queue
	 * */
	public static final String COLLECT_CONSUMER_QUEUE = "collect.queue";

	/**
	 * 数据存储消费者-route key
	 * */
	public static final String COLLECT_CONSUMER_KEY = "storage";

	/**
	 * 采集数据补充消费者-route key
	 * */
	public static final String SUPPLEMENT_CONSUMER_KEY = "supplement";

	/**
	 * 状态变更消费者-route key
	 * */
	public static final String STATECHANGE_CONSUMER_KEY = "statechange";
}
