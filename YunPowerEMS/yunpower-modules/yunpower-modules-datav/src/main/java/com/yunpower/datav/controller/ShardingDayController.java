package com.yunpower.datav.controller;

import com.yunpower.datav.domain.ShardingDay;
import com.yunpower.datav.service.IShardingDayService;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日数据存储
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Api(tags = "C 日数据存储表")
@RestController
@RequestMapping("/sharding-day")
public class ShardingDayController extends BaseController {
    @Autowired
    private IShardingDayService shardingDayService;

    /**
     * 查询日数据存储列表
     * /system/day/list?params[beginTime]=2023-10-03 00:00:00&params[endTime]=2023-10-05 23:59:59
     */
    @ApiOperation("查询日数据存储列表")
    @RequiresPermissions("system:day:list")
    @GetMapping("/list")
    public TableDataInfo list(ShardingDay shardingDay) {
        if (shardingDay.getSaveTime() == null && shardingDay.getParams().isEmpty()) {
            return getDataTable("请输入查询时间");
        }

        startPage();
        List<ShardingDay> list = shardingDayService.selectShardingDayList(shardingDay);
        return getDataTable(list);
    }

    /**
     * 导出日数据存储列表
     */
    @ApiOperation("导出日数据存储列表")
    @RequiresPermissions("system:day:export")
    @Log(title = "日数据存储", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ShardingDay shardingDay) {
        List<ShardingDay> list = shardingDayService.selectShardingDayList(shardingDay);
        ExcelUtil<ShardingDay> util = new ExcelUtil<ShardingDay>(ShardingDay.class);
        util.exportExcel(response, list, "日数据存储数据");
    }

    /**
     * 获取日数据存储详细信息
     */
    @ApiOperation("获取日数据存储详细信息")
    @RequiresPermissions("system:day:query")
    @GetMapping(value = "/{variableName}")
    public AjaxResult getInfo(@PathVariable("variableName") String variableName) {
        return success(shardingDayService.selectShardingDayByVariableName(variableName));
    }

    /**
     * 查询：1小时数据
     */
    @ApiOperation("查询：1小时数据")
    @RequiresPermissions("system:day:query")
    @GetMapping(value = "/getHourData")
    public AjaxResult getHourData(ShardingDay shardingDay, Integer storageType) {
        shardingDay.setParams(DateUtils.dateToParamForBackOneDay("2023-10-17", "2023-10-17"));
        List<ShardingDay> list = shardingDayService.selectShardingDayForHour(shardingDay, storageType, 0);
        return success(list);
    }

    /**
     * 查询：30分钟数据
     */
    @ApiOperation("查询：30分钟数据")
    @RequiresPermissions("system:day:query")
    @GetMapping(value = "/getMinuteData30")
    public AjaxResult getMinuteData30(ShardingDay shardingDay, Integer storageType) {
        shardingDay.setParams(DateUtils.dateToParamForBackOneDay("2023-10-17", "2023-10-17"));
        List<ShardingDay> list = shardingDayService.selectShardingDayListForMinute30(shardingDay, storageType, 0);
        return success(list);
    }
}
