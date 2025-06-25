package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemotePublicService;
import com.yunpower.common.core.domain.R;
import com.yunpower.system.api.domain.SysStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @title: 公共方法
 * @Author: Jiajiaglam
 * @date: 2023-10-07 10:04
 * @description:
 */
@Component
public class RemotePublicFallbackFactory implements FallbackFactory<RemotePublicService> {
    private static final Logger log = LoggerFactory.getLogger(RemotePublicFallbackFactory.class);

    @Override
    public RemotePublicService create(Throwable cause) {

        log.error("公共服务调用失败:{}", cause.getMessage());
        return new RemotePublicService()
        {
            @Override
            public R<Boolean> setUserCurrentStation(String source) {
                return R.fail("设置用户默认站点失败:" + cause.getMessage());
            }

            @Override
            public SysStation getCurrentStationInfo(String source) {
                return null;
            }
        };
    }
}
