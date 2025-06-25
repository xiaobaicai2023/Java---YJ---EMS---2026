package com.yunpower.datav.controller;

import com.alibaba.fastjson.JSON;
import com.yunpower.common.core.enums.DateFormatEnum;
import com.yunpower.datav.domain.dto.ChartConfigDto;
import com.yunpower.datav.domain.vo.ChartQueryVo;
import com.yunpower.datav.enums.DatavDateDimEnum;
import com.yunpower.datav.enums.DatavDateDimSecondEnum;
import com.yunpower.datav.enums.SysChartTypeEnum;
import com.yunpower.datav.service.CommonChartService;
import com.yunpower.common.core.constant.SecurityConstants;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.core.web.controller.BaseController;
import com.yunpower.common.core.web.domain.AjaxResult;
import com.yunpower.system.api.RemoteDashboardConfigCardService;
import com.yunpower.system.api.domain.DashboardConfigCard;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用图表接口
 * @author XIAOTONG.CAO
 * @date: 2024-06-12 10:00
 */
@Api(tags = ">>> 通用图表接口 <<<")
@RestController
@RequestMapping("/common/chart")
public class CommonChartController extends BaseController {

    @Autowired(required = false)
    private RemoteDashboardConfigCardService remoteDashboardConfigCardService;

    @Autowired
    private CommonChartService commonChartService;

    /**
     * 获取>> 通用图表数据
     * @param vo 查询参数
     * */
    @ApiOperation("获取>>通用图表数据")
    @GetMapping("/getChart")
    public AjaxResult getChartData(@Validated ChartQueryVo vo) {
        //获取配置数据
        DashboardConfigCard config = remoteDashboardConfigCardService.getInfo(vo.getConfigId(),vo.getCardKey(), SecurityConstants.INNER);
        if(StringUtils.isNull(config)){
            return success();
        }
        //通用变量需要前端传入设备编码
        //指定变量
        if(!SysChartTypeEnum.SpecialVarList.contains(config.getChartType()) && config.getIndicator() == 2 && StringUtils.isEmpty(vo.getDeviceSn())){
            return error("请选择设备");
        }
        if(StringUtils.isNull(config.getIsFullDate())){
            config.setIsFullDate(0);
        }
        if(config.getSecondDateDim() == null || config.getSecondDateDim()<=0){
            config.setSecondDateDim(DatavDateDimSecondEnum.HOUR_1.getKey());
        }
        vo.setDashboardConfigCard(config);
        vo.setChartConfig(JSON.parseObject(config.getCardConfig(), ChartConfigDto.class));
        //如果时间都为空 则默认取今日的数据
        //非专用图表
        if(StringUtils.isEmpty(vo.getDayTime()) && StringUtils.isEmpty(vo.getMonthTime()) && StringUtils.isEmpty(vo.getYearTime())){
            vo.setDayTime(DateUtils.getDate());
        }
        //单值图表、峰谷报表 默认是全日期 SysChartTypeEnum.SINGLE_VALUE.getKey().equals(config.getChartType()) ||
        if(SysChartTypeEnum.STACKED_CHART.getKey().equals(config.getChartType())){
            config.setIsFullDate(1);
            config.setSecondDateDim(DatavDateDimSecondEnum.HOUR_1.getKey());
        }
        //单值图-按月和按年 重新计算时间
        if(SysChartTypeEnum.SINGLE_VALUE.getKey().equals(config.getChartType()) ){
            if(StringUtils.isNotNull(config.getDateDim())){
                if(DatavDateDimEnum.MONTH.getKey().equals(config.getDateDim())){
                    vo.setDayTime(null);
                    vo.setMonthTime(DateUtils.dateTimeNow(DateFormatEnum.YYYY_MM.getValue()));
                }
                if(DatavDateDimEnum.YEAR.getKey().equals(config.getDateDim())){
                    vo.setDayTime(null);
                    vo.setYearTime(String.valueOf(DateUtils.getCurrYear()));
                }
                if(DatavDateDimEnum.TOTAL.getKey().equals(config.getDateDim())){
                     vo.setDayTime(null);
                }
            }
        }
        //处理图表配置
        return success(commonChartService.handleChartData(vo));
    }
}
