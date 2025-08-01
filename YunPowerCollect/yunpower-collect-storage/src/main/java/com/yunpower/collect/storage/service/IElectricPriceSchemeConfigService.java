package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.jsonvo.SeasonalRangeVo;

import java.util.Map;

/**
 * @title: 电度电价配置Service接口
 * @Author: Jiajiaglam
 * @date: 2023-12-28 15:12
 * @description:
 */
public interface IElectricPriceSchemeConfigService {
    /**
     * 通过电价标准ID获取尖峰平谷时间段
     * 电价标准ID-YearMonth：<时：（seasonalName, price）>
     */
    public Map<String, Map<Integer, SeasonalRangeVo>> selectSeasonalRangeList();
}
