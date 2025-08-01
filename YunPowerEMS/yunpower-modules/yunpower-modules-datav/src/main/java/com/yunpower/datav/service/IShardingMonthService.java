package com.yunpower.datav.service;

import com.yunpower.datav.domain.ShardingMonth;

import java.util.List;

/**
 * 月统计数据存储Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
public interface IShardingMonthService {
    /**
     * 查询月统计数据存储（最新的一条数据）
     */
    public ShardingMonth selectShardingMonthByVariableName(String variableName);

    /**
     * 查询月统计数据存储（限定时间内，最新的一条数据）
     */
    public ShardingMonth selectShardingMonthByVariableName(String variableName, String beginTime, String endTime);

    /**
     * 查询：最大值（单值）
     */
    public Double selectShardingMonthMax(ShardingMonth shardingMonth);

    /**
     * 查询：最小值（单值）
     */
    public Double selectShardingMonthMin(ShardingMonth shardingMonth);

    /**
     * 查询：平均值（单值）
     */
    public Double selectShardingMonthAvg(ShardingMonth shardingMonth);

    /**
     * 查询：日累积值（单值）
     */
    public Double selectShardingMonthDay(ShardingMonth shardingMonth);

    /**
     * 查询：日记录数（单值）
     */
    public Integer selectShardingMonthCount(ShardingMonth shardingMonth);

    /**
     * 查询：月累计值（单值）
     */
    public Double selectShardingMonthMonth(ShardingMonth shardingMonth);

    /**
     * 查询：月累计值（单值）
     */
    public Double selectShardingMonthYear(ShardingMonth shardingMonth);

    /**
     * 查询月统计数据存储
     */
    public ShardingMonth selectShardingMonth(ShardingMonth shardingMonth);

    /**
     * 查询月统计数据存储
     *
     * @param id 月统计数据存储主键
     * @return 月统计数据存储
     */
    public ShardingMonth selectShardingMonthById(Long id);

    /**
     * 查询月统计数据存储列表（查询：日统计数据）
     *
     * @param shardingMonth 月统计数据存储
     * @param storageType   使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain       是否环比：0否1是
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthList(ShardingMonth shardingMonth, Integer storageType, Integer isChain);

    /**
     * 查询：月统计数据（列表）
     *
     * @param shardingMonth 月统计数据存储
     * @param storageType   使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain       是否环比：0否1是
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthStatisticForMonth(ShardingMonth shardingMonth, Integer storageType, Integer isChain);

    /**
     * 查询：年统计数据（列表）
     *
     * @param shardingMonth 月统计数据存储
     * @param storageType   使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain       是否环比：0否1是
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthStatisticForYear(ShardingMonth shardingMonth, Integer storageType, Integer isChain);

    /**
     * 新增月统计数据存储
     *
     * @param shardingMonth 月统计数据存储
     * @return 结果
     */
    public int insertShardingMonth(ShardingMonth shardingMonth);

    /**
     * 修改月统计数据存储
     *
     * @param shardingMonth 月统计数据存储
     * @return 结果
     */
    public int updateShardingMonth(ShardingMonth shardingMonth);

    /**
     * 批量删除月统计数据存储
     *
     * @param ids 需要删除的月统计数据存储主键集合
     * @return 结果
     */
    public int deleteShardingMonthByIds(Long[] ids);

    /**
     * 删除月统计数据存储信息
     *
     * @param id 月统计数据存储主键
     * @return 结果
     */
    public int deleteShardingMonthById(Long id);
}
