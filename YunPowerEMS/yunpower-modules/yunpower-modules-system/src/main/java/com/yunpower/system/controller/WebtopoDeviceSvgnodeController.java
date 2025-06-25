package com.yunpower.system.controller;

import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.system.domain.WebtopoDeviceSvgnode;
import com.yunpower.system.service.IWebtopoDeviceSvgnodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * svg与设备绑定
 */
@Api(tags = "W 接线图SVG表")
@RestController
@RequestMapping("/webtopo/svgnode")
public class WebtopoDeviceSvgnodeController extends BaseController {

    @Autowired
    private IWebtopoDeviceSvgnodeService webtopoDeviceSvgnodeService;

    /**
     * 查询svg与设备绑定列表
     */
    @ApiOperation("查询svg与设备绑定列表")
    @GetMapping("/list")
    public AjaxResult list(WebtopoDeviceSvgnode webtopoDeviceSvgnode) {
        List<WebtopoDeviceSvgnode> list = webtopoDeviceSvgnodeService.selectWebtopoDeviceSvgnodeList(webtopoDeviceSvgnode);
        return success(list);
    }

    /**
     * 获取svg与设备绑定详细信息
     */
    @ApiOperation("获取svg与设备绑定详细信息")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long id) {
        return success(webtopoDeviceSvgnodeService.selectWebtopoDeviceSvgnodeByProjectId(id));
    }

    /**
     * 新增svg与设备绑定
     */
    @ApiOperation("新增svg与设备绑定")
    @PostMapping
    public AjaxResult add(@RequestBody WebtopoDeviceSvgnode webtopoDeviceSvgnode) {
        return toAjax(webtopoDeviceSvgnodeService.insertWebtopoDeviceSvgnode(webtopoDeviceSvgnode));
    }

    /**
     * 修改svg与设备绑定
     */
    @ApiOperation("新增svg与设备绑定")
    @PutMapping
    public AjaxResult edit(@RequestBody WebtopoDeviceSvgnode webtopoDeviceSvgnode) {
        return toAjax(webtopoDeviceSvgnodeService.updateWebtopoDeviceSvgnode(webtopoDeviceSvgnode));
    }

    /**
     * 删除svg与设备绑定
     */
    @ApiOperation("删除svg与设备绑定")
    @DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds) {
        return toAjax(webtopoDeviceSvgnodeService.deleteWebtopoDeviceSvgnodeByProjectIds(projectIds));
    }

    /**
     * 解除svg与设备绑定
     */
    @ApiOperation("解除svg与设备绑定")
    @DeleteMapping("/unbind")
    public AjaxResult unbind(@RequestBody WebtopoDeviceSvgnode webtopoDeviceSvgnode) {
        return toAjax(webtopoDeviceSvgnodeService.unbindWebtopoDeviceSvgnode(webtopoDeviceSvgnode));
    }

    /**
     * 导出svg与设备绑定列表
     */
    @ApiOperation("导出svg与设备绑定列表")
    @PostMapping("/export")
    public void export(HttpServletResponse response, WebtopoDeviceSvgnode webtopoDeviceSvgnode) {
        List<WebtopoDeviceSvgnode> list = webtopoDeviceSvgnodeService.selectWebtopoDeviceSvgnodeList(webtopoDeviceSvgnode);
        ExcelUtil<WebtopoDeviceSvgnode> util = new ExcelUtil<WebtopoDeviceSvgnode>(WebtopoDeviceSvgnode.class);
        util.exportExcel(response, list, "svg与设备绑定数据");
    }
}
