package com.yunpower.collect.storage.algorithm;

import com.yunpower.common.core.utils.LocalDateTimeUtils;
import com.google.common.collect.Range;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;

/**
 * @title: 分片算法（日存储表）
 * @Author: Jiajiaglam
 * @date: 2023-10-18 13:50
 * @description:
 */
public class DayShardingAlgorithm implements StandardShardingAlgorithm<Timestamp> {

    /**
     * 精准匹配
     *
     * @param collection           列值集合
     * @param preciseShardingValue 精确日期
     * @return null
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Timestamp> preciseShardingValue) {
        LocalDateTime time = LocalDateTimeUtils.convertTimestamp(preciseShardingValue.getValue());
        if (time == null) {
            return null;
        }

        //格式化列值
        String dateStr = LocalDateTimeUtils.GetYYYYMMDD(time);

        //神奇的发现，这个 collection 竟然是配置中的：actual-data-nodes: ds0.day_$->{20231018..20231020}
        //因此，下列写法只能实现固定分表功能，无法满足动态分表
        //* 使用第4种方案
        for (String each : collection) {
            if (each.endsWith(dateStr)) {
                return each;
            }
        }

        //region //动态分表策略有4种解决办法
        /*
         * 第1种：动态更新配置节点 collection 的值（AutoConfigShardingDateNodesListener）
         * 如果使用定时任务创建新表，那么在创建新表后，动态刷新一下 ShardingSphere 节点配置
         * 缺点1：如果有一天新表未创建成功，而节点配置是按天刷新的，那么前端查询时就会报表不存在的错误（绑定成事务可以解决）
         * 缺点2：如果系统运行了三年，每天一张表，那么就会有1000多张表，这绝对是个负担（不过在内存允许的条件下，理论上是无上限的[下标10位数]）
         * 缺点3：查询日期不能小于第一天的日期，也不能大于今天的日期（限制日期可以配置到yml中）
         *
         * 第2种：也需要使用定时任务（AutoConfigShardingDateNodesListener）
         * 不过他是从 information_schema（PublicMapper -> getSomeTables） 中查询所有已经创建的表，然后动态刷新到节点配置中
         * 缺点1：他不存在第1种方案中的缺点1，因为实际创建了什么表就会加载什么表
         * 缺点2：同上
         * 缺点3：一般用户没有查询 information_schema 表的权限，需要特别配置
         *
         * 第3种：什么也不做
         * 查询什么表，那就返回什么表即可。但是它要求你的定时任务每天的表必须都得创建，即使没数据，空表也得创建。
         * 缺点1：同上
         * 缺点2：同上
         * 缺点3：同上
         *
         * 第4种：加一张中间表，使用定时任务更新配置（AutoConfigShardingDateNodesListener）
         * 缺点2：同上
         */
        //endregion

        return null;
    }

    /**
     * 范围算法
     * SQL中有 BETWEEN AND、>、<、>=、<= 等条件的分片
     *
     * @param collection         列值集合
     * @param rangeShardingValue 范围日期
     * @return 返回多个表节点
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Timestamp> rangeShardingValue) {
        // 用于存储结果
        Collection<String> result = new LinkedHashSet<>(collection.size());

        // 最小和最大值
        Range<Timestamp> valueRange = rangeShardingValue.getValueRange();
        LocalDateTime lowerDate = LocalDateTimeUtils.strToLocalDateTime(valueRange.lowerEndpoint() + "");
        LocalDateTime upperDate = LocalDateTimeUtils.strToLocalDateTime(valueRange.upperEndpoint() + "");

        List<String> dayList = LocalDateTimeUtils.GetDayListYYYYMMDD(lowerDate, upperDate);
        for (String day : dayList) {
            // 使用第4种方案
            for (String each : collection) {
                if (each.endsWith(day)) {
                    result.add(each);
                }
            }
        }

        return result;
    }


    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }
}
