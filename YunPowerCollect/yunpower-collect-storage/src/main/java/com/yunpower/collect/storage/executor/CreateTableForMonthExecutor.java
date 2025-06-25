package com.yunpower.collect.storage.executor;

import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.common.core.utils.EltDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 定时创建【日】数据表
 * @Author: Jiajiaglam
 * @date: 2023-12-25 11:05
 * @description:
 */
public class CreateTableForMonthExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTableForMonthExecutor.class);

    public CreateTableForMonthExecutor() {
        //todo: 需要创建两张数据表
        //month_202212
        //month_accumulate_202212
    }

    @Override
    public void run() {
        try {
            //1.判断本月表是否创建
            String dateSuffix = EltDateUtils.dateTimeMontrh();
            StorageVariables.dayDataService.createMonthDataTable("month_" + dateSuffix);
            StorageVariables.dayDataService.createMonthAccumulateDataTable("month_accumulate_" + dateSuffix);

            LOGGER.info("month->" + dateSuffix + " 表已创建！");

            //2.创建下个月的表
            String lastDateSuffix = EltDateUtils.getPastMonth(-1);
            StorageVariables.dayDataService.createMonthDataTable("month_" + lastDateSuffix);
            StorageVariables.dayDataService.createMonthAccumulateDataTable("month_accumulate_" + lastDateSuffix);

            LOGGER.info("month->" + lastDateSuffix + " 表已创建！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
