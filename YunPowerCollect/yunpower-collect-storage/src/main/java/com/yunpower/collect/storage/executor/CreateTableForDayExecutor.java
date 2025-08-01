package com.yunpower.collect.storage.executor;

import com.yunpower.collect.storage.StorageVariables;
import com.yunpower.common.core.utils.EltDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @title: 定时创建【日】数据表
 * @Author: Jiajiaglam
 * @date: 2023-12-25 10:07
 * @description:
 */
public class CreateTableForDayExecutor implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTableForDayExecutor.class);

    /**
     * ScheduledExecutorService
     * 定时器，可以用来将任务安排在指定的时间后执行，或者周期性的执行一个任务。
     * - command：要延迟执行的任务（Runnable / Callable）
     * - delay：延时的时间
     * - unit：时间单位
     * 延时delay时长执行Runnable或者Callable任务，只会执行一次。执行Runnable任务时是没有结果返回的，那为什么还会返回ScheduledFuture，因为我们可以通过Future做一些取消任务等操作。该方法会使任务在delay时间之后去执行。
     * 调度之后还可以通过Future.get()阻塞直至任务执行完毕。
     * <p>
     * scheduleAtFixedRate
     * - command：Runnable任务
     * - initialDelay：任务首次执行前的延迟时间
     * - period：周期时间
     * - unit：时间单位
     * 固定周期性执行任务，当任务的执行时长大于周期，那么下一个周期任务将在上一个执行完毕之后马上执行。
     * <p>
     * scheduleWithFixedDelay
     * command：Runnable任务
     * initialDelay：任务首次执行前的延迟时间
     * delay：延时时间
     * unit：时间单位
     * 固定延时执行任务，也是周期性任务，和scheduleAtFixedRate不同的是：scheduleAtFixedRate当任务执行时间小于周期时间时，此时周期时间到了的时候会进入下一周期，这一点和scheduleWithFixedDelay是没有区别的；
     * 但是如果任务执行时间大于周期时间时，scheduleAtFixedRate的任务结束后会立即进入下一周期；而scheduleWithFixedDelay是无论你任务时间是否超过，都将会在你任务执行完毕后延迟固定时间（delay），才会进入下一周期。
     * 作用：在指定的延迟时间（ delay）调度第一次，后续以 period 为一个时间周期进行调度，该方法非常 care 上一次任务执行的耗时，如果某次耗时超过调度周期（period），则下一次调度时间为 上一次任务结束时间 + 调度周期时间
     * 也就是说使用scheduleWithFixedDelay可以实现在每一次执行终止和下一次执行开始之间都存在给定的延迟（delay）。
     */
    public CreateTableForDayExecutor() {
        //todo: 表名格式 day_20231225
    }

    @Override
    public void run() {
        try {
            //1.先判断今天的表是否创建，如果没有创建则创建
            String suffixTableName = EltDateUtils.dateTime();
            StorageVariables.dayDataService.createDayDataTable("day_" + suffixTableName);

            LOGGER.info("day_" + suffixTableName + " 表已创建！");

            //2.创建明天（下一天）的表
            String suffixLastTableName = EltDateUtils.getPastDate(-1);
            StorageVariables.dayDataService.createDayDataTable("day_" + suffixLastTableName);

            LOGGER.info("day_" + suffixLastTableName + " 表已创建！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
