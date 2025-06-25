package com.yunpower.system.controller;

import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.ReportTemplates;
import com.yunpower.system.service.IReportTemplatesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 报表模版
 *
 * @author yunpower
 * @date 2024-06-06
 */
@Api(tags = "B 报表模版表")
@RestController
@RequestMapping("/report-templates")
public class ReportTemplatesController extends BaseController
{
    @Autowired
    private IReportTemplatesService reportTemplatesService;

    /**
     * 查询报表模版列表
     */
    @ApiOperation("查询报表模版列表")
    @RequiresPermissions("system:report-templates:list")
    @GetMapping("/list")
    public TableDataInfo list(ReportTemplates reportTemplates)
    {
        startPage();
        List<ReportTemplates> list = reportTemplatesService.selectReportTemplatesList(reportTemplates);
        return getDataTable(list);
    }

    /**
     * 查询报表模版列表-不分页
     */
    @ApiOperation("查询报表模版列表-不分页")
    @RequiresPermissions("system:report-templates:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(ReportTemplates reportTemplates)
    {
        List<ReportTemplates> list = reportTemplatesService.selectReportTemplatesList(reportTemplates);
        return success(list);
    }

    /**
     * 导出报表模版列表
     */
    @ApiOperation("导出报表模版列表")
    @RequiresPermissions("system:report-templates:export")
    @Log(title = "报表模版", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReportTemplates reportTemplates)
    {
        List<ReportTemplates> list = reportTemplatesService.selectReportTemplatesList(reportTemplates);
        ExcelUtil<ReportTemplates> util = new ExcelUtil<ReportTemplates>(ReportTemplates.class);
        util.exportExcel(response, list, "报表模版数据");
    }

    /**
     * 获取报表模版详细信息
     */
    @ApiOperation("获取报表模版详细信息")
    @RequiresPermissions("system:report-templates:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(reportTemplatesService.selectReportTemplatesById(id));
    }

    /**
     * 新增报表模版
     */
    @ApiOperation("新增报表模版")
    @RequiresPermissions("system:report-templates:add")
    @Log(title = "报表模版", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ReportTemplates reportTemplates)
    {
        return toAjax(reportTemplatesService.insertReportTemplates(reportTemplates));
    }

    /**
     * 修改报表模版
     */
    @ApiOperation("修改报表模版")
    @RequiresPermissions("system:report-templates:edit")
    @Log(title = "报表模版", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ReportTemplates reportTemplates)
    {
        return toAjax(reportTemplatesService.updateReportTemplates(reportTemplates));
    }

    /**
     * 修改报表模版状态
     */
    @ApiOperation("修改报表模版状态")
    @RequiresPermissions("system:report-templates:state")
    @Log(title = "报表模版", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state)
    {
        return toAjax(reportTemplatesService.updateReportTemplatesState(id, state));
    }

    /**
     * 删除报表模版
     */
    @ApiOperation("删除报表模版")
    @RequiresPermissions("system:report-templates:remove")
    @Log(title = "报表模版", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(reportTemplatesService.deleteReportTemplatesByIds(ids));
    }

    /**
     * 获取报表模板配置
     */
    @ApiOperation("获取报表模板配置")
    @InnerAuth
    @GetMapping("/inner/{templateId}")
    public ReportTemplates innerGetInfo(@PathVariable("templateId") Long templateId) {
        return reportTemplatesService.selectReportTemplatesById(templateId);
    }
}
