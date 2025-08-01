package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteMonitorDeviceService;
import com.yunpower.system.api.domain.FeignMonitorDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 监控设备服务降级处理
 *
 * @author XIAOTONG.CAO
 */
public class RemoteMonitorDeviceFallbackFactory implements FallbackFactory<RemoteMonitorDeviceService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteMonitorDeviceFallbackFactory.class);

    @Override
    public RemoteMonitorDeviceService create(Throwable throwable)
    {
        log.error("监控设备服务调用失败:{}", throwable.getMessage());
        return new RemoteMonitorDeviceService()
        {
            @Override
            public FeignMonitorDevice getInfoByDeviceSn(String deviceSn, String source) {
                return null;
            }

            @Override
            public Boolean statusAll(Long deptId, String source) {
                return false;
            }
        };
    }
}
