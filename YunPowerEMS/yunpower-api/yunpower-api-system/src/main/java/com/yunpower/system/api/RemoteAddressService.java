package com.yunpower.system.api;

import com.yunpower.system.api.factory.RemoteAddressServiceFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 地址管理
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remoteAddressService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteAddressServiceFallbackFactory.class)
public interface RemoteAddressService {

    /**
     * 根据给定的省、市、县ID获取对应的地址名称。
     *
     * @param provinceId 省份ID
     * @param cityId 城市ID
     * @param countyId 县/区ID
     * @param source 请求来源，请求头中指定
     * @return 返回地址名称，以字符串形式
     */
    @GetMapping("/address/getName/{provinceId}/{cityId}/{countyId}")
    public String getAddressName(@PathVariable("provinceId") Long provinceId, @PathVariable("cityId") Long cityId, @PathVariable("countyId") Long countyId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
