package com.yunpower.system.service;

import com.yunpower.system.api.domain.ReportTemplates;

import java.util.List;

/**
 * 报表模版Service接口
 * 
 * @author yunpower
 * @date 2024-06-06
 */
public interface IReportTemplatesService 
{
    /**
     * 查询报表模版
     * 
     * @param id 报表模版主键
     * @return 报表模版
     */
    public ReportTemplates selectReportTemplatesById(Long id);

    /**
     * 查询报表模版列表
     * 
     * @param reportTemplates 报表模版
     * @return 报表模版集合
     */
    public List<ReportTemplates> selectReportTemplatesList(ReportTemplates reportTemplates);

    /**
     * 新增报表模版
     * 
     * @param reportTemplates 报表模版
     * @return 结果
     */
    public int insertReportTemplates(ReportTemplates reportTemplates);

    /**
     * 修改报表模版
     * 
     * @param reportTemplates 报表模版
     * @return 结果
     */
    public int updateReportTemplates(ReportTemplates reportTemplates);

    /**
     * 修改报表模版状态
     *
     * @param id 报表模版主键
     * @param state 状态
     * @return 结果
     */
    public int updateReportTemplatesState(Long id, Integer state);

    /**
     * 批量删除报表模版
     * 
     * @param ids 需要删除的报表模版主键集合
     * @return 结果
     */
    public int deleteReportTemplatesByIds(Long[] ids);

    /**
     * 删除报表模版信息
     * 
     * @param id 报表模版主键
     * @return 结果
     */
    public int deleteReportTemplatesById(Long id);
}
