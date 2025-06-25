package com.yunpower.datav.service;

import com.yunpower.datav.domain.vo.ChartCardInfoVo;
import com.yunpower.datav.domain.vo.ChartQueryVo;
import com.yunpower.datav.enums.SysChartTypeEnum;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;

/**
 * 通用图表服务
 * @author: XIAOTONG.CAO
 * @date: 2024/6/12 16:50
 **/
public interface CommonChartService {

    /**
     * 图表枚举对应的处理方法
     * */
    Map<Integer, Function<ChartQueryVo, ChartCardInfoVo>> CHART_TYPE_MAP = Maps.newHashMapWithExpectedSize(SysChartTypeEnum.values().length);

    /**
     * 获取图表数据
     * @param queryVo 查询参数
     * @return 对象
     **/
    Object handleChartData(ChartQueryVo queryVo);
}
