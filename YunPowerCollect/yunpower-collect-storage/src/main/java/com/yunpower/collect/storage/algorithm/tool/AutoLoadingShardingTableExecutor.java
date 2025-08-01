package com.yunpower.collect.storage.algorithm.tool;

import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新节点配置
 *
 * @Author: Jiajiaglam
 * @Date: 2024/8/9 7:47
 */
public class AutoLoadingShardingTableExecutor implements Runnable {
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
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLoadingShardingTableExecutor.class);

    // 数据采集库：配置别名（如：ds0）
    private final String aliasName;

    // 动态更新 ShardingSphere 节点配置
    private final RefreshActualDataNodesAO refreshActualDataNodesAO;

    // 数据库连接
    private final String dbUrl;

    // 数据库用户名
    private final String dbUser;

    // 数据库密码
    private final String dbPwd;

    public AutoLoadingShardingTableExecutor(String aliasName, RefreshActualDataNodesAO refreshActualDataNodesAO, String dbUrl, String dbUser, String dbPwd) {
        this.aliasName = aliasName;
        this.refreshActualDataNodesAO = refreshActualDataNodesAO;
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;
    }

    @Override
    public void run() {
        try {
            List<String> dayList = new ArrayList<>();
            List<String> monList = new ArrayList<>();

            packageCreateTableList(dayList, monList);

            if (dayList.isEmpty()) {
                return;
            }

            //region 日生成表
            StringBuilder dayNodes = new StringBuilder();
            int i = 0;
            for (String table : dayList) {
                i++;
                dayNodes.append(aliasName).append(".day_").append(table);
                if (i != dayList.size()) {
                    dayNodes.append(",");
                }
            }
            refreshActualDataNodesAO.generateActualDataNodes("sharding_day", dayNodes.toString());
            //endregion

            //region 月生成表
            StringBuilder monthNodes = new StringBuilder();
            int j = 0;
            for (String table : monList) {
                j++;
                monthNodes.append(aliasName).append(".month_").append(table);
                if (j != monList.size()) {
                    monthNodes.append(",");
                }
            }

            refreshActualDataNodesAO.generateActualDataNodes("sharding_month", monthNodes.toString());
            //endregion

            //region 月累计值表
            StringBuilder monthNodesAccu = new StringBuilder();
            int k = 0;
            for (String table : monList) {
                k++;
                monthNodesAccu.append(aliasName).append(".month_accumulate_").append(table);
                if (k != monList.size()) {
                    monthNodesAccu.append(",");
                }
            }

            refreshActualDataNodesAO.generateActualDataNodes("sharding_month_accumulate", monthNodesAccu.toString());
            //endregion

            LOGGER.debug("节点-动态生成表已更新：" + DateUtil.now());
            System.out.println("节点-动态生成表已更新：" + DateUtil.now());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 实时获取所有表名，并挑选出日、月存储表
     *
     * @param dayList 存放日存储表后缀
     * @param monList 存放月存储表后缀
     */
    private void packageCreateTableList(List<String> dayList, List<String> monList) {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
             Statement stmt = conn.createStatement()) {

            // SQL查询语句，获取所有表名
            String sql = "SHOW TABLES";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // 获取表名
                String tableName = rs.getString(1);

                // 日存储表的日期
                if (tableName.startsWith("day_")) {
                    dayList.add(tableName.replace("day_", ""));
                }

                // 月存储表的日期
                if (tableName.startsWith("month_accumulate_")) {
                    monList.add(tableName.replace("month_accumulate_", ""));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
