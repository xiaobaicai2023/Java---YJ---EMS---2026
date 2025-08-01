package com.yunpower.system.api.factory;

import com.yunpower.system.api.RemoteReportTemplateService;
import com.yunpower.system.api.domain.ReportTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 报表获取模板数据
 * @Author: Jiajiaglam
 * @Date: 2024/6/19 18:23
 */
public class RemoteReportTemplateFallbackFactory implements FallbackFactory<RemoteReportTemplateService> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteReportTemplateFallbackFactory.class);

    @Override
    public RemoteReportTemplateService create(Throwable cause) {
        LOGGER.info("报表模板数据服务调用失败:{}", cause.getMessage());
        return new RemoteReportTemplateService() {
            @Override
            public ReportTemplates getInfo(Long templateId, String source) {
                return null;
            }
        };
    }
}
