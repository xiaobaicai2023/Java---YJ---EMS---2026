package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteElectricPriceSchemeConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * 电度电价配置服务降级处理
 *
 * @author XIAOTONG.CAO
 */
public class RemoteElectricPriceSchemeConfigFallbackFactory implements FallbackFactory<RemoteElectricPriceSchemeConfigService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteElectricPriceSchemeConfigFallbackFactory.class);

    @Override
    public RemoteElectricPriceSchemeConfigService create(Throwable throwable) {
        log.error("通讯设备服务调用失败:{}", throwable.getMessage());
        return new RemoteElectricPriceSchemeConfigService() {
            @Override
            public Map<String, List<Object>> getSeasonalRangeList(Long schemeId, String source) {
                return null;
            }

            @Override
            public List<Map<String, Object>> getSeasonalRangeListForDayStack(String source) {
                return null;
            }
        };
    }
}
