package com.yunpower.datav.service;

import com.yunpower.datav.domain.ShardingDay;

import java.util.List;

/**
 * 日数据存储Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
public interface IShardingDayService {
    /**
     * 查询日数据存储
     *
     * @param variableName 日数据存储主键
     * @return 日数据存储
     */
    public ShardingDay selectShardingDayByVariableName(String variableName);

    /**
     * 查询日数据存储
     */
    public ShardingDay selectShardingDay(String variableName, String beginTime, String endTime);

    /**
     * 查询日数据存储列表
     *
     * @param shardingDay 日数据存储
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayList(ShardingDay shardingDay);

    /**
     * 查询5分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayListForMinute5(ShardingDay shardingDay, Integer storageType, Integer isChain);

    /**
     * 查询10分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayListForMinute10(ShardingDay shardingDay, Integer storageType, Integer isChain);

    /**
     * 查询15分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayListForMinute15(ShardingDay shardingDay, Integer storageType, Integer isChain);

    /**
     * 查询30分钟数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayListForMinute30(ShardingDay shardingDay, Integer storageType, Integer isChain);

    /**
     * 查询：1小时数据（列表）
     *
     * @param shardingDay 日数据存储
     * @param storageType 使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain     是否环比：0否1是
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayForHour(ShardingDay shardingDay, Integer storageType, Integer isChain);

    /**
     * 新增日数据存储
     *
     * @param shardingDay 日数据存储
     * @return 结果
     */
    public int insertShardingDay(ShardingDay shardingDay);

    /**
     * 修改日数据存储
     *
     * @param shardingDay 日数据存储
     * @return 结果
     */
    public int updateShardingDay(ShardingDay shardingDay);

    /**
     * 批量删除日数据存储
     *
     * @param variableNames 需要删除的日数据存储主键集合
     * @return 结果
     */
    public int deleteShardingDayByVariableNames(String[] variableNames);

    /**
     * 删除日数据存储信息
     *
     * @param variableName 日数据存储主键
     * @return 结果
     */
    public int deleteShardingDayByVariableName(String variableName);


    /**
     * 查询最新一条数据
     * <pre>
     * single-change-day-day 单值-变化值-日-日表
     * </pre>
     * @param variableName 变量
     * @return 最新数据
     * */
    public ShardingDay singleChangeDayDay(String variableName);
}
