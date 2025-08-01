package com.yunpower.system.api;


import com.yunpower.system.api.factory.RemoteShardingMonthAccumulateFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * 变量累积数据月存储
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteShardingMonthAccumulateService", value = ServiceNameConstants.DATA_V_SERVICE, fallbackFactory = RemoteShardingMonthAccumulateFallbackFactory.class)
public interface RemoteShardingMonthAccumulateService {

    /**
     * 重新更新峰谷数据
     *
     * @param map 更新数据
     * @return 更新结果
     */
    @PostMapping("/sharding-month-accumulate/updateShardingMonthAccumulateSeasonal/inner")
    public int updateShardingMonthAccumulateSeasonal(@RequestBody Map<String, Object> map, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
