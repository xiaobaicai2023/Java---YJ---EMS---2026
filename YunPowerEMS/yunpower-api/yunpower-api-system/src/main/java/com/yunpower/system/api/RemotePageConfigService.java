package com.yunpower.system.api;

import com.yunpower.system.api.domain.FeignSysPageConfig;
import com.yunpower.system.api.factory.RemotePageConfigFallbackFactory;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 页面配置服务
 *
 * @author XIAOTONG.CAO
 */
@FeignClient(contextId = "remotePageConfig", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemotePageConfigFallbackFactory.class)
public interface RemotePageConfigService {
    /**
     * 通过页面标识查询页面配置
     *
     * @param pageValue 页面标识
     * @param source    请求来源
     * @return 结果
     */
    @GetMapping("/page-config/pageValue/{pageValue}")
    public FeignSysPageConfig getInfoByPageValue(@PathVariable("pageValue") String pageValue, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据条件查询
     *
     * @param config 查询条件
     * @return 配置集合
     */
    @PostMapping("/page-config/list/inner")
    public List<FeignSysPageConfig> getList(@RequestBody FeignSysPageConfig config, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
