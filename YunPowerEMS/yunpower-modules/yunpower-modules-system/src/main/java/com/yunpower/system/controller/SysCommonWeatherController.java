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
import com.yunpower.system.domain.SysCommonWeather;
import com.yunpower.system.service.ISysCommonWeatherService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 通用天气数据
 * 
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "T 天气数据表")
@RestController
@RequestMapping("/weather")
public class SysCommonWeatherController extends BaseController
{
    @Autowired
    private ISysCommonWeatherService sysCommonWeatherService;

    /**
     * 查询通用天气数据列表
     */
    @ApiOperation("查询通用天气数据列表")
    @RequiresPermissions("system:weather:list")
    @GetMapping("/list")
    public TableDataInfo list(SysCommonWeather sysCommonWeather)
    {
        startPage();
        List<SysCommonWeather> list = sysCommonWeatherService.selectSysCommonWeatherList(sysCommonWeather);
        return getDataTable(list);
    }

    /**
     * 导出通用天气数据列表
     */
    @ApiOperation("导出通用天气数据列表")
    @RequiresPermissions("system:weather:export")
    @Log(title = "通用天气数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommonWeather sysCommonWeather)
    {
        List<SysCommonWeather> list = sysCommonWeatherService.selectSysCommonWeatherList(sysCommonWeather);
        ExcelUtil<SysCommonWeather> util = new ExcelUtil<SysCommonWeather>(SysCommonWeather.class);
        util.exportExcel(response, list, "通用天气数据数据");
    }

    /**
     * 获取通用天气数据详细信息
     */
    @ApiOperation("获取通用天气数据详细信息")
    @RequiresPermissions("system:weather:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysCommonWeatherService.selectSysCommonWeatherById(id));
    }
}
