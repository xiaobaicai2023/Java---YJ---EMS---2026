package com.yunpower.publisher;

import com.yunpower.mq.publisher.service.PublisherService;
import com.yunpower.CollectApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ测试
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/5 16:41
 */
@SpringBootTest
public class RabiitMQTest extends CollectApplicationTest {

    @Autowired
    private PublisherService publisherService;

    @Test
    public void sendMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("姓名", "张三");
        map.put("年龄", 30);
        map.put("职业", "工程师");
        for (int i = 0; i < 20; i++) {
            String key = "storage"+ i % 4;
            map.put("i", i);
            map.put("storage", key);
            System.out.println("发送消息 key: " + key + ", i: " + i);
            //publisherService.sendMessage(key ,map);
        }
    }
}
