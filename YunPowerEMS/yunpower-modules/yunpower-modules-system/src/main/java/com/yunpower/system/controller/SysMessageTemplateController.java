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
import com.yunpower.system.domain.SysMessageTemplate;
import com.yunpower.system.service.ISysMessageTemplateService;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 消息模板
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "X 消息模板表")
@RestController
@RequestMapping("/message-template")
public class SysMessageTemplateController extends BaseController {
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    /**
     * 查询消息模板列表
     */
    @ApiOperation("查询消息模板列表")
    @RequiresPermissions("system:message-template:list")
    @GetMapping("/list")
    public TableDataInfo list(SysMessageTemplate sysMessageTemplate) {
        startPage();
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        return getDataTable(list);
    }

    /**
     * 查询消息模板列表（不分页）
     */
    @ApiOperation("查询消息模板列表（不分页）")
    @RequiresPermissions("system:message-template:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(SysMessageTemplate sysMessageTemplate) {
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        return success(list);
    }

    /**
     * 导出消息模板列表
     */
    @ApiOperation("导出消息模板列表")
    @RequiresPermissions("system:message-template:export")
    @Log(title = "消息模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMessageTemplate sysMessageTemplate) {
        List<SysMessageTemplate> list = sysMessageTemplateService.selectSysMessageTemplateList(sysMessageTemplate);
        ExcelUtil<SysMessageTemplate> util = new ExcelUtil<SysMessageTemplate>(SysMessageTemplate.class);
        util.exportExcel(response, list, "消息模板数据");
    }

    /**
     * 获取消息模板详细信息
     */
    @ApiOperation("获取消息模板详细信息")
    @RequiresPermissions("system:message-template:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysMessageTemplateService.selectSysMessageTemplateById(id));
    }

    /**
     * 新增消息模板
     */
    @ApiOperation("新增消息模板")
    @RequiresPermissions("system:message-template:add")
    @Log(title = "消息模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMessageTemplate sysMessageTemplate) {
        if (StringUtils.intIsBlack0(sysMessageTemplate.getTemplateType())) {
            return error("请选择模板类型");
        }
        if (StringUtils.isBlank(sysMessageTemplate.getTopic())) {
            return error("请选择消息主题");
        }
        if (StringUtils.intIsBlack0(sysMessageTemplate.getTemplateId())) {
            sysMessageTemplate.setTemplateId(0);
        }
        return toAjax(sysMessageTemplateService.insertSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * 修改消息模板
     */
    @ApiOperation("修改消息模板")
    @RequiresPermissions("system:message-template:edit")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMessageTemplate sysMessageTemplate) {
        if (StringUtils.intIsBlack0(sysMessageTemplate.getTemplateType())) {
            return error("请选择模板类型");
        }
        if (StringUtils.isBlank(sysMessageTemplate.getTopic())) {
            return error("请选择消息主题");
        }
        return toAjax(sysMessageTemplateService.updateSysMessageTemplate(sysMessageTemplate));
    }

    /**
     * 修改消息模板状态
     */
    @ApiOperation("修改消息模板状态")
    @RequiresPermissions("system:message-template:state")
    @Log(title = "消息模板", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(sysMessageTemplateService.updateSysMessageTemplateState(new SysMessageTemplate(), id, state));
    }

    /**
     * 删除消息模板
     */
    @ApiOperation("删除消息模板")
    @RequiresPermissions("system:message-template:remove")
    @Log(title = "消息模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysMessageTemplateService.deleteSysMessageTemplateByIds(new SysMessageTemplate(), ids));
    }
}
