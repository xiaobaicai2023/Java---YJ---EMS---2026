package com.yunpower.datav.controller;

import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.service.IShardingMonthService;
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
 * 月统计数据存储
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Api(tags = "C 月统计数据存储表")
@RestController
@RequestMapping("/sharding-month")
public class ShardingMonthController extends BaseController {
    @Autowired
    private IShardingMonthService shardingMonthService;

    /**
     * 查询月统计数据存储列表
     */
    @ApiOperation("查询月统计数据存储列表")
    @RequiresPermissions("system:month:list")
    @GetMapping("/list")
    public TableDataInfo list(ShardingMonth shardingMonth) {
        if (shardingMonth.getSaveTime() == null && shardingMonth.getParams().isEmpty()) {
            return getDataTable("请输入查询时间");
        }
        List<ShardingMonth> list = shardingMonthService.selectShardingMonthList(shardingMonth, 1, 0);
        return getDataTable(list);
    }

    /**
     * 导出月统计数据存储列表
     */
    @ApiOperation("导出月统计数据存储列表")
    @RequiresPermissions("system:month:export")
    @Log(title = "月统计数据存储", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ShardingMonth shardingMonth) {
        List<ShardingMonth> list = shardingMonthService.selectShardingMonthList(shardingMonth, 1, 0);
        ExcelUtil<ShardingMonth> util = new ExcelUtil<ShardingMonth>(ShardingMonth.class);
        util.exportExcel(response, list, "月统计数据存储数据");
    }

    /**
     * 获取月统计数据存储详细信息
     */
    @ApiOperation("获取月统计数据存储详细信息")
    @RequiresPermissions("system:month:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(shardingMonthService.selectShardingMonthById(id));
    }

    /**
     * 查询：最大值
     */
    @ApiOperation("查询：最大值")
    @RequiresPermissions("system:month:query")
    @GetMapping(value = "/getDataMax")
    public AjaxResult getDataMax(ShardingMonth shardingMonth) {
        shardingMonth.setParams(DateUtils.dateToParamForMonth("2023-10-18", "2023-10-18"));
        Double data = shardingMonthService.selectShardingMonthMax(shardingMonth);
        return success(data);
    }

    /**
     * 查询：月统计数据
     */
    @ApiOperation("查询：月统计数据")
    @RequiresPermissions("system:month:query")
    @GetMapping(value = "/getStatisticForMonth")
    public AjaxResult getStatisticForMonth(ShardingMonth shardingMonth) {
        shardingMonth.setParams(DateUtils.dateToParamForMonth("2023-06-18", "2023-10-18"));
        List<ShardingMonth> list = shardingMonthService.selectShardingMonthStatisticForMonth(shardingMonth, 1, 0);
        return success(list);
    }

    /**
     * 查询：年统计数据
     */
    @ApiOperation("查询：年统计数据")
    @RequiresPermissions("system:month:query")
    @GetMapping(value = "/getStatisticForYear")
    public AjaxResult getStatisticForYear(ShardingMonth shardingMonth) {
        shardingMonth.setParams(DateUtils.dateToParamForMonth("2022-06-18", "2023-10-18"));
        List<ShardingMonth> list = shardingMonthService.selectShardingMonthStatisticForYear(shardingMonth, 1, 0);
        return success(list);
    }
}
