package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.domain.CommunicationDeviceAreaMap;
import com.yunpower.system.service.ICommunicationDeviceAreaMapService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 通讯设备数据区域类型（公共）
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "T 通讯设备数据区域类型（公共）表")
@RestController
@RequestMapping("/area-map")
public class CommunicationDeviceAreaMapController extends BaseController
{
    @Autowired
    private ICommunicationDeviceAreaMapService communicationDeviceAreaMapService;

    /**
     * 查询通讯设备数据区域类型（公共）列表
     */
    @ApiOperation("查询通讯设备数据区域类型（公共）列表")
    @RequiresPermissions("system:area-map:list")
    @GetMapping("/list")
    public TableDataInfo list(CommunicationDeviceAreaMap communicationDeviceAreaMap)
    {
        startPage();
        List<CommunicationDeviceAreaMap> list = communicationDeviceAreaMapService.selectCommunicationDeviceAreaMapList(communicationDeviceAreaMap);
        return getDataTable(list);
    }

    /**
     * 查询通讯设备数据区域类型（公共）列表（不分页）
     */
    @ApiOperation("查询通讯设备数据区域类型（公共）列表（不分页）")
    @RequiresPermissions("system:area-map:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(CommunicationDeviceAreaMap communicationDeviceAreaMap) {
        List<CommunicationDeviceAreaMap> list = communicationDeviceAreaMapService.selectCommunicationDeviceAreaMapList(communicationDeviceAreaMap);
        return success(list);
    }

    /**
     * 导出通讯设备数据区域类型（公共）列表
     */
    @ApiOperation("导出通讯设备数据区域类型（公共）列表")
    @RequiresPermissions("system:area-map:export")
    @Log(title = "通讯设备数据区域类型（公共）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CommunicationDeviceAreaMap communicationDeviceAreaMap)
    {
        List<CommunicationDeviceAreaMap> list = communicationDeviceAreaMapService.selectCommunicationDeviceAreaMapList(communicationDeviceAreaMap);
        ExcelUtil<CommunicationDeviceAreaMap> util = new ExcelUtil<CommunicationDeviceAreaMap>(CommunicationDeviceAreaMap.class);
        util.exportExcel(response, list, "通讯设备数据区域类型（公共）数据");
    }

    /**
     * 获取通讯设备数据区域类型（公共）详细信息
     */
    @ApiOperation("获取通讯设备数据区域类型（公共）详细信息")
    @RequiresPermissions("system:area-map:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(communicationDeviceAreaMapService.selectCommunicationDeviceAreaMapById(id));
    }
}
