package com.yunpower.collect.storage.mapper;

import com.yunpower.collect.storage.domain.MonthAccumulateDataEntity;
import com.yunpower.collect.storage.domain.ShardingMonthAccumulate;
import com.yunpower.collect.storage.domain.ShardingQuery;
import org.apache.ibatis.annotations.Param;

/**
 * @title: 月累计数据存储
 * @Author: Jiajiaglam
 * @date: 2023-12-28 11:17
 * @description:
 */
public interface MonthAccumulateDataMapper {

    /**
     * 查询月累计数据存储
     *
     * @param tableName 数据表
     * @param varsn     变量编码
     * @param year      年
     * @param month     月
     * @param day       天
     * @param hour      时
     * @return 月累计数据存储
     */
    public MonthAccumulateDataEntity selectMonthDataByVarSnAndHour(@Param("tableName") String tableName, @Param("varsn") String varsn, @Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day, @Param("hour") Integer hour);

    /**
     * 新增月累计数据存储
     *
     * @param monthAccumulateDataEntity 月累计数据存储
     * @return 结果
     */
    public int insertMonthAccumulateData(MonthAccumulateDataEntity monthAccumulateDataEntity);

    /**
     * 修改月累计数据存储
     *
     * @param monthAccumulateDataEntity 月累计数据存储
     * @return 结果
     */
    public int updateMonthAccumulateData(MonthAccumulateDataEntity monthAccumulateDataEntity);

    /**
     * 查询近7天内的最后一条数据
     * */
    public ShardingMonthAccumulate selectLastMonthAccumulateData(ShardingQuery query);
}
