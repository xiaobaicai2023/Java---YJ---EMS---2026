package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteSpecialDataVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Map;

/**
 * @Author: Jiajiaglam
 * @Date: 2024/6/22 10:00
 */
public class RemoteSpecialDataVFallbackFactory implements FallbackFactory<RemoteSpecialDataVService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteSpecialDataVFallbackFactory.class);

    @Override
    public RemoteSpecialDataVService create(Throwable cause) {
        LOGGER.info("大屏通用接口数据服务调用失败:{}", cause.getMessage());
        return new RemoteSpecialDataVService() {
            @Override
            public Object getSpecialData(Map<String, Object> param, String source) {
                return null;
            }
        };
    }
}
