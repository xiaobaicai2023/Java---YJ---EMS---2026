package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteShardingMonthAccumulateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Map;

/**
 * 变量累积数据月存储服务降级处理
 *
 * @author yunpower
 */
public class RemoteShardingMonthAccumulateFallbackFactory  implements FallbackFactory<RemoteShardingMonthAccumulateService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteShardingMonthAccumulateFallbackFactory.class);


    @Override
    public RemoteShardingMonthAccumulateService create(Throwable throwable) {
        log.error("变量累积数据月存储服务调用失败:{}", throwable.getMessage());
        return new RemoteShardingMonthAccumulateService()
        {
            @Override
            public int updateShardingMonthAccumulateSeasonal(Map<String, Object> map, String source) {
                return 0;
            }
        };
    }
}
