package com.yunpower.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.StringUtils;
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
import org.springframework.web.bind.annotation.*;
import com.yunpower.system.domain.MonitorDeviceVarMap;
import com.yunpower.system.service.IMonitorDeviceVarMapService;

/**
 * 监控设备变量索引地图
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "J 监控设备变量索引地图表")
@RestController
@RequestMapping("/var-map")
public class MonitorDeviceVarMapController extends BaseController {
    @Autowired
    private IMonitorDeviceVarMapService monitorDeviceVarMapService;

    /**
     * 查询监控设备变量索引地图列表
     */
    @ApiOperation("查询监控设备变量索引地图列表")
    @RequiresPermissions("system:var-map:list")
    @GetMapping("/list")
    public TableDataInfo list(MonitorDeviceVarMap monitorDeviceVarMap) {
        startPage();
        List<MonitorDeviceVarMap> list = monitorDeviceVarMapService.selectMonitorDeviceVarMapList(monitorDeviceVarMap);
        return getDataTable(list);
    }

    /**
     * 查询监控设备变量索引地图列表（不分页）
     */
    @ApiOperation("查询监控设备变量索引地图列表（不分页）")
    @RequiresPermissions("system:var-map:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(MonitorDeviceVarMap monitorDeviceVarMap) {
        List<MonitorDeviceVarMap> list = monitorDeviceVarMapService.selectMonitorDeviceVarMapList(monitorDeviceVarMap);
        return success(list);
    }

    /**
     * 导出监控设备变量索引地图列表
     */
    @ApiOperation("导出监控设备变量索引地图列表")
    @RequiresPermissions("system:var-map:export")
    @Log(title = "监控设备变量索引地图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MonitorDeviceVarMap monitorDeviceVarMap) {
        List<MonitorDeviceVarMap> list = monitorDeviceVarMapService.selectMonitorDeviceVarMapList(monitorDeviceVarMap);
        ExcelUtil<MonitorDeviceVarMap> util = new ExcelUtil<MonitorDeviceVarMap>(MonitorDeviceVarMap.class);
        util.exportExcel(response, list, "监控设备变量索引地图数据");
    }

    /**
     * 获取监控设备变量索引地图详细信息
     */
    @ApiOperation("获取监控设备变量索引地图详细信息")
    @RequiresPermissions("system:var-map:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(monitorDeviceVarMapService.selectMonitorDeviceVarMapById(id));
    }

    /**
     * 新增变量索引地图
     */
    @ApiOperation("新增变量索引地图")
    @RequiresPermissions("system:var-map:add")
    @Log(title = "监控设备变量索引地图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MonitorDeviceVarMap monitorDeviceVarMap) {
        //属性名称必填
        if (StringUtils.isNull(monitorDeviceVarMap.getIndexName())) {
            return error("请填写属性名称");
        }

        //如果不是父节点，则属性编码必填，且不能重复
        if (monitorDeviceVarMap.getParentId() > 0) {
            if (StringUtils.isNull(monitorDeviceVarMap.getIndexSn())) {
                return error("请填写属性编码");
            }

            //判断属性编码是否重复
            if (StringUtils.isNotEmpty(monitorDeviceVarMap.getIndexSn()) && monitorDeviceVarMapService.checkIndexSnUnique(monitorDeviceVarMap.getIndexSn())) {
                return error("该属性编码已存在");
            }
        }

        return toAjax(monitorDeviceVarMapService.insertMonitorDeviceVarMap(monitorDeviceVarMap));
    }

    /**
     * 修改变量索引地图
     */
    @ApiOperation("修改变量索引地图")
    @RequiresPermissions("system:var-map:edit")
    @Log(title = "监控设备变量索引地图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MonitorDeviceVarMap monitorDeviceVarMap) {
        return toAjax(monitorDeviceVarMapService.updateMonitorDeviceVarMap(monitorDeviceVarMap));
    }

    //region 修改变量索引地图状态

    /**
     * 修改变量索引地图状态
     */
    @ApiOperation("修改变量索引地图状态")
    @RequiresPermissions("system:var-map:state")
    @Log(title = "监控设备变量索引地图", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(monitorDeviceVarMapService.updateMonitorDeviceVarMapState(id, state));
    }
    //endregion

    //region 删除变量索引地图

    /**
     * 删除变量索引地图
     */
    @ApiOperation("删除变量索引地图")
    @RequiresPermissions("system:var-map:remove")
    @Log(title = "监控设备变量索引地图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        if (monitorDeviceVarMapService.hasChildrenById(id)) {
            return warn("该索引下含有子索引，不允许删除");
        }
        return toAjax(monitorDeviceVarMapService.deleteMonitorDeviceVarMapById(id));
    }
    //endregion

}
