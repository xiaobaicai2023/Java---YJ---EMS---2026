package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.MonthDataEntity;

/**
 * @title: 月统计数据存储Service接口
 * @Author: Jiajiaglam
 * @date: 2023-12-28 10:59
 * @description:
 */
public interface IMonthDataService {

    /**
     * 查询月统计数据存储
     *
     * @param tableName 数据表
     * @param varSn     变量编码
     * @param day       天
     * @return 月统计数据存储
     */
    public MonthDataEntity selectMonthDataByVarSnAndDay(String tableName, String varSn, Integer day);

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
}
