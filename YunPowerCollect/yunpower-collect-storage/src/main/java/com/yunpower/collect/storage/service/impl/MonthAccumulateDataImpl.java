package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.MonthAccumulateDataEntity;
import com.yunpower.collect.storage.mapper.MonthAccumulateDataMapper;
import com.yunpower.collect.storage.service.IMonthAccumulateDataService;
import com.yunpower.common.dds.annotation.DSCollect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @title: 月累计数据存储
 * @Author: Jiajiaglam
 * @date: 2023-12-28 11:20
 * @description:
 */
@Service
@DSCollect
public class MonthAccumulateDataImpl implements IMonthAccumulateDataService {
    @Autowired
    private MonthAccumulateDataMapper monthAccumulateDataMapper;

    @Override
    public MonthAccumulateDataEntity selectMonthDataByVarSnAndHour(String tableName, String varsn, Integer year, Integer month, Integer day, Integer hour) {
        return monthAccumulateDataMapper.selectMonthDataByVarSnAndHour(tableName, varsn, year, month, day, hour);
    }

    @Override
    public int insertMonthAccumulateData(MonthAccumulateDataEntity monthAccumulateDataEntity) {
        return monthAccumulateDataMapper.insertMonthAccumulateData(monthAccumulateDataEntity);
    }

    @Override
    public int updateMonthAccumulateData(MonthAccumulateDataEntity monthAccumulateDataEntity) {
        return monthAccumulateDataMapper.updateMonthAccumulateData(monthAccumulateDataEntity);
    }
}
