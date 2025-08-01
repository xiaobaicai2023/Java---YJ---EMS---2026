package com.yunpower.collect.storage;

import com.yunpower.collect.storage.algorithm.tool.AutoLoadingShardingTableExecutor;
import com.yunpower.collect.storage.algorithm.tool.RefreshActualDataNodesAO;
import com.yunpower.collect.storage.executor.CalculateMemoryVariablesExecutor;
import com.yunpower.collect.storage.executor.CreateTableForDayExecutor;
import com.yunpower.collect.storage.executor.CreateTableForMonthExecutor;
import com.yunpower.collect.storage.service.*;
import com.yunpower.common.core.service.ScheduledService;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.mq.publisher.service.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @title: 全局变量初始化
 * @Author: Jiajiaglam
 * @date: 2023-07-14 16:30
 * @description:
 */
@Component
public class StorageService implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    @Value("${yunpower.collect.scheduled.corePoolSize:1000}")
    private Integer scheduledCorePoolSize;

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

    @Autowired
    private ICommunicationChannelService communicationChannelService;

    @Autowired
    private ICommunicationDeviceService communicationDeviceService;

    @Autowired
    private IMonitorDeviceService monitorDeviceService;

    @Autowired
    private IMonitorDeviceVarService monitorDeviceVarService;

    @Autowired
    private IDayDataService dayDataService;

    @Autowired
    private IMonthDataService monthDataService;

    @Autowired
    private IMonthAccumulateDataService accumulateDataService;

    @Autowired
    private IElectricPriceSchemeConfigService schemeConfigService;

    @Autowired
    private ISysStationService stationService;

    @Autowired
    private ISysCommonCreateService sysCommonCreateService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RefreshActualDataNodesAO refreshActualDataNodesAO;

    @Autowired
    private IShardingCommonService shardingCommonService;

    @Autowired
    private PublisherService publisherService;

    @Autowired
    private IAlarmTriggerConfigService alarmTriggerConfigService;

    @Autowired
    private IAlarmTriggerService alarmTriggerService;

    @Override
    public void start() {
        try {
            //加载 全局变量
            initStorageVariables();
            //加载 数据
            loadDataToMemery();
            //创建 日表、月表
            createStorageTable();
            //加载 节点
            autoConfigShardingDateNodes();
            //计算内存型变量
            calculateMemoryVariables();
        } catch (Exception ex) {
            LOGGER.error("数据存储服务启动异常", ex);
        }
    }

    @Override
    public void stop() {
        ScheduledService.getInstance().shutdown();
        try {
            if (!ScheduledService.getInstance().awaitTermination(10, TimeUnit.SECONDS)) {
                System.err.println("Executor did not terminate in the specified time.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getPhase() {
        return 1;
    }

    private void initStorageVariables() {
        LOGGER.info("数据存储服务 初始化全局变量 （1）... start");
        StorageVariables.communicationChannelService = this.communicationChannelService;
        StorageVariables.communicationDeviceService = this.communicationDeviceService;
        StorageVariables.monitorDeviceService = this.monitorDeviceService;
        StorageVariables.monitorDeviceVarService = this.monitorDeviceVarService;
        StorageVariables.dayDataService = this.dayDataService;
        StorageVariables.monthDataService = this.monthDataService;
        StorageVariables.accumulateDataService = this.accumulateDataService;
        StorageVariables.sysCommonCreateService = this.sysCommonCreateService;
        StorageVariables.schemeConfigService = this.schemeConfigService;
        StorageVariables.stationService = this.stationService;
        StorageVariables.redisService = this.redisService;
        StorageVariables.refreshActualDataNodesAO = this.refreshActualDataNodesAO;
        StorageVariables.shardingCommonService = this.shardingCommonService;
        StorageVariables.publisherService = this.publisherService;
        StorageVariables.alarmTriggerConfigService = this.alarmTriggerConfigService;
        StorageVariables.alarmTriggerService = this.alarmTriggerService;
        StorageVariables.aliasName = this.aliasName;
        ScheduledService.initialize(scheduledCorePoolSize);
        LOGGER.info("数据存储服务 初始化全局变量 （1）... end");
    }

    private void loadDataToMemery() {
        LOGGER.info("数据存储服务 加载全局变量数据 （2）... start");
        StorageVariables.loadData();
        LOGGER.info("数据存储服务 加载全局变量数据 （2）... end");
    }

    /**
     * 每天定时生成：日数据表、月数据表
     */
    private void createStorageTable() {
        LOGGER.info("数据存储服务 初始化表 （3）... start");

        // 定时生成数据表
        ScheduledService.getInstance().scheduleAtFixedRate(new CreateTableForDayExecutor(), 0, 1, TimeUnit.DAYS);
        ScheduledService.getInstance().scheduleAtFixedRate(new CreateTableForMonthExecutor(), 0, 7, TimeUnit.DAYS);

        // 启动顺序 Order(2) 中，需要定时加载到内存。
        // 因为有启动顺序，因此在此处加定时任务
        ScheduledService.getInstance().scheduleAtFixedRate(this::loadDataToMemery, 1, 1, TimeUnit.MINUTES);
        LOGGER.info("数据存储服务 初始化表 （3）... end");
    }

    /**
     * 加载Sharding节点
     */
    private void autoConfigShardingDateNodes() {
        ScheduledService.getInstance().scheduleWithFixedDelay(new AutoLoadingShardingTableExecutor(aliasName, refreshActualDataNodesAO, dbUrl, dbUser, dbPwd), 0, 1, TimeUnit.HOURS);
    }

    /**
     * 计算内存型变量
     * 5分钟执行一次
     */
    private void calculateMemoryVariables() {
        ScheduledService.getInstance().scheduleAtFixedRate(new CalculateMemoryVariablesExecutor(), 1, 5, TimeUnit.MINUTES);
    }
}
