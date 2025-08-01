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
import com.yunpower.system.domain.CommunicationDeviceArea;
import com.yunpower.system.service.ICommunicationDeviceAreaService;

/**
 * 通讯设备数据区域
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "T 通讯设备数据区域表")
@RestController
@RequestMapping("/channel-device-area")
public class CommunicationDeviceAreaController extends BaseController
{
    @Autowired
    private ICommunicationDeviceAreaService communicationDeviceAreaService;

    /**
     * 查询通讯设备数据区域列表
     */
    @ApiOperation("查询通讯设备数据区域列表")
    @RequiresPermissions("system:channel-device-area:list")
    @GetMapping("/list")
    public TableDataInfo list(CommunicationDeviceArea communicationDeviceArea)
    {
        startPage();
        List<CommunicationDeviceArea> list = communicationDeviceAreaService.selectCommunicationDeviceAreaList(communicationDeviceArea);
        return getDataTable(list);
    }

    /**
     * 查询通讯设备数据区域列表（不分页）
     */
    @ApiOperation("查询通讯设备数据区域列表（不分页）")
    @RequiresPermissions("system:channel-device-area:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(CommunicationDeviceArea communicationDeviceArea) {
        List<CommunicationDeviceArea> list = communicationDeviceAreaService.selectCommunicationDeviceAreaList(communicationDeviceArea);
        return success(list);
    }

    /**
     * 导出通讯设备数据区域列表
     */
    @ApiOperation("导出通讯设备数据区域列表")
    @RequiresPermissions("system:channel-device-area:export")
    @Log(title = "通讯设备数据区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommunicationDeviceArea communicationDeviceArea)
    {
        List<CommunicationDeviceArea> list = communicationDeviceAreaService.selectCommunicationDeviceAreaList(communicationDeviceArea);
        ExcelUtil<CommunicationDeviceArea> util = new ExcelUtil<CommunicationDeviceArea>(CommunicationDeviceArea.class);
        util.exportExcel(response, list, "通讯设备数据区域数据");
    }

    /**
     * 获取通讯设备数据区域详细信息
     */
    @ApiOperation("获取通讯设备数据区域详细信息")
    @RequiresPermissions("system:channel-device-area:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(communicationDeviceAreaService.selectCommunicationDeviceAreaById(id));
    }

    /**
     * 新增通讯设备数据区域
     */
    @ApiOperation("新增通讯设备数据区域")
    @RequiresPermissions("system:channel-device-area:add")
    @Log(title = "通讯设备数据区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommunicationDeviceArea communicationDeviceArea)
    {
        return toAjax(communicationDeviceAreaService.insertCommunicationDeviceArea(communicationDeviceArea));
    }

    /**
     * 修改通讯设备数据区域
     */
    @ApiOperation("修改通讯设备数据区域")
    @RequiresPermissions("system:channel-device-area:edit")
    @Log(title = "通讯设备数据区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CommunicationDeviceArea communicationDeviceArea)
    {
        return toAjax(communicationDeviceAreaService.updateCommunicationDeviceArea(communicationDeviceArea));
    }

    /**
     * 修改通讯设备数据区域状态
     */
    @ApiOperation("修改通讯设备数据区域状态")
    @RequiresPermissions("system:channel-device-area:state")
    @Log(title = "通讯设备数据区域", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(communicationDeviceAreaService.updateCommunicationDeviceAreaState(id, state));
    }

    /**
     * 删除通讯设备数据区域
     */
    @ApiOperation("删除通讯设备数据区域")
    @RequiresPermissions("system:channel-device-area:remove")
    @Log(title = "通讯设备数据区域", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(communicationDeviceAreaService.deleteCommunicationDeviceAreaByIds(ids));
    }
}
