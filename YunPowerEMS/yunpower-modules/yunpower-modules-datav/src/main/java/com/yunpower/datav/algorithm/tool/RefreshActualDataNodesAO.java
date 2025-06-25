package com.yunpower.datav.algorithm.tool;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.datanode.DataNode;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.infra.util.expr.InlineExpressionParser;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.rule.ShardingRule;
import org.apache.shardingsphere.sharding.rule.TableRule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @title: 动态刷新分库分表节点配置
 * @Author: Jiajiaglam
 * @date: 2023-10-19 18:31
 * @description:
 */
@Component
public class RefreshActualDataNodesAO {
    @Resource
    private ShardingSphereDataSource shardingSphereDataSource;

    /**
     * 数据库模式，有以下几5种：
     * 1.logic_db
     * 2.information_schema
     * 3.performance_schema
     * 4.mysql
     * 5.sys
     */
    private final static String schemaName = "logic_db";

    /**
     * Sharding数据源
     */
    public static ContextManager getContextManager(final ShardingSphereDataSource dataSource) throws NoSuchFieldException, IllegalAccessException {
        Field field = ShardingSphereDataSource.class.getDeclaredField("contextManager");
        field.setAccessible(true);
        return (ContextManager) field.get(dataSource);
    }

    /**
     * 入口 A
     *
     * @param logicTableName  逻辑表名，如：sharding_day
     * @param actualDataNodes 行表达式，如："ds_$->{0..1}.student_$->{0..2},ds_0.student_12345,ds_1.student_14258";
     */
    public void generateActualDataNodes(String logicTableName, String actualDataNodes) throws NoSuchFieldException, IllegalAccessException {
        this.updateShardRuleActualDataNodes(shardingSphereDataSource, schemaName, logicTableName, actualDataNodes);
    }

    /**
     * 入口 B
     *
     * @param logicTableName  逻辑表名，如：sharding_day
     * @param actualDataNodes 行表达式列表，如：Arrays.asList("ds_$->{0..1}.student_$->{0..2}","ds_0.student_12345","ds_1.student_14258");
     */
    public void generateActualDataNodes(String logicTableName, List<String> actualDataNodes) throws NoSuchFieldException, IllegalAccessException {
        this.updateShardRuleActualDataNodes(shardingSphereDataSource, schemaName, logicTableName, String.join(",", actualDataNodes));
    }

    /**
     * 更新节点配置
     *
     * @param dataSource      数据源
     * @param schemaName      数据库模式名称
     * @param logicTableName  逻辑表名
     * @param actualDataNodes 行表达式
     */
    public void updateShardRuleActualDataNodes(ShardingSphereDataSource dataSource, String schemaName, String logicTableName, String actualDataNodes) throws NoSuchFieldException, IllegalAccessException {
        // 根据inline 表达式转换DataNode节点
        List<String> newStrDataNodes = new InlineExpressionParser(actualDataNodes).splitAndEvaluate();

        // Sharding数据源
        ContextManager contextManager = getContextManager(dataSource);

        // 所有的拆分表
        Collection<ShardingSphereRule> tableRules = contextManager.getMetaDataContexts()
                .getMetaData()
                .getDatabase(schemaName)
                .getRuleMetaData()
                .getRules();

        try {
            for (ShardingSphereRule shardingSphereRule : tableRules) {
                if (shardingSphereRule instanceof ShardingRule) {
                    ShardingRule rule = (ShardingRule) shardingSphereRule;
                    TableRule tableRule = rule.getTableRule(logicTableName);

                    // 动态刷新：ActualDataNodesField
                    Field actualDataNodesField = TableRule.class.getDeclaredField("actualDataNodes");
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);

                    // 设置修饰符：private
                    modifiersField.setInt(actualDataNodesField, actualDataNodesField.getModifiers() & ~Modifier.FINAL);

                    //  新节点 循环动态拼接所有节点表名
                    List<DataNode> newDataNodes = newStrDataNodes.stream().map(DataNode::new).collect(Collectors.toList());
                    actualDataNodesField.setAccessible(true);

                    // 数据更新回去
                    actualDataNodesField.set(tableRule, newDataNodes);
                    Set<String> actualTables = Sets.newHashSet();

                    // DataNodeIntegerMap
                    Map<DataNode, Integer> dataNodeIntegerMap = Maps.newHashMap();
                    newDataNodes.forEach(LambadaTools.forEachWithIndex(dataNodeIntegerMap::put));

                    Map<String, List<DataNode>> dataSourceNodes = newDataNodes.stream().collect(Collectors.groupingBy(DataNode::getDataSourceName));

                    // 动态刷新：ActualTables
                    Field actualTablesField = TableRule.class.getDeclaredField("actualTables");
                    actualTablesField.setAccessible(true);
                    actualTablesField.set(tableRule, actualTables);

                    // 动态刷新：DataNodeIndexMap
                    Field dataNodeIndexMapField = TableRule.class.getDeclaredField("dataNodeIndexMap");
                    dataNodeIndexMapField.setAccessible(true);
                    dataNodeIndexMapField.set(tableRule, dataNodeIntegerMap);

                    // 动态刷新：DatasourceToTablesMap
                    Map<String, Collection<String>> datasourceToTablesMap = Maps.newHashMap();

                    // 不同数据源，表不一样
                    dataSourceNodes.forEach((ds, node) -> {
                        datasourceToTablesMap.put(ds, node.stream().map(DataNode::getTableName).collect(Collectors.toSet()));
                    });

                    Field datasourceToTablesMapField = TableRule.class.getDeclaredField("dataSourceToTablesMap");
                    datasourceToTablesMapField.setAccessible(true);
                    datasourceToTablesMapField.set(tableRule, datasourceToTablesMap);
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            System.out.println("刷新节点报错了" + e.getMessage());
        }
    }
}
