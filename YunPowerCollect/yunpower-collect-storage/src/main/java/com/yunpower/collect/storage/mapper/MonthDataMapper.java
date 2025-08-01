package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.MonthDataEntity;
import com.yunpower.collect.storage.domain.ShardingMonth;
import com.yunpower.collect.storage.domain.ShardingQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @title: 月统计数据存储
 * @Author: Jiajiaglam
 * @date: 2023-12-28 11:18
 * @description:
 */
public interface MonthDataMapper {

    /**
     * 查询月统计数据存储
     *
     * @param tableName 数据表
     * @param varSn     变量编码
     * @param day       天
     * @return 月统计数据存储
     */
    public MonthDataEntity selectMonthDataByVarSnAndDay(@Param("tableName") String tableName, @Param("varSn") String varSn, @Param("day") Integer day);

    /**
     * 新增月统计数据存储
     *
     * @param monthDataEntity 月统计数据存储
     * @return 结果
     */
    public int insertMonthData(MonthDataEntity monthDataEntity);

    /**
     * 修改月统计数据存储
     *
     * @param monthDataEntity 月统计数据存储
     * @return 结果
     */
    public int updateMonthData(MonthDataEntity monthDataEntity);

    /**
     * 查询最后一条数据
     * */
    public ShardingMonth selectLastMonthData(ShardingQuery shardingQuery);
}
