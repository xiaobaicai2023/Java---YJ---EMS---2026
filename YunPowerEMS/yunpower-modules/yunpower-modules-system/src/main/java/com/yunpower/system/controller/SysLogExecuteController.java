package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.domain.SysLogExecute;
import com.yunpower.system.service.ISysLogExecuteService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 执行日志
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
@Api(tags = "R 执行日志表")
@RestController
@RequestMapping("/executelog")
public class SysLogExecuteController extends BaseController
{
    @Autowired
    private ISysLogExecuteService sysLogExecuteService;

    /**
     * 查询执行日志列表
     */
    @ApiOperation("查询执行日志列表")
    @RequiresPermissions("system:execute:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogExecute sysLogExecute)
    {
        startPage();
        List<SysLogExecute> list = sysLogExecuteService.selectSysLogExecuteList(sysLogExecute);
        return getDataTable(list);
    }

    /**
     * 导出执行日志列表
     */
    @ApiOperation("导出执行日志列表")
    @RequiresPermissions("system:execute:export")
    @Log(title = "执行日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogExecute sysLogExecute)
    {
        List<SysLogExecute> list = sysLogExecuteService.selectSysLogExecuteList(sysLogExecute);
        ExcelUtil<SysLogExecute> util = new ExcelUtil<SysLogExecute>(SysLogExecute.class);
        util.exportExcel(response, list, "执行日志数据");
    }

    /**
     * 获取执行日志详细信息
     */
    @ApiOperation("获取执行日志详细信息")
    @RequiresPermissions("system:execute:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysLogExecuteService.selectSysLogExecuteById(id));
    }

    /**
     * 新增执行日志
     */
    @ApiOperation("新增执行日志")
    @RequiresPermissions("system:execute:add")
    @Log(title = "执行日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLogExecute sysLogExecute)
    {
        return toAjax(sysLogExecuteService.insertSysLogExecute(sysLogExecute));
    }

    /**
     * 修改执行日志
     */
    @ApiOperation("修改执行日志")
    @RequiresPermissions("system:execute:edit")
    @Log(title = "执行日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLogExecute sysLogExecute)
    {
        return toAjax(sysLogExecuteService.updateSysLogExecute(sysLogExecute));
    }

    /**
     * 删除执行日志
     */
    @ApiOperation("删除执行日志")
    @RequiresPermissions("system:execute:remove")
    @Log(title = "执行日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLogExecuteService.deleteSysLogExecuteByIds(ids));
    }
}
