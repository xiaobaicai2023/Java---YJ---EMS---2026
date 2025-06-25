package com.yunpower.datav.algorithm;

import com.google.common.collect.Range;
import com.yunpower.common.core.utils.LocalDateTimeUtils;
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
 * @title: 分片算法（月存储表）
 * @Author: Jiajiaglam
 * @date: 2023-10-18 13:50
 * @description:
 */
public class MonthShardingAlgorithm implements StandardShardingAlgorithm<Timestamp> {

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
        String dateStr = LocalDateTimeUtils.GetYYYYMM(time);

        //具体原因和方案请参考日分表算法
        //使用第4种方案
        for (String each : collection) {
            if (each.endsWith(dateStr)) {
                return each;
            }
        }

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

        List<String> monthList = LocalDateTimeUtils.GetMonthListYYYYMM(lowerDate, upperDate);

        for (String month : monthList) {
            //具体原因和方案请参考日分表算法
            //使用第4种方案
            for (String each : collection) {
                if (each.endsWith(month)) {
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
