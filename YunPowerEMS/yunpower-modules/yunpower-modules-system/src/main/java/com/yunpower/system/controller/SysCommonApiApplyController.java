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
import com.yunpower.system.domain.SysCommonApiApply;
import com.yunpower.system.service.ISysCommonApiApplyService;

/**
 * 第三方接入申请
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "D 第三方接入申请表")
@RestController
@RequestMapping("/apply")
public class SysCommonApiApplyController extends BaseController {
    @Autowired
    private ISysCommonApiApplyService sysCommonApiApplyService;

    /**
     * 查询第三方接入申请列表
     */
    @ApiOperation("查询第三方接入申请列表")
    @RequiresPermissions("system:apply:list")
    @GetMapping("/list")
    public TableDataInfo list(SysCommonApiApply sysCommonApiApply) {
        startPage();
        List<SysCommonApiApply> list = sysCommonApiApplyService.selectSysCommonApiApplyList(sysCommonApiApply);
        return getDataTable(list);
    }

    /**
     * 导出第三方接入申请列表
     */
    @ApiOperation("导出第三方接入申请列表")
    @RequiresPermissions("system:apply:export")
    @Log(title = "第三方接入申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommonApiApply sysCommonApiApply) {
        List<SysCommonApiApply> list = sysCommonApiApplyService.selectSysCommonApiApplyList(sysCommonApiApply);
        ExcelUtil<SysCommonApiApply> util = new ExcelUtil<SysCommonApiApply>(SysCommonApiApply.class);
        util.exportExcel(response, list, "第三方接入申请数据");
    }

    /**
     * 获取第三方接入申请详细信息
     */
    @ApiOperation("获取第三方接入申请详细信息")
    @RequiresPermissions("system:apply:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysCommonApiApplyService.selectSysCommonApiApplyById(id));
    }

    /**
     * 新增第三方接入申请
     */
    @ApiOperation("新增第三方接入申请")
    @RequiresPermissions("system:apply:add")
    @Log(title = "第三方接入申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCommonApiApply sysCommonApiApply) {
        if (sysCommonApiApply.getEffectMinute() == null) {
            sysCommonApiApply.setEffectMinute(0);
        }
        return toAjax(sysCommonApiApplyService.insertSysCommonApiApply(sysCommonApiApply));
    }

    /**
     * 修改第三方接入申请
     */
    @ApiOperation("修改第三方接入申请")
    @RequiresPermissions("system:apply:edit")
    @Log(title = "第三方接入申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCommonApiApply sysCommonApiApply) {
        return toAjax(sysCommonApiApplyService.updateSysCommonApiApply(sysCommonApiApply));
    }

    /**
     * 修改第三方接入申请状态
     */
    @ApiOperation("修改第三方接入申请状态")
    @RequiresPermissions("system:apply:state")
    @Log(title = "第三方接入申请", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysCommonApiApplyService.updateSysCommonApiApplyState(id, state));
    }

    /**
     * 删除第三方接入申请
     */
    @ApiOperation("删除第三方接入申请")
    @RequiresPermissions("system:apply:remove")
    @Log(title = "第三方接入申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysCommonApiApplyService.deleteSysCommonApiApplyByIds(ids));
    }
}
