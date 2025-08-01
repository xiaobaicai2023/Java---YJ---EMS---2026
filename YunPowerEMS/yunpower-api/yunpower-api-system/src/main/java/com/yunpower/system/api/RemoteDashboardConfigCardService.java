package com.yunpower.system.api;

import com.yunpower.system.api.factory.RemoteDashboardConfigCardFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import com.yunpower.system.api.domain.DashboardConfigCard;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

/**
 * 仪表盘卡片配置
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteDashboardConfigCard", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteDashboardConfigCardFallbackFactory.class)
public interface RemoteDashboardConfigCardService {


    /**
     * 通过站点才查询大屏配置
     * @param stationId 站点ID
     * @param isPre 是否预览 1是 0否
     * */
    @GetMapping("/dashboard/config/station/inner/{stationId}/{isPre}")
    public Map<String,Object> getDashboardConfigByStationId(@PathVariable("stationId") Long stationId, @PathVariable("isPre") Integer isPre, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询卡片配置
     * @param dashboardConfigId 配置ID
     * @param cardKey 卡片key
     * */
    @GetMapping("/dashboard/card/config/inner/{dashboardConfigId}/{cardKey}")
    public DashboardConfigCard getInfo(@PathVariable("dashboardConfigId") Long dashboardConfigId, @PathVariable("cardKey") String cardKey, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
