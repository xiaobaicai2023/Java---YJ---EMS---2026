package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.MonthAccumulateDataEntity;

/**
 * @title: 月累计数据存储service接口
 * @Author: Jiajiaglam
 * @date: 2023-12-28 11:13
 * @description:
 */
public interface IMonthAccumulateDataService {

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
    public MonthAccumulateDataEntity selectMonthDataByVarSnAndHour(String tableName, String varsn, Integer year, Integer month, Integer day, Integer hour);

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
}
