package com.yunpower.datav.service;

import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.domain.ShardingQuery;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 公共接口
 * @author: XIAOTONG.CAO
 */
public interface ShardingCommonService {

    /**
     * 读取日、月表
     * */
    public Map<String, Function<ShardingQuery, List<ShardingMonth>>> SHARDING_TYPE_MAP = Maps.newHashMapWithExpectedSize(11);

    /**
     * 月表-查询指定字段数据
     * <pre>
     * single-change-day-month 单值+日维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    public List<ShardingMonth> singleChangeDayMonth(ShardingQuery shardingQuery);

    /**
     * 月表-查询指定字段数据
     * <pre>
     * single-change-day-month 单值+日维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    public List<ShardingMonth> singleChangeMonthMonth(ShardingQuery shardingQuery);

    /**
     * 日表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+变化值+日维度(日期维度 5 10 15 30分钟 1时)
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    public List<ShardingMonth> continuityChangeDayDay(ShardingQuery shardingQuery);

    /**
     * 日表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+累计值+日维度(日期维度 5 10 15 30分钟 1时)
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    public List<ShardingMonth> continuityChangeDayDayForAccu(ShardingQuery shardingQuery);

    /**
     * 月表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+变化值+月维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    public List<ShardingMonth> continuityChangeMonthDay(ShardingQuery shardingQuery);

    /**
     * 月表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+变化值+年维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    public List<ShardingMonth> continuityChangeYearDay(ShardingQuery shardingQuery);

    /**
     * 峰平谷数据 - 月（日）维度
     */
    public List<ShardingMonth> continuityFengGuMonthDay(ShardingQuery shardingQuery);

    /**
     * 峰平谷数据 - 年（月）维度
     */
    public List<ShardingMonth> continuityFengGuYearDay(ShardingQuery shardingQuery);

    /**
     * 获取年度数据
     */
    public Float selectTotalDataForYear(ShardingQuery shardingQuery);

    /**
     * 大屏统计变量数据查询
     */
    public double selectDatavStatVarData(ShardingQuery shardingQuery);
}
