package com.yunpower.common.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


/**
 * ScheduledService定时任务管理器
 * */
public class ScheduledService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledService.class);
    private static ScheduledExecutorService POOL;

    private ScheduledService() {
    }

    /**
     * 初始化
     * */
    public static void initialize(int configCorePoolSize) {
        if (POOL == null) {
            LOGGER.info("ScheduledService 开始初始化");

            // 获取系统的 CPU 核心数和线程数
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            int coreCount = availableProcessors / 2; // 物理核心数
            LOGGER.info("CPU 核心数: {} 个", coreCount);
            LOGGER.info("CPU 线程数: {} 个", availableProcessors);
            //设置核心数
            POOL = getExecutor(coreCount);
            POOL.scheduleAtFixedRate(() -> {
                // 获取线程池实际类型为 ThreadPoolExecutor
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) POOL;

                // 获取线程池状态信息
                int currentActiveCount = threadPoolExecutor.getActiveCount();
                int currentPoolSize = threadPoolExecutor.getPoolSize();
                int currentCorePoolSize = threadPoolExecutor.getCorePoolSize();
                int currentMaximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
                int currentQueueSize = threadPoolExecutor.getQueue().size();

                // 记录线程池状态信息（中文输出）
                LOGGER.debug("活跃线程数: {}", currentActiveCount);
                LOGGER.debug("线程池大小: {}", currentPoolSize);
                LOGGER.debug("核心线程池大小: {}", currentCorePoolSize);
                LOGGER.debug("最大线程池大小: {}", currentMaximumPoolSize);
                LOGGER.debug("队列大小: {}", currentQueueSize);
                LOGGER.info("--------------------------");

            }, 0, 1, TimeUnit.MINUTES);
        } else {
            throw new IllegalStateException("ScheduledService has already been initialized");
        }
    }

    private static ScheduledThreadPoolExecutor getExecutor(int coreCount) {
        int corePoolSize = coreCount * (1 + 3);
        // 核心线程数的 2 倍
        int maximumPoolSize = corePoolSize * 2;
        // 空闲线程存活时间
        long keepAliveTime = 1;
        // 时间单位
        TimeUnit unit = TimeUnit.MINUTES;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(128); // 任务队列
        ScheduledThreadPoolExecutor executor =  new ScheduledThreadPoolExecutor(corePoolSize, new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setMaximumPoolSize(maximumPoolSize);
        executor.setKeepAliveTime(keepAliveTime, unit);
        return executor;
    }

    /**
     * 获取实例
     * */
    public static ScheduledExecutorService getInstance() {
        if (POOL == null) {
            throw new IllegalStateException("ScheduledService has not been initialized");
        }
        return POOL;
    }
}
