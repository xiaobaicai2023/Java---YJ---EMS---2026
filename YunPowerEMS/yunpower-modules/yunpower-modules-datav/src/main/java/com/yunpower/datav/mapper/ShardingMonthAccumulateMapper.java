package com.yunpower.datav.mapper;

import com.yunpower.datav.domain.ShardingMonthAccumulate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 变量累积数据月存储Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
@Mapper
public interface ShardingMonthAccumulateMapper {
    /**
     * 查询变量累积数据月存储
     *
     * @param id 变量累积数据月存储主键
     * @return 变量累积数据月存储
     */
    public ShardingMonthAccumulate selectShardingMonthAccumulateById(Long id);

    /**
     * 获取某一个时点的数据，必传条件：recordYear，recordMonth，recordDay，recordHour
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储
     */
    public ShardingMonthAccumulate selectShardingMonthAccumulate(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 数据用于：尖峰平谷，按【时】分组
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByHour(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 数据用于：尖峰平谷，按【日】分组
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByDay(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 数据用于：尖峰平谷，按【周】分组
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByWeek(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 数据用于：尖峰平谷，按【月】分组
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByMonth(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 数据用于：尖峰平谷，按【年】分组
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupByYear(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 数据用于：尖峰平谷，按【类型】分组
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectAccumulateGroupBySeasonalName(ShardingMonthAccumulate shardingMonthAccumulate);

    /**
     * 查询某个变量、某个时间段内的电量总和
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 结果
     */
    public Float sumAccumulate(ShardingMonthAccumulate shardingMonthAccumulate);

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
     * @return 变量累积数据月存储集合
     */
    public List<ShardingMonthAccumulate> selectShardingMonthAccumulateList(ShardingMonthAccumulate shardingMonthAccumulate);

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
     * 删除变量累积数据月存储
     *
     * @param id 变量累积数据月存储主键
     * @return 结果
     */
    public int deleteShardingMonthAccumulateById(Long id);

    /**
     * 批量删除变量累积数据月存储
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShardingMonthAccumulateByIds(Long[] ids);
}
