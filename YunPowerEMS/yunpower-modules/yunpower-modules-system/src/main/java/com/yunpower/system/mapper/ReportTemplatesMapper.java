package com.yunpower.system.mapper;

import com.yunpower.system.api.domain.ReportTemplates;

import java.util.List;

/**
 * 报表模版Mapper接口
 * 
 * @author yunpower
 * @date 2024-06-06
 */
public interface ReportTemplatesMapper 
{
    /**
     * 查询报表模版
     * 
     * @param id 报表模版主键
     * @return 报表模版
     */
    public ReportTemplates selectReportTemplatesById(Long id);

    /**
     * 根据条件查询数量
     * */
    public int count(ReportTemplates reportTemplates);

    /**
     * 查询报表模版列表
     * 
     * @param reportTemplates 报表模版
     * @return 报表模版集合
     */
    public List<ReportTemplates> selectReportTemplatesList(ReportTemplates reportTemplates);

}
