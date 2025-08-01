package com.yunpower.datav.controller;

import com.yunpower.datav.domain.ShardingMonthAccumulate;
import com.yunpower.datav.service.IShardingMonthAccumulateService;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.InnerAuth;
import com.yunpower.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 变量累积数据月存储
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Api(tags = "C 变量累积数据月存储表")
@RestController
@RequestMapping("/sharding-month-accumulate")
public class ShardingMonthAccumulateController extends BaseController {
    @Autowired
    private IShardingMonthAccumulateService shardingMonthAccumulateService;

    /**
     * 查询变量累积数据月存储列表
     */
    @ApiOperation("查询变量累积数据月存储列表")
    @RequiresPermissions("system:sharding-month-accumulate:list")
    @GetMapping("/list")
    public TableDataInfo list(ShardingMonthAccumulate shardingMonthAccumulate) {
        startPage();
        List<ShardingMonthAccumulate> list = shardingMonthAccumulateService.selectShardingMonthAccumulateList(shardingMonthAccumulate, 0);
        return getDataTable(list);
    }

    /**
     * 导出变量累积数据月存储列表
     */
    @ApiOperation("导出变量累积数据月存储列表")
    @RequiresPermissions("system:sharding-month-accumulate:export")
    @Log(title = "变量累积数据月存储", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ShardingMonthAccumulate shardingMonthAccumulate) {
        List<ShardingMonthAccumulate> list = shardingMonthAccumulateService.selectShardingMonthAccumulateList(shardingMonthAccumulate, 0);
        ExcelUtil<ShardingMonthAccumulate> util = new ExcelUtil<ShardingMonthAccumulate>(ShardingMonthAccumulate.class);
        util.exportExcel(response, list, "变量累积数据月存储数据");
    }

    /**
     * 获取变量累积数据月存储详细信息
     */
    @ApiOperation("获取变量累积数据月存储详细信息")
    @RequiresPermissions("system:sharding-month-accumulate:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(shardingMonthAccumulateService.selectShardingMonthAccumulateById(id));
    }

    /**
     * 新增变量累积数据月存储
     */
    @ApiOperation("新增变量累积数据月存储")
    @RequiresPermissions("system:sharding-month-accumulate:add")
    @Log(title = "变量累积数据月存储", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ShardingMonthAccumulate shardingMonthAccumulate) {
        return toAjax(shardingMonthAccumulateService.insertShardingMonthAccumulate(shardingMonthAccumulate));
    }

    /**
     * 修改变量累积数据月存储
     */
    @ApiOperation("修改变量累积数据月存储")
    @RequiresPermissions("system:sharding-month-accumulate:edit")
    @Log(title = "变量累积数据月存储", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ShardingMonthAccumulate shardingMonthAccumulate) {
        return toAjax(shardingMonthAccumulateService.updateShardingMonthAccumulate(shardingMonthAccumulate));
    }

    /**
     * 删除变量累积数据月存储
     */
    @ApiOperation("删除变量累积数据月存储")
    @RequiresPermissions("system:sharding-month-accumulate:remove")
    @Log(title = "变量累积数据月存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(shardingMonthAccumulateService.deleteShardingMonthAccumulateByIds(ids));
    }

    /**
     * 远程内部调用-重新更新峰谷数据
     * @param map 设备号
     * @return 更新结果
     */
    @InnerAuth
    @PostMapping("/updateShardingMonthAccumulateSeasonal/inner")
    public int updateShardingMonthAccumulateSeasonal(@RequestBody Map<String, Object> map) {
       return shardingMonthAccumulateService.updateShardingMonthAccumulateSeasonal(map);
    }

}
