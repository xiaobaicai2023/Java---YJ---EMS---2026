package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteMonitorDeviceVarService;
import com.yunpower.system.api.domain.FeignMonitorDeviceVar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * 监控设备变量服务降级处理
 *
 * @author XIAOTONG.CAO
 */
public class RemoteMonitorDeviceVarFallbackFactory implements FallbackFactory<RemoteMonitorDeviceVarService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteMonitorDeviceVarFallbackFactory.class);


    @Override
    public RemoteMonitorDeviceVarService create(Throwable throwable)
    {
        log.error("监控设备变量服务调用失败:{}", throwable.getMessage());
        return new RemoteMonitorDeviceVarService()
        {
            @Override
            public List<FeignMonitorDeviceVar> getListInner(FeignMonitorDeviceVar request, String source) {
                return null;
            }

            @Override
            public FeignMonitorDeviceVar getInfoInnerAuth(String varSn, String source) {
                return null;
            }
        };
    }
}
