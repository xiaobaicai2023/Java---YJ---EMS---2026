package com.yunpower.datav.service;

import com.yunpower.common.core.enums.vo.EnumSOVO;
import com.yunpower.system.api.domain.FeignSysPageConfig;

import java.util.List;
import java.util.Map;

/**
 * @title: 日存储数据和月统计数据中间层
 * @Author: Jiajiaglam
 * @date: 2023-10-25 15:34
 * @description:
 */
public interface IShardingService {

    /**
     * 获取图表数据（融合）
     *
     * @param pageConfig 变量配置（Json：变量/单位/存储类型：1变化值 2累计值/取值类型：枚举）
     * @param xAxisType  X轴数据类型，枚举：XAxisTypeEnum
     * @param beginTime  开始日期
     * @param endTime    结束日期
     * @return 结果
     */
    public Map<String, Object> fusionShardingData(FeignSysPageConfig pageConfig, Integer xAxisType, String beginTime, String endTime);

    /**
     * 从数据存储表中提取（普通图表）（未使用，使用融合方法）
     *
     * @param pageConfig 变量配置（Json：变量/单位/存储类型：1变化值 2累计值/取值类型：枚举）
     * @param xAxisType  X轴数据类型，枚举：XAxisTypeEnum
     * @param beginTime  开始日期
     * @param endTime    结束日期
     * @return 结果
     */
    public Map<String, Object> packageShardingData(FeignSysPageConfig pageConfig, Integer xAxisType, String beginTime, String endTime);

    /**
     * 提取累计数据（尖、峰、平、谷、深谷）（未使用，使用融合方法）
     *
     * @param pageConfig 变量配置（Json：变量/单位/存储类型：1变化值 2累计值/取值类型：枚举）
     * @param xAxisType  X轴数据类型，枚举：XAxisTypeEnum
     * @param beginTime  开始日期
     * @param endTime    结束日期
     * @return 结果
     */
    public Map<String, Object> packageAccumulatePPFVData(FeignSysPageConfig pageConfig, Integer xAxisType, String beginTime, String endTime);

    /**
     * 获取【尖峰平谷】数据（表格数据）（未使用）
     *
     * @param deviceSns 变量
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 变量累积数据月存储集合
     */
    public Map<String, Object> selectAccumulateGroupBySeasonalName(String[] deviceSns, String beginTime, String endTime);

    /**
     * 获取【尖峰平谷】数据（表格数据）-2
     *
     * @param deviceSns 变量
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 变量累积数据月存储集合
     */
    public List<List<EnumSOVO>> selectAccumulateGroupBySeasonalName2(String[] deviceSns, String beginTime, String endTime);
}
