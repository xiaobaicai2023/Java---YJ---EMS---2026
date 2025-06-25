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
import com.yunpower.system.domain.MonitorDeviceAssociation;
import com.yunpower.system.service.IMonitorDeviceAssociationService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 能源监控设备关联
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "J 监控设备关联表")
@RestController
@RequestMapping("/association")
public class MonitorDeviceAssociationController extends BaseController
{
    @Autowired
    private IMonitorDeviceAssociationService monitorDeviceAssociationService;

    /**
     * 查询能源监控设备关联列表
     */
    @ApiOperation("查询能源监控设备关联列表")
    @RequiresPermissions("system:association:list")
    @GetMapping("/list")
    public TableDataInfo list(MonitorDeviceAssociation monitorDeviceAssociation)
    {
        startPage();
        List<MonitorDeviceAssociation> list = monitorDeviceAssociationService.selectMonitorDeviceAssociationList(monitorDeviceAssociation);
        return getDataTable(list);
    }

    /**
     * 查询能源监控设备关联列表（不分页）
     */
    @ApiOperation("查询能源监控设备关联列表（不分页）")
    @RequiresPermissions("system:association:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(MonitorDeviceAssociation monitorDeviceAssociation) {
        List<MonitorDeviceAssociation> list = monitorDeviceAssociationService.selectMonitorDeviceAssociationList(monitorDeviceAssociation);
        return success(list);
    }

    /**
     * 导出能源监控设备关联列表
     */
    @ApiOperation("导出能源监控设备关联列表")
    @RequiresPermissions("system:association:export")
    @Log(title = "能源监控设备关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MonitorDeviceAssociation monitorDeviceAssociation)
    {
        List<MonitorDeviceAssociation> list = monitorDeviceAssociationService.selectMonitorDeviceAssociationList(monitorDeviceAssociation);
        ExcelUtil<MonitorDeviceAssociation> util = new ExcelUtil<MonitorDeviceAssociation>(MonitorDeviceAssociation.class);
        util.exportExcel(response, list, "能源监控设备关联数据");
    }

    /**
     * 获取能源监控设备关联详细信息
     */
    @ApiOperation("获取能源监控设备关联详细信息")
    @RequiresPermissions("system:association:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(monitorDeviceAssociationService.selectMonitorDeviceAssociationById(id));
    }

    /**
     * 新增能源监控设备关联
     */
    @ApiOperation("新增能源监控设备关联")
    @RequiresPermissions("system:association:add")
    @Log(title = "能源监控设备关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MonitorDeviceAssociation monitorDeviceAssociation)
    {
        return toAjax(monitorDeviceAssociationService.insertMonitorDeviceAssociation(monitorDeviceAssociation));
    }

    /**
     * 修改能源监控设备关联
     */
    @ApiOperation("修改能源监控设备关联")
    @RequiresPermissions("system:association:edit")
    @Log(title = "能源监控设备关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MonitorDeviceAssociation monitorDeviceAssociation)
    {
        return toAjax(monitorDeviceAssociationService.updateMonitorDeviceAssociation(monitorDeviceAssociation));
    }

    /**
     * 删除能源监控设备关联
     */
    @ApiOperation("删除能源监控设备关联")
    @RequiresPermissions("system:association:remove")
    @Log(title = "能源监控设备关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(monitorDeviceAssociationService.deleteMonitorDeviceAssociationByIds(ids));
    }
}
