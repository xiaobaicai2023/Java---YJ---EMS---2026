package com.yunpower.mq.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定向交换机配置与绑定
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/3 15:49
 */
//缺点：routingKey太多时绑起来麻烦，因此可选择注解绑定（二选一）
//@Configuration
public class DirectConfiguration {

    /**
     * 交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("yunpower.direct");
    }

    /**
     * 数据采集队列
     */
    @Bean
    public Queue directQueueCollect() {
        return new Queue("queue.collect");
    }

    /**
     * 数据采集队列绑定交换机【存储】
     */
    @Bean
    public Binding directQueueBindingStorage0(Queue directQueueCollect, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueCollect).to(directExchange).with("storage0");
    }

    @Bean
    public Binding directQueueBindingStorage1(Queue directQueueCollect, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueCollect).to(directExchange).with("storage1");
    }

    @Bean
    public Binding directQueueBindingStorage2(Queue directQueueCollect, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueCollect).to(directExchange).with("storage2");
    }

    @Bean
    public Binding directQueueBindingStorage3(Queue directQueueCollect, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueCollect).to(directExchange).with("storage3");
    }

    @Bean
    public Binding directQueueBindingStorage4(Queue directQueueCollect, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueueCollect).to(directExchange).with("storage4");
    }
}
