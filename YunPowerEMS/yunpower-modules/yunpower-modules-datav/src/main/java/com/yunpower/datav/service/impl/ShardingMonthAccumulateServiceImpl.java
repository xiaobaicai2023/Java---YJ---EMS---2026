package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.ShardingMonthAccumulate;
import com.yunpower.datav.mapper.ShardingMonthAccumulateMapper;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datasource.annotation.ShardingDataSource;
import com.yunpower.datav.service.IShardingMonthAccumulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 变量累积数据月存储Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-11-02
 */
//多数据源（采集数据库，注解在类上适用）
@ShardingDataSource
@Service
public class ShardingMonthAccumulateServiceImpl implements IShardingMonthAccumulateService {
    @Autowired
    private ShardingMonthAccumulateMapper shardingMonthAccumulateMapper;

    /**
     * 查询变量累积数据月存储
     *
     * @param id 变量累积数据月存储主键
     * @return 变量累积数据月存储
     */
    @Override
    public ShardingMonthAccumulate selectShardingMonthAccumulateById(Long id) {
        return shardingMonthAccumulateMapper.selectShardingMonthAccumulateById(id);
    }

    /**
     * 获取某一个时点的数据，必传条件：recordYear，recordMonth，recordDay，recordHour
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param hour  时
     * @return 结果
     */
    @Override
    public ShardingMonthAccumulate selectShardingMonthAccumulate(Integer year, Integer month, Integer day, Integer hour) {
        if (StringUtils.isNull(year) || StringUtils.isNull(month) || StringUtils.isNull(day) || StringUtils.isNull(hour)) {
            return null;
        }
        ShardingMonthAccumulate shardingMonthAccumulate = new ShardingMonthAccumulate();
        shardingMonthAccumulate.setRecordYear(year);
        shardingMonthAccumulate.setRecordMonth(month);
        shardingMonthAccumulate.setRecordDay(day);
        shardingMonthAccumulate.setRecordHour(hour);
        return shardingMonthAccumulateMapper.selectShardingMonthAccumulate(shardingMonthAccumulate);
    }

    /**
     * 修改累积数据
     * <p>
     * AccuData累积数 逻辑说明
     * 首先 存储的是整点数据（按天存储的在月统计表中）
     * 举例：2023-11-02 00:00:00
     * 0点的数据是 00:00:00 到 01:00:00 之间的数据
     * 因此 AccuData =（01:00:00）DataValue -（00:00:00）DataValue
     * <p>
     * 注意：例如计算当前上传是8时的值
     * 情况一，只能计算7时的积累值；如果因为通讯关系7时未上传，那么6时的值就是0，不能是8时-6时的差值。（采用）
     * 情况二，计算上一个整点时的累计值。这时拿上一个例子来说明，6时的累计值就会偏高，因为他是6-8时两个小时的累计值。
     *
     * @param id       编号ID
     * @param accuData 累积值
     * @return 结果
     */
    @Override
    public int updateAccuData(Long id, Double accuData) {
        if (StringUtils.isNull(id) || StringUtils.isNull(accuData)) {
            return 0;
        }

        ShardingMonthAccumulate shardingMonthAccumulate = new ShardingMonthAccumulate();
        shardingMonthAccumulate.setId(id);
        shardingMonthAccumulate.setAccuData(accuData);
        return shardingMonthAccumulateMapper.updateShardingMonthAccumulate(shardingMonthAccumulate);
    }

    /**
     * 数据用于：尖峰平谷，按【时】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<ShardingMonthAccumulate> selectAccumulateGroupByHour(String variableName, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = packageDTO(variableName, beginTime, endTime);
        if (shardingMonthAccumulate == null) {
            return new ArrayList<>();
        }
        return shardingMonthAccumulateMapper.selectAccumulateGroupByHour(shardingMonthAccumulate);
    }

    /**
     * 数据用于：尖峰平谷，按【日】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<ShardingMonthAccumulate> selectAccumulateGroupByDay(String variableName, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = packageDTO(variableName, beginTime, endTime);
        if (shardingMonthAccumulate == null) {
            return new ArrayList<>();
        }
        return shardingMonthAccumulateMapper.selectAccumulateGroupByDay(shardingMonthAccumulate);
    }

    /**
     * 数据用于：尖峰平谷，按【周】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<ShardingMonthAccumulate> selectAccumulateGroupByWeek(String variableName, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = packageDTO(variableName, beginTime, endTime);
        if (shardingMonthAccumulate == null) {
            return new ArrayList<>();
        }
        return shardingMonthAccumulateMapper.selectAccumulateGroupByWeek(shardingMonthAccumulate);
    }

    /**
     * 数据用于：尖峰平谷，按【月】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<ShardingMonthAccumulate> selectAccumulateGroupByMonth(String variableName, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = packageDTO(variableName, beginTime, endTime);
        if (shardingMonthAccumulate == null) {
            return new ArrayList<>();
        }
        return shardingMonthAccumulateMapper.selectAccumulateGroupByMonth(shardingMonthAccumulate);
    }

    /**
     * 数据用于：尖峰平谷，按【年】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<ShardingMonthAccumulate> selectAccumulateGroupByYear(String variableName, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = packageDTO(variableName, beginTime, endTime);
        if (shardingMonthAccumulate == null) {
            return new ArrayList<>();
        }
        return shardingMonthAccumulateMapper.selectAccumulateGroupByYear(shardingMonthAccumulate);
    }

    /**
     * 数据用于：尖峰平谷，按【类型】分组
     *
     * @param variableName 变量
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @return 变量累积数据月存储集合
     */
    @Override
    public List<ShardingMonthAccumulate> selectAccumulateGroupBySeasonalName(String variableName, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = packageDTO(variableName, beginTime, endTime);
        if (shardingMonthAccumulate == null) {
            return new ArrayList<>();
        }
        return shardingMonthAccumulateMapper.selectAccumulateGroupBySeasonalName(shardingMonthAccumulate);
    }

    /**
     * 查询某个变量、某个时间段内的电量总和
     *
     * @param variable_name 变量名称
     * @param beginTime     开始时间
     * @param endTime       结束时间
     * @return 结果
     */
    @Override
    public Float sumAccumulate(String variable_name, String beginTime, String endTime) {
        ShardingMonthAccumulate shardingMonthAccumulate = new ShardingMonthAccumulate();
        shardingMonthAccumulate.setVariableName(variable_name);
        shardingMonthAccumulate.setParams(DateUtils.dateToParamForDay(beginTime, endTime));
        return shardingMonthAccumulateMapper.sumAccumulate(shardingMonthAccumulate);
    }

    private ShardingMonthAccumulate packageDTO(String variableName, String beginTime, String endTime) {
        if (StringUtils.isEmpty(variableName) || StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return null;
        }
        ShardingMonthAccumulate shardingMonthAccumulate = new ShardingMonthAccumulate();
        shardingMonthAccumulate.setVariableName(variableName);
        shardingMonthAccumulate.setParams(DateUtils.dateToParamForDay(beginTime, endTime));
        return shardingMonthAccumulate;
    }

    /**
     * 重新更新峰谷数据
     *
     * @param map 参数集合
     * @return 结果
     */
    @Override
    public int updateShardingMonthAccumulateSeasonal(Map<String, Object> map) {
        return shardingMonthAccumulateMapper.updateShardingMonthAccumulateSeasonal(map);
    }

    /**
     * 查询变量累积数据月存储列表
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @param isChain                 是否环比：0否1是
     * @return 变量累积数据月存储
     */
    @Override
    public List<ShardingMonthAccumulate> selectShardingMonthAccumulateList(ShardingMonthAccumulate shardingMonthAccumulate, Integer isChain) {
        List<ShardingMonthAccumulate> list = shardingMonthAccumulateMapper.selectShardingMonthAccumulateList(shardingMonthAccumulate);

        //如果是环比数据，则需要计算一下
        if (isChain == 0 || list.isEmpty()) {
            return list;
        }

        //计算环比
        return packageAccuListChain(list, shardingMonthAccumulate.getParams().get("beginTime").toString());
    }

    // 公共处理代码，提出公用（环比 = [今天值 - 昨天值]/昨天值）
    private List<ShardingMonthAccumulate> packageAccuListChain(List<ShardingMonthAccumulate> list, String beginTime) {
        //数据库取出的结果是每个“时段”的值累计值（如电能）因此需要计算
        List<ShardingMonthAccumulate> newList = new ArrayList<>();

        //计算前一天日期
        String forwardTime = DateUtils.getPastDate(beginTime, -1);

        ShardingMonthAccumulate shardingAccu = new ShardingMonthAccumulate();
        shardingAccu.setVariableName(list.get(0).getVariableName());
        shardingAccu.setParams(DateUtils.dateToParamForDay(forwardTime));
        List<ShardingMonthAccumulate> forwardList = shardingMonthAccumulateMapper.selectShardingMonthAccumulateList(shardingAccu);

        //默认时间正序，那就要取出最后一条记录
        double forwardDataValue = 0f;
        if (!forwardList.isEmpty()) {
            forwardDataValue = forwardList.get(forwardList.size() - 1).getAccuData();
        }

        for (int i = 0; i < list.size(); i++) {
            ShardingMonthAccumulate temp = new ShardingMonthAccumulate();
            temp.setRecordYear(list.get(i).getRecordYear());
            temp.setRecordMonth(list.get(i).getRecordMonth());
            temp.setRecordDay(list.get(i).getRecordDay());
            temp.setRecordHour(list.get(i).getRecordHour());
            temp.setSaveTime(list.get(i).getSaveTime());

            //计算环比（百分比）
            if (i == 0) { //第一条记录
                if (forwardDataValue == 0) {
                    temp.setAccuData(0D);
                } else {
                    temp.setAccuData(DoubleUtils.computePercent(list.get(i).getAccuData(), forwardDataValue));
                }
            } else {
                if (list.get(i - 1).getAccuData() == 0) {
                    temp.setAccuData(0D);
                } else {
                    temp.setAccuData(DoubleUtils.computePercent(list.get(i).getAccuData(), list.get(i - 1).getAccuData()));
                }
            }
            newList.add(temp);
        }

        //因为是倒序计算的，所以要重新排一下序
        //Collections.reverse(newList);

        return newList;
    }

    /**
     * 新增变量累积数据月存储
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 结果
     */
    @Override
    public int insertShardingMonthAccumulate(ShardingMonthAccumulate shardingMonthAccumulate) {
        return shardingMonthAccumulateMapper.insertShardingMonthAccumulate(shardingMonthAccumulate);
    }

    /**
     * 修改变量累积数据月存储
     *
     * @param shardingMonthAccumulate 变量累积数据月存储
     * @return 结果
     */
    @Override
    public int updateShardingMonthAccumulate(ShardingMonthAccumulate shardingMonthAccumulate) {
        return shardingMonthAccumulateMapper.updateShardingMonthAccumulate(shardingMonthAccumulate);
    }

    /**
     * 批量删除变量累积数据月存储
     *
     * @param ids 需要删除的变量累积数据月存储主键
     * @return 结果
     */
    @Override
    public int deleteShardingMonthAccumulateByIds(Long[] ids) {
        return shardingMonthAccumulateMapper.deleteShardingMonthAccumulateByIds(ids);
    }

    /**
     * 删除变量累积数据月存储信息
     *
     * @param id 变量累积数据月存储主键
     * @return 结果
     */
    @Override
    public int deleteShardingMonthAccumulateById(Long id) {
        return shardingMonthAccumulateMapper.deleteShardingMonthAccumulateById(id);
    }
}
