package com.yunpower.datav.algorithm.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 不使用中间表，直接从数据库中加载已创建的表
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/9 7:47
 */
@Component
public class AutoLoadingShardingTableListener implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLoadingShardingTableListener.class);

    // 数据库连接
    @Value("${spring.shardingsphere.datasource.ds0.jdbc-url}")
    private String dbUrl;

    // 数据库用户名
    @Value("${spring.shardingsphere.datasource.ds0.username}")
    private String dbUser;

    // 数据库密码
    @Value("${spring.shardingsphere.datasource.ds0.password}")
    private String dbPwd;

    // 数据采集库：配置别名（如：ds0）
    @Value("${app.database.collect.db0.alias}")
    private String aliasName;

    // 动态更新 ShardingSphere 节点配置
    @Autowired
    private RefreshActualDataNodesAO refreshActualDataNodesAO;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 每小时执行一次
        LOGGER.debug("已启动-动态分表节点更新任务");
        System.out.println("已启动-动态分表节点更新任务");
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new AutoLoadingShardingTableExecutor(aliasName, refreshActualDataNodesAO, dbUrl, dbUser, dbPwd), 0, 1, TimeUnit.HOURS);
    }
}
