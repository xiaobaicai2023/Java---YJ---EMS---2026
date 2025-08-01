package com.yunpower.datav.algorithm.tool;

import com.yunpower.common.core.utils.DateUtils;
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

            LOGGER.debug("节点-动态生成表已更新：" + DateUtils.getTime());
            System.out.println("节点-动态生成表已更新：" + DateUtils.getTime());

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
