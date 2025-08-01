package com.yunpower.datav.mapper;

import com.yunpower.datav.domain.ShardingDay;
import com.yunpower.datav.domain.ShardingQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 日数据存储Mapper接口
 * 
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Mapper
public interface ShardingDayMapper 
{
    /**
     * 查询日数据存储
     */
    public ShardingDay selectShardingDay(ShardingDay shardingDay);

    /**
     * 查询日数据存储列表
     * 
     * @param shardingDay 日数据存储
     * @return 日数据存储集合
     */
    public List<ShardingDay> selectShardingDayList(ShardingDay shardingDay);

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
     * 删除日数据存储
     * 
     * @param variableName 日数据存储主键
     * @return 结果
     */
    public int deleteShardingDayByVariableName(String variableName);

    /**
     * 批量删除日数据存储
     * 
     * @param variableNames 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShardingDayByVariableNames(String[] variableNames);


    /**
     * 查询最新一条数据
     * <pre>
     * single-change-day-day 单值-变化值-日-日表
     * </pre>
     * @param variableName 变量
     * @return 最新数据
     * */
    public ShardingDay singleChangeDayDay(String variableName);

    /**
     * 获取上一条数据
     * */
    public Double selectUpperData(ShardingQuery shardingQuery);
}
