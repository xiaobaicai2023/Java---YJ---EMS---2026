package com.yunpower.datav.service.impl;

import com.yunpower.common.core.DoubleUtils;
import com.yunpower.datav.domain.ShardingMonth;
import com.yunpower.datav.mapper.ShardingMonthMapper;
import com.yunpower.common.core.enums.sharding.StorageTypeEnum;
import com.yunpower.common.core.utils.DateUtils;
import com.yunpower.common.core.utils.FormatUtils;
import com.yunpower.common.core.utils.StringUtils;
import com.yunpower.common.datasource.annotation.ShardingDataSource;
import com.yunpower.datav.service.IShardingMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 月统计数据存储Service业务层处理
 *
 * @author JUNFU.WANG
 * @date 2023-10-18
 */
//多数据源（采集数据库，注解在类上适用）
@ShardingDataSource
@Service
public class ShardingMonthServiceImpl implements IShardingMonthService {
    @Autowired
    private ShardingMonthMapper shardingMonthMapper;

    /**
     * 查询月统计数据存储（最新的一条数据）
     */
    @Override
    public ShardingMonth selectShardingMonthByVariableName(String variableName) {
        ShardingMonth shardingMonth = new ShardingMonth();
        shardingMonth.setVariableName(variableName);
        return shardingMonthMapper.selectShardingMonth(shardingMonth);
    }

    /**
     * 查询月统计数据存储（限定时间内，最新的一条数据）
     */
    @Override
    public ShardingMonth selectShardingMonthByVariableName(String variableName, String beginTime, String endTime) {
        ShardingMonth shardingMonth = new ShardingMonth();
        shardingMonth.setVariableName(variableName);
        shardingMonth.setParams(DateUtils.dateToParamForMonth(beginTime, endTime));
        return shardingMonthMapper.selectShardingMonth(shardingMonth);
    }

    /**
     * 查询：最大值（单值）
     */
    @Override
    public Double selectShardingMonthMax(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return 0D;
        }

        ShardingMonth dto = shardingMonthMapper.selectShardingMonthMax(shardingMonth);
        return dto == null ? 0F : dto.getMaxValue();
    }

    /**
     * 查询：最小值（单值）
     */
    @Override
    public Double selectShardingMonthMin(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return 0D;
        }

        ShardingMonth dto = shardingMonthMapper.selectShardingMonthMin(shardingMonth);
        return dto == null ? 0F : dto.getMinValue();
    }

    /**
     * 查询：平均值（单值）
     */
    @Override
    public Double selectShardingMonthAvg(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return 0D;
        }

        ShardingMonth dto = shardingMonthMapper.selectShardingMonthAvg(shardingMonth);
        return dto == null ? 0D : dto.getAvgValue();
    }

    /**
     * 查询：日累积值（单值）
     */
    @Override
    public Double selectShardingMonthDay(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null || shardingMonth.getDaySign() == null) {
            return 0D;
        }

        ShardingMonth dto = shardingMonthMapper.selectShardingMonth(shardingMonth);
        return dto == null ? 0F : dto.getAccuValue();
    }

    /**
     * 查询：日记录数（单值）
     */
    @Override
    public Integer selectShardingMonthCount(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null || shardingMonth.getDaySign() == null) {
            return 0;
        }

        ShardingMonth dto = shardingMonthMapper.selectShardingMonth(shardingMonth);
        return dto == null ? 0 : dto.getTotalCount();
    }

    /**
     * 查询：月累计值（单值）
     */
    @Override
    public Double selectShardingMonthMonth(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return 0D;
        }

        List<ShardingMonth> list = shardingMonthMapper.selectShardingMonthStatisticForMonth(shardingMonth);
        return list == null ? 0F : list.get(0).getStatisticValue();
    }

    /**
     * 查询：年累计值（单值）
     */
    @Override
    public Double selectShardingMonthYear(ShardingMonth shardingMonth) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return 0D;
        }

        List<ShardingMonth> list = shardingMonthMapper.selectShardingMonthStatisticForYear(shardingMonth);
        return list == null ? 0F : list.get(0).getStatisticValue();
    }

    /**
     * 查询月统计数据存储
     */
    @Override
    public ShardingMonth selectShardingMonth(ShardingMonth shardingMonth) {
        return shardingMonthMapper.selectShardingMonth(shardingMonth);
    }

    /**
     * 查询月统计数据存储
     *
     * @param id 月统计数据存储主键
     * @return 月统计数据存储
     */
    @Override
    public ShardingMonth selectShardingMonthById(Long id) {
        return shardingMonthMapper.selectShardingMonthById(id);
    }

    /**
     * 查询月统计数据存储列表（查询：日统计数据）
     *
     * @param shardingMonth 月统计数据存储
     * @param storageType   使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain       是否环比：0否1是
     * @return 月统计数据存储
     */
    @Override
    public List<ShardingMonth> selectShardingMonthList(ShardingMonth shardingMonth, Integer storageType, Integer isChain) {
        if (shardingMonth.getSaveTime() != null) {
            shardingMonth.setParams(DateUtils.dateToParamForMonth(shardingMonth.getSaveTime()));
            shardingMonth.setSaveTime(null);
        }

        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return new ArrayList<>();
        }

        List<ShardingMonth> list = shardingMonthMapper.selectShardingMonthList(shardingMonth);
        if (isChain == 0 || list.isEmpty()) {
            return list;
        }

        //region 计算环比
        List<ShardingMonth> newList = new ArrayList<>();

        //region （环比 = [今天累计值 - 昨天累计值]/昨天累计值）
        // 计算前一天日期
        String beginTime = shardingMonth.getParams().get("beginTime").toString();
        String forwardTime = DateUtils.getPastDate(beginTime, -1);

        ShardingMonth shardingMon = new ShardingMonth();
        shardingMon.setVariableName(shardingMonth.getVariableName());
        Map<String, Object> map = DateUtils.dateToParamForDay(forwardTime);
        shardingMon.setParams(map);
        List<ShardingMonth> forwardList = shardingMonthMapper.selectShardingMonthList(shardingMon);

        //默认时间正序，那就要取出最后一条记录
        double forwardDataValue = 0f;
        if (!forwardList.isEmpty()) {
            if (storageType == StorageTypeEnum.CHANGE.getKey()) {
                forwardDataValue = forwardList.get(forwardList.size() - 1).getAvgValue();
            } else {
                forwardDataValue = forwardList.get(forwardList.size() - 1).getAccuValue();
            }
        }
        //endregion

        for (int i = 0; i < list.size(); i++) {
            ShardingMonth temp = new ShardingMonth();
            temp.setYear(list.get(i).getYear());
            temp.setMonth(list.get(i).getMonth());
            temp.setDay(list.get(i).getDay());
            temp.setMinTime(list.get(i).getMinTime());
            temp.setSaveTime(list.get(i).getSaveTime());

            //region 计算环比（百分比）
            if (i == 0) {
                if (forwardDataValue == 0) {
                    temp.setChainValue(0D);
                } else {
                    if (storageType == StorageTypeEnum.CHANGE.getKey()) {
                        temp.setChainValue(DoubleUtils.computePercent(list.get(i).getAvgValue(), forwardDataValue));
                    } else {
                        temp.setChainValue(DoubleUtils.computePercent(list.get(i).getAccuValue(), forwardDataValue));
                    }
                }
            } else {
                if (storageType == StorageTypeEnum.CHANGE.getKey()) {
                    if (list.get(i - 1).getAvgValue() == 0) {
                        temp.setChainValue(0D);
                    } else {
                        temp.setChainValue(DoubleUtils.computePercent(list.get(i).getAvgValue(), list.get(i - 1).getAvgValue()));
                    }
                } else {
                    if (list.get(i - 1).getAccuValue() == 0) {
                        temp.setChainValue(0D);
                    } else {
                        temp.setChainValue(DoubleUtils.computePercent(list.get(i).getAccuValue(), list.get(i - 1).getAccuValue()));
                    }
                }
            }
            //endregion

            newList.add(temp);
        }

        //因为是倒序计算的，所以要重新排一下序
        //Collections.reverse(newList);

        return newList;
        //endregion
    }

    /**
     * 查询：月统计数据（列表）
     *
     * @param shardingMonth 月统计数据存储
     * @param storageType   使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain       是否环比：0否1是
     * @return 月统计数据存储集合
     */
    @Override
    public List<ShardingMonth> selectShardingMonthStatisticForMonth(ShardingMonth shardingMonth, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return new ArrayList<>();
        }

        List<ShardingMonth> list;
        if (storageType == StorageTypeEnum.CHANGE.getKey()) {
            //1.变化值 取平均值（最值都不合适）
            list = shardingMonthMapper.selectShardingMonthStatisticForMonthAvg(shardingMonth);
        } else {
            //2.累计值
            list = shardingMonthMapper.selectShardingMonthStatisticForMonth(shardingMonth);
        }

        if (isChain == 0 || list.isEmpty()) {
            return list;
        }

        //region 计算环比
        List<ShardingMonth> newList = new ArrayList<>();

        //region （环比 = [本月累计值 - 上月累计值]/上月累计值）
        String beginTime = shardingMonth.getParams().get("beginTime").toString();
        String forwardTime = DateUtils.getPastMonth(beginTime, -1);

        ShardingMonth shardingMon = new ShardingMonth();
        shardingMon.setVariableName(shardingMonth.getVariableName());
        Map<String, Object> map = DateUtils.dateToParamForMonth(forwardTime, forwardTime);
        shardingMon.setParams(map);
        List<ShardingMonth> forwardList;

        if (storageType == StorageTypeEnum.CHANGE.getKey()) {
            //1.变化值 取平均值（最值都不合适）
            forwardList = shardingMonthMapper.selectShardingMonthStatisticForMonthAvg(shardingMon);
        } else {
            //2.累计值
            forwardList = shardingMonthMapper.selectShardingMonthStatisticForMonth(shardingMon);
        }

        //默认时间正序，那就要取出最后一条记录
        double forwardDataValue = 0f;
        if (!forwardList.isEmpty()) {
            forwardDataValue = forwardList.get(forwardList.size() - 1).getStatisticValue();
        }
        //endregion

        for (int i = 0; i < list.size(); i++) {
            ShardingMonth temp = new ShardingMonth();
            temp.setYear(list.get(i).getYear());
            temp.setMonth(list.get(i).getMonth());
            temp.setMinTime(list.get(i).getMinTime());
            temp.setSaveTime(list.get(i).getSaveTime());

            //计算环比（百分比）
            if (i == 0) { //第一条记录
                if (forwardDataValue == 0) {
                    temp.setChainValue(0D);
                } else {
                    temp.setChainValue(DoubleUtils.computePercent(list.get(i).getStatisticValue(), forwardDataValue));
                }
            } else {
                if (list.get(i - 1).getStatisticValue() == 0) {
                    temp.setChainValue(0D);
                } else {
                    temp.setChainValue(DoubleUtils.computePercent(list.get(i).getStatisticValue(), list.get(i - 1).getStatisticValue()));
                }
            }
            newList.add(temp);
        }

        //因为是倒序计算的，所以要重新排一下序
        //Collections.reverse(newList);

        return newList;
        //endregion
    }

    /**
     * 查询：年统计数据（列表）
     *
     * @param shardingMonth 月统计数据存储
     * @param storageType   使用场景：1=变化值（如电能）；2=累计值（如电流、电压）；3=环比
     * @param isChain       是否环比：0否1是
     * @return 月统计数据存储集合
     */
    @Override
    public List<ShardingMonth> selectShardingMonthStatisticForYear(ShardingMonth shardingMonth, Integer storageType, Integer isChain) {
        //必填参数验证
        if (StringUtils.isEmpty(shardingMonth.getVariableName()) || shardingMonth.getParams() == null) {
            return new ArrayList<>();
        }

        List<ShardingMonth> list;
        if (storageType == StorageTypeEnum.CHANGE.getKey()) {
            //1.变化值 取平均值（最值都不合适）
            list = shardingMonthMapper.selectShardingMonthStatisticForMonthAvg(shardingMonth);
        } else {
            //2.累计值
            list = shardingMonthMapper.selectShardingMonthStatisticForYear(shardingMonth);
        }

        if (isChain == 0 || list.isEmpty()) {
            return list;
        }

        //region 计算环比
        List<ShardingMonth> newList = new ArrayList<>();

        //region （环比 = [本年累计值 - 上一年累计值]/上一年累计值）
        String beginYear = shardingMonth.getParams().get("beginTime").toString().substring(0, 4);

        ShardingMonth shardingMon = new ShardingMonth();
        shardingMon.setVariableName(shardingMonth.getVariableName());
        Map<String, Object> map = DateUtils.dateToParamForYear(Integer.parseInt(beginYear));
        shardingMon.setParams(map);
        List<ShardingMonth> forwardList;

        if (storageType == StorageTypeEnum.CHANGE.getKey()) {
            //1.变化值 取平均值（最值都不合适）
            forwardList = shardingMonthMapper.selectShardingMonthStatisticForMonthAvg(shardingMon);
        } else {
            //2.累计值
            forwardList = shardingMonthMapper.selectShardingMonthStatisticForYear(shardingMon);
        }

        //默认时间正序，那就要取出最后一条记录
        double forwardDataValue = 0f;
        if (!forwardList.isEmpty()) {
            forwardDataValue = forwardList.get(forwardList.size() - 1).getStatisticValue();
        }
        //endregion

        for (int i = 0; i < list.size(); i++) {
            ShardingMonth temp = new ShardingMonth();
            temp.setYear(list.get(i).getYear());
            temp.setMinTime(list.get(i).getMinTime());
            temp.setSaveTime(list.get(i).getSaveTime());

            //计算环比（百分比）
            if (i == 0) { //第一条记录
                if (forwardDataValue == 0) {
                    temp.setChainValue(0D);
                } else {
                    temp.setChainValue(DoubleUtils.computePercent(list.get(i).getStatisticValue(), forwardDataValue));
                }
            } else {
                if (list.get(i - 1).getStatisticValue() == 0) {
                    temp.setChainValue(0D);
                } else {
                    temp.setChainValue(DoubleUtils.computePercent(list.get(i).getStatisticValue(), list.get(i - 1).getStatisticValue()));
                }
            }
            newList.add(temp);
        }

        return newList;
        //endregion
    }

    /**
     * 新增月统计数据存储
     *
     * @param shardingMonth 月统计数据存储
     * @return 结果
     */
    @Override
    public int insertShardingMonth(ShardingMonth shardingMonth) {
        return shardingMonthMapper.insertShardingMonth(shardingMonth);
    }

    /**
     * 修改月统计数据存储
     *
     * @param shardingMonth 月统计数据存储
     * @return 结果
     */
    @Override
    public int updateShardingMonth(ShardingMonth shardingMonth) {
        return shardingMonthMapper.updateShardingMonth(shardingMonth);
    }

    /**
     * 批量删除月统计数据存储
     *
     * @param ids 需要删除的月统计数据存储主键
     * @return 结果
     */
    @Override
    public int deleteShardingMonthByIds(Long[] ids) {
        return shardingMonthMapper.deleteShardingMonthByIds(ids);
    }

    /**
     * 删除月统计数据存储信息
     *
     * @param id 月统计数据存储主键
     * @return 结果
     */
    @Override
    public int deleteShardingMonthById(Long id) {
        return shardingMonthMapper.deleteShardingMonthById(id);
    }
}
