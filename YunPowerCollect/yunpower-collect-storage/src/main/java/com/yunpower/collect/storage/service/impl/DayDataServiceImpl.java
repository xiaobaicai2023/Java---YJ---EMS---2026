package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.mapper.DayDataMapper;
import com.yunpower.collect.storage.service.IDayDataService;
import com.yunpower.common.dds.annotation.DSCollect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: 日数据
 * @Author: Jiajiaglam
 * @date: 2023-07-17 16:01
 * @description:
 */
@Service
@DSCollect
public class DayDataServiceImpl implements IDayDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DayDataServiceImpl.class);

    @Autowired
    private DayDataMapper dayDataMapper;

    /**
     * 保存数据到数据库
     *
     * @param varSn     变量编码
     * @param value     值
     * @param saveDate  存盘日期
     * @param tableDate 日期
     */
    @Override
    public void saveToDatabase(String varSn, Double value, Date saveDate, String tableDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("variableName", varSn);
        params.put("dataValue", value);
        params.put("saveTime", saveDate);
        params.put("tableName", "day_" + tableDate);
        int rows = dayDataMapper.insert(params);
        if (rows > 0) {
            LOGGER.debug("存盘成功！");
        } else {
            LOGGER.debug("存盘失败！");
        }
    }

    /**
     * 创建日数据存储表
     *
     * @param tableName 要创建的表名
     */
    @Override
    public void createDayDataTable(String tableName) {
        dayDataMapper.createDayDataTable(tableName);
    }

    /**
     * 月统计数据存储表
     *
     * @param tableName 要创建的表名
     */
    @Override
    public void createMonthDataTable(String tableName) {
        dayDataMapper.createMonthDataTable(tableName);
    }

    /**
     * 月变量累积数据存储表
     *
     * @param tableName 要创建的表名
     */
    @Override
    public void createMonthAccumulateDataTable(String tableName) {
        dayDataMapper.createMonthAccumulateDataTable(tableName);
    }
}
