package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.log.annotation.Log;
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
import com.yunpower.system.domain.SysLogSystem;
import com.yunpower.system.service.ISysLogSystemService;

/**
 * 系统日志
 * 
 * @author JUNFU.WANG
 * @date 2024-01-18
 */
@Api(tags = "R 系统日志表")
@RestController
@RequestMapping("/systemlog")
public class SysLogSystemController extends BaseController
{
    @Autowired
    private ISysLogSystemService sysLogSystemService;

    /**
     * 查询系统日志列表
     */
    @ApiOperation("查询系统日志列表")
    @RequiresPermissions("system:system:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogSystem sysLogSystem)
    {
        startPage();
        List<SysLogSystem> list = sysLogSystemService.selectSysLogSystemList(sysLogSystem);
        return getDataTable(list);
    }

    /**
     * 导出系统日志列表
     */
    @ApiOperation("导出系统日志列表")
    @RequiresPermissions("system:system:export")
    @Log(title = "系统日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogSystem sysLogSystem)
    {
        List<SysLogSystem> list = sysLogSystemService.selectSysLogSystemList(sysLogSystem);
        ExcelUtil<SysLogSystem> util = new ExcelUtil<SysLogSystem>(SysLogSystem.class);
        util.exportExcel(response, list, "系统日志数据");
    }

    /**
     * 获取系统日志详细信息
     */
    @ApiOperation("获取系统日志详细信息")
    @RequiresPermissions("system:system:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysLogSystemService.selectSysLogSystemById(id));
    }

    /**
     * 新增系统日志
     */
    @ApiOperation("新增系统日志")
    @RequiresPermissions("system:system:add")
    @Log(title = "系统日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLogSystem sysLogSystem)
    {
        return toAjax(sysLogSystemService.insertSysLogSystem(sysLogSystem));
    }

    /**
     * 修改系统日志
     */
    @ApiOperation("修改系统日志")
    @RequiresPermissions("system:system:edit")
    @Log(title = "系统日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLogSystem sysLogSystem)
    {
        return toAjax(sysLogSystemService.updateSysLogSystem(sysLogSystem));
    }

    /**
     * 删除系统日志
     */
    @ApiOperation("删除系统日志")
    @RequiresPermissions("system:system:remove")
    @Log(title = "系统日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysLogSystemService.deleteSysLogSystemByIds(ids));
    }
}
