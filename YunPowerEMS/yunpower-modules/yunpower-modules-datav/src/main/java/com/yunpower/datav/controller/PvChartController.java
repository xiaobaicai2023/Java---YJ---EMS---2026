package com.yunpower.datav.controller;

import com.yunpower.datav.domain.vo.ChartYAxisVo;
import com.yunpower.datav.service.IShardingService;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.utils.ConversionUtils;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.system.api.RemoteAddressService;
import com.yunpower.system.api.RemoteDictDataService;
import com.yunpower.system.api.RemotePageConfigService;
import com.yunpower.system.api.RemotePublicService;
import com.yunpower.system.api.domain.FeignSysPageConfig;
import com.yunpower.system.api.domain.SysStation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: 电站概览（光伏）
 * @Author: Jiajiaglam
 * @date: 2023-11-29 11:14
 * @description:
 */
@Api(tags = ">>> 电站概览（光伏）<<<")
@RestController
@RequestMapping("/pv/chart")
public class PvChartController  extends BaseController {

    @Autowired(required = false)
    private RemotePublicService publicService;

    @Autowired(required = false)
    private RemoteDictDataService dictDataService;

    @Autowired(required = false)
    private RemotePageConfigService remotePageConfigService;

    @Autowired(required = false)
    private RemoteAddressService addressService;

    @Autowired
    private IShardingService shardingService;

    //region *** 获取>>电站概览【电站】基础数据 ***

    /**
     * 获取>>电站概览【电站】基础数据
     */
    @ApiOperation("获取>>电站概览【电站】基础数据")
    @GetMapping("/getStationInfo")
    public AjaxResult getCurrentStationInfo() {
        Map<String, Object> map = new HashMap<>();
        SysStation station = publicService.getCurrentStationInfo(SecurityConstants.INNER);
        if (station != null) {
            map.put("picUrl", station.getPicUrl());
            map.put("stationName", station.getStationName());

            String address = addressService.getAddressName(station.getProvince(), station.getCity(), station.getCounty(), SecurityConstants.INNER) + station.getStationAddress();
            map.put("address", address);

            map.put("pvType", dictDataService.getDictLabel("sys_pv_type", station.getPvType() + "",SecurityConstants.INNER));
            map.put("buildDate", DateUtils.dateTime(station.getBuildSiteTime()));
        }
        return success(map);
    }
    //endregion

    //region *** 获取>>电站概览【发电实时】数据 ***

    /**
     * 获取>>电站概览【发电实时】数据
     */
    @ApiOperation("获取>>电站概览【发电实时】数据")
    @GetMapping("/getRuntimeInfo")
    public AjaxResult getRuntimeInfo() {
        //有三个数据：发电功率、装机容量和功率归一化
        //功率归一化是衡量一座光伏电站发电能力的重要指标之一，用于衡量电站当前发电能力。
        //计算公式：功率归一化=当前发电功率/装机容量
        //例如功率归一化为80%，表示此电站以额定功率的80%运行。
        Map<String, Object> map = new HashMap<>();

        //region 获取发电功率（power-generation）
        float powerGeneration = 0f;
        String dayStr = DateUtils.getPastDate(0);
        String beginTime = DateUtils.completionDayTime(dayStr, true);
        String endTime = DateUtils.completionDayTime(dayStr, false);
        FeignSysPageConfig sysPageConfig = remotePageConfigService.getInfoByPageValue("power-generation",SecurityConstants.INNER);
        if (sysPageConfig != null && !sysPageConfig.getVariableConfig().isEmpty()) {
            Map<String, Object> shardingMap = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);
            List<ChartYAxisVo> yAxis = StringUtils.castList(shardingMap.get("yAxis"), ChartYAxisVo.class);
            if (!yAxis.isEmpty()) {
                Object data = yAxis.get(0).getDataList().get(0);
                if (null != data) {
                    powerGeneration = Float.parseFloat(data.toString());
                }
            }
        }
        //endregion

        //region 获取装机容量
        float stationVolume = 0;
        SysStation station = publicService.getCurrentStationInfo(SecurityConstants.INNER);
        if (station != null) {
            stationVolume = station.getSationVolume();
        }
        //endregion

        //region 计算归一化率
        String normalization = "0.00";
        if (powerGeneration > 0 && stationVolume != 0) {
            normalization = FormatUtils.fmt2point(powerGeneration / stationVolume);
        }
        //endregion

        map.put("发电功率", powerGeneration);
        map.put("装机容量", stationVolume);
        map.put("功率归一化", normalization);
        return success(map);
    }
    //endregion

    //region *** 获取>>电站概览【发电历史】头部数据 ***

    /**
     * 获取>>电站概览【发电历史】头部数据
     */
    @ApiOperation("获取>>电站概览【发电历史】头部数据")
    @GetMapping("/getHistoryHeadInfo/{electric}")
    public AjaxResult getHistoryHeadInfo(@PathVariable("electric") float electric) {
        //有三个数据：当日发电量、当日幅照量、满发电小时
        //满发小时是衡量一座光伏电站发电能力的重要指标之一，日满发小时则表示日发电能力。
        //计算公式：满发小时=发电量/装机容量
        //例如日满发小时为4.5h，表示此电站以额定功率工作了4.5个小时
        Map<String, Object> map = new HashMap<>();

        //region 获取当日发电量（today-pv-power）（通过传入参数计算，不获取了）
        //float todayPvPower = 0f;
        //String dayStr = DateUtils.getPastDate(0);
        //String beginTime = DateUtils.completionDayTime(dayStr, true);
        //String endTime = DateUtils.completionDayTime(dayStr, false);
        //SysPageConfig sysPageConfig = pageConfigService.selectSysPageConfigByPageValue("power-generation"); //发电功率
        //if (sysPageConfig != null && !sysPageConfig.getVariableConfig().isEmpty()) {
        //    Map<String, Object> shardingMap = shardingService.fusionShardingData(sysPageConfig, sysPageConfig.getxAxis(), beginTime, endTime);
        //    List<ChartYAxisVo> yAxis = StringUtils.castList(shardingMap.get("yAxis"), ChartYAxisVo.class);
        //    if (!yAxis.isEmpty()) {
        //        Object data = yAxis.get(0).getDataList().get(0);
        //        if (null != data) {
        //            todayPvPower = Float.parseFloat(data.toString());
        //        }
        //    }
        //}
        //endregion

        //region 获取装机容量
        float stationVolume = 0;
        SysStation station = publicService.getCurrentStationInfo(SecurityConstants.INNER);
        if (station != null) {
            stationVolume = station.getSationVolume();
        }
        //endregion

        //region 计算满发电小时
        float powerHour = 0f;
        if (electric > 0 && stationVolume != 0) {
            powerHour = electric / stationVolume;
            powerHour = Float.parseFloat(FormatUtils.fmt2point(powerHour));
        }
        //endregion

        map.put("当日发电量", electric);
        map.put("当日幅照量", 0);
        map.put("满发电小时", powerHour);
        return success(map);
    }

    /**
     * 获取>>电站概览【发电历史】右侧数据
     */
    @ApiOperation("获取>>电站概览【发电历史】右侧数据")
    @GetMapping("/getHistoryRightInfo/{electric}")
    public AjaxResult getHistoryRightInfo(@PathVariable("electric") float electric) {
        Map<String, Object> map = new HashMap<>();
        map.put("节约标准煤", ConversionUtils.electricConserveCoal(electric));
        map.put("等效植树量", ConversionUtils.electricConserveTree(electric));
        map.put("CO₂减排量", ConversionUtils.electricConserveCO2(electric));
        return success(map);
    }
    //endregion
}
