package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.ShardingDay;
import com.yunpower.datav.mapper.ShardingDayMapper;
import com.yunpower.common.core.enums.sharding.StorageTypeEnum;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datasource.annotation.ShardingDataSource;
import com.yunpower.datav.service.IShardingDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日数据存储Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
//多数据源（采集数据库，注解在类上适用）
@ShardingDataSource
@Service
public class ShardingDayServiceImpl implements IShardingDayService {
    @Autowired
    private ShardingDayMapper shardingDayMapper;

    /**
     * 查询日数据存储
     *
     * @param variableName 日数据存储主键
     * @return 日数据存储
     */
    @Override
    public ShardingDay selectShardingDayByVariableName(String variableName) {
        ShardingDay shardingDay = new ShardingDay();
        shardingDay.setVariableName(variableName);
        return shardingDayMapper.selectShardingDay(shardingDay);
    }

    /**
     * 查询日数据存储
     */
    @Override
    public ShardingDay selectShardingDay(String variableName, String beginTime, String endTime) {
        ShardingDay shardingDay = new ShardingDay();
        shardingDay.setVariableName(variableName);
        shardingDay.setParams(DateUtils.dateToParamForDay(beginTime, endTime));
        return shardingDayMapper.selectShardingDay(shardingDay);
    }

    /**
     * 查询日数据存储列表
     *
     * @param shardingDay 日数据存储
     * @return 日数据存储
     */
    @Override
    public List<ShardingDay> selectShardingDayList(ShardingDay shardingDay) {
        if (shardingDay.getSaveTime() != null) {
            shardingDay.setParams(DateUtils.dateToParamForDay(shardingDay.getSaveTime()));
            shardingDay.setSaveTime(null);
        }

        return shardingDayMapper.selectShardingDayList(shardingDay);
    }

    /**
     * 查询5分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    @Override
    public List<ShardingDay> selectShardingDayListForMinute5(ShardingDay shardingDay, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingDay.getVariableName()) || shardingDay.getParams() == null) {
            return new ArrayList<>();
        }

        //5分钟数据标识
        Map<String, Object> map = shardingDay.getParams();
        map.put("minute", "5");
        shardingDay.setParams(map);

        return handleList(shardingDay, storageType, isChain, new String[]{"minute", "5"});
    }

    /**
     * 查询10分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    @Override
    public List<ShardingDay> selectShardingDayListForMinute10(ShardingDay shardingDay, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingDay.getVariableName()) || shardingDay.getParams() == null) {
            return new ArrayList<>();
        }

        //10分钟数据标识
        Map<String, Object> map = shardingDay.getParams();
        map.put("minute", "10");
        shardingDay.setParams(map);

        return handleList(shardingDay, storageType, isChain, new String[]{"minute", "10"});
    }

    /**
     * 查询15分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    @Override
    public List<ShardingDay> selectShardingDayListForMinute15(ShardingDay shardingDay, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingDay.getVariableName()) || shardingDay.getParams() == null) {
            return new ArrayList<>();
        }

        //15分钟数据标识
        Map<String, Object> map = shardingDay.getParams();
        map.put("minute", "15");
        shardingDay.setParams(map);

        return handleList(shardingDay, storageType, isChain, new String[]{"minute", "15"});
    }

    /**
     * 查询30分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    @Override
    public List<ShardingDay> selectShardingDayListForMinute30(ShardingDay shardingDay, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingDay.getVariableName()) || shardingDay.getParams() == null) {
            return new ArrayList<>();
        }

        //30分钟数据标识
        Map<String, Object> map = shardingDay.getParams();
        map.put("minute", "30");
        shardingDay.setParams(map);

        return handleList(shardingDay, storageType, isChain, new String[]{"minute", "30"});
    }

    /**
     * 查询：1小时数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    @Override
    public List<ShardingDay> selectShardingDayForHour(ShardingDay shardingDay, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingDay.getVariableName()) || shardingDay.getParams() == null) {
            return new ArrayList<>();
        }

        //1小时数据标识
        Map<String, Object> map = shardingDay.getParams();
        map.put("hour", "0");
        shardingDay.setParams(map);

        return handleList(shardingDay, storageType, isChain, new String[]{"hour", "0"});
    }

    // 公共处理代码
    private List<ShardingDay> handleList(ShardingDay shardingDay, Integer storageType, Integer isChain, String[] param) {

        //取出来的是变化值
        List<ShardingDay> list = shardingDayMapper.selectShardingDayList(shardingDay);

        //计算累计值
        if (storageType == StorageTypeEnum.ACCUM.getKey()) {
            list = packageAccuList(list, shardingDay.getParams().get("endTime").toString(), param);
        }

        //计算环比
        if (isChain == 1) {
            list = packageAccuListChain(list, shardingDay.getParams().get("beginTime").toString(), param, storageType);
        }

        return list;
    }

    // 公共处理代码【累计值】，提出公用（今天累计值 = 后一天值 - 今天值）
    private List<ShardingDay> packageAccuList(List<ShardingDay> list, String endTime, String[] param) {

        //数据库取出的结果是每个“时段”的值累计值（如电能）因此需要计算
        List<ShardingDay> newList = new ArrayList<>();
        int size = list.size();
        if (size == 0) {
            return newList;
        }

        //累计值 = 后一条 - 前一条，因此要取出后一天的第一条记录做减法
        //计算后一天日期
        String nextTime = DateUtils.getPastDate(endTime, 1);

        ShardingDay shardingDay = new ShardingDay();
        shardingDay.setVariableName(list.get(0).getVariableName());
        Map<String, Object> map = DateUtils.dateToParamForDay(nextTime);
        assert map != null;
        map.put(param[0], param[1]);
        shardingDay.setParams(map);
        List<ShardingDay> nextList = selectShardingDayList(shardingDay);

        //默认时间正序，那就要取出第一条记录
        ShardingDay nextShard = null;
        if (!nextList.isEmpty()) {
            nextShard = nextList.get(0);
        }

        for (int i = 0; i < size; i++) {
            ShardingDay temp = new ShardingDay();
            temp.setYear(list.get(i).getYear());
            temp.setMonth(list.get(i).getMonth());
            temp.setDay(list.get(i).getDay());
            temp.setHour(list.get(i).getHour());
            temp.setMinute(list.get(i).getMinute());
            temp.setSaveTime(list.get(i).getSaveTime());

            if (i == size - 1) { //最后一条记录
                if (nextShard == null) {
                    temp.setDataValue(0D);
                } else {
                    temp.setDataValue(nextShard.getDataValue() - list.get(i).getDataValue());
                }
            } else {
                double dValue = list.get(i + 1).getDataValue() - list.get(i).getDataValue();
                temp.setDataValue(dValue < 0 ? 0 : dValue);
            }

            newList.add(temp);
        }

        return newList;
    }

    // 公共处理代码【环比】，提出公用（环比 = [今天值 - 昨天值]/昨天值）
    private List<ShardingDay> packageAccuListChain(List<ShardingDay> list, String beginTime, String[] param, Integer storageType) {

        //数据库取出的结果是每个“时段”的值累计值（如电能）因此需要计算
        List<ShardingDay> newList = new ArrayList<>();
        int size = list.size();
        if (size == 0) {
            return newList;
        }

        //环比 = [今天值 - 昨天值]/昨天值，因此要取出前一天的最后一条记录
        //计算前一天日期
        String forwardTime = DateUtils.getPastDate(beginTime, -1);

        ShardingDay shardingDay = new ShardingDay();
        shardingDay.setVariableName(list.get(0).getVariableName());
        Map<String, Object> map = DateUtils.dateToParamForDay(forwardTime);
        assert map != null;
        map.put(param[0], param[1]);
        shardingDay.setParams(map);
        List<ShardingDay> forwardList = selectShardingDayList(shardingDay);

        //默认时间正序，那就要取出最后一条记录
        double forwardDataValue = 0f;
        if (!forwardList.isEmpty()) {
            if (storageType == StorageTypeEnum.ACCUM.getKey()) {
                //如果是累计值，那就要算两条记录差
                if (forwardList.size() > 1) {
                    forwardDataValue = forwardList.get(forwardList.size() - 1).getDataValue() - forwardList.get(forwardList.size() - 2).getDataValue();
                }
            } else {
                forwardDataValue = forwardList.get(forwardList.size() - 1).getDataValue();
            }
        }

        for (int i = 0; i < size; i++) {
            ShardingDay temp = new ShardingDay();
            temp.setYear(list.get(i).getYear());
            temp.setMonth(list.get(i).getMonth());
            temp.setDay(list.get(i).getDay());
            temp.setHour(list.get(i).getHour());
            temp.setMinute(list.get(i).getMinute());
            temp.setSaveTime(list.get(i).getSaveTime());

            //计算环比（百分比）
            if (i == 0) { //第一条记录
                if (forwardDataValue == 0) {
                    temp.setDataValue(0D);
                } else {
                    temp.setDataValue(DoubleUtils.computePercent(list.get(i).getDataValue(), forwardDataValue));
                }
            } else {
                if (list.get(i - 1).getDataValue() == 0) {
                    temp.setDataValue(0D);
                } else {
                    temp.setDataValue(DoubleUtils.computePercent(list.get(i).getDataValue(), list.get(i - 1).getDataValue()));
                }
            }
            newList.add(temp);

        }

        //因为是倒序计算的，所以要重新排一下序
        //Collections.reverse(newList);

        return newList;
    }

    /**
     * 新增日数据存储
     *
     * @param shardingDay 日数据存储
     * @return 结果
     */
    @Override
    public int insertShardingDay(ShardingDay shardingDay) {
        return shardingDayMapper.insertShardingDay(shardingDay);
    }

    /**
     * 修改日数据存储
     *
     * @param shardingDay 日数据存储
     * @return 结果
     */
    @Override
    public int updateShardingDay(ShardingDay shardingDay) {
        return shardingDayMapper.updateShardingDay(shardingDay);
    }

    /**
     * 批量删除日数据存储
     *
     * @param variableNames 需要删除的日数据存储主键
     * @return 结果
     */
    @Override
    public int deleteShardingDayByVariableNames(String[] variableNames) {
        return shardingDayMapper.deleteShardingDayByVariableNames(variableNames);
    }

    /**
     * 删除日数据存储信息
     *
     * @param variableName 日数据存储主键
     * @return 结果
     */
    @Override
    public int deleteShardingDayByVariableName(String variableName) {
        return shardingDayMapper.deleteShardingDayByVariableName(variableName);
    }

    /**
     * 查询最新一条数据
     * <pre>
     * single-change-day-day 单值-变化值-日-日表
     * </pre>
     * @param variableName 变量
     * @return 最新数据
     * */
    @Override
    public ShardingDay singleChangeDayDay(String variableName){
        return shardingDayMapper.singleChangeDayDay(variableName);
    }
}
