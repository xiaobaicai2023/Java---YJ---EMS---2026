package com.yunpower.system.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.system.domain.AlarmTrigger;
import com.yunpower.system.domain.AlarmTriggerCategory;
import com.yunpower.system.domain.MonitorDevice;
import com.yunpower.system.domain.MonitorDeviceVar;
import com.yunpower.system.service.*;
import com.yunpower.common.core.enums.vo.EnumSOVO;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.api.domain.SysDept;
import com.yunpower.system.domain.*;
import com.yunpower.system.domain.jsonvo.ChartYAxisVo;
import com.yunpower.system.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.yunpower.common.log.annotation.Log;
import com.yunpower.common.log.enums.BusinessType;
import com.yunpower.common.security.annotation.RequiresPermissions;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.common.core.utils.poi.ExcelUtil;
import com.yunpower.common.core.web.page.TableDataInfo;

/**
 * 报警管理
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
@Api(tags = "B 报警管理表")
@RestController
@RequestMapping("/trigger")
public class AlarmTriggerController extends BaseController {
    @Autowired
    private IAlarmTriggerService alarmTriggerService;

    @Autowired
    private IAlarmTriggerCategoryService alarmTriggerCategoryService;

    @Autowired
    private ISysCommonDictService dictTypeService;

    @Autowired
    private IMonitorDeviceVarService monitorDeviceVarService;

    @Autowired
    private IMonitorDeviceService monitorDevicerService;

    @Autowired
    private ISysCommonDictDataService dictDataService;

    @Autowired
    private ISysDeptService deptService;

    //region 查询报警管理列表

    /**
     * 查询报警管理列表
     */
    @ApiOperation("查询报警管理列表")
    @RequiresPermissions("system:trigger:list")
    @GetMapping("/list")
    public TableDataInfo list(AlarmTrigger alarmTrigger) {
        startPage();
        List<AlarmTrigger> list = alarmTriggerService.selectAlarmTriggerList(alarmTrigger);
        for (AlarmTrigger item : list) {
            packageInfo(item);
        }
        return getDataTable(list);
    }

    @ApiOperation("查询报警管理列表（不分页）")
    @RequiresPermissions("system:trigger:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(AlarmTrigger alarmTrigger) {
        List<AlarmTrigger> list = alarmTriggerService.selectAlarmTriggerList(alarmTrigger);
        for (AlarmTrigger item : list) {
            packageInfo(item);
        }
        return success(list);
    }

    private void packageInfo(AlarmTrigger item) {
        //站点名称（部门名称）
        SysDept dept = deptService.selectDeptById(item.getDeptId());
        if (dept != null) {
            item.setStationName(dept.getDeptName());
        }

        if(StringUtils.isBlank(item.getDeviceName())){
            //设备名称
            MonitorDevice device = monitorDevicerService.selectMonitorDeviceById(item.getDeviceId());
            if (device != null) {
                item.setDeviceName(device.getDeviceName());
            }
        }


        //变量名称
        if(StringUtils.isBlank(item.getVarName())){
            MonitorDeviceVar deviceVar = monitorDeviceVarService.selectMonitorDeviceVarById(item.getVarId());
            if (deviceVar != null) {
                item.setVarName(deviceVar.getVarName());
            }
        }

        //触发条件名称
        if(StringUtils.isBlank(item.getCategoryName())){
            AlarmTriggerCategory category = alarmTriggerCategoryService.selectAlarmTriggerCategoryById(item.getCategoryId());
            if (category != null) {
                item.setCategoryName(category.getTriggerName());
            }
        }

        //事件名称
        item.setTriggerName("[" + item.getDeviceName() + "，" + item.getVarName() + "]" + item.getCategoryName());
    }
    //endregion

    //region 导出报警管理列表

    /**
     * 导出报警管理列表
     */
    @ApiOperation("导出报警管理列表")
    @RequiresPermissions("system:trigger:export")
    @Log(title = "报警管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AlarmTrigger alarmTrigger) {
        List<AlarmTrigger> list = alarmTriggerService.selectAlarmTriggerList(alarmTrigger);
        ExcelUtil<AlarmTrigger> util = new ExcelUtil<AlarmTrigger>(AlarmTrigger.class);
        util.exportExcel(response, list, "报警管理数据");
    }
    //endregion

    //region 获取报警管理详细信息

    /**
     * 获取报警管理详细信息
     */
    @ApiOperation("获取报警管理详细信息")
    @RequiresPermissions("system:trigger:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        AlarmTrigger dto = alarmTriggerService.selectAlarmTriggerById(id);
        packageInfo(dto);
        return success(dto);
    }
    //endregion

    //region 删除报警管理

    /**
     * 删除报警管理
     */
    @ApiOperation("删除报警管理")
    @RequiresPermissions("system:trigger:remove")
    @Log(title = "报警管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alarmTriggerService.deleteAlarmTriggerByIds(new AlarmTrigger(), ids));
    }
    //endregion

    //region 报警事件确认

    /**
     * 报警事件确认
     */
    @ApiOperation("报警事件确认")
    @RequiresPermissions("system:trigger:query")
    @PutMapping(value = "/confirmTrigger")
    public AjaxResult confirmTrigger(@RequestBody AlarmTrigger alarmTrigger) {
        AlarmTrigger update = new AlarmTrigger();
        update.setId(alarmTrigger.getId());
        update.setTriggerStatus(alarmTrigger.getTriggerStatus());
        update.setConfirmBy(alarmTrigger.getConfirmBy());
        update.setConfirmContent(alarmTrigger.getConfirmContent());
        update.setConfirmTime(new Date());
        return toAjax(alarmTriggerService.updateAlarmTrigger(update));
    }
    //endregion

    //region 根据【设备名称、报警级别】表格统计报警数据
    @ApiOperation("根据【设备名称、报警级别】表格统计报警数据")
    @RequiresPermissions("system:trigger:list")
    @GetMapping("/getStatisticlist")
    public TableDataInfo getStatisticlist(AlarmTrigger alarmTrigger) {
        startPage();
        List<AlarmTrigger> list = alarmTriggerService.selectDistinctDevice(alarmTrigger);

        //取出所有报警级别
        List<SysCommonDictData> alaramLevelList = dictTypeService.selectDictDataByType("sys_alaram_level");

        // 表头
        List<EnumSOVO> heads = new ArrayList<>();
        for (SysCommonDictData item : alaramLevelList) {
            EnumSOVO vo = new EnumSOVO();
            vo.setKey(item.getDictValue());
            vo.setValue(item.getDictLabel());
            heads.add(vo);
        }

        // 结果
        List<List<EnumSOVO>> result = new ArrayList<>();

        //循环取每一个设备不同报警级别的统计数据
        for (AlarmTrigger item : list) {
            //获取设备信息
            MonitorDevice device = monitorDevicerService.selectMonitorDeviceById(item.getDeviceId());
            if (device == null) {
                continue;
            }

            alarmTrigger.setDeviceId(item.getDeviceId());
            List<AlarmTrigger> staticList = alarmTriggerService.selectStatisticDataByAlarmLevel(alarmTrigger);

            //存储中间数据
            List<EnumSOVO> midList = new ArrayList<>();

            //获取站点信息（部门）
            SysDept sysDept = deptService.selectDeptById(item.getDeptId());

            EnumSOVO vo1 = new EnumSOVO();
            vo1.setKey("所属站点");
            vo1.setValue(sysDept == null ? "" : sysDept.getDeptName());
            midList.add(vo1);

            EnumSOVO vo2 = new EnumSOVO();
            vo2.setKey("设备名称");
            vo2.setValue(device.getDeviceName());
            midList.add(vo2);

            int total = 0;

            for (EnumSOVO head : heads) {
                List<AlarmTrigger> filterList = staticList.stream().filter(s -> s.getTriggerLevel().toString().equals(head.getKey())).collect(Collectors.toList());
                EnumSOVO vo = new EnumSOVO();
                vo.setKey(head.getValue().toString());
                if (filterList.size() > 0) {
                    int count = filterList.get(0).getStatisticValue();
                    vo.setValue(count);
                    total += count;
                } else {
                    vo.setValue(0);
                }
                midList.add(vo);
            }

            EnumSOVO vo3 = new EnumSOVO();
            vo3.setKey("总计");
            vo3.setValue(total);
            midList.add(vo3);

            result.add(midList);
        }
        return getDataTable(result);
    }
    //endregion

    //region 获取【级别统计】堆叠图表数据

    /**
     * 获取【级别统计】堆叠图表数据
     * 如（堆叠）：一级报警、二级报警、三级报警、故障、事件
     * X轴：日期
     * Y轴：报警数
     *
     * @param stationType 电站类型（1配电 2光伏）
     * @param beginTime   开始日期
     * @param endTime     结束日期
     * @param triggerStatus 报警状态
     * @return 结果
     */
    @ApiOperation("获取【级别统计】堆叠图表数据")
    @GetMapping("/getLevelStaticList")
    public AjaxResult getLevelStaticList(Integer stationType, String beginTime, String endTime,Integer triggerStatus) {
        AlarmTrigger alarmTrigger = new AlarmTrigger();
        alarmTrigger.setStationType(stationType);
        alarmTrigger.setParams(DateUtils.dateToParamForDayFormat(beginTime, endTime));
        alarmTrigger.setTriggerStatus(triggerStatus);
        List<AlarmTrigger> list = alarmTriggerService.selectStatisticDataByAlarmLevelDate(alarmTrigger);
        return success(packageLevelData(list));
    }

    // 将数据包装成堆叠图格式
    private Map<String, Object> packageLevelData(List<AlarmTrigger> alarmTriggerList) {

        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();

        // X轴（如：2023-10-20，2023-10-21...）
        List<String> xAxis = new ArrayList<>();

        // Y轴单位（如：用电量（kW-h））
        List<String> yAxisUnit = new ArrayList<>();
        yAxisUnit.add("个");

        // 数据展示单位
        List<String> dataUnit = new ArrayList<>();

        // 图表类型
        List<String> chartType = new ArrayList<>();
        chartType.add("bar");

        // Legend（如：用电量、电流、电压）
        List<String> legend = new ArrayList<>();

        // Y轴（存在多曲线情况）
        List<ChartYAxisVo> yAxis = new ArrayList<>();

        // 获取报警级别
        List<SysCommonDictData> dictDataList = dictTypeService.selectDictDataByType("sys_alaram_level");
        Map<String, String> levelMap = new HashMap<>();

        //把列表中出现的类型加到Legend里
        for (SysCommonDictData dictData : dictDataList) {
            dataUnit.add("个");
            for (AlarmTrigger item : alarmTriggerList) {
                if (item.getTriggerLevel().toString().equals(dictData.getDictValue())) {
                    legend.add(dictData.getDictLabel());
                    levelMap.put(dictData.getDictLabel(), dictData.getDictValue());
                    break;
                }
            }
        }

        // 无数据返回空列表
        if (alarmTriggerList.isEmpty()) {
            resultMap.put("xAxis", xAxis);
            resultMap.put("yAxisUnit", yAxisUnit);
            resultMap.put("dataUnit", dataUnit);
            resultMap.put("chartType", chartType);
            resultMap.put("legend", legend);
            resultMap.put("yAxis", yAxis);
            return resultMap;
        }

        // 以实际查询出来的日期做为X轴
        String beginTime = DateUtils.dateTime(alarmTriggerList.get(0).getHappenTime());
        String endTime = DateUtils.dateTime(alarmTriggerList.get(alarmTriggerList.size() - 1).getHappenTime());
        xAxis = DateUtils.getDayList(beginTime, endTime);

        // 这里有三层数据结构
        // 1.X轴时间列表
        // 2.Legend
        // 3.数据列表
        // 因此，循环关系不能错。最外层是Legend（levelList），中间层是X轴，内层是数据列表
        for (Map.Entry<String, String> entry : levelMap.entrySet()) {
            // 数据集合，没有补0
            List<Object> emptyList = new ArrayList<>();
            for (String x : xAxis) {
                List<AlarmTrigger> list = alarmTriggerList.stream()
                        .filter(s -> s.getTriggerLevel().toString().equals(entry.getValue()))
                        .filter(s -> DateUtils.dateTime(s.getHappenTime()).equals(x))
                        .collect(Collectors.toList());

                if (list.isEmpty()) {
                    emptyList.add(0);
                } else {
                    emptyList.add(list.get(0).getStatisticValue());
                }
            }

            ChartYAxisVo chartTempVo = new ChartYAxisVo();
            chartTempVo.setName(entry.getKey());
            chartTempVo.setGroupName("级别统计");
            chartTempVo.setChartType("bar");
            chartTempVo.setDataList(emptyList);
            yAxis.add(chartTempVo);
        }

        resultMap.put("xAxis", xAxis);
        resultMap.put("yAxisUnit", yAxisUnit);
        resultMap.put("dataUnit", dataUnit);
        resultMap.put("chartType", chartType);
        resultMap.put("legend", legend);
        resultMap.put("yAxis", yAxis);
        return resultMap;
    }
    //endregion

    //region 获取【类型统计】柱状图表数据

    /**
     * 获取【类型统计】柱状图表数据
     * X轴：自定义类型
     * Y轴：报警数
     *
     * @param stationType 电站类型（1配电 2光伏）
     * @param beginTime   开始日期
     * @param endTime     结束日期
     * @param triggerStatus 报警状态
     * @return 结果
     */
    @ApiOperation("获取【类型统计】柱状图表数据")
    @GetMapping("/getCategoryStaticList")
    public AjaxResult getCategoryStaticList(Integer stationType, String beginTime, String endTime,Integer triggerStatus) {
        AlarmTrigger alarmTrigger = new AlarmTrigger();
        alarmTrigger.setStationType(stationType);
        alarmTrigger.setParams(DateUtils.dateToParamForDayFormat(beginTime, endTime));
        alarmTrigger.setTriggerStatus(triggerStatus);
        List<AlarmTrigger> list = alarmTriggerService.selectStatisticDataByCategory(alarmTrigger);
        return success(packageCategoryData(list));
    }

    private Map<String, Object> packageCategoryData(List<AlarmTrigger> alarmTriggerList) {

        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();

        // X轴（如：2023-10-20，2023-10-21...）
        List<String> xAxis = new ArrayList<>();

        // Y轴单位（如：用电量（kW-h））
        List<String> yAxisUnit = new ArrayList<>();
        yAxisUnit.add("个");

        // 数据展示单位
        List<String> dataUnit = new ArrayList<>();
        dataUnit.add("个");

        // 图表类型
        List<String> chartType = new ArrayList<>();
        chartType.add("bar");

        // Legend（如：用电量、电流、电压）
        List<String> legend = new ArrayList<>();
        legend.add("报警数");

        // Y轴（存在多曲线情况）
        List<ChartYAxisVo> yAxis = new ArrayList<>();

        // 获取报警类型
        // 把列表中出现的类型加到Legend里
        List<AlarmTriggerCategory> categoryList = alarmTriggerCategoryService.selectAlarmTriggerCategoryList(new AlarmTriggerCategory());
        List<Long> cateList = new ArrayList<>();

        for (AlarmTriggerCategory category : categoryList) {
            for (AlarmTrigger item : alarmTriggerList) {
                if (category.getId().equals(item.getCategoryId())) {
                    xAxis.add(category.getTriggerName());
                    cateList.add(category.getId());
                    break;
                }
            }
        }

        // 无数据返回空列表
        if (alarmTriggerList.isEmpty()) {
            resultMap.put("xAxis", xAxis);
            resultMap.put("yAxisUnit", yAxisUnit);
            resultMap.put("dataUnit", dataUnit);
            resultMap.put("chartType", chartType);
            resultMap.put("legend", legend);
            resultMap.put("yAxis", yAxis);
            return resultMap;
        }

        // 以实际查询出来的日期做为X轴
        //String beginTime = DateUtils.dateTime(alarmTriggerList.get(0).getHappenTime());
        //String endTime = DateUtils.dateTime(alarmTriggerList.get(alarmTriggerList.size() - 1).getHappenTime());
        //xAxis = DateUtils.getDayList(beginTime, endTime);

        // 注意会出现一种情况，就是某一种类型某一天没有数据时会错位。所以要循环X轴（cateList，与X轴一一对应），而不是数据列表
        List<Object> emptyList = new ArrayList<>();
        for (Long cate : cateList) {
            List<AlarmTrigger> list = alarmTriggerList.stream().filter(s -> s.getCategoryId().equals(cate)).collect(Collectors.toList());
            if (list.isEmpty()) {
                emptyList.add(0);
            } else {
                emptyList.add(list.get(0).getStatisticValue());
            }
        }

        ChartYAxisVo chartTempVo = new ChartYAxisVo();
        chartTempVo.setName("报警数");
        chartTempVo.setChartType("bar");
        chartTempVo.setDataList(emptyList);
        yAxis.add(chartTempVo);

        resultMap.put("xAxis", xAxis);
        resultMap.put("yAxisUnit", yAxisUnit);
        resultMap.put("dataUnit", dataUnit);
        resultMap.put("chartType", chartType);
        resultMap.put("legend", legend);
        resultMap.put("yAxis", yAxis);
        return resultMap;
    }
    //endregion
}
