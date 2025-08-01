package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.AlarmTrigger;

/**
 * 报警管理Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface AlarmTriggerMapper {
    /**
     * 查询报警管理
     *
     * @param id 报警管理主键
     * @return 报警管理
     */
    public AlarmTrigger selectAlarmTriggerById(Long id);

    /**
     * 查询报警管理列表
     *
     * @param alarmTrigger 报警管理
     * @return 报警管理集合
     */
    public List<AlarmTrigger> selectAlarmTriggerList(AlarmTrigger alarmTrigger);

    /**
     * 根据报警级别统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    public List<AlarmTrigger> selectStatisticDataByAlarmLevelDate(AlarmTrigger alarmTrigger);

    /**
     * 根据报警类型统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    public List<AlarmTrigger> selectStatisticDataByCategory(AlarmTrigger alarmTrigger);

    /**
     * 根据报警级别统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    public List<AlarmTrigger> selectStatisticDataByAlarmLevel(AlarmTrigger alarmTrigger);

    /**
     * 根据日期统计数据
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    public List<AlarmTrigger> selectStatisticByDate(AlarmTrigger alarmTrigger);

    /**
     * 获取不重复的设备列表
     *
     * @param alarmTrigger 报警管理
     * @return 结果
     */
    public List<AlarmTrigger> selectDistinctDevice(AlarmTrigger alarmTrigger);

}
