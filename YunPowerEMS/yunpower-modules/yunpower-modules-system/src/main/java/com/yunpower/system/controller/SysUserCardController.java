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
import com.yunpower.system.domain.SysUserCard;
import com.yunpower.system.service.ISysUserCardService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 用户充值卡
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "Y 用户充值卡表")
@RestController
@RequestMapping("/user-card")
public class SysUserCardController extends BaseController {
    @Autowired
    private ISysUserCardService sysUserCardService;

    /**
     * 查询用户充值卡列表
     */
    @ApiOperation("查询用户充值卡列表")
    @RequiresPermissions("system:user-card:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUserCard sysUserCard) {
        startPage();
        List<SysUserCard> list = sysUserCardService.selectSysUserCardList(sysUserCard);
        return getDataTable(list);
    }

    /**
     * 导出用户充值卡列表
     */
    @ApiOperation("导出用户充值卡列表")
    @RequiresPermissions("system:user-card:export")
    @Log(title = "用户充值卡", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUserCard sysUserCard) {
        List<SysUserCard> list = sysUserCardService.selectSysUserCardList(sysUserCard);
        ExcelUtil<SysUserCard> util = new ExcelUtil<SysUserCard>(SysUserCard.class);
        util.exportExcel(response, list, "用户充值卡数据");
    }

    /**
     * 获取用户充值卡详细信息
     */
    @ApiOperation("获取用户充值卡详细信息")
    @RequiresPermissions("system:user-card:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysUserCardService.selectSysUserCardById(id));
    }

    /**
     * 新增用户充值卡
     */
    @ApiOperation("新增用户充值卡")
    @RequiresPermissions("system:user-card:add")
    @Log(title = "用户充值卡", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUserCard sysUserCard) {
        return toAjax(sysUserCardService.insertSysUserCard(sysUserCard));
    }

    /**
     * 修改用户充值卡
     */
    @ApiOperation("修改用户充值卡")
    @RequiresPermissions("system:user-card:edit")
    @Log(title = "用户充值卡", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUserCard sysUserCard) {
        return toAjax(sysUserCardService.updateSysUserCard(sysUserCard));
    }

    /**
     * 修改用户充值卡状态
     */
    @ApiOperation("修改用户充值卡状态")
    @RequiresPermissions("system:user-card:state")
    @Log(title = "用户充值卡", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysUserCardService.updateSysUserCardState(new SysUserCard(), id, state));
    }

    /**
     * 删除用户充值卡
     */
    @ApiOperation("删除用户充值卡")
    @RequiresPermissions("system:user-card:remove")
    @Log(title = "用户充值卡", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysUserCardService.deleteSysUserCardByIds(new SysUserCard(), ids));
    }
}
