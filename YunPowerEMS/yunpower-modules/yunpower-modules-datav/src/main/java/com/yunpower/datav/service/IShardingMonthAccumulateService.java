package com.yunpower.datav.service;

import com.yunpower.datav.domain.ShardingMonthAccumulate;

import java.util.List;
import java.util.Map;

/**
 * 变量累积数据月存储Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
public interface IShardingMonthAccumulateService {
    /**
     * 查询变量累积数据月存储
     *
     * @param id 变量累积数据月存储主键
     * @return 变量累积数据月存储
     */
    public ShardingMonthAccumulate selectShardingMonthAccumulateById(Long id);

    /**
     * 查询变量累积数据月存储
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param hour  时
     * @return 结果
     */
    public ShardingMonthAccumulate selectShardingMonthAccumulate(Integer year, Integer month, Integer day, Integer hour);

    /**
     * 修改累积数据
     *
     * @param id       编号ID
     * @param accuData 累积值
     * @return 结果
     */
    public int updateAccuData(Long id, Double accuData);

    /**
     * 数据用于：尖峰平谷，按【时】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByHour(String variableName, String beginTime, String endTime);

    /**
     * 数据用于：尖峰平谷，按【日】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByDay(String variableName, String beginTime, String endTime);

    /**
     * 数据用于：尖峰平谷，按【周】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByWeek(String variableName, String beginTime, String endTime);

    /**
     * 数据用于：尖峰平谷，按【月】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByMonth(String variableName, String beginTime, String endTime);

    /**
     * 数据用于：尖峰平谷，按【年】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByYear(String variableName, String beginTime, String endTime);

    /**
     * 数据用于：尖峰平谷，按【类型】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupBySeasonalName(String variableName, String beginTime, String endTime);

    /**
     * 查询某个变量、某个时间段内的电量总和
     *
     * @param variable_name 变量名称
     * @param beginTime     开始时间
     * @param endTime       结束时间
     * @return 结果
     */
    public Float sumAccumulate(String variable_name, String beginTime, String endTime);

    /**
     * 重新更新峰谷数据
     *
     * @param map 参数集合
     * @return 结果
     */
    public int updateShardingMonthAccumulateSeasonal(Map<String, Object> map);

    /**
     * 查询变量累积数据月存储列表
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @param isChain                 是否环比：0否1是
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectShardingMonthAccumulateList(ShardingMonthAccumulate shardingMonthAccumulate, Integer isChain);

    /**
     * 新增变量累积数据月存储
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 结果
     */
    public int insertShardingMonthAccumulate(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 修改变量累积数据月存储
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 结果
     */
    public int updateShardingMonthAccumulate(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 批量删除变量累积数据月存储
     *
     * @param ids 需要删除的变量累积数据月存储主键集合
     * @return 结果
     */
    public int deleteShardingMonthAccumulateByIds(Long[] ids);

    /**
     * 删除变量累积数据月存储信息
     *
     * @param id 变量累积数据月存储主键
     * @return 结果
     */
    public int deleteShardingMonthAccumulateById(Long id);
}
