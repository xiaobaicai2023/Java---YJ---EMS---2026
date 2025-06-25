package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteCommunicationDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 通讯设备服务降级处理
 *
 * @author XIAOTONG.CAO
 */
public class RemoteCommunicationDeviceFallbackFactory implements FallbackFactory<RemoteCommunicationDeviceService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteCommunicationDeviceFallbackFactory.class);

    @Override
    public RemoteCommunicationDeviceService create(Throwable throwable)
    {
        log.error("通讯设备服务调用失败:{}", throwable.getMessage());
        return new RemoteCommunicationDeviceService()
        {
            @Override
            public Boolean statusAll(Long deptId, String source) {
                return false;
            }
        };
    }
}
