package com.yunpower.mq.publisher.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * MQ的回调配置
 */
@Configuration
public class MqConfirmConfig implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqConfirmConfig.class);

    /**
     * 配置MQ的回调
     * 需要在配置文件中开启：spring.rabbitmq.publisher-returns=true
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                LOGGER.debug("收到消息的return callback，exchange:{}, key:{}, msg:{}, code:{}, text:{}",
                        returned.getExchange(), returned.getRoutingKey(), returned.getMessage(),
                        returned.getReplyCode(), returned.getReplyText());
            }
        });
    }
}
