package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.ShardingDay;
import com.yunpower.collect.storage.domain.ShardingQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @title: 日数据
 * @Author: Jiajiaglam
 * @date: 2023-07-17 15:59
 * @description:
 */
public interface DayDataMapper {

    public int insert(Map<String, Object> params);

    /**
     * 创建日数据存储表
     *
     * @param tableName 要创建的表名
     */
    public void createDayDataTable(@Param("tableName") String tableName);

    /**
     * 月统计数据存储表
     *
     * @param tableName 要创建的表名
     */
    public void createMonthDataTable(@Param("tableName") String tableName);

    /**
     * 月变量累积数据存储表
     *
     * @param tableName 要创建的表名
     */
    public void createMonthAccumulateDataTable(@Param("tableName") String tableName);

    public ShardingDay selectDayValue(ShardingQuery shardingQuery);
}
