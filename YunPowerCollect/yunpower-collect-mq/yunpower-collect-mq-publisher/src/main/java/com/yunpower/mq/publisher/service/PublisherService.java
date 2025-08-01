package com.yunpower.mq.publisher.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生产者 工具类
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/3 15:37
 */
@Component
@Slf4j
public class PublisherService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 默认交换机名称
    private static final String EXCHANGE_NAME = "yunpower.direct";

    // 默认队列名称
    private static final String QUEUE_NAME = "collect.queue";

    // 默认路由键
    private static final String ROUTING_KEY = "storage0";

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(Object message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }

    /**
     * 发送消息
     *
     * @param routingKey 路由键
     * @param message    消息
     */
    public void sendMessage(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, message);
    }

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public void sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
