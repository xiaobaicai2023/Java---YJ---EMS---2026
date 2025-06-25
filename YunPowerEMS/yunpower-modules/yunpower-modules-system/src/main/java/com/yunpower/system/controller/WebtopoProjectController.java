package com.yunpower.system.controller;

import com.alibaba.fastjson2.*;
import com.yunpower.common.core.DoubleUtils;
import com.yunpower.common.core.constant.CacheConstants;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.web.page.TableDataInfo;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.system.controller.vo.SvgRuntimeVO;
import com.yunpower.system.domain.WebtopoDeviceSvgnode;
import com.yunpower.system.domain.WebtopoProject;
import com.yunpower.system.service.IWebtopoDeviceSvgnodeService;
import com.yunpower.system.service.IWebtopoProjectService;
import com.yunpower.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目列
 */
@Api(tags = "W 接线图项目表")
@RestController
@RequestMapping("/webtopo/project")
public class WebtopoProjectController extends BaseController {

    @Autowired
    private IWebtopoProjectService webtopoProjectService;

    @Autowired
    private IWebtopoDeviceSvgnodeService webtopoDeviceSvgnodeService;

    @Autowired
    private RedisService redisService;

    /**
     * 查询项目列列表
     */
    @ApiOperation("查询项目列列表")
    @RequiresPermissions("system:maotu:list")
    @GetMapping("/list")
    public TableDataInfo list(WebtopoProject webtopoProject) {
        startPage();
        List<WebtopoProject> list = webtopoProjectService.selectWebtopoProjectList(webtopoProject);
        return getDataTable(list);
    }

    /**
     * 获取项目列详细信息
     */
    @ApiOperation("获取项目列详细信息")
    @RequiresPermissions("system:maotu:query")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long projectId) {
        return success(webtopoProjectService.selectWebtopoProjectByProjectId(projectId));
    }

    /**
     * 新增项目列
     */
    @ApiOperation("新增项目")
    @RequiresPermissions("system:maotu:add")
    @Log(title = "接线图管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WebtopoProject webtopoProject) {
        return toAjax(webtopoProjectService.insertWebtopoProject(webtopoProject));
    }

    /**
     * 修改项目列
     */
    @ApiOperation("修改项目")
    @RequiresPermissions("system:maotu:edit")
    @Log(title = "接线图管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WebtopoProject webtopoProject) {
        return toAjax(webtopoProjectService.updateWebtopoProject(webtopoProject, true));
    }

    /**
     * 保存项目信息
     */
    @ApiOperation("修改项目")
    @RequiresPermissions("system:maotu:edit")
    @Log(title = "接线图管理", businessType = BusinessType.UPDATE)
    @PutMapping("/save")
    public AjaxResult save(@RequestBody WebtopoProject webtopoProject) {
        return toAjax(webtopoProjectService.updateWebtopoProject(webtopoProject, false));
    }

    /**
     * 删除项目列
     */
    @ApiOperation("删除项目")
    @RequiresPermissions("system:maotu:remove")
    @Log(title = "接线图管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds) {
        return toAjax(webtopoProjectService.deleteWebtopoProjectByProjectIds(projectIds));
    }

    /**
     * 导出项目列列表
     */
    @ApiOperation("导出项目列表")
    @RequiresPermissions("system:maotu:export")
    @Log(title = "接线图管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WebtopoProject webtopoProject) {
        List<WebtopoProject> list = webtopoProjectService.selectWebtopoProjectList(webtopoProject);
        ExcelUtil<WebtopoProject> util = new ExcelUtil<WebtopoProject>(WebtopoProject.class);
        util.exportExcel(response, list, "接线图项目数据");
    }

    /**
     * 获取显示变量（实时数据）
     */
    @ApiOperation("获取显示变量（实时数据）")
    @GetMapping("/runTimeList")
    public AjaxResult getrunTimeList(Long projectId) {

        // 获取所有的变量
        WebtopoDeviceSvgnode deviceSvgnode = new WebtopoDeviceSvgnode();
        deviceSvgnode.setProjectId(projectId);
        List<WebtopoDeviceSvgnode> varList = webtopoDeviceSvgnodeService.selectWebtopoDeviceSvgnodeList(deviceSvgnode);
        if (varList.isEmpty()) {
            return success();
        }

        // 保存结果
        List<SvgRuntimeVO> result = new ArrayList<>();

        for (WebtopoDeviceSvgnode item : varList) {
            SvgRuntimeVO vo = null;
            JSONObject js=redisService.getCacheObject(getVariableCacheKey(item.getDeviceProp()));
            String json ="";
            if(js!=null) {
            	json = js.toString();           	
            }            
            if (!StringUtils.isEmpty(json)) {
                vo = JSON.parseObject(json, SvgRuntimeVO.class);
            }
            if (vo == null) {
                vo = new SvgRuntimeVO();
                vo.setValue("--");
                vo.setTime("--");
                vo.setSvgNodeId("--");
            } else {
                double _value = DoubleUtils.stringToDouble(vo.getValue().toString());
                if (item.getDevicePropType() == 1) {
                    vo.setValue(_value);
                } else {
                    vo.setValue((int)_value);
                }
//                vo.setValue(FormatUtils.fmt2point(Float.parseFloat(vo.getValue().toString())));
            }
            vo.setKey(item.getSvgNodeProp());
            vo.setSvgNodeId(item.getSvgNodeId());
            vo.setVarSn(item.getDeviceProp());
            result.add(vo);
        }

        return success(result);
    }

    // 变量数据存储KEY
    private String getVariableCacheKey(String varSn) {
        return CacheConstants.MONITOR_DEVICE_VARSN_KEY + varSn;
    }
}
