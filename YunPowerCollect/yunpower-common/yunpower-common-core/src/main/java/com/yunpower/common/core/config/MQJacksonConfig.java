package com.yunpower.common.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ消息转换器
 * @Author: Jiajiaglam
 * @Date: 2024/8/5 17:52
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@RequiredArgsConstructor
public class MQJacksonConfig {
    @Bean
    public MessageConverter jacksonMessageConvertor(){
        return new Jackson2JsonMessageConverter();
    }
}
