package com.yunpower.system.api;

import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.constant.ServiceNameConstants;
import com.yunpower.system.api.domain.ReportTemplates;
import com.yunpower.system.api.factory.RemoteReportTemplateFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 报表获取模板数据
 * @Author: Jiajiaglam
 * @Date: 2024/6/19 18:04
 */
@FeignClient(contextId = "remoteReportTemplate", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteReportTemplateFallbackFactory.class)
public interface RemoteReportTemplateService {
    /**
     * 查询表格模板配置
     * @param templateId 模板ID
     * */
    @GetMapping("/report-templates/inner/{templateId}")
    public ReportTemplates getInfo(@PathVariable("templateId") Long templateId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
