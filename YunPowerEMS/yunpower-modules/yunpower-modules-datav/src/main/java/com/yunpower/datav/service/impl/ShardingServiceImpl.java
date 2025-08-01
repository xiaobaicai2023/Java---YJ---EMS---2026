package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.ShardingDay;
import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.domain.ShardingMonthAccumulate;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.enums.DateFormatEnum;
import com.yunpower.common.core.enums.sharding.ShardingDataTypeEnum;
import com.yunpower.common.core.enums.sharding.XAxisTypeEnum;
import com.yunpower.common.core.enums.vo.EnumSOVO;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.datav.domain.vo.ChartYAxisVo;
import com.yunpower.datav.service.IShardingDayService;
import com.yunpower.datav.service.IShardingMonthAccumulateService;
import com.yunpower.datav.service.IShardingMonthService;
import com.yunpower.datav.service.IShardingService;
import com.yunpower.system.api.RemoteDictDataService;
import com.yunpower.system.api.RemoteMonitorDeviceVarService;
import com.yunpower.system.api.domain.FeignMonitorDeviceVar;
import com.yunpower.system.api.domain.FeignSysPageConfig;
import com.yunpower.system.api.domain.SysCommonDictData;
import com.yunpower.system.api.domain.vo.FeignVariableConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @title: 日存储数据和月统计数据中间层
 * @Author: Jiajiaglam
 * @date: 2023-10-25 15:46
 * @description:
 */
@Service
public class ShardingServiceImpl implements IShardingService {
    @Autowired
    private IShardingDayService shardingDayService;

    @Autowired
    private IShardingMonthService shardingMonthService;

    @Autowired
    private IShardingMonthAccumulateService shardingMonthAccumulateService;

    @Autowired(required = false)
    private RemoteDictDataService dictDataService;

    @Autowired(required = false)
    private RemoteMonitorDeviceVarService monitorDeviceVarService;

    //region 获取图表数据（融合）

    /**
     * 获取图表数据（融合）
     *
     * @param pageConfig 变量配置（Json：变量/单位/存储类型：1变化值 2累计值/取值类型：枚举）
     * @param xAxisType  X轴数据类型，枚举：XAxisTypeEnum
     * @param beginTime  开始日期
     * @param endTime    结束日期
     * @return 结果
     */
    @Override
    public Map<String, Object> fusionShardingData(FeignSysPageConfig pageConfig, Integer xAxisType, String beginTime, String endTime) {
        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();

        // X轴（如：2023-10-20，2023-10-21...）
        List<String> xAxis = new ArrayList<>();

        // Y轴单位（如：用电量（kW-h））
        List<String> yAxisUnit = new ArrayList<>();

        // 数据展示单位
        List<String> dataUnit = new ArrayList<>();

        // 图表类型
        List<String> chartType = new ArrayList<>();

        // Y轴（存在多曲线情况）（普通图表和堆叠图表在此有区别）
        List<ChartYAxisVo> yAxis = new ArrayList<>();

        // Legend（如：用电量、电流、电压）
        List<String> legend = new ArrayList<>();

        // 当前模块有几个变量，轮询读取
        for (FeignVariableConfigVo variableVo : pageConfig.getVariableConfig()) {
            if (variableVo.getIsStack() != null && variableVo.getIsStack() == 1) {
                //region //堆叠图表（尖、峰、平、谷、深谷）
                xAxis = packagePPFVData(variableVo, xAxisType, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
                //endregion
            } else {
                //region //普通图表
                if (variableVo.getDataType().startsWith("M")) {
                    //月数据
                    xAxis = packageMonthData(variableVo, xAxisType, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
                } else if (variableVo.getDataType().startsWith("D")) {
                    //日数据
                    xAxis = packageDayData(variableVo, xAxisType, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
                } else if (variableVo.getDataType().startsWith("A")) {
                    //累计数据中间表
                    xAxis = packageAccumData(variableVo, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
                }
                //endregion
            }
        }

        // 总电量
        float totalElectric = 0f;
        for (ChartYAxisVo vo : yAxis) {
            if (vo.getyAxisUnit().equalsIgnoreCase("kwh")) {
                for (Object val : vo.getDataList()) {
                    totalElectric += Float.parseFloat(val.toString());
                }
            }
        }

        resultMap.put("xAxis", xAxis);
        resultMap.put("yAxisUnit", yAxisUnit);
        resultMap.put("dataUnit", dataUnit);
        resultMap.put("chartType", chartType);
        resultMap.put("legend", legend);
        resultMap.put("yAxis", yAxis);
        resultMap.put("totalElectric", totalElectric);
        return resultMap;
    }
    //endregion

    //region 从数据存储表中提取（普通图表）（未使用，使用融合方法）

    /**
     * 从数据存储表中提取（普通图表）（未使用，使用融合方法）
     *
     * @param pageConfig 变量配置（Json：变量/单位/存储类型：1变化值 2累计值/取值类型：枚举）
     * @param xAxisType  X轴数据类型，枚举：XAxisTypeEnum
     * @param beginTime  开始日期
     * @param endTime    结束日期
     * @return 结果
     */
    @Override
    public Map<String, Object> packageShardingData(FeignSysPageConfig pageConfig, Integer xAxisType, String beginTime, String endTime) {

        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();

        // X轴（如：2023-10-20，2023-10-21...）
        List<String> xAxis = new ArrayList<>();

        // Y轴单位（如：用电量（kW-h））
        List<String> yAxisUnit = new ArrayList<>();

        // 数据展示单位
        List<String> dataUnit = new ArrayList<>();

        // 图表类型
        List<String> chartType = new ArrayList<>();

        // Y轴（存在多曲线情况）（普通图表和堆叠图表在此有区别）
        List<ChartYAxisVo> yAxis = new ArrayList<>();

        // Legend（如：用电量、电流、电压）
        List<String> legend = new ArrayList<>();

        // 当前模块有几个变量，轮询读取
        for (FeignVariableConfigVo variableVo : pageConfig.getVariableConfig()) {
            if (variableVo.getDataType().startsWith("M")) {
                //月数据
                xAxis = packageMonthData(variableVo, xAxisType, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
            } else if (variableVo.getDataType().startsWith("D")) {
                //日数据
                xAxis = packageDayData(variableVo, xAxisType, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
            } else if (variableVo.getDataType().startsWith("A")) {
                //累计数据中间表
                xAxis = packageAccumData(variableVo, beginTime, endTime, xAxis, yAxis, yAxisUnit, dataUnit, chartType, legend);
            }
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

    //region 公共方法

    /**
     * 从【日】数据存储表中提取
     */
    private List<String> packageDayData(FeignVariableConfigVo variableVo, Integer xAxisType, String beginTime, String endTime, List<String> xAxis,
                                        List<ChartYAxisVo> yAxis, List<String> yAxisUnit, List<String> dataUnit, List<String> chartType, List<String> legend) {

        //查询结果
        List<ShardingDay> shardingDayList = new ArrayList<>();

        ShardingDay shardingDay = new ShardingDay();
        shardingDay.setVariableName(variableVo.getVarSn());
        shardingDay.setParams(DateUtils.dateToParamForDay(beginTime, endTime));

        //取值类型：枚举 ShardingDataTypeEnum
        if (xAxisType == XAxisTypeEnum.M5.getValue()) {  //5分钟数据
            shardingDayList = shardingDayService.selectShardingDayListForMinute5(shardingDay, variableVo.getStorageType(), variableVo.getIsChain());
        }
        if (xAxisType == XAxisTypeEnum.M10.getValue()) { //10分钟数据
            shardingDayList = shardingDayService.selectShardingDayListForMinute10(shardingDay, variableVo.getStorageType(), variableVo.getIsChain());
        }
        if (xAxisType == XAxisTypeEnum.M15.getValue()) { //15分钟数据
            shardingDayList = shardingDayService.selectShardingDayListForMinute15(shardingDay, variableVo.getStorageType(), variableVo.getIsChain());
        }
        if (xAxisType == XAxisTypeEnum.M30.getValue()) { //30分钟数据
            shardingDayList = shardingDayService.selectShardingDayListForMinute30(shardingDay, variableVo.getStorageType(), variableVo.getIsChain());
        }
        if (xAxisType == XAxisTypeEnum.HOUR.getValue()) {//1小时数据
            shardingDayList = shardingDayService.selectShardingDayForHour(shardingDay, variableVo.getStorageType(), variableVo.getIsChain());
        }

        if (shardingDayList.isEmpty()) {
            return xAxis;
        }

        //计算总电量（保留此方法）
        //if (variableVo.getIsChain() == 0) {
        //    totalElectric = shardingDayList.stream().mapToDouble(ShardingDay::getDataValue).sum();
        //}

        //组装X轴（在有多个变量的情况下，组装一次就够了）
        if (xAxis.isEmpty()) {
            //X轴时间
            ShardingDay bVo = shardingDayList.get(0);
            String xBeginTime = DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue(), bVo.getSaveTime());

            ShardingDay eVo = shardingDayList.get(shardingDayList.size() - 1);
            String xEndTime = DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue(), eVo.getSaveTime());

            if (xAxisType == XAxisTypeEnum.M5.getValue()) { //5分钟数据
                xAxis = DateUtils.getMinuteList(xBeginTime, xEndTime, 5);
            }
            if (xAxisType == XAxisTypeEnum.M10.getValue()) { //10分钟数据
                xAxis = DateUtils.getMinuteList(xBeginTime, xEndTime, 10);
            }
            if (xAxisType == XAxisTypeEnum.M15.getValue()) { //15分钟数据
                xAxis = DateUtils.getMinuteList(xBeginTime, xEndTime, 15);
            }
            if (xAxisType == XAxisTypeEnum.M30.getValue()) { //30分钟数据
                xAxis = DateUtils.getMinuteList(xBeginTime, xEndTime, 30);
            }
            if (xAxisType == XAxisTypeEnum.HOUR.getValue()) { //小时数据
                xAxis = DateUtils.getHourList(xBeginTime, xEndTime);
            }
        }

        // 一个变量一个单位（如：电流和电压就是不同的）
        if (!yAxisUnit.contains(variableVo.getUnit())) {
            yAxisUnit.add(variableVo.getUnit());
        }

        // 数据单位
        dataUnit.add(variableVo.getUnit());

        chartType.add(variableVo.getChartType());
        legend.add(variableVo.getVarName());

        //region //注意：如果某一天没有数据时会错位。所以要循环X轴，而不是数据列表
        List<Object> emptyList = new ArrayList<>();
        for (String item : xAxis) {
            List<ShardingDay> list;
            if (xAxisType == XAxisTypeEnum.HOUR.getValue()) {
                //小时数据
                list = shardingDayList.stream().filter(s -> (s.getHour() + "时").equals(item)).collect(Collectors.toList());
            } else {
                //分钟数据
                list = shardingDayList.stream().filter(s -> (StringUtils.padLeft(s.getHour(), 2) + ":" + StringUtils.padLeft(s.getMinute(), 2)).equals(item)).collect(Collectors.toList());
            }

            if (list.isEmpty()) {
                emptyList.add(0);
            } else {
                emptyList.add(list.get(0).getDataValue());
            }
        }

        ChartYAxisVo chartTempVo = new ChartYAxisVo();
        chartTempVo.setName(variableVo.getVarName());
        chartTempVo.setChartType(variableVo.getChartType());
        chartTempVo.setyAxisUnit(variableVo.getUnit());
        chartTempVo.setDataList(emptyList);
        for (int i = 0; i < yAxisUnit.size(); i++) {
            if (yAxisUnit.get(i).equals(variableVo.getUnit())) {
                chartTempVo.setyAxisIndex(i);
            }
        }
        yAxis.add(chartTempVo);
        //endregion

        return xAxis;
    }

    /**
     * 从【月】数据存储表中提取
     */
    private List<String> packageMonthData(FeignVariableConfigVo variableVo, Integer xAxisType, String beginTime, String endTime, List<String> xAxis,
                                          List<ChartYAxisVo> yAxis, List<String> yAxisUnit, List<String> dataUnit, List<String> chartType, List<String> legend) {

        //查询结果
        List<ShardingMonth> shardingMonthList = new ArrayList<>();

        ShardingMonth shardingMonth = new ShardingMonth();
        shardingMonth.setVariableName(variableVo.getVarSn());
        shardingMonth.setParams(DateUtils.dateToParamForDay(beginTime, endTime));

        //X轴时间
        String xBeginTime = beginTime;
        String xEndTime = endTime;

        //取值类型：枚举 ShardingDataTypeEnum
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_D.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
            //日变化值（月统计表取平均值） & 日累计值
            shardingMonthList = shardingMonthService.selectShardingMonthList(shardingMonth, variableVo.getStorageType(), variableVo.getIsChain());

            if (!shardingMonthList.isEmpty()) {
                ShardingMonth bVo = shardingMonthList.get(0);
                xBeginTime = DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue(), bVo.getMinTime());

                ShardingMonth eVo = shardingMonthList.get(shardingMonthList.size() - 1);
                xEndTime = DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue(), eVo.getMinTime());
            }
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_M.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_M.getKey())) {
            //月累计值
            shardingMonthList = shardingMonthService.selectShardingMonthStatisticForMonth(shardingMonth, variableVo.getStorageType(), variableVo.getIsChain());

            if (!shardingMonthList.isEmpty()) {
                ShardingMonth bVo = shardingMonthList.get(0);
                xBeginTime = bVo.getYear() + "-" + StringUtils.padLeft(bVo.getMonth(), 2) + "-01 00:00:00";

                ShardingMonth eVo = shardingMonthList.get(shardingMonthList.size() - 1);
                xEndTime = eVo.getYear() + "-" + StringUtils.padLeft(eVo.getMonth(), 2) + "-31 23:59:59";
            }
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_Y.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_Y.getKey())) {
            //年累计值
            shardingMonthList = shardingMonthService.selectShardingMonthStatisticForYear(shardingMonth, variableVo.getStorageType(), variableVo.getIsChain());

            if (!shardingMonthList.isEmpty()) {
                xBeginTime = shardingMonthList.get(0).getYear() + "-01-01 00:00:00";
                xEndTime = shardingMonthList.get(shardingMonthList.size() - 1).getYear() + "-12-31 23:59:59";
            }
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_MAX.getKey())) {
            //最大值
            Double dataValue = shardingMonthService.selectShardingMonthMax(shardingMonth);
            ShardingMonth dto = new ShardingMonth();
            dto.setStatisticValue(dataValue);
            shardingMonthList.add(dto);
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_MIN.getKey())) {
            //最小值
            Double dataValue = shardingMonthService.selectShardingMonthMin(shardingMonth);
            ShardingMonth dto = new ShardingMonth();
            dto.setStatisticValue(dataValue);
            shardingMonthList.add(dto);
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_AVG.getKey())) {
            //平均值
            Double dataValue = shardingMonthService.selectShardingMonthAvg(shardingMonth);
            ShardingMonth dto = new ShardingMonth();
            dto.setStatisticValue(dataValue);
            shardingMonthList.add(dto);
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJZ.getKey())) {
            //累计值
            shardingMonth.setDaySign(Integer.parseInt(DateUtils.getDateDay(beginTime)));
            Double dataValue = shardingMonthService.selectShardingMonthDay(shardingMonth);
            ShardingMonth dto = new ShardingMonth();
            dto.setStatisticValue(dataValue);
            shardingMonthList.add(dto);
        }
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_CONT.getKey())) {
            //记录数
            shardingMonth.setDaySign(Integer.parseInt(DateUtils.getDateDay(beginTime)));
            Integer dataValue = shardingMonthService.selectShardingMonthCount(shardingMonth);
            ShardingMonth dto = new ShardingMonth();
            dto.setStatisticValue(dataValue.doubleValue());
            shardingMonthList.add(dto);
        }

        if (shardingMonthList.isEmpty()) {
            return xAxis;
        }

        //计算总电量（保留此方法）
        //if (variableVo.getIsChain() == 0) {
        //    if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_D.getKey())) {
        //        totalElectric = shardingMonthList.stream().mapToDouble(ShardingMonth::getAvgValue).sum();
        //    } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
        //        totalElectric = shardingMonthList.stream().mapToDouble(ShardingMonth::getAccuValue).sum();
        //    } else {
        //        totalElectric = shardingMonthList.stream().mapToDouble(ShardingMonth::getStatisticValue).sum();
        //    }
        //}

        //组装X轴（在有多个变量的情况下，组装一次就够了）
        if (xAxis.isEmpty()) {
            if (xAxisType == XAxisTypeEnum.DAY.getValue()) {   //日数据
                xAxis = DateUtils.getDayList(xBeginTime, xEndTime);
            }
            if (xAxisType == XAxisTypeEnum.MONTH.getValue()) { //月数据
                xAxis = DateUtils.getMonthList(xBeginTime, xEndTime);
            }
            if (xAxisType == XAxisTypeEnum.YEAR.getValue()) {  //年数据
                xAxis = DateUtils.getYearList(xBeginTime, xEndTime);
            }
        }

        // 一个变量一个单位（如：电流和电压就是不同的）
        if (!yAxisUnit.contains(variableVo.getUnit())) {
            yAxisUnit.add(variableVo.getUnit());
        }

        // 数据单位
        dataUnit.add(variableVo.getUnit());

        chartType.add(variableVo.getChartType());
        legend.add(variableVo.getVarName());

        //region //注意：如果某一天没有数据时会错位。所以要循环X轴，而不是数据列表
        List<Object> emptyList = new ArrayList<>();
        for (String item : xAxis) {
            List<ShardingMonth> list;
            //1平均 2累计 其它为统计值
            int markSign = 0;

            if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_D.getKey())) {
                //日变化值（月统计表取..平均值）
                //这里要用min_time，而不用save_time，原因是最后一次更新时间有可能是第二天的0点
                list = shardingMonthList.stream().filter(s -> DateUtils.dateTime(s.getMinTime()).equals(item)).collect(Collectors.toList());
                markSign = 1;
            } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
                //日累计值（月统计表取..累计值）
                list = shardingMonthList.stream().filter(s -> DateUtils.dateTime(s.getMinTime()).equals(item)).collect(Collectors.toList());
                markSign = 2;
            } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_M.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_M.getKey())) {
                //月累计值
                list = shardingMonthList.stream().filter(s -> (s.getYear() + "-" + StringUtils.padLeft(s.getMonth(), 2)).equals(item)).collect(Collectors.toList());
                markSign = 3;
            } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_Y.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_Y.getKey())) {
                //年累计值
                list = shardingMonthList.stream().filter(s -> s.getYear().toString().equals(item)).collect(Collectors.toList());
                markSign = 4;
            } else {
                //取单值的情况下
                list = shardingMonthList;
            }

            if (list.isEmpty()) {
                emptyList.add(0);
            } else {
                if (variableVo.getIsChain() == 1) {
                    emptyList.add(list.get(0).getChainValue());
                } else {
                    switch (markSign) {
                        case 1:
                            emptyList.add(list.get(0).getAvgValue());
                            break;
                        case 2:
                            emptyList.add(list.get(0).getAccuValue());
                            break;
                        default:
                            emptyList.add(list.get(0).getStatisticValue());
                    }
                }
            }
        }

        ChartYAxisVo chartTempVo = new ChartYAxisVo();
        chartTempVo.setName(variableVo.getVarName());
        chartTempVo.setChartType(variableVo.getChartType());
        chartTempVo.setyAxisUnit(variableVo.getUnit());
        chartTempVo.setDataList(emptyList);
        for (int i = 0; i < yAxisUnit.size(); i++) {
            if (yAxisUnit.get(i).equals(variableVo.getUnit())) {
                chartTempVo.setyAxisIndex(i);
            }
        }
        yAxis.add(chartTempVo);
        //endregion

        return xAxis;
    }

    /**
     * 从【从累积数据，中间表】数据存储表中提取
     */
    private List<String> packageAccumData(FeignVariableConfigVo variableVo, String beginTime, String endTime, List<String> xAxis, List<ChartYAxisVo> yAxis,
                                          List<String> yAxisUnit, List<String> dataUnit, List<String> chartType, List<String> legend) {

        //查询条件
        ShardingMonthAccumulate shardingAccu = new ShardingMonthAccumulate();
        shardingAccu.setVariableName(variableVo.getVarSn());
        shardingAccu.setParams(DateUtils.dateToParamForDay(beginTime));

        //取值类型：枚举 ShardingDataTypeEnum
        List<ShardingMonthAccumulate> shardingAccuList = shardingMonthAccumulateService.selectShardingMonthAccumulateList(shardingAccu, variableVo.getIsChain());

        if (shardingAccuList.isEmpty()) {
            return xAxis;
        }

        //计算总电量（保留此方法）
        //if (variableVo.getIsChain() == 0) {
        //    totalElectric = shardingAccuList.stream().mapToDouble(ShardingMonthAccumulate::getAccuData).sum();
        //}

        //组装X轴（在有多个变量的情况下，组装一次就够了）
        if (xAxis.isEmpty()) {
            //X轴时间
            ShardingMonthAccumulate bVo = shardingAccuList.get(0);
            String xBeginTime = DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue(), bVo.getSaveTime());

            ShardingMonthAccumulate eVo = shardingAccuList.get(shardingAccuList.size() - 1);
            String xEndTime = DateUtils.parseDateToStr(DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getValue(), eVo.getSaveTime());

            //小时数据
            xAxis = DateUtils.getHourList(xBeginTime, xEndTime);
        }

        // 一个变量一个单位（如：电流和电压就是不同的）
        if (!yAxisUnit.contains(variableVo.getUnit())) {
            yAxisUnit.add(variableVo.getUnit());
        }

        // 数据单位
        dataUnit.add(variableVo.getUnit());

        chartType.add(variableVo.getChartType());
        legend.add(variableVo.getVarName());

        //region //注意：如果某一天没有数据时会错位。所以要循环X轴，而不是数据列表
        List<Object> emptyList = new ArrayList<>();
        for (String item : xAxis) {
            List<ShardingMonthAccumulate> list = shardingAccuList.stream().filter(s -> (s.getRecordHour() + "时").equals(item)).collect(Collectors.toList());

            if (list.isEmpty()) {
                emptyList.add(0);
            } else {
                emptyList.add(list.get(0).getAccuData());
            }
        }

        ChartYAxisVo chartTempVo = new ChartYAxisVo();
        chartTempVo.setName(variableVo.getVarName());
        chartTempVo.setChartType(variableVo.getChartType());
        chartTempVo.setyAxisUnit(variableVo.getUnit());
        chartTempVo.setDataList(emptyList);
        for (int i = 0; i < yAxisUnit.size(); i++) {
            if (yAxisUnit.get(i).equals(variableVo.getUnit())) {
                chartTempVo.setyAxisIndex(i);
            }
        }
        yAxis.add(chartTempVo);
        //endregion

        return xAxis;
    }
    //endregion

    //region 从数据存储表中提取（堆叠图表）

    /**
     * 从数据存储表中提取（堆叠图表）
     */
    private List<String> packagePPFVData(FeignVariableConfigVo variableVo, Integer xAxisType, String beginTime, String endTime, List<String> xAxis, List<ChartYAxisVo> yAxis, List<String> yAxisUnit, List<String> dataUnit, List<String> chartType, List<String> legend) {

        // 获取尖锋平谷类别
        List<SysCommonDictData> dictDataList = dictDataService.getListByDictType("sys_seasonal_type",SecurityConstants.INNER);
        for (SysCommonDictData dictData : dictDataList) {
            if (!legend.contains(dictData.getDictLabel())) {
                legend.add(dictData.getDictLabel());
            }
        }

        //临时：如果是计算环比，还要取出前一天（有、年）的值
        List<ShardingMonthAccumulate> chainAccumulateList = new ArrayList<>();

        //X轴时间
        String xBeginTime = beginTime;
        String xEndTime = endTime;

        //查询结果
        List<ShardingMonthAccumulate> shardingMonthAccumulateList = new ArrayList<>();

        //日累计值
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
            shardingMonthAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByDay(variableVo.getVarSn(), beginTime, endTime);

            //X轴的开始、结束日期
            if (!shardingMonthAccumulateList.isEmpty()) {
                ShardingMonthAccumulate bVo = shardingMonthAccumulateList.get(0);
                xBeginTime = bVo.getRecordYear() + "-" + StringUtils.padLeft(bVo.getRecordMonth(), 2) + "-" + StringUtils.padLeft(bVo.getRecordDay(), 2) + " 00:00:00";

                ShardingMonthAccumulate eVo = shardingMonthAccumulateList.get(shardingMonthAccumulateList.size() - 1);
                xEndTime = eVo.getRecordYear() + "-" + StringUtils.padLeft(eVo.getRecordMonth(), 2) + "-" + StringUtils.padLeft(eVo.getRecordDay(), 2) + " 23:59:59";
            }

            //如果是环比值，还要取出前一天的值
            if (variableVo.getIsChain() == 1) {
                String forwardTime = DateUtils.getPastDate(xBeginTime, -1);
                Map<String, Object> map = DateUtils.dateToParamForDay(forwardTime);
                assert map != null;
                chainAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByDay(variableVo.getVarSn(), map.get("beginTime").toString(), map.get("endTime").toString());
            }
        }

        //月累计值
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_M.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_M.getKey())) {
            shardingMonthAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByMonth(variableVo.getVarSn(), beginTime, endTime);

            //X轴的开始、结束日期
            if (!shardingMonthAccumulateList.isEmpty()) {
                ShardingMonthAccumulate bVo = shardingMonthAccumulateList.get(0);
                xBeginTime = bVo.getRecordYear() + "-" + StringUtils.padLeft(bVo.getRecordMonth(), 2) + "-01 00:00:00";

                ShardingMonthAccumulate eVo = shardingMonthAccumulateList.get(shardingMonthAccumulateList.size() - 1);
                xEndTime = eVo.getRecordYear() + "-" + StringUtils.padLeft(eVo.getRecordMonth(), 2) + "-31 23:59:59";
            }

            //如果是环比值，还要取出前一月的值
            if (variableVo.getIsChain() == 1) {
                String forwardTime = DateUtils.getPastMonth(xBeginTime, -1);
                Map<String, Object> map = DateUtils.dateToParamForMonth(forwardTime, forwardTime);
                assert map != null;
                chainAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByMonth(variableVo.getVarSn(), map.get("beginTime").toString(), map.get("endTime").toString());
            }

        }

        //年累计值
        if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_Y.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_Y.getKey())) {
            shardingMonthAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByYear(variableVo.getVarSn(), beginTime, endTime);

            //X轴的开始、结束日期
            if (!shardingMonthAccumulateList.isEmpty()) {
                xBeginTime = shardingMonthAccumulateList.get(0).getRecordYear() + "-01-01 00:00:00";
                xEndTime = shardingMonthAccumulateList.get(shardingMonthAccumulateList.size() - 1).getRecordYear() + "-12-31 23:59:59";
            }

            //如果是环比值，还要取出前一年的值
            if (variableVo.getIsChain() == 1) {
                String beginYear = xBeginTime.substring(0, 4);
                Map<String, Object> map = DateUtils.dateToParamForYear(Integer.parseInt(beginYear));
                chainAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByYear(variableVo.getVarSn(), map.get("beginTime").toString(), map.get("endTime").toString());
            }
        }

        //组装X轴（在有多个变量的情况下，组装一次就够了）
        //X轴应根据所获取数据的时间组装
        if (xAxis.isEmpty()) {
            if (xAxisType == XAxisTypeEnum.DAY.getValue()) {
                //日数据
                xAxis = DateUtils.getDayList(xBeginTime, xEndTime);
            }
            if (xAxisType == XAxisTypeEnum.MONTH.getValue()) {
                //月数据
                xAxis = DateUtils.getMonthList(xBeginTime, xEndTime);
            }
            if (xAxisType == XAxisTypeEnum.YEAR.getValue()) {
                //年数据
                xAxis = DateUtils.getYearList(xBeginTime, xEndTime);
            }
        }

        // 一个变量一个单位（如：电流和电压就是不同的）
        if (!yAxisUnit.contains(variableVo.getUnit())) {
            yAxisUnit.add(variableVo.getUnit());
        }

        // 数据单位
        dataUnit.add(variableVo.getUnit());

        // 一个图表一种类型
        chartType.add(variableVo.getChartType());

        //region //以前的旧方法会出现一种情况，就是某一天没有数据时会错位。所以要循环Legend和X轴，而不是数据列表
        // 这里有三层数据结构
        // 1.X轴时间列表
        // 2.Legend
        // 3.数据列表
        // 因此，循环关系不能错。最外层是Legend，中间层是X轴，内层是数据列表
        List<ChartYAxisVo> yxisTemp = new ArrayList<>();
        for (String leg : legend) {
            List<Object> emptyList = new ArrayList<>();
            for (String x : xAxis) {
                List<ShardingMonthAccumulate> list = new ArrayList<>();
                if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
                    //日累计值
                    list = shardingMonthAccumulateList.stream()
                            .filter(s -> (s.getRecordYear() + "-" + StringUtils.padLeft(s.getRecordMonth(), 2) + "-" + StringUtils.padLeft(s.getRecordDay(), 2)).equals(x))
                            .filter(s -> s.getSeasonalTypeName().equals(leg))
                            .collect(Collectors.toList());
                } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_M.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_M.getKey())) {
                    //月累计值
                    list = shardingMonthAccumulateList.stream()
                            .filter(s -> (s.getRecordYear() + "-" + StringUtils.padLeft(s.getRecordMonth(), 2)).equals(x))
                            .filter(s -> s.getSeasonalTypeName().equals(leg))
                            .collect(Collectors.toList());
                } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_Y.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_Y.getKey())) {
                    //年累计值
                    list = shardingMonthAccumulateList.stream()
                            .filter(s -> s.getRecordYear().toString().equals(x))
                            .filter(s -> s.getSeasonalTypeName().equals(leg))
                            .collect(Collectors.toList());
                }

                if (list.isEmpty()) {
                    emptyList.add(0);
                } else {
                    if (variableVo.getUnit().equalsIgnoreCase("kwh")) {
                        // 用电量
                        emptyList.add(list.get(0).getAccuData());
                    } else if (variableVo.getUnit().equalsIgnoreCase("rmb") || variableVo.getUnit().equals("元")) {
                        // 电费
                        emptyList.add(list.get(0).getChargePrice());
                    } else {
                        // 环比（通过累计值计算环比）
                        emptyList.add(list.get(0).getAccuData());
                    }
                }
            }

            ChartYAxisVo chartTempVo = new ChartYAxisVo();
            chartTempVo.setName(leg);
            chartTempVo.setGroupName(variableVo.getVarName());
            chartTempVo.setChartType(variableVo.getChartType());
            chartTempVo.setyAxisUnit(variableVo.getUnit());
            chartTempVo.setDataList(emptyList);
            for (int i = 0; i < yAxisUnit.size(); i++) {
                if (yAxisUnit.get(i).equals(variableVo.getUnit())) {
                    chartTempVo.setyAxisIndex(i);
                }
            }
            yxisTemp.add(chartTempVo);
        }
        //endregion

        //region 如果有环比项，那么取出并计算环比
        if (variableVo.getIsChain() == 1) {
            for (ChartYAxisVo entry : yxisTemp) {
                List<Object> computeList = entry.getDataList();
                List<Object> chainList = new ArrayList<>();
                for (int j = 0; j < computeList.size(); j++) {
                    if (j == 0) {
                        if (chainAccumulateList.isEmpty()) {
                            chainList.add(0);
                            continue;
                        }

                        double tempValue = 0f;
                        for (ShardingMonthAccumulate temp : chainAccumulateList) {
                            if (temp.getSeasonalTypeName().equals(entry.getName())) {
                                tempValue = temp.getAccuData();
                                break;
                            }
                        }

                        if (tempValue == 0) {
                            chainList.add(0);
                        } else {
                            chainList.add(FormatUtils.computePercent(computeList.get(j), tempValue));
                        }

                        continue;
                    }

                    if ("0".equals(computeList.get(j - 1).toString())) {
                        chainList.add(0);
                        continue;
                    }

                    chainList.add(FormatUtils.computePercent(computeList.get(j), computeList.get(j - 1)));
                }
                entry.setDataList(chainList);
            }
        }
        //endregion

        //region 为了保持和普通图表一样的结构，重新封装一下
        List<ChartYAxisVo> newYAxis = new ArrayList<>();
        for (ChartYAxisVo vo : yxisTemp) {
            vo.setName(vo.getGroupName() + "-" + vo.getName());
            newYAxis.add(vo);
        }
        //endregion

        yAxis.addAll(newYAxis);

        return xAxis;
    }
    //endregion

    //region 提取累计数据（尖、峰、平、谷、深谷）（未使用，使用融合方法）

    /**
     * 提取累计数据（尖、峰、平、谷、深谷）（未使用，使用融合方法）
     *
     * @param pageConfig 变量配置（Json：变量/单位/存储类型：1变化值 2累计值/取值类型：枚举）
     * @param xAxisType  X轴数据类型，枚举：XAxisTypeEnum
     * @param beginTime  开始日期
     * @param endTime    结束日期
     * @return 结果
     */
    @Override
    public Map<String, Object> packageAccumulatePPFVData(FeignSysPageConfig pageConfig, Integer xAxisType, String beginTime, String endTime) {

        // 返回结果
        Map<String, Object> resultMap = new HashMap<>();

        // X轴（如：2023-10-20，2023-10-21...）
        List<String> xAxis = new ArrayList<>();

        // Y轴单位（如：用电量（kW-h））
        List<String> yAxisUnit = new ArrayList<>();

        // 图表类型
        List<String> chartType = new ArrayList<>();

        // Y轴（存在多曲线情况）
        List<List<ChartYAxisVo>> yAxis = new ArrayList<>();

        // Legend（如：用电量、电流、电压）
        List<String> legend = new ArrayList<>();

        // 获取尖锋平谷类别
        List<SysCommonDictData> dictDataList = dictDataService.getListByDictType("sys_seasonal_type",SecurityConstants.INNER);
        for (SysCommonDictData dictData : dictDataList) {
            legend.add(dictData.getDictLabel());
        }

        // 临时：存储是否取环比值
        List<Integer> tempChain = new ArrayList<>();

        //临时：如果是计算环比，还要取出前一天（有、年）的值
        List<ShardingMonthAccumulate> chainAccumulateList = new ArrayList<>();

        // 当前模块有几个变量，轮询读取
        for (FeignVariableConfigVo variableVo : pageConfig.getVariableConfig()) {
            //X轴时间
            String xBeginTime = beginTime;
            String xEndTime = endTime;

            //查询结果
            List<ShardingMonthAccumulate> shardingMonthAccumulateList = new ArrayList<>();

            if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
                //日累计值
                shardingMonthAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByDay(variableVo.getVarSn(), beginTime, endTime);

                //X轴的开始、结束日期
                if (!shardingMonthAccumulateList.isEmpty()) {
                    ShardingMonthAccumulate bVo = shardingMonthAccumulateList.get(0);
                    xBeginTime = bVo.getRecordYear() + "-" + StringUtils.padLeft(bVo.getRecordMonth(), 2) + "-" + StringUtils.padLeft(bVo.getRecordDay(), 2) + " 00:00:00";

                    ShardingMonthAccumulate eVo = shardingMonthAccumulateList.get(shardingMonthAccumulateList.size() - 1);
                    xEndTime = eVo.getRecordYear() + "-" + StringUtils.padLeft(eVo.getRecordMonth(), 2) + "-" + StringUtils.padLeft(eVo.getRecordDay(), 2) + " 23:59:59";
                }

                //如果是环比值，还要取出前一天的值
                if (variableVo.getIsChain() == 1) {
                    String forwardTime = DateUtils.getPastDate(xBeginTime, -1);
                    Map<String, Object> map = DateUtils.dateToParamForDay(forwardTime);
                    assert map != null;
                    chainAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByDay(variableVo.getVarSn(), map.get("beginTime").toString(), map.get("endTime").toString());
                }
            }
            if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_M.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_M.getKey())) {
                //月累计值
                shardingMonthAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByMonth(variableVo.getVarSn(), beginTime, endTime);

                //X轴的开始、结束日期
                if (!shardingMonthAccumulateList.isEmpty()) {
                    ShardingMonthAccumulate bVo = shardingMonthAccumulateList.get(0);
                    xBeginTime = bVo.getRecordYear() + "-" + StringUtils.padLeft(bVo.getRecordMonth(), 2) + "-01 00:00:00";

                    ShardingMonthAccumulate eVo = shardingMonthAccumulateList.get(shardingMonthAccumulateList.size() - 1);
                    xEndTime = eVo.getRecordYear() + "-" + StringUtils.padLeft(eVo.getRecordMonth(), 2) + "-31 23:59:59";
                }

                //如果是环比值，还要取出前一月的值
                if (variableVo.getIsChain() == 1) {
                    String forwardTime = DateUtils.getPastMonth(xBeginTime, -1);
                    Map<String, Object> map = DateUtils.dateToParamForMonth(forwardTime, forwardTime);
                    assert map != null;
                    chainAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByMonth(variableVo.getVarSn(), map.get("beginTime").toString(), map.get("endTime").toString());
                }

            }
            if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_Y.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_Y.getKey())) {
                //年累计值
                shardingMonthAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByYear(variableVo.getVarSn(), beginTime, endTime);

                //X轴的开始、结束日期
                if (!shardingMonthAccumulateList.isEmpty()) {
                    xBeginTime = shardingMonthAccumulateList.get(0).getRecordYear() + "-01-01 00:00:00";
                    xEndTime = shardingMonthAccumulateList.get(shardingMonthAccumulateList.size() - 1).getRecordYear() + "-12-31 23:59:59";
                }

                //如果是环比值，还要取出前一年的值
                if (variableVo.getIsChain() == 1) {
                    String beginYear = xBeginTime.substring(0, 4);
                    Map<String, Object> map = DateUtils.dateToParamForYear(Integer.parseInt(beginYear));
                    chainAccumulateList = shardingMonthAccumulateService.selectAccumulateGroupByYear(variableVo.getVarSn(), map.get("beginTime").toString(), map.get("endTime").toString());
                }
            }

            //组装X轴（在有多个变量的情况下，组装一次就够了）
            //X轴应根据所获取数据的时间组装
            if (xAxis.isEmpty()) {
                if (xAxisType == XAxisTypeEnum.DAY.getValue()) {
                    //日数据
                    xAxis = DateUtils.getDayList(xBeginTime, xEndTime);
                }
                if (xAxisType == XAxisTypeEnum.MONTH.getValue()) {
                    //月数据
                    xAxis = DateUtils.getMonthList(xBeginTime, xEndTime);
                }
                if (xAxisType == XAxisTypeEnum.YEAR.getValue()) {
                    //年数据
                    xAxis = DateUtils.getYearList(xBeginTime, xEndTime);
                }
            }

            // 一个变量一个单位（如：电流和电压就是不同的）
            if (!yAxisUnit.contains(variableVo.getUnit())) {
                yAxisUnit.add(variableVo.getUnit());
            }

            // 一个图表一种类型
            chartType.add(variableVo.getChartType());

            // 临时存储
            tempChain.add(variableVo.getIsChain());

            //region //注意：如果某一天没有数据时会错位。所以要循环Legend和X轴，而不是数据列表
            // 这里有三层数据结构
            // 1.X轴时间列表
            // 2.Legend
            // 3.数据列表
            // 因此，循环关系不能错。最外层是Legend，中间层是X轴，内层是数据列表
            List<ChartYAxisVo> yxisTemp = new ArrayList<>();
            for (String leg : legend) {
                List<Object> emptyList = new ArrayList<>();
                for (String x : xAxis) {
                    List<ShardingMonthAccumulate> list = new ArrayList<>();
                    if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_D.getKey())) {
                        //日累计值
                        list = shardingMonthAccumulateList.stream()
                                .filter(s -> (s.getRecordYear() + "-" + StringUtils.padLeft(s.getRecordMonth(), 2) + "-" + StringUtils.padLeft(s.getRecordDay(), 2)).equals(x))
                                .filter(s -> s.getSeasonalTypeName().equals(leg))
                                .collect(Collectors.toList());
                    } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_M.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_M.getKey())) {
                        //月累计值
                        list = shardingMonthAccumulateList.stream()
                                .filter(s -> (s.getRecordYear() + "-" + StringUtils.padLeft(s.getRecordMonth(), 2)).equals(x))
                                .filter(s -> s.getSeasonalTypeName().equals(leg))
                                .collect(Collectors.toList());
                    } else if (variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_BH_Y.getKey()) || variableVo.getDataType().equals(ShardingDataTypeEnum.MONTH_LJ_Y.getKey())) {
                        //年累计值
                        list = shardingMonthAccumulateList.stream()
                                .filter(s -> s.getRecordYear().toString().equals(x))
                                .filter(s -> s.getSeasonalTypeName().equals(leg))
                                .collect(Collectors.toList());
                    }

                    if (list.isEmpty()) {
                        emptyList.add(0);
                    } else {
                        if (variableVo.getUnit().equalsIgnoreCase("kwh")) {
                            // 用电量
                            emptyList.add(list.get(0).getAccuData());
                        } else if (variableVo.getUnit().equalsIgnoreCase("rmb") || variableVo.getUnit().equals("元")) {
                            // 电费
                            emptyList.add(list.get(0).getChargePrice());
                        } else {
                            // 环比（通过累计值计算环比）
                            emptyList.add(list.get(0).getAccuData());
                        }
                    }
                }

                ChartYAxisVo chartTempVo = new ChartYAxisVo();
                chartTempVo.setName(leg);
                chartTempVo.setGroupName(variableVo.getVarName());
                chartTempVo.setChartType(variableVo.getChartType());
                chartTempVo.setDataList(emptyList);
                yxisTemp.add(chartTempVo);
            }
            //endregion

            yAxis.add(yxisTemp);
        }

        resultMap.put("xAxis", xAxis);
        resultMap.put("yAxisUnit", yAxisUnit);
        resultMap.put("chartType", chartType);

        //region 如果有环比项，那么取出并计算环比
        for (int k = 0; k < tempChain.size(); k++) {
            if (tempChain.get(k) == 0) {
                continue;
            }

            //意思就是：找出第几项是环比
            List<ChartYAxisVo> computeLiVo = yAxis.get(k);

            for (ChartYAxisVo entry : computeLiVo) {
                List<Object> computeList = entry.getDataList();
                List<Object> chainList = new ArrayList<>();
                for (int j = 0; j < computeList.size(); j++) {
                    if (j == 0) {
                        if (chainAccumulateList.isEmpty()) {
                            chainList.add(0);
                            continue;
                        }

                        double tempValue = 0f;
                        for (ShardingMonthAccumulate temp : chainAccumulateList) {
                            if (temp.getSeasonalTypeName().equals(entry.getName())) {
                                tempValue = temp.getAccuData();
                                break;
                            }
                        }

                        if (tempValue == 0) {
                            chainList.add(0);
                        } else {
                            chainList.add(FormatUtils.computePercent(computeList.get(j), tempValue));
                        }

                        continue;
                    }

                    if ("0".equals(computeList.get(j - 1).toString())) {
                        chainList.add(0);
                        continue;
                    }

                    chainList.add(FormatUtils.computePercent(computeList.get(j), computeList.get(j - 1)));
                }
                entry.setDataList(chainList);
            }

            yAxis.set(k, computeLiVo);
        }
        //endregion

        //region 说明：为了保持和普通图表一样的结构，重新封装一下
        List<ChartYAxisVo> newYAxis = new ArrayList<>();
        for (List<ChartYAxisVo> LiVo : yAxis) {
            for (ChartYAxisVo vo : LiVo) {
                vo.setName(vo.getGroupName() + "-" + vo.getName());
                newYAxis.add(vo);
            }
        }
        //endregion

        resultMap.put("yAxis", newYAxis);

        //region 重新计算Legend（使用新方法后不需要再计算）
        /* *
        if (yAxis.size() > 0) {
            Map<String, ChartYAxisVo> legendMap = yAxis.get(0);
            legend = new ArrayList<>(legendMap.keySet());
        }
        resultMap.put("legend", legend);
        */
        //endregion

        resultMap.put("legend", legend);
        return resultMap;
    }
    //endregion

    //region 获取【尖峰平谷】数据（表格数据）（未使用）

    /**
     * 获取【尖峰平谷】数据（表格数据）（未使用）
     *
     * @param deviceSns 变量
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public Map<String, Object> selectAccumulateGroupBySeasonalName(String[] deviceSns, String beginTime, String endTime) {

        // 通过设备SN取得变量SN（变量只取累积量且正向有功电能）
        List<String> deviceSnList = Arrays.asList(deviceSns);
        Map<String, Object> params = new HashMap<>();
        params.put("deviceSns", deviceSnList);

        FeignMonitorDeviceVar monitorDeviceVar = new FeignMonitorDeviceVar();
        monitorDeviceVar.setIsAccumulation(1);
        monitorDeviceVar.setVarMapSn("EPF");
        monitorDeviceVar.setParams(params);
        monitorDeviceVar.setStopFlag(0);
        List<FeignMonitorDeviceVar> deviceVarList = monitorDeviceVarService.getListInner(monitorDeviceVar, SecurityConstants.INNER);

        Map<String, Object> result = new HashMap<>();

        for (FeignMonitorDeviceVar deviceVar : deviceVarList) {
            List<ShardingMonthAccumulate> accumulateList = shardingMonthAccumulateService.selectAccumulateGroupBySeasonalName(deviceVar.getVarSn(), beginTime, endTime);

            Double total = 0D;
            Map<String, Double> temp = new HashMap<>();
            for (ShardingMonthAccumulate item : accumulateList) {
                total += item.getAccuData();
                temp.put(item.getSeasonalTypeName(), item.getAccuData());
            }
            temp.put("倍率", DoubleUtils.floatToDouble(deviceVar.getCoefficient()));
            temp.put("总", total);
            result.put(deviceVar.getVarName(), temp);
        }

        return result;
    }
    //endregion

    //region 获取【尖峰平谷】数据（表格数据）-2

    /**
     * 获取【尖峰平谷】数据（表格数据）-2
     *
     * @param deviceSns 变量
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<List<EnumSOVO>> selectAccumulateGroupBySeasonalName2(String[] deviceSns, String beginTime, String endTime) {
        // 通过设备SN取得变量SN（变量只取累积量且正向有功电能）
        List<String> deviceSnList = Arrays.asList(deviceSns);
        Map<String, Object> params = new HashMap<>();
        params.put("deviceSns", deviceSnList);

        // 取出统计数据列表
        FeignMonitorDeviceVar monitorDeviceVar = new FeignMonitorDeviceVar();
        monitorDeviceVar.setIsAccumulation(1);
        monitorDeviceVar.setVarMapSn("EPF");
        monitorDeviceVar.setParams(params);
        monitorDeviceVar.setStopFlag(0);
        List<FeignMonitorDeviceVar> deviceVarList = monitorDeviceVarService.getListInner(monitorDeviceVar, SecurityConstants.INNER);

        // 取出尖峰平谷
        List<SysCommonDictData> dictDataList = dictDataService.getListByDictType("sys_seasonal_type",SecurityConstants.INNER);

        // 结果
        List<List<EnumSOVO>> result = new ArrayList<>();

        for (FeignMonitorDeviceVar deviceVar : deviceVarList) {
            List<ShardingMonthAccumulate> accumulateList = shardingMonthAccumulateService.selectAccumulateGroupBySeasonalName(deviceVar.getVarSn(), beginTime, endTime);

            List<EnumSOVO> singleList = new ArrayList<>();

            EnumSOVO temp1 = new EnumSOVO();
            temp1.setKey("设备");
            temp1.setValue(deviceVar.getVarName());
            singleList.add(temp1);

            Double total = 0D;

            //region //因为要按顺序返回数据，因此需要按顺序排序（旧方法不删除，备份）
            /* *
            for (ShardingMonthAccumulate item : accumulateList) {
                total += item.getAccuData();

                EnumSOVO temp = new EnumSOVO();
                temp.setKey(item.getSeasonalTypeName());
                temp.setValue(item.getAccuData() + "");
                singleList.add(temp);
            }
            */
            //endregion

            //region //解决上述问题，按“尖峰平谷”循环取数
            for (SysCommonDictData dictData : dictDataList) {
                List<ShardingMonthAccumulate> list = accumulateList.stream().filter(s -> s.getSeasonalTypeName().equals(dictData.getDictLabel())).collect(Collectors.toList());
                EnumSOVO temp = new EnumSOVO();
                temp.setKey(dictData.getDictLabel());

                if (list.isEmpty()) {
                    temp.setValue(0);
                } else {
                    temp.setValue(list.get(0).getAccuData());
                    total += list.get(0).getAccuData();
                }
                singleList.add(temp);
            }
            //endregion

            EnumSOVO temp2 = new EnumSOVO();
            temp2.setKey("倍率");
            temp2.setValue(String.valueOf(deviceVar.getCoefficient()));
            singleList.add(temp2);

            EnumSOVO temp3 = new EnumSOVO();
            temp3.setKey("总");
            temp3.setValue(String.valueOf(total));
            singleList.add(temp3);

            result.add(singleList);
        }

        return result;
    }
    //endregion
}
