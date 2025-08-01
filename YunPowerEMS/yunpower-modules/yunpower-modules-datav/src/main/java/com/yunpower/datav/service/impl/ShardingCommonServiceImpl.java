package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.domain.vo.ChartQueryVo;
import com.yunpower.datav.mapper.ShardingMonthMapper;
import com.yunpower.common.datasource.annotation.ShardingDataSource;
import com.yunpower.datav.domain.ShardingQuery;
import com.yunpower.datav.service.ShardingCommonService;
import com.yunpower.datav.utils.GenerateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公共接口
 *
 * @author: XIAOTONG.CAO
 */
@ShardingDataSource
@Service
public class ShardingCommonServiceImpl implements ShardingCommonService {

    /**
     * 初始化
     * <pre>
     * valueType:数据类型（1单值 2连续值）
     * storageType:存储类型（1变化值 2累积值）
     * dateDim:日期维度（5、10 15 30 1 月 年）
     * </pre>
     */
    @PostConstruct
    public void init() {
        //单值-变化值+日维度
        SHARDING_TYPE_MAP.put("valueType:1|storageType:1|dateDim:70", this::singleChangeDayMonth);
        //单值+变化值+月维度
        SHARDING_TYPE_MAP.put("valueType:1|storageType:1|dateDim:80", this::singleChangeMonthMonth);
        //单值+变化值+年维度
        SHARDING_TYPE_MAP.put("valueType:1|storageType:1|dateDim:90", this::singleChangeMonthMonth);
        //单值+变化值+汇总
        SHARDING_TYPE_MAP.put("valueType:1|storageType:1|dateDim:100", this::singleChangeMonthMonth);

        //单值-累计值+日维度
        SHARDING_TYPE_MAP.put("valueType:1|storageType:2|dateDim:70", this::singleChangeDayMonth);
        //单值+累计值+月维度
        SHARDING_TYPE_MAP.put("valueType:1|storageType:2|dateDim:80", this::singleChangeMonthMonth);
        //单值+累计值+年维度
        SHARDING_TYPE_MAP.put("valueType:1|storageType:2|dateDim:90", this::singleChangeMonthMonth);
        //单值+累计值+汇总
        SHARDING_TYPE_MAP.put("valueType:1|storageType:2|dateDim:100", this::singleChangeMonthMonth);

        //连续值+变化值+日维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:1|dateDim:70", this::continuityChangeDayDay);
        //连续值+变化值+月维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:1|dateDim:80", this::continuityChangeMonthDay);
        //连续值+变化值+年维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:1|dateDim:90", this::continuityChangeYearDay);

        //连续值+累计值+日维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:2|dateDim:70", this::continuityChangeDayDay);
        //连续值+累计值+月维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:2|dateDim:80", this::continuityChangeMonthDay);
        //连续值+累计值+年维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:2|dateDim:90", this::continuityChangeYearDay);

        //连续值+峰谷平+月维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:3|dateDim:80", this::continuityFengGuMonthDay);
        //连续值+峰谷平+年维度
        SHARDING_TYPE_MAP.put("valueType:2|storageType:3|dateDim:90", this::continuityFengGuYearDay);
    }

    @Autowired
    private ShardingMonthMapper shardingMonthMapper;

    /**
     * 查询最新一条数据
     * <pre>
     * single-change-day-day 单值 + 变化值 + 日维度
     * </pre>
     *
     * @param shardingQuery 查询条件 变量、数值类型（max,min,avg,acc,runtime）
     * @return 最新数据
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> singleChangeDayMonth(ShardingQuery shardingQuery) {
        return shardingMonthMapper.singleChangeDayMonth(shardingQuery);
    }

    /**
     * 月表-查询指定字段数据
     * <pre>
     * single-change-day-month 单值+日维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> singleChangeMonthMonth(ShardingQuery shardingQuery) {
        List<ShardingMonth> list = shardingMonthMapper.singleChangeMonthMonth(shardingQuery);
        if(StringUtils.isNotEmpty(list)){
            list = list.stream().filter(StringUtils::isNotNull).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 日表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+变化值+日维度 (5 10 15 30分钟 1时)
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> continuityChangeDayDay(ShardingQuery shardingQuery) {
        return shardingMonthMapper.continuityChangeDayDay(shardingQuery);
    }

    /**
     * 日表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+累计值+日维度(日期维度 5 10 15 30分钟 1时)
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> continuityChangeDayDayForAccu(ShardingQuery shardingQuery) {
        //String endTime = String.valueOf(shardingQuery.getParams().get("endTime"));
        //LocalDateTime endDate = LocalDateTimeUtils.strToLocalDateTime(endTime);
        //endDate = endDate.plusHours(1);
        //shardingQuery.getParams().put("endTime", endDate.format(dateTimeFormat));
        //List<ShardingMonth> =
        return null;
    }

    /**
     * 月表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+变化值+月维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> continuityChangeMonthDay(ShardingQuery shardingQuery) {
        return shardingMonthMapper.continuityChangeMonthDay(shardingQuery);
    }

    /**
     * 月表-查询指定时间段内数据
     * <pre>
     * continuity-change-day-day 连续值+变化值+年维度
     * </pre>
     *
     * @param shardingQuery 查询条件
     * @return 最新数据
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> continuityChangeYearDay(ShardingQuery shardingQuery) {
        if (shardingQuery.getChangeType() == 1) {
            shardingQuery.setChangeType(2);
        }
        if (shardingQuery.getValueType() == null) {
            shardingQuery.setValueType(2);
        }
        List<ShardingMonth> list = shardingMonthMapper.continuityChangeYearDay(shardingQuery);
        //特殊处理
        if (shardingQuery.getValueType() == 2 && (shardingQuery.getChangeType() == 2 || shardingQuery.getChangeType() == 3)) {
            List<ChartQueryVo.TimeInfo> timeList = GenerateTimeUtils.generateMonthlySlots(String.valueOf(shardingQuery.getParams().get("beginTime")), String.valueOf(shardingQuery.getParams().get("endTime")), false);
            //依次补充值 数据库中的值肯会中断，中断的值默认是0
            Map<String, Double> shardingMonthMap = list.stream()
                    .collect(Collectors.toMap(ShardingMonth::getFormattedDatetime, ShardingMonth::getDataValue));
            for (int i = 0; i < timeList.size(); i++) {
                ChartQueryVo.TimeInfo timeInfo = timeList.get(i);
                if (!shardingMonthMap.containsKey(timeInfo.getKey())) {
                    shardingMonthMap.put(timeInfo.getKey(), 0D);
                }
            }
            List<ShardingMonth> newList = shardingMonthMap.entrySet().stream()
                    .map(entry -> new ShardingMonth(entry.getKey(), entry.getValue())).collect(Collectors.toList());
            //计算平均值
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
            if (shardingQuery.getChangeType() == 2) {
                Map<String, Double> averageValuesByMonth = newList.stream()
                        .collect(Collectors.groupingBy(
                                record -> LocalDate.parse(record.getFormattedDatetime(), formatter).format(monthFormatter),
                                Collectors.averagingDouble(ShardingMonth::getDataValue)
                        ));
                list = averageValuesByMonth.entrySet().stream()
                        .map(entry -> new ShardingMonth(entry.getKey(), DoubleUtils.fmt2Point(entry.getValue()))).collect(Collectors.toList());
            }
            //计算最小值
            if (shardingQuery.getChangeType() == 3) {
                Map<String, Optional<ShardingMonth>> optionalMap = newList.stream()
                        .collect(Collectors.groupingBy(
                                record -> LocalDate.parse(record.getFormattedDatetime(), formatter).format(monthFormatter),
                                Collectors.minBy(Comparator.comparing(ShardingMonth::getDataValue))
                        ));
                // 将 Optional<DataRecord> 转换为只包含最小值的 Map
                list = optionalMap.entrySet().stream().map(entry -> new ShardingMonth(entry.getKey(), entry.getValue().map(item -> DoubleUtils.fmt2Point(item.getDataValue())).orElse(0D))).collect(Collectors.toList());
            }
        }
        return list;
    }

    /**
     * 峰平谷数据 - 月（日）维度
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> continuityFengGuMonthDay(ShardingQuery shardingQuery) {
        return shardingMonthMapper.continuityFengGuMonthDay(shardingQuery);
    }

    /**
     * 峰平谷数据 - 年（月）维度
     */
    @Transactional(readOnly = true)
    @Override
    public List<ShardingMonth> continuityFengGuYearDay(ShardingQuery shardingQuery) {
        return shardingMonthMapper.continuityFengGuYearDay(shardingQuery);
    }

    /**
     * 获取今年年度数据
     */
    @Override
    public Float selectTotalDataForYear(ShardingQuery shardingQuery) {
        return shardingMonthMapper.selectTotalDataForYear(shardingQuery);
    }

    /**
     * 大屏统计变量数据查询
     */
    @Override
    public double selectDatavStatVarData(ShardingQuery shardingQuery) {
        List<ShardingMonth> list = shardingMonthMapper.selectDatavStatVarData(shardingQuery);
        if(StringUtils.isEmpty(list)){
            return 0;
        }
        return list.stream().filter(item->StringUtils.isNotNull(item) && StringUtils.isNotNull(item.getDataValue())).mapToDouble(ShardingMonth::getDataValue).sum();
    }
}
