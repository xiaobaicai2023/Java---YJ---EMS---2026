package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteDashboardConfigCardService;
import com.yunpower.system.api.domain.DashboardConfigCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 仪表盘卡片配置服务降级处理
 *
 * @author yunpower
 */
@Component
public class RemoteDashboardConfigCardFallbackFactory implements FallbackFactory<RemoteDashboardConfigCardService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteDashboardConfigCardFallbackFactory.class);

    @Override
    public RemoteDashboardConfigCardService create(Throwable throwable) {
        log.error("仪表盘卡片配置服务调用失败:{}", throwable.getMessage());
        return new RemoteDashboardConfigCardService() {
            @Override
            public DashboardConfigCard getInfo(Long dashboardConfigId, String cardKey, String source) {
                return null;
            }

            @Override
            public Map<String,Object> getDashboardConfigByStationId(Long stationId, Integer isPre, String source) {
                return null;
            }
        };
    }
}

