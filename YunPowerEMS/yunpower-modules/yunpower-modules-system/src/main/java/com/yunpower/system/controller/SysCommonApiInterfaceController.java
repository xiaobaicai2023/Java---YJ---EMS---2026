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
import com.yunpower.system.domain.SysCommonApiInterface;
import com.yunpower.system.service.ISysCommonApiInterfaceService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 数据接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "S 数据接口表")
@RestController
@RequestMapping("/apply-interface")
public class SysCommonApiInterfaceController extends BaseController
{
    @Autowired
    private ISysCommonApiInterfaceService sysCommonApiInterfaceService;

    /**
     * 查询数据接口列表
     */
    @ApiOperation("查询数据接口列表")
    @RequiresPermissions("system:apply-interface:list")
    @GetMapping("/list")
    public TableDataInfo list(SysCommonApiInterface sysCommonApiInterface)
    {
        startPage();
        List<SysCommonApiInterface> list = sysCommonApiInterfaceService.selectSysCommonApiInterfaceList(sysCommonApiInterface);
        return getDataTable(list);
    }

    /**
     * 导出数据接口列表
     */
    @ApiOperation("导出数据接口列表")
    @RequiresPermissions("system:apply-interface:export")
    @Log(title = "数据接口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommonApiInterface sysCommonApiInterface)
    {
        List<SysCommonApiInterface> list = sysCommonApiInterfaceService.selectSysCommonApiInterfaceList(sysCommonApiInterface);
        ExcelUtil<SysCommonApiInterface> util = new ExcelUtil<SysCommonApiInterface>(SysCommonApiInterface.class);
        util.exportExcel(response, list, "数据接口数据");
    }

    /**
     * 获取数据接口详细信息
     */
    @ApiOperation("获取数据接口详细信息")
    @RequiresPermissions("system:apply-interface:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysCommonApiInterfaceService.selectSysCommonApiInterfaceById(id));
    }

    /**
     * 新增数据接口
     */
    @ApiOperation("新增数据接口")
    @RequiresPermissions("system:apply-interface:add")
    @Log(title = "数据接口", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCommonApiInterface sysCommonApiInterface)
    {
        return toAjax(sysCommonApiInterfaceService.insertSysCommonApiInterface(sysCommonApiInterface));
    }

    /**
     * 修改数据接口
     */
    @ApiOperation("修改数据接口")
    @RequiresPermissions("system:apply-interface:edit")
    @Log(title = "数据接口", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCommonApiInterface sysCommonApiInterface)
    {
        return toAjax(sysCommonApiInterfaceService.updateSysCommonApiInterface(sysCommonApiInterface));
    }

    /**
     * 修改数据接口状态
     */
    @ApiOperation("修改数据接口状态")
    @RequiresPermissions("system:apply-interface:state")
    @Log(title = "数据接口", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysCommonApiInterfaceService.updateSysCommonApiInterfaceState(id, state));
    }

    /**
     * 删除数据接口
     */
    @ApiOperation("删除数据接口")
    @RequiresPermissions("system:apply-interface:remove")
    @Log(title = "数据接口", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysCommonApiInterfaceService.deleteSysCommonApiInterfaceByIds(ids));
    }
}
