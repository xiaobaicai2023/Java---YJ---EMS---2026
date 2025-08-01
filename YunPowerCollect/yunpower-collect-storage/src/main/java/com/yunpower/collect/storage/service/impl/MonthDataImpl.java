package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.MonthDataEntity;
import com.yunpower.collect.storage.mapper.MonthDataMapper;
import com.yunpower.collect.storage.service.IMonthDataService;
import com.yunpower.common.dds.annotation.DSCollect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: 月统计数据存储
 * @Author: Jiajiaglam
 * @date: 2023-12-28 11:20
 * @description:
 */
@Service
@DSCollect
public class MonthDataImpl implements IMonthDataService {
    @Autowired
    private MonthDataMapper monthDataMapper;

    @Override
    public MonthDataEntity selectMonthDataByVarSnAndDay(String tableName, String varSn, Integer day) {
        return monthDataMapper.selectMonthDataByVarSnAndDay(tableName, varSn, day);
    }

    @Override
    public int insertMonthData(MonthDataEntity monthDataEntity) {
        return monthDataMapper.insertMonthData(monthDataEntity);
    }

    @Override
    public int updateMonthData(MonthDataEntity monthDataEntity) {
        return monthDataMapper.updateMonthData(monthDataEntity);
    }
}
