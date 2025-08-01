package com.yunpower.datav.mapper;

import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.domain.ShardingQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 月统计数据存储Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
@Mapper
public interface ShardingMonthMapper {
    /**
     * 查询：最大值
     */
    public ShardingMonth selectShardingMonthMax(ShardingMonth shardingMonth);

    /**
     * 查询：最小值
     */
    public ShardingMonth selectShardingMonthMin(ShardingMonth shardingMonth);

    /**
     * 查询：平均值
     */
    public ShardingMonth selectShardingMonthAvg(ShardingMonth shardingMonth);

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
     * 查询：月统计数据（累计值）
     *
     * @param shardingMonth 月统计数据存储
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthStatisticForMonth(ShardingMonth shardingMonth);

    /**
     * 查询：月统计数据（平均值）
     *
     * @param shardingMonth 月统计数据存储
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthStatisticForMonthAvg(ShardingMonth shardingMonth);

    /**
     * 查询：年统计数据（累计值）
     *
     * @param shardingMonth 月统计数据存储
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthStatisticForYear(ShardingMonth shardingMonth);

    /**
     * 查询：年统计数据（平均值）
     *
     * @param shardingMonth 月统计数据存储
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthStatisticForYearAvg(ShardingMonth shardingMonth);

    /**
     * 查询月统计数据存储列表
     *
     * @param shardingMonth 月统计数据存储
     * @return 月统计数据存储集合
     */
    public List<ShardingMonth> selectShardingMonthList(ShardingMonth shardingMonth);

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
     * 删除月统计数据存储
     *
     * @param id 月统计数据存储主键
     * @return 结果
     */
    public int deleteShardingMonthById(Long id);

    /**
     * 批量删除月统计数据存储
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShardingMonthByIds(Long[] ids);

    /**
     * 查询指定数值类型查询值
     * <pre>
     *  查询参数：变量、数值类型（max,min,avg,acc,runtime）
     * </pre>
     */
    public List<ShardingMonth> singleChangeDayMonth(ShardingQuery ShardingQuery);

    /**
     * 查询指定数值类型查询值
     * <pre>
     *  查询参数：变量、数值类型（max,min,avg,acc）
     * </pre>
     */
    public List<ShardingMonth> singleChangeMonthMonth(ShardingQuery ShardingQuery);

    /**
     * 查询指定数值类型查询值
     * <pre>
     *  查询参数：连续值+变化值+日维度 (5 10 15 30分钟 1时)
     * </pre>
     */
    public List<ShardingMonth> continuityChangeDayDay(ShardingQuery ShardingQuery);

    /**
     * 查询指定数值类型查询值
     * <pre>
     *  查询参数：连续值+变化值+月维度
     * </pre>
     */
    public List<ShardingMonth> continuityChangeMonthDay(ShardingQuery ShardingQuery);

    /**
     * 查询指定数值类型查询值
     * <pre>
     *  查询参数：连续值+变化值+年维度
     * </pre>
     */
    public List<ShardingMonth> continuityChangeYearDay(ShardingQuery ShardingQuery);

    /**
     * 峰平谷数据 - 月（日）维度
     */
    public List<ShardingMonth> continuityFengGuMonthDay(ShardingQuery shardingQuery);

    /**
     * 峰平谷数据 - 年（月）维度
     */
    public List<ShardingMonth> continuityFengGuYearDay(ShardingQuery shardingQuery);

    /**
     * 获取年度数据
     */
    public Float selectTotalDataForYear(ShardingQuery shardingQuery);

    /**
     * 大屏统计变量数据查询
     */
    public List<ShardingMonth> selectDatavStatVarData(ShardingQuery shardingQuery);
}
