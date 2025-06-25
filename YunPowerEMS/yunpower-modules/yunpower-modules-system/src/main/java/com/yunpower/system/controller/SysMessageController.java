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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunpower.system.domain.SysMessage;
import com.yunpower.system.service.ISysMessageService;

/**
 * 消息
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "X 消息表")
@RestController
@RequestMapping("/message")
public class SysMessageController extends BaseController {
    @Autowired
    private ISysMessageService sysMessageService;

    /**
     * 查询消息列表
     */
    @ApiOperation("查询消息列表")
    @RequiresPermissions("system:message:list")
    @GetMapping("/list")
    public TableDataInfo list(SysMessage sysMessage) {
        startPage();
        List<SysMessage> list = sysMessageService.selectSysMessageList(sysMessage);
        return getDataTable(list);
    }

    /**
     * 导出消息列表
     */
    @ApiOperation("导出消息列表")
    @RequiresPermissions("system:message:export")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMessage sysMessage) {
        List<SysMessage> list = sysMessageService.selectSysMessageList(sysMessage);
        ExcelUtil<SysMessage> util = new ExcelUtil<SysMessage>(SysMessage.class);
        util.exportExcel(response, list, "消息数据");
    }

    /**
     * 获取消息详细信息
     */
    @ApiOperation("获取消息详细信息")
    @RequiresPermissions("system:message:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysMessageService.selectSysMessageById(id));
    }

    /**
     * 删除消息
     */
    @ApiOperation("删除消息")
    @RequiresPermissions("system:message:remove")
    @Log(title = "消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysMessageService.deleteSysMessageByIds(new SysMessage(), ids));
    }

    @ApiOperation("清空消息")
    @RequiresPermissions("system:message:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        sysMessageService.cleanMessage();
        return success();
    }
}
