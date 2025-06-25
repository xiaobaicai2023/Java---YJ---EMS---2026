package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemotePageConfigService;
import com.yunpower.system.api.domain.FeignSysPageConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 页面配置服务降级处理
 * 
 * @author XIAOTONG.CAO
 */
@Component
public class RemotePageConfigFallbackFactory implements FallbackFactory<RemotePageConfigService>
{
    private static final Logger log = LoggerFactory.getLogger(RemotePageConfigFallbackFactory.class);

    @Override
    public RemotePageConfigService create(Throwable throwable)
    {
        log.error("页面配置服务调用失败:{}", throwable.getMessage());
        return new RemotePageConfigService()
        {
            @Override
            public FeignSysPageConfig getInfoByPageValue(String pageValue, String source) {
                return null;
            }

            @Override
            public List<FeignSysPageConfig> getList(FeignSysPageConfig config, String source) {
                return null;
            }
        };
    }
}
