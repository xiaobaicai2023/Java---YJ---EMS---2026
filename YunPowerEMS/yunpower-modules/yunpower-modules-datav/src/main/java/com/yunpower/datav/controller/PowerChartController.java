package com.yunpower.datav.controller;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.ShardingDay;
import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.domain.ShardingMonthAccumulate;
import com.yunpower.datav.domain.vo.ChartYAxisVo;
import com.yunpower.datav.service.IShardingDayService;
import com.yunpower.datav.service.IShardingMonthAccumulateService;
import com.yunpower.datav.service.IShardingMonthService;
import com.yunpower.datav.service.IShardingService;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.enums.sharding.StorageTypeEnum;
import com.yunpower.common.core.enums.vo.EnumSOCVO;
import com.yunpower.common.core.enums.vo.EnumSOVO;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.system.api.*;
import com.yunpower.system.api.domain.*;
import com.yunpower.system.api.domain.vo.FeignVariableConfigPresetsUtils;
import com.yunpower.system.api.domain.vo.FeignVariableConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @title: 图表接口
 * @author XIAOTONG.CAO
 * @date: 2024-05-08 10:00
 * @description: 图表接口
 */
@Api(tags = ">>> 能耗概览（用电）<<<")
@RestController
@RequestMapping("/power/chart")
public class PowerChartController extends BaseController {

    @Autowired(required = false)
    private RemotePageConfigService remotePageConfigService;

    @Autowired(required = false)
    private RemoteDictDataService dictDataService;

    @Autowired(required = false)
    private RemoteGroupService groupService;

    @Autowired(required = false)
    private RemoteMonitorDeviceVarService monitorDeviceVarService;

    @Autowired(required = false)
    private RemoteMonitorDeviceService deviceService;

    @Autowired
    private IShardingService shardingService;

    @Autowired
    private IShardingDayService shardingDayService;

    @Autowired
    private IShardingMonthService shardingMonthService;

    @Autowired
    private IShardingMonthAccumulateService shardingMonthAccumulateService;

    @Autowired(required = false)
    private RemotePublicService publicService;

    @Value("${app.database.create-date}")
    private String createDate;

    //region 获取 能耗概览【普通】图表数据

    /**
     * 获取>>能耗概览【普通】图表数据
     *
     * @param pageValue 模块唯一参数
     * @param dayTime   按天筛选，格式：2023-08-08（三选一）
     * @param monthTime 按月筛选，格式：2023-08（三选一）
     * @param yearTime  按年筛选，格式：2023（三选一）
     * @return 结果
     */
    @ApiOperation("获取>>能耗概览【普通】图表数据")
    @GetMapping("/getChart")
    public AjaxResult getChartData(String pageValue, String dayTime, String monthTime, String yearTime) {
        if (StringUtils.isEmpty(pageValue)) {
            return error("请输入模块参数");
        }
        pageValue = pageValue.replace("_", "-");

        String[] times = packageTime(pageValue, dayTime, monthTime, yearTime, "", "");
        String beginTime = times[0];
        String endTime = times[1];

        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return error("请输入日期");
        }

        // 获取配置（如果有多条，获取最新的一条）
        FeignSysPageConfig sysPageConfig = remotePageConfigService.getInfoByPageValue(pageValue, SecurityConstants.INNER);
        if (sysPageConfig == null || sysPageConfig.getVariableConfig().isEmpty()) {
            return error("请先配置当前页面参数");
        }

        if (pageValue.equals("power-factor-day")) {
            System.out.println("in");
        }

        // 不同的表取不同的数据
        Map<String, Object> map = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);

        //region 总用电量是按年获取的，需要合并一下（比如能耗概览中的总用电量）
        if (pageValue.toLowerCase().startsWith("total-")) {
            List<ChartYAxisVo> yAxis = StringUtils.castList(map.get("yAxis"), ChartYAxisVo.class);
            if (yAxis != null) {
                List<Object> yAxisList = new ArrayList<>();

                float totalEle = 0f;
                for (ChartYAxisVo origin : yAxis) {
                    List<Object> objectList = origin.getDataList();
                    for (Object obj : objectList) {
                        totalEle += Float.parseFloat(obj.toString());
                    }
                }
                yAxisList.add(totalEle);

                ChartYAxisVo chartYAxisVo = new ChartYAxisVo();
                chartYAxisVo.setName("总用电量");
                chartYAxisVo.setDataList(yAxisList);

                yAxis = new ArrayList<>();
                yAxis.add(chartYAxisVo);
                map.put("yAxis", yAxis);
            }
        }
        //endregion

        //region 容量需量，再获取个最大容量
        if (pageValue.equalsIgnoreCase("capacity-demand")) {
            getCapacityDemand(map);
        }
        //endregion

        return success(map);
    }
    //endregion

    //region *** 获取>>能耗概览【堆叠】图表数据 ***

    /**
     * 获取>>能耗概览【堆叠】图表数据
     *
     * @param pageValue 模块唯一参数
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return 结果
     */
    @ApiOperation("获取>>能耗概览【堆叠】图表数据")
    @GetMapping("/getStackChart")
    public AjaxResult getStackChart(String pageValue, String beginTime, String endTime) {
        if (StringUtils.isEmpty(pageValue)) {
            return error("请输入模块参数");
        }
        pageValue = pageValue.replace("_", "-");

        String[] times = packageTime(pageValue, "", "", "", beginTime, endTime);
        beginTime = times[0];
        endTime = times[1];

        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return error("请输入日期");
        }

        // 获取配置（如果有多条，获取最新的一条）
        FeignSysPageConfig sysPageConfig = remotePageConfigService.getInfoByPageValue(pageValue, SecurityConstants.INNER);
        if (sysPageConfig == null || sysPageConfig.getVariableConfig().isEmpty()) {
            return error("请先配置当前页面参数");
        }

        // 不同的表取不同的数据
        Map<String, Object> map = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);
        return success(map);
    }
    //endregion

    //region *** 获取通用【单日期】图表数据 ***

    /**
     * 获取通用【单日期】图表数据
     *
     * @param pageValue 模块唯一参数
     * @param deviceSn  设备编码
     * @param dayTime   按天筛选，格式：2023-08-08（三选一）
     * @param monthTime 按月筛选，格式：2023-08（三选一）
     * @param yearTime  按年筛选，格式：2023（三选一）
     * @return 结果
     */
    @ApiOperation("获取通用【单日期】图表数据")
    @GetMapping("/getChartBySingleDate")
    public AjaxResult getChartBySingleDate(String pageValue, String deviceSn, String dayTime, String monthTime, String yearTime) {
        if (StringUtils.isEmpty(pageValue)) {
            return error("请输入模块参数");
        }
        pageValue = pageValue.replace("_", "-");

        String[] times = packageTime(pageValue, dayTime, monthTime, yearTime, "", "");
        String beginTime = times[0];
        String endTime = times[1];

        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return error("请输入日期");
        }

        if (pageValue.equals("electric-power")) {
            System.out.println("in");
        }

        //动态配置数据
        FeignSysPageConfig sysPageConfig = dynamicConfiguration(pageValue, deviceSn, dayTime, monthTime, yearTime, "s");
        if (sysPageConfig == null) {
            return error("请先配置当前页面参数");
        }

        // 不同的表取不同的数据
        Map<String, Object> map = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);
        map.put("title", sysPageConfig.getGroupName());
        return success(map);
    }
    //endregion

    //region *** 获取通用【双日期】图表数据 ***

    /**
     * 获取通用【双日期】图表数据
     *
     * @param pageValue 模块唯一参数
     * @param deviceSn  设备编码
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return 结果
     */
    @ApiOperation("获取通用【双日期】图表数据")
    @GetMapping("/getChartByDoubleDate")
    public AjaxResult getChartByDoubleDate(String pageValue, String deviceSn, String beginTime, String endTime) {
        if (StringUtils.isEmpty(pageValue)) {
            return error("请输入模块参数");
        }
        pageValue = pageValue.replace("_", "-");

        //region 判断取的是日数据
        String dayTime = null;
        if (beginTime.length() == 10) {
            dayTime = "ok";
        }
        String monthTime = null;
        if (beginTime.length() == 7) {
            monthTime = "ok";
        }
        String yearTime = null;
        if (beginTime.length() == 4) {
            yearTime = "ok";
        }
        //endregion

        String[] times = packageTime(pageValue, "", "", "", beginTime, endTime);
        beginTime = times[0];
        endTime = times[1];

        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return error("请输入日期");
        }

        //动态配置数据
        FeignSysPageConfig sysPageConfig = dynamicConfiguration(pageValue, deviceSn, dayTime, monthTime, yearTime, "d");
        if (sysPageConfig == null) {
            return error("请先配置当前页面参数");
        }

        // 不同的表取不同的数据
        Map<String, Object> map = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);
        map.put("title", sysPageConfig.getGroupName());
        return success(map);
    }
    //endregion

    //region *** 获取【尖峰平谷】表格数据 ***

    /**
     * 获取【尖峰平谷】表格数据
     * 取数逻辑：
     * 1、正向有功电能（EPF）
     * 2、是累积变量
     *
     * @param deviceSns 设备SN，可多选
     * @param dayTime   按天筛选，格式：2023-08-08（三选一）
     * @param monthTime 按月筛选，格式：2023-08（三选一）
     * @param yearTime  按年筛选，格式：2023（三选一）
     * @return 结果
     */
    @ApiOperation("获取【尖峰平谷】表格数据")
    @GetMapping("/ppfvList")
    public AjaxResult ppfvList(String[] deviceSns, String dayTime, String monthTime, String yearTime) {
        if (deviceSns == null || deviceSns.length == 0) {
            return error("请先选择设备");
        }

        String[] times = packageTime(null, dayTime, monthTime, yearTime, "", "");
        String beginTime = times[0];
        String endTime = times[1];

        return success(shardingService.selectAccumulateGroupBySeasonalName2(deviceSns, beginTime, endTime));
    }
    //endregion

    //region *** 获取【基础报表】表格数据 ***

    /**
     * 获取【基础报表】表格数据
     * 取数逻辑：
     * 1、可取：用电量（EPF）、负载率（LF）、相电流（IA）、相电压（IU）、功率因素（PF）、有功功率（P）、无功功率（Q）、视在功率（S）
     * 2、一期只取：用电量（EPF）
     *
     * @param deviceSns 设备SN，可多选
     * @param dayTime   按天筛选，格式：2023-08-08（三选一）
     * @param monthTime 按月筛选，格式：2023-08（三选一）
     * @param yearTime  按年筛选，格式：2023（三选一）
     * @return 结果
     */
    @ApiOperation("获取【基础报表】表格数据")
    @GetMapping("/basicDataList")
    public AjaxResult basicDataList(String[] deviceSns, String paramType, String dayTime, String monthTime, String yearTime) {
        if (deviceSns == null || deviceSns.length == 0) {
            return error("请先选择设备");
        }

        // 默认取用电量（EPF）
        if (StringUtils.isEmpty(paramType)) {
            if (deviceSns[0].contains("_nbq")) { //逆变器
                paramType = "EPrD";
            } else { //用电量
                paramType = "EPF";
            }
        }

        // 参数名称
        String paramName = dictDataService.getDictLabel("sys_search_param_type", paramType,SecurityConstants.INNER);

        // 设备编码
        List<String> deviceSnList = new ArrayList<>();
        if (deviceSns.length > 1) {
            deviceSnList = Arrays.asList(deviceSns);
        }

        // 索引地图编码
        List<String> varMapSnList = new ArrayList<>();
        if (paramType.equals("IA")) {
            varMapSnList.add("IA");
            varMapSnList.add("IB");
            varMapSnList.add("IC");
        }
        if (paramType.equals("UA")) {
            varMapSnList.add("UA");
            varMapSnList.add("UB");
            varMapSnList.add("UC");
        }

        // 获取变量编码
        FeignMonitorDeviceVar monitorDeviceVar = new FeignMonitorDeviceVar();
        Map<String, Object> params = new HashMap<>();
        if (deviceSnList.isEmpty()) {
            monitorDeviceVar.setDeviceSn(deviceSns[0]);
        } else {
            params.put("deviceSns", deviceSnList);
        }

        if (varMapSnList.isEmpty()) {
            monitorDeviceVar.setVarMapSn(paramType);
        } else {
            params.put("varMapSns", varMapSnList);
        }

        if (!params.isEmpty()) {
            monitorDeviceVar.setParams(params);
        }
        monitorDeviceVar.setStopFlag(0);
        List<FeignMonitorDeviceVar> deviceVarList = monitorDeviceVarService.getListInner(monitorDeviceVar, SecurityConstants.INNER);

        String[] times = packageTime(null, dayTime, monthTime, yearTime, "", "");
        String beginTime = times[0];
        String endTime = times[1];

        // 表头
        List<Integer> heads = new ArrayList<>();
        if (StringUtils.isNotEmpty(dayTime)) {
            for (int i = 0; i < 24; i++) {
                heads.add(i);
            }
        }
        if (StringUtils.isNotEmpty(monthTime)) {
            int lastday = DateUtils.getMonthLastDay(Integer.parseInt(monthTime.split("-")[0]), Integer.parseInt(monthTime.split("-")[1]));
            for (int i = 1; i < lastday; i++) {
                heads.add(i);
            }
        }
        if (StringUtils.isNotEmpty(yearTime)) {
            for (int i = 1; i <= 12; i++) {
                heads.add(i);
            }
        }

        // 结果
        List<List<EnumSOVO>> result = new ArrayList<>();

        for (FeignMonitorDeviceVar item : deviceVarList) {

            //region 【公用部分 - 开始】存储中间数据
            List<EnumSOVO> midList = new ArrayList<>();

            EnumSOVO vo1 = new EnumSOVO();
            vo1.setKey("设备");
            vo1.setValue(item.getVarName());
            midList.add(vo1);

            EnumSOVO vo2 = new EnumSOVO();
            vo2.setKey("参数");
            vo2.setValue(paramName);
            midList.add(vo2);

            double min = 99999999d;
            double max = 0f;
            double total = 0f;
            int count = 0;
            //endregion

            //region 取【日】数据
            if (StringUtils.isNotEmpty(dayTime)) {
                if (paramType.equals("EPF")) {
                    // 累计值（用电量）==> 月累计表
                    List<ShardingMonthAccumulate> list = shardingMonthAccumulateService.selectAccumulateGroupByHour(item.getVarSn(), beginTime, endTime);

                    count = list.size();
                    for (Integer head : heads) {
                        EnumSOVO vo = new EnumSOVO();
                        vo.setKey(head + "时");
                        vo.setValue("0");
                        for (ShardingMonthAccumulate day : list) {
                            if (day.getRecordHour().equals(head)) {
                                double dataValue = day.getAccuData();
                                min = Math.min(min, dataValue);
                                max = Math.max(max, dataValue);
                                total += dataValue;
                                vo.setValue(dataValue + "");
                                break;
                            }
                        }
                        midList.add(vo);
                    }
                } else {
                    //变化值 ==> 日存储表
                    ShardingDay shardingDay = new ShardingDay();
                    shardingDay.setVariableName(item.getVarSn());
                    shardingDay.setParams(DateUtils.dateToParamForDay(beginTime, endTime));
                    List<ShardingDay> list = shardingDayService.selectShardingDayForHour(shardingDay, StorageTypeEnum.CHANGE.getKey(), 0);

                    count = list.size();
                    for (Integer head : heads) {
                        EnumSOVO vo = new EnumSOVO();
                        vo.setKey(head + "时");
                        vo.setValue("0");
                        for (ShardingDay day : list) {
                            if (day.getHour().equals(head)) {
                                double dataValue = day.getDataValue();
                                min = Math.min(min, dataValue);
                                max = Math.max(max, dataValue);
                                total += dataValue;
                                vo.setValue(dataValue + "");
                                break;
                            }
                        }
                        midList.add(vo);
                    }
                }
            }
            //endregion

            //region 取【月】数据
            if (StringUtils.isNotEmpty(monthTime)) {
                ShardingMonth shardingMonth = new ShardingMonth();
                shardingMonth.setVariableName(item.getVarSn());
                shardingMonth.setParams(DateUtils.dateToParamForDay(beginTime, endTime));

                int storageType;
                if (paramType.equals("EPF")) {
                    // 累计值（用电量）==> 月统计表
                    storageType = StorageTypeEnum.ACCUM.getKey();
                } else {
                    //变化值 ==> 月统计表
                    storageType = StorageTypeEnum.CHANGE.getKey();
                }
                List<ShardingMonth> list = shardingMonthService.selectShardingMonthList(shardingMonth, storageType, 0);

                count = list.size();
                for (Integer head : heads) {
                    EnumSOVO vo = new EnumSOVO();
                    vo.setKey(head + "日");
                    vo.setValue("0");
                    for (ShardingMonth month : list) {
                        if (month.getDay().equals(head)) {
                            double dataValue;
                            if (paramType.equals("EPF")) {
                                dataValue = month.getAccuValue();
                            } else {
                                dataValue = month.getAvgValue();
                            }
                            min = Math.min(min, dataValue);
                            max = Math.max(max, dataValue);
                            total += dataValue;
                            vo.setValue(dataValue + "");
                            break;
                        }
                    }
                    midList.add(vo);
                }
            }
            //endregion

            //region 取【年】数据
            if (StringUtils.isNotEmpty(yearTime)) {
                ShardingMonth shardingMonth = new ShardingMonth();
                shardingMonth.setVariableName(item.getVarSn());
                shardingMonth.setParams(DateUtils.dateToParamForDay(beginTime, endTime));

                int storageType;
                if (paramType.equals("EPF")) {
                    // 累计值（用电量）==> 月统计表
                    storageType = StorageTypeEnum.ACCUM.getKey();
                } else {
                    //变化值 ==> 月统计表
                    storageType = StorageTypeEnum.CHANGE.getKey();
                }
                List<ShardingMonth> list = shardingMonthService.selectShardingMonthStatisticForMonth(shardingMonth, storageType, 0);

                count = list.size();
                for (Integer head : heads) {
                    EnumSOVO vo = new EnumSOVO();
                    vo.setKey(head + "月");
                    vo.setValue("0");
                    for (ShardingMonth month : list) {
                        if (month.getMonth().equals(head)) {
                            double dataValue = month.getStatisticValue();
                            min = Math.min(min, dataValue);
                            max = Math.max(max, dataValue);
                            total += dataValue;
                            vo.setValue(dataValue + "");
                            break;
                        }
                    }
                    midList.add(vo);
                }
            }
            //endregion

            //region 【公用部分 - 结束】
            EnumSOVO vo3 = new EnumSOVO();
            vo3.setKey("最小值");
            if (min == 99999999f) {
                vo3.setValue("0.00");
            } else {
                vo3.setValue(DoubleUtils.fmt2Point(min));
            }
            midList.add(vo3);

            EnumSOVO vo4 = new EnumSOVO();
            vo4.setKey("最大值");
            vo4.setValue(DoubleUtils.fmt2Point(max));
            midList.add(vo4);

            EnumSOVO vo5 = new EnumSOVO();
            vo5.setKey("平均值");
            vo5.setValue(count == 0 ? "0.00" : DoubleUtils.fmt2Point(total / count));
            midList.add(vo5);

            EnumSOVO vo6 = new EnumSOVO();
            vo6.setKey("合计");
            vo6.setValue(DoubleUtils.fmt2Point(total));
            midList.add(vo6);

            result.add(midList);
            //endregion
        }

        return success(result);
    }
    //endregion

    //region *** 获取当前页面有多少种图表，如：历史数据页面有三种图表，分别是：电能和功率、电流电压温湿度、功率因素和负载率 ***

    /**
     * 获取页面图表种类
     *
     * @param pageSign 页面标志，如历史数据：history-data
     * @return 返回 pageValue 列表
     */
    @ApiOperation("获取页面图表种类")
    @GetMapping("/getPageValueList")
    public AjaxResult getPageValueList(String pageSign) {
        pageSign = pageSign.replace("_", "-");
        Map<String, String> map = new HashMap<>();

        SysGroup group = groupService.getInfoByGroupSn(pageSign,SecurityConstants.INNER);
        if (group == null) {
            return error("请先进行图表配置");
        }

        SysGroup sysGroup = new SysGroup();
        sysGroup.setParentId(group.getId());
        sysGroup.setMapId(5L);
        sysGroup.setStopFlag(0);
        List<SysGroup> groupList = groupService.getListBySearch(sysGroup, SecurityConstants.INNER);
        if (groupList.isEmpty()) {
            return error("请先进行图表配置");
        }

        List<Long> groupIds = new ArrayList<>();
        for (SysGroup item : groupList) {
            groupIds.add(item.getId());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("groupIds", groupIds);

        FeignSysPageConfig pageConfig = new FeignSysPageConfig();
        pageConfig.setParams(params);
        pageConfig.setStopFlag(0);
        List<FeignSysPageConfig> pageConfigList = remotePageConfigService.getList(pageConfig, SecurityConstants.INNER);
        for (FeignSysPageConfig item : pageConfigList) {
            map.put(item.getPageValue(), item.getGroupName());
        }
        return success(map);
    }
    //endregion

    //region *** 获取【能源流向】数据 ***

    /**
     * 获取【能源流向】数据
     *
     * @param pageValue 模块唯一参数
     * @param beginTime 开始日期
     * @param endTime   结束日期
     * @return 结果
     */
    @ApiOperation("获取【能源流向】数据")
    @GetMapping("/getEnergySteamDate")
    public AjaxResult getEnergySteamDate(String pageValue, String beginTime, String endTime) {
        if (StringUtils.isEmpty(pageValue)) {
            return error("请输入模块参数");
        }
        pageValue = pageValue.replace("_", "-");

        String[] times = packageTime(pageValue, "", "", "", beginTime, endTime);
        beginTime = times[0];
        endTime = times[1];

        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return error("请输入日期");
        }

        // 获取配置（如果有多条，获取最新的一条）
        FeignSysPageConfig sysPageConfig = remotePageConfigService.getInfoByPageValue(pageValue, SecurityConstants.INNER);
        if (sysPageConfig == null || sysPageConfig.getVariableConfig().isEmpty()) {
            return null;
        }

        List<EnumSOCVO> list = recursionGetData(sysPageConfig.getVariableConfig(), beginTime, endTime);
        return success(list);
    }

    //endregion

    //region 格式化时间

    /**
     * 格式化时间
     */
    private String[] packageTime(String pageValue, String dayTime, String monthTime, String yearTime, String beginTime, String endTime) {

        //region // 格式化时间
        // 格式化【日】时间
        if (StringUtils.isNotEmpty(dayTime)) {
            beginTime = DateUtils.completionDayTime(dayTime, true);
            endTime = DateUtils.completionDayTime(dayTime, false);
        }

        // 格式化【月】时间
        if (StringUtils.isNotEmpty(monthTime)) {
            beginTime = DateUtils.completionMonthTime(monthTime, true);
            endTime = DateUtils.completionMonthTime(monthTime, false);
        }

        // 格式化【年】时间
        if (StringUtils.isNotEmpty(yearTime)) {
            beginTime = DateUtils.completionYearTime(yearTime, true);
            endTime = DateUtils.completionYearTime(yearTime, false);
        }
        //endregion

        //region // 特殊模块变量，如果没有给时间，则赋值默认时间
        if (pageValue != null && (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime))) {
            // 获取今天日期
            if (pageValue.toLowerCase().startsWith("today-")) {
                String dayStr = DateUtils.getDate();
                beginTime = DateUtils.completionDayTime(dayStr, true);
                endTime = DateUtils.completionDayTime(dayStr, false);
            }
            // 获取昨天日期
            if (pageValue.toLowerCase().startsWith("yesterday-")) {
                String dayStr = DateUtils.getPastDate(1);
                beginTime = DateUtils.completionDayTime(dayStr, true);
                endTime = DateUtils.completionDayTime(dayStr, false);
            }
            // 获取本月日期
            if (pageValue.toLowerCase().startsWith("month-")) {
                String monthStr = DateUtils.getCurrYearMonth();
                beginTime = DateUtils.completionMonthTime(monthStr, true);
                endTime = DateUtils.completionMonthTime(monthStr, false);
            }
            // 获取今年日期
            if (pageValue.toLowerCase().startsWith("year-")) {
                String yearStr = DateUtils.getCurrYear() + "";
                beginTime = DateUtils.completionYearTime(yearStr, true);
                endTime = DateUtils.completionYearTime(yearStr, false);
            }
            // 获取总量日期
            if (pageValue.toLowerCase().startsWith("total-")) {
                beginTime = DateUtils.completionDayTime(this.createDate, true);
                endTime = DateUtils.completionDayTime(DateUtils.getDate(), false);
            }
        }
        //endregion

        //region // 最后判断一下日期格式，日期格式错误会报错
        if (beginTime.length() == 4) {
            beginTime = DateUtils.completionYearTime(beginTime, true);
            endTime = DateUtils.completionYearTime(endTime, false);
        }
        if (beginTime.length() == 7) {
            beginTime = DateUtils.completionMonthTime(beginTime, true);
            endTime = DateUtils.completionMonthTime(endTime, false);
        }
        if (beginTime.length() == 10) {
            beginTime = DateUtils.completionDayTime(beginTime, true);
            endTime = DateUtils.completionDayTime(endTime, false);
        }
        //endregion

        return new String[]{beginTime, endTime};
    }
    //endregion

    //region 递归获取数据

    /**
     * 递归获取数据
     */
    private List<EnumSOCVO> recursionGetData(List<FeignVariableConfigVo> variableVos, String beginTime, String endTime) {
        List<EnumSOCVO> result = new ArrayList<>();
        if (variableVos == null || variableVos.isEmpty()) {
            return result;
        }

        for (FeignVariableConfigVo item : variableVos) {
            Float data = shardingMonthAccumulateService.sumAccumulate(item.getVarSn(), beginTime, endTime);
            EnumSOCVO vo = new EnumSOCVO();
            vo.setKey(item.getVarName());
            vo.setValue(data);
            vo.setChildren(recursionGetData(item.getChildren(), beginTime, endTime));
            result.add(vo);
        }

        return result;
    }
    //endregion

    //region 动态配置数据

    /**
     * 动态配置数据
     */
    private FeignSysPageConfig dynamicConfiguration(String pageValue, String deviceSn, String dayTime, String monthTime, String yearTime, String sign) {

        // 获取配置（如果有多条，获取最新的一条）
        FeignSysPageConfig sysPageConfig = remotePageConfigService.getInfoByPageValue(pageValue, SecurityConstants.INNER);
        if (sysPageConfig == null || sysPageConfig.getVariableConfig().isEmpty()) {
            return null;
        }

        // 提前预设好的数据
        FeignVariableConfigVo presetsVo = FeignVariableConfigPresetsUtils.config("day-s-1", 1);

        //region 重新组装 pageConfig
        List<FeignVariableConfigVo> newVoList = new ArrayList<>();
        for (FeignVariableConfigVo item : sysPageConfig.getVariableConfig()) {

            // 判断是日、月、年数据
            String suffix = item.getStorageType() + "";
            if (pageValue.toLowerCase().startsWith("ppfv-")) {
                suffix = "ppfv";
            }

            if (StringUtils.isNotEmpty(dayTime)) {
                presetsVo = FeignVariableConfigPresetsUtils.config("day-" + sign + "-" + suffix, item.getStorageType());
            }
            if (StringUtils.isNotEmpty(monthTime)) {
                presetsVo = FeignVariableConfigPresetsUtils.config("month-" + sign + "-" + suffix, item.getStorageType());
            }
            if (StringUtils.isNotEmpty(yearTime)) {
                presetsVo = FeignVariableConfigPresetsUtils.config("year-" + sign + "-" + suffix, item.getStorageType());
            }

            // 根据设备SN获取变量
            // 变量获取规则：一个设备一项数据只能采一个（var_map_sn 识别）；并且采到的数据要标识为“是否监控”（is_monitor = 1）
            Map<String, String> varSnMap = getVarSnByDeviceSn(deviceSn, item.getVarMapSn());

            for (Map.Entry<String, String> varSn : varSnMap.entrySet()) {
                FeignVariableConfigVo newVo = new FeignVariableConfigVo();
                if (item.getIsChain() == 1 || pageValue.toLowerCase().startsWith("ppfv-")) { //环比和峰尖平谷就使用原名称
                    newVo.setVarName(item.getVarName());
                } else {
                    newVo.setVarName(varSn.getValue());
                }
                newVo.setVarSn(varSn.getKey());
                newVo.setVarMapSn(item.getVarMapSn());
                newVo.setUnit(item.getUnit());
                newVo.setStorageType(item.getStorageType());
                newVo.setIsStack(item.getIsStack());
                newVo.setIsChain(item.getIsChain());
                newVo.setDataType(presetsVo.getDataType());
                newVo.setChartType(item.getChartType());
                newVoList.add(newVo);
            }
        }
        sysPageConfig.setxAxis(presetsVo.getxAxis());
        sysPageConfig.setVariableConfig(newVoList);
        //endregion

        return sysPageConfig;
    }
    //endregion

    //region 获取设备最大容量

    /**
     * 获取设备最大容量
     */
    private void getCapacityDemand(Map<String, Object> map) {
        try {
            // 获取额定容量
            List<ChartYAxisVo> yAxis = StringUtils.castList(map.get("yAxis"), ChartYAxisVo.class);
            if (yAxis == null) {
                yAxis = new ArrayList<>();
            }

            //更改名称 --> 最大需量
            yAxis.get(0).setName("最大需量");

            List<Object> yAxisList = new ArrayList<>();
            SysStation station = publicService.getCurrentStationInfo(SecurityConstants.INNER);
            if (station != null && station.getCapacityKva() != null) {
                yAxisList.add(Float.parseFloat(station.getCapacityKva() + ""));
            }
            if (yAxisList.isEmpty()) {
                yAxisList.add(0);
            }

            ChartYAxisVo chartYAxisVo = new ChartYAxisVo();
            chartYAxisVo.setName("额定容量");
            chartYAxisVo.setDataList(yAxisList);
            yAxis.add(chartYAxisVo);
            map.put("yAxis", yAxis);

            // Legend
            List<String> legendList = StringUtils.castList(map.get("legend"), String.class);
            if (legendList == null) {
                legendList = new ArrayList<>();
            }
            legendList.add("额定容量");
            map.put("legend", legendList);

            // Unit
            List<String> yAxisUnitList = StringUtils.castList(map.get("yAxisUnit"), String.class);
            if (yAxisUnitList == null) {
                yAxisUnitList = new ArrayList<>();
            }
            yAxisUnitList.add("kVA");
            map.put("yAxisUnit", yAxisUnitList);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    //region 根据设备SN获取变量，在变量中找出相应的，如：正向有功电能
    private Map<String, String> getVarSnByDeviceSn(String deviceSn, String varMapSn) {
        FeignMonitorDeviceVar monitorDeviceVar = new FeignMonitorDeviceVar();
        monitorDeviceVar.setDeviceSn(deviceSn);
        monitorDeviceVar.setVarMapSn(varMapSn);
        monitorDeviceVar.setIsMonitor(1);
        monitorDeviceVar.setStopFlag(0);
        List<FeignMonitorDeviceVar> deviceVarList = monitorDeviceVarService.getListInner(monitorDeviceVar, SecurityConstants.INNER);

        Map<String, String> varSnMap = new HashMap<>();
        for (FeignMonitorDeviceVar item : deviceVarList) {
            varSnMap.put(item.getVarSn(), item.getVarName());
        }

        return varSnMap;
    }
    //endregion

    //region *** 获取>>光伏电站【辅助分析】图表数据 ***

    /**
     * 获取>>光伏电站【辅助分析】图表数据
     *
     * @param deviceSn  设备编码
     * @param dayTime   按天筛选，格式：2023-08-08（三选一）
     * @param monthTime 按月筛选，格式：2023-08（三选一）
     * @param yearTime  按年筛选，格式：2023（三选一）
     * @return 结果
     */
    @ApiOperation("获取>>光伏电站【辅助分析】图表数据")
    @GetMapping("/getAuxiliaryAnalysis")
    public AjaxResult getAuxiliaryAnalysis(String deviceSn, String dayTime, String monthTime, String yearTime) {
        // 光伏设备分组是固定的，分别是: 逆变器、汇流箱、并网柜、电能表、气象站，五个所以，你前端不需要判断，你只传入deviseSn即可，
        // 我通过deviceSn判断是哪个组，然后返回不同的图表辅助分析返回的数据也都是标准的。

        // 获取设备信息
        FeignMonitorDevice device = deviceService.getInfoByDeviceSn(deviceSn, SecurityConstants.INNER);
        if (device == null) {
            return success();
        }

        // 获取分组信息
        SysGroup sysGroup = groupService.getInfoByGroupId(device.getGroupId(), SecurityConstants.INNER);
        if (sysGroup == null) {
            return success();
        }

        // 只有5种分组：逆变器、汇流箱、并网柜、电能表、气象站
        // 逆变器：发电量（photovoltaic-electric）
        // 汇流箱：总路数据（photovoltaic-master）
        // 并网柜：发电量（photovoltaic-electric）
        // 电能表：正向有功总电能（photovoltaic-energy）
        // 气象站：环境数据（photovoltaic-environment）

        String pageValue;
        switch (sysGroup.getGroupName()) {
            case "汇流箱": //总路数据
                pageValue = "photovoltaic-master";
                break;
            case "电能表": //正向有功总电能
                pageValue = "photovoltaic-energy";
                break;
            case "气象站": //环境数据
                pageValue = "photovoltaic-environment";
                break;
            default: //发电量
                pageValue = "photovoltaic-electric";
        }

        String[] times = packageTime(pageValue, dayTime, monthTime, yearTime, "", "");
        String beginTime = times[0];
        String endTime = times[1];

        if (StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return error("请输入日期");
        }

        //动态配置数据
        FeignSysPageConfig sysPageConfig = dynamicConfiguration(pageValue, deviceSn, dayTime, monthTime, yearTime, "s");
        if (sysPageConfig == null) {
            return error("请先配置当前页面参数");
        }

        // 不同的表取不同的数据
        Map<String, Object> map = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);
        map.put("title", sysPageConfig.getGroupName());
        return success(map);
    }
    //endregion
}
