package com.yunpower.collect.storage.service;

import java.util.Date;

/**
 * @title: 日数据
 * @Author: Jiajiaglam
 * @date: 2023-07-17 15:56
 * @description:
 */
public interface IDayDataService {

    /**
     * 保存数据到数据库
     *
     * @param varSn     变量编码
     * @param value     值
     * @param saveDate  存盘日期
     * @param tableDate 日期
     */
    public void saveToDatabase(String varSn, Double value, Date saveDate, String tableDate);


    /**
     * 创建日数据存储表
     *
     * @param tableName 要创建的表名
     */
    public void createDayDataTable(String tableName);

    /**
     * 月统计数据存储表
     *
     * @param tableName 要创建的表名
     */
    public void createMonthDataTable(String tableName);

    /**
     * 月变量累积数据存储表
     *
     * @param tableName 要创建的表名
     */
    public void createMonthAccumulateDataTable(String tableName);
}
