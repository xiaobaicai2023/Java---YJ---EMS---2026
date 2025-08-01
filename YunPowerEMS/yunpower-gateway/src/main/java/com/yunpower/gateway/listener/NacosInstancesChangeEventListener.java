package com.yunpower.gateway.listener;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.yunpower.common.core.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;
import org.springframework.stereotype.Component;

/**
 * @title: 网关不稳定时，自动刷新nacos服务
 * @Author: Jiajiaglam
 * @date: 2023-10-09 9:01
 * @description:
 */
@Component
public class NacosInstancesChangeEventListener extends Subscriber<InstancesChangeEvent> {
    private static final Logger log = LoggerFactory.getLogger(NacosInstancesChangeEventListener.class);

    @Override
    public void onEvent(InstancesChangeEvent instancesChangeEvent) {
        log.info("spring gateway receive refresh event ：{}, refresh cache start ...", JacksonUtils.toJson(instancesChangeEvent));
        LoadBalancerCacheManager cacheManager = SpringUtils.getBean(LoadBalancerCacheManager.class);
        Cache cache = cacheManager.getCache(CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME);
        if (cache != null) {
            cache.evict(instancesChangeEvent.getServiceName());
        }
        log.info("spring gateway refresh finish .");
    }

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }
}
