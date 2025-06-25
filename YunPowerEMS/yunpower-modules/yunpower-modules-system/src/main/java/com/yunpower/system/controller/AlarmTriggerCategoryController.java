package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.GenRandomUtils;
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
import com.yunpower.system.domain.AlarmTriggerCategory;
import com.yunpower.system.service.IAlarmTriggerCategoryService;

/**
 * 报警类型
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "B 报警类型表")
@RestController
@RequestMapping("/trigger-category")
public class AlarmTriggerCategoryController extends BaseController {
    @Autowired
    private IAlarmTriggerCategoryService alarmTriggerCategoryService;

    /**
     * 查询报警类型列表
     */
    @ApiOperation("查询报警类型列表")
    @RequiresPermissions("system:trigger-category:list")
    @GetMapping("/list")
    public TableDataInfo list(AlarmTriggerCategory alarmTriggerCategory) {
        startPage();
        List<AlarmTriggerCategory> list = alarmTriggerCategoryService.selectAlarmTriggerCategoryList(alarmTriggerCategory);
        return getDataTable(list);
    }

    /**
     * 查询报警类型列表（不分页）
     */
    @ApiOperation("查询报警类型列表（不分页）")
    @RequiresPermissions("system:trigger-category:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(AlarmTriggerCategory alarmTriggerCategory) {
        List<AlarmTriggerCategory> list = alarmTriggerCategoryService.selectAlarmTriggerCategoryList(alarmTriggerCategory);
        return success(list);
    }

    /**
     * 导出报警类型列表
     */
    @ApiOperation("导出报警类型列表")
    @RequiresPermissions("system:trigger-category:export")
    @Log(title = "报警类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlarmTriggerCategory alarmTriggerCategory) {
        List<AlarmTriggerCategory> list = alarmTriggerCategoryService.selectAlarmTriggerCategoryList(alarmTriggerCategory);
        ExcelUtil<AlarmTriggerCategory> util = new ExcelUtil<AlarmTriggerCategory>(AlarmTriggerCategory.class);
        util.exportExcel(response, list, "报警类型数据");
    }

    /**
     * 获取报警类型详细信息
     */
    @ApiOperation("获取报警类型详细信息")
    @RequiresPermissions("system:trigger-category:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(alarmTriggerCategoryService.selectAlarmTriggerCategoryById(id));
    }

    /**
     * 新增报警类型
     */
    @ApiOperation("新增报警类型")
    @RequiresPermissions("system:trigger-category:add")
    @Log(title = "报警类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmTriggerCategory alarmTriggerCategory) {
        alarmTriggerCategory.setTriggerSn(GenRandomUtils.GenRandom(10));
        return toAjax(alarmTriggerCategoryService.insertAlarmTriggerCategory(alarmTriggerCategory));
    }

    /**
     * 修改报警类型
     */
    @ApiOperation("修改报警类型")
    @RequiresPermissions("system:trigger-category:edit")
    @Log(title = "报警类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlarmTriggerCategory alarmTriggerCategory) {
        return toAjax(alarmTriggerCategoryService.updateAlarmTriggerCategory(alarmTriggerCategory));
    }

    /**
     * 修改报警类型状态
     */
    @ApiOperation("修改报警类型状态")
    @RequiresPermissions("system:trigger-category:state")
    @Log(title = "报警类型", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(alarmTriggerCategoryService.updateAlarmTriggerCategoryState(new AlarmTriggerCategory(), id, state));
    }

    /**
     * 删除报警类型
     */
    @ApiOperation("删除报警类型")
    @RequiresPermissions("system:trigger-category:remove")
    @Log(title = "报警类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alarmTriggerCategoryService.deleteAlarmTriggerCategoryByIds(new AlarmTriggerCategory(), ids));
    }
}
