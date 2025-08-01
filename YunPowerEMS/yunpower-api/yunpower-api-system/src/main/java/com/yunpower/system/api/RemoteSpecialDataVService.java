package com.yunpower.system.api;

import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import com.yunpower.system.api.factory.RemoteSpecialDataVFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 大屏专用数据、特殊数据接口
 *
 * @Author: Jiajiaglam
 * @Date: 2024/6/22 9:56
 */
@FeignClient(contextId = "remoteSpecialDataV", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteSpecialDataVFallbackFactory.class)
public interface RemoteSpecialDataVService {

    /**
     * 获取通用数据，以对象的方式返回结果
     */
    @GetMapping("/common-provide/inner/tableList")
    public Object getSpecialData(@RequestParam Map<String, Object> param, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
