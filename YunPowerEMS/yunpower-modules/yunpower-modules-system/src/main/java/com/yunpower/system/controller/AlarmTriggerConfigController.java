package com.yunpower.system.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.system.service.*;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.system.domain.AlarmTriggerCategory;
import com.yunpower.system.domain.MonitorDevice;
import com.yunpower.system.domain.MonitorDeviceVar;
import com.yunpower.system.service.*;
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
import com.yunpower.system.domain.AlarmTriggerConfig;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 报警配置
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "B 报警配置表")
@RestController
@RequestMapping("/trigger-config")
public class AlarmTriggerConfigController extends BaseController {
    @Autowired
    private IAlarmTriggerConfigService alarmTriggerConfigService;

    @Autowired
    private IAlarmTriggerCategoryService categoryService;

    @Autowired
    private IMonitorDeviceVarService monitorDeviceVarService;

    @Autowired
    private IMonitorDeviceService monitorDevicerService;

    @Autowired
    private ISysCommonDictDataService dictDataService;

    /**
     * 查询报警配置列表
     */
    @ApiOperation("查询报警配置列表")
    @RequiresPermissions("system:trigger-config:list")
    @GetMapping("/list")
    public TableDataInfo list(AlarmTriggerConfig alarmTriggerConfig) {
        startPage();
        List<AlarmTriggerConfig> list = alarmTriggerConfigService.selectAlarmTriggerConfigList(alarmTriggerConfig);
        packageTriggerConfig(list);
        return getDataTable(list);
    }

    //region 公共：封装设备具体信息
    private void packageTriggerConfig(List<AlarmTriggerConfig> list) {
        for (AlarmTriggerConfig item : list) {
            MonitorDeviceVar deviceVar = monitorDeviceVarService.selectMonitorDeviceVarById(item.getVarId());
            if (StringUtils.isNotNull(deviceVar)) {
                item.setVarName(deviceVar.getVarName());
            }

            MonitorDevice device = monitorDevicerService.selectMonitorDeviceById(item.getDeviceId());
            if (StringUtils.isNotNull(device)) {
                item.setDeviceName(device.getDeviceName());
            }

            AlarmTriggerCategory category = categoryService.selectAlarmTriggerCategoryById(item.getCategoryId());
            if (StringUtils.isNotNull(category)) {
                item.setCategoryName(category.getTriggerName());
            }

            String varTypeName = dictDataService.selectDictLabel("sys_variable_type", item.getVarType() + "");
            item.setVarTypeName(varTypeName);
        }
    }
    //endregion

    /**
     * 导出报警配置列表
     */
    @ApiOperation("导出报警配置列表")
    @RequiresPermissions("system:trigger-config:export")
    @Log(title = "报警配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlarmTriggerConfig alarmTriggerConfig) {
        List<AlarmTriggerConfig> list = alarmTriggerConfigService.selectAlarmTriggerConfigList(alarmTriggerConfig);
        ExcelUtil<AlarmTriggerConfig> util = new ExcelUtil<AlarmTriggerConfig>(AlarmTriggerConfig.class);
        util.exportExcel(response, list, "报警配置数据");
    }

    /**
     * 获取报警配置详细信息
     */
    @ApiOperation("获取报警配置详细信息")
    @RequiresPermissions("system:trigger-config:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        AlarmTriggerConfig alarmTriggerConfig = alarmTriggerConfigService.selectAlarmTriggerConfigById(id);
        if (alarmTriggerConfig == null) {
            return success();
        }

        List<AlarmTriggerConfig> list = new ArrayList<>();
        list.add(alarmTriggerConfig);
        packageTriggerConfig(list);
        return success(list.get(0));
    }

    /**
     * 新增报警配置
     */
    @ApiOperation("新增报警配置")
    @RequiresPermissions("system:trigger-config:add")
    @Log(title = "报警配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmTriggerConfig alarmTriggerConfig) {
        int rows = 0;
        if (alarmTriggerConfig.getVarIds() != null && alarmTriggerConfig.getVarIds().length > 0) {
            for (String varId : alarmTriggerConfig.getVarIds()) {
                // 获取变量信息
                MonitorDeviceVar deviceVar = monitorDeviceVarService.selectMonitorDeviceVarById(Long.parseLong(varId));
                if (deviceVar == null) {
                    continue;
                }

                alarmTriggerConfig.setVarId(deviceVar.getId());
                alarmTriggerConfig.setVarSn(deviceVar.getVarSn());
                alarmTriggerConfig.setDeviceId(deviceVar.getDeviceId());
                alarmTriggerConfig.setDeviceSn(deviceVar.getDeviceSn());
                rows = alarmTriggerConfigService.insertAlarmTriggerConfig(alarmTriggerConfig);
            }
        } else {
            rows = alarmTriggerConfigService.insertAlarmTriggerConfig(alarmTriggerConfig);
        }
        return toAjax(rows);
    }

    /**
     * 修改报警配置
     */
    @ApiOperation("修改报警配置")
    @RequiresPermissions("system:trigger-config:edit")
    @Log(title = "报警配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlarmTriggerConfig alarmTriggerConfig) {
        return toAjax(alarmTriggerConfigService.updateAlarmTriggerConfig(alarmTriggerConfig));
    }

    /**
     * 修改报警配置状态
     */
    @ApiOperation("修改报警配置状态")
    @RequiresPermissions("system:trigger-config:state")
    @Log(title = "报警配置", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus/{id}/{state}")
    public AjaxResult changeStatus(@PathVariable Long id, @PathVariable Integer state) {
        return toAjax(alarmTriggerConfigService.updateAlarmTriggerConfigState(new AlarmTriggerConfig(), id, state));
    }

    /**
     * 删除报警配置
     */
    @ApiOperation("删除报警配置")
    @RequiresPermissions("system:trigger-config:remove")
    @Log(title = "报警配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alarmTriggerConfigService.deleteAlarmTriggerConfigByIds(new AlarmTriggerConfig(), ids));
    }
}
