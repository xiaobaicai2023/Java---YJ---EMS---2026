package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.StringUtils;
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
import com.yunpower.system.domain.SysLeaveMessage;
import com.yunpower.system.service.ISysLeaveMessageService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 留言回复管理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "L 留言回复管理表")
@RestController
@RequestMapping("/leave-message")
public class SysLeaveMessageController extends BaseController {
    @Autowired
    private ISysLeaveMessageService sysLeaveMessageService;

    /**
     * 查询留言回复管理列表
     */
    @ApiOperation("查询留言回复管理列表")
    @RequiresPermissions("system:leave-message:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLeaveMessage sysLeaveMessage) {
        if (StringUtils.isBlank(sysLeaveMessage.getTableName())) {
            return getDataTable("请输入tablename");
        }
        if (StringUtils.longIsBlack0(sysLeaveMessage.getRecordId())) {
            return getDataTable("请输入recordid");
        }

        startPage();
        List<SysLeaveMessage> list = sysLeaveMessageService.selectSysLeaveMessageList(sysLeaveMessage);
        return getDataTable(list);
    }

    /**
     * 查询留言回复管理列表（不分页）
     */
    @ApiOperation("查询留言回复管理列表（不分页）")
    @RequiresPermissions("system:leave-message:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysLeaveMessage sysLeaveMessage) {
        if (StringUtils.isBlank(sysLeaveMessage.getTableName())) {
            return error("请输入tableName");
        }
        if (StringUtils.longIsBlack0(sysLeaveMessage.getRecordId())) {
            return error("请输入recordId");
        }

        List<SysLeaveMessage> list = sysLeaveMessageService.selectSysLeaveMessageList(sysLeaveMessage);
        return success(list);
    }

    /**
     * 导出留言回复管理列表
     */
    @ApiOperation("导出留言回复管理列表")
    @RequiresPermissions("system:leave-message:export")
    @Log(title = "留言回复管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLeaveMessage sysLeaveMessage) {
        List<SysLeaveMessage> list = sysLeaveMessageService.selectSysLeaveMessageList(sysLeaveMessage);
        ExcelUtil<SysLeaveMessage> util = new ExcelUtil<SysLeaveMessage>(SysLeaveMessage.class);
        util.exportExcel(response, list, "留言回复管理数据");
    }

    /**
     * 获取留言回复管理详细信息
     */
    @ApiOperation("获取留言回复管理详细信息")
    @RequiresPermissions("system:leave-message:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysLeaveMessageService.selectSysLeaveMessageById(id));
    }

    /**
     * 新增留言回复管理
     */
    @ApiOperation("新增留言回复管理")
    @RequiresPermissions("system:leave-message:add")
    @Log(title = "留言回复管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLeaveMessage sysLeaveMessage) {
        if (StringUtils.isBlank(sysLeaveMessage.getTableName())) {
            return error("请输入tableName");
        }
        if (StringUtils.longIsBlack0(sysLeaveMessage.getRecordId())) {
            return error("请输入recordId");
        }
        return toAjax(sysLeaveMessageService.insertSysLeaveMessage(sysLeaveMessage));
    }

    /**
     * 修改留言回复管理
     */
    @ApiOperation("修改留言回复管理")
    @RequiresPermissions("system:leave-message:edit")
    @Log(title = "留言回复管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLeaveMessage sysLeaveMessage) {
        return toAjax(sysLeaveMessageService.updateSysLeaveMessage(sysLeaveMessage));
    }

    /**
     * 修改留言回复管理状态
     */
    @ApiOperation("修改留言回复管理状态")
    @RequiresPermissions("system:leave-message:state")
    @Log(title = "留言回复管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysLeaveMessageService.updateSysLeaveMessageState(new SysLeaveMessage(), id, state));
    }

    /**
     * 删除留言回复管理
     */
    @ApiOperation("删除留言回复管理")
    @RequiresPermissions("system:leave-message:remove")
    @Log(title = "留言回复管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysLeaveMessageService.deleteSysLeaveMessageByIds(new SysLeaveMessage(), ids));
    }
}
