package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.api.domain.SysLogOper;
import com.yunpower.system.service.ISysLogOperService;

/**
 * 操作日志记录
 * 
 * @author yunpower
 */
@Api(tags = "R 操作日志记录表")
@RestController
@RequestMapping("/operlog")
public class SysLogOperController extends BaseController
{
    @Autowired
    private ISysLogOperService operLogService;

    /**获取操作日志记录列表
     * 双日期格式如下：
     * http://localhost/dev-api/system/operlog/list?pageNum=1&pageSize=10&params[beginTime]=2023-10-03 00:00:00&params[endTime]=2023-11-14 23:59:59
     *
     * @param operLog 实体类
     * @return 结果
     */
    @ApiOperation("获取操作日志记录列表")
    @RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogOper operLog)
    {
        startPage();
        List<SysLogOper> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @ApiOperation("导出操作日志记录")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:operlog:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogOper operLog)
    {
        List<SysLogOper> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysLogOper> util = new ExcelUtil<SysLogOper>(SysLogOper.class);
        util.exportExcel(response, list, "操作日志");
    }

    @ApiOperation("删除操作日志记录")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:operlog:remove")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds)
    {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @ApiOperation("清空操作日志记录")
    @RequiresPermissions("system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        operLogService.cleanOperLog();
        return success();
    }

    @ApiOperation("新增操作日志记录")
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogOper operLog)
    {
        return toAjax(operLogService.insertOperlog(operLog));
    }
}
