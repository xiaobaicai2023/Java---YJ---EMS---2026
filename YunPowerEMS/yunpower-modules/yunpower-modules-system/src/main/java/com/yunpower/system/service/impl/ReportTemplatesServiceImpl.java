package com.yunpower.system.service.impl;

import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.security.utils.SecurityUtils;
import com.yunpower.system.api.domain.ReportTemplates;
import com.yunpower.system.mapper.ReportTemplatesMapper;
import com.yunpower.system.service.IPublicService;
import com.yunpower.system.service.IReportTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报表模版Service业务层处理
 * 
 * @author yunpower
 * @date 2024-06-06
 */
@Service
public class ReportTemplatesServiceImpl implements IReportTemplatesService 
{
    @Autowired
    private ReportTemplatesMapper reportTemplatesMapper;

    @Autowired
    private IPublicService publicService;

    /**
     * 查询报表模版
     * 
     * @param id 报表模版主键
     * @return 报表模版
     */
    @Override
    public ReportTemplates selectReportTemplatesById(Long id)
    {
        return reportTemplatesMapper.selectReportTemplatesById(id);
    }

    /**
     * 查询报表模版列表
     * 
     * @param reportTemplates 报表模版
     * @return 报表模版
     */
    @Override
    public List<ReportTemplates> selectReportTemplatesList(ReportTemplates reportTemplates)
    {
        checkDefaultData(reportTemplates);
        reportTemplates.setDeptId(publicService.getCurrentStation());
        reportTemplates.setUserId(SecurityUtils.getUserId());
        reportTemplates.setDeleteFlag(0);
        return reportTemplatesMapper.selectReportTemplatesList(reportTemplates);
    }

    /**
     * 检查是否有默认模版
     * @param search 查询条件
     */
    private void checkDefaultData(ReportTemplates search){
        //根据站点+站点类型+报表类型 来查询是否存在默认数据
        ReportTemplates query = new ReportTemplates();
        query.setDeptId(publicService.getCurrentStation());
        query.setStationType(search.getStationType());
        query.setReportType(search.getReportType());
        query.setDeleteFlag(0);
        int count  = reportTemplatesMapper.count(query);
        if(count == 0){
            ReportTemplates info = packageDefaultReportTemplates(query);
        }
    }

    /**
     * 组装默认模版数据
     * */
    private ReportTemplates packageDefaultReportTemplates(ReportTemplates query) {
        ReportTemplates info = new ReportTemplates();
        info.setReportName("默认模版");
        info.setReportType(query.getReportType());
        info.setStationType(query.getStationType());
        info.setHeadConfig("[]");
        info.setSystemFlag(0);
        info.setVisibilityType(0);
        info.setMultiLevelHeader(0);
        info.setRemark("默认普通模版");
        info.setCreateBy(SecurityUtils.getNickName());
        info.setCreateTime(DateUtils.getNowDate());
        info.setEntId(publicService.getCurrentEnterprise());
        info.setDeptId(publicService.getCurrentStation());
        return info;
    }

}
