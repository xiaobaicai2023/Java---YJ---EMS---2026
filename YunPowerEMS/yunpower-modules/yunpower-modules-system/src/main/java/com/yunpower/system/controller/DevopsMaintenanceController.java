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
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.service.ISysDeptService;
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
import com.yunpower.system.domain.DevopsMaintenance;
import com.yunpower.system.service.IDevopsMaintenanceService;

/**
 * 维保记录
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "W 维保记录表")
@RestController
@RequestMapping("/maintenance")
public class DevopsMaintenanceController extends BaseController {
    @Autowired
    private IDevopsMaintenanceService devopsMaintenanceService;

    @Autowired
    private ISysDeptService deptService;
    /**
     * 查询维保记录列表
     */
    @ApiOperation("查询维保记录列表")
    @RequiresPermissions("system:maintenance:list")
    @GetMapping("/list")
    public TableDataInfo list(DevopsMaintenance devopsMaintenance) {
        startPage();
        List<DevopsMaintenance> list = devopsMaintenanceService.selectDevopsMaintenanceList(devopsMaintenance);
        list.forEach(this::handleStationName);
        return getDataTable(list);
    }

    /**
     * 导出维保记录列表
     */
    @ApiOperation("导出维保记录列表")
    @RequiresPermissions("system:maintenance:export")
    @Log(title = "维保记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DevopsMaintenance devopsMaintenance) {
        List<DevopsMaintenance> list = devopsMaintenanceService.selectDevopsMaintenanceList(devopsMaintenance);
        ExcelUtil<DevopsMaintenance> util = new ExcelUtil<DevopsMaintenance>(DevopsMaintenance.class);
        util.exportExcel(response, list, "维保记录数据");
    }

    /**
     * 获取维保记录详细信息
     */
    @ApiOperation("获取维保记录详细信息")
    @RequiresPermissions("system:maintenance:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        DevopsMaintenance info = devopsMaintenanceService.selectDevopsMaintenanceById(id);
        if(info !=null){
            handleStationName(info);
        }
        return success(info);
    }

    /**
     * 新增维保记录
     */
    @ApiOperation("新增维保记录")
    @RequiresPermissions("system:maintenance:add")
    @Log(title = "维保记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DevopsMaintenance devopsMaintenance) {
        if (StringUtils.longIsBlack0(devopsMaintenance.getStationId())) {
            return error("请选择站点");
        }
        return toAjax(devopsMaintenanceService.insertDevopsMaintenance(devopsMaintenance));
    }

    /**
     * 修改维保记录
     */
    @ApiOperation("修改维保记录")
    @RequiresPermissions("system:maintenance:edit")
    @Log(title = "维保记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DevopsMaintenance devopsMaintenance) {
        if (StringUtils.longIsBlack0(devopsMaintenance.getStationId())) {
            return error("请选择站点");
        }
        return toAjax(devopsMaintenanceService.updateDevopsMaintenance(devopsMaintenance));
    }

    /**
     * 修改维保记录状态
     */
    @ApiOperation("修改维保记录状态")
    @RequiresPermissions("system:maintenance:state")
    @Log(title = "维保记录", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(devopsMaintenanceService.updateDevopsMaintenanceState(new DevopsMaintenance(), id, state));
    }

    /**
     * 删除维保记录
     */
    @ApiOperation("删除维保记录")
    @RequiresPermissions("system:maintenance:remove")
    @Log(title = "维保记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(devopsMaintenanceService.deleteDevopsMaintenanceByIds(new DevopsMaintenance(), ids));
    }

    /**
     * 处理站点名称
     * */
    private void handleStationName(DevopsMaintenance devopsMaintenance){
        SysDept dept = deptService.selectDeptById(devopsMaintenance.getDeptId());
        if (dept != null) {
            devopsMaintenance.setStationName(dept.getDeptName());
        }
    }
}
