package com.yunpower.collect.storage.mapper;


import com.yunpower.collect.storage.domain.AlarmTriggerConfig;

import java.util.List;

/**
 * 报警配置Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface AlarmTriggerConfigMapper
{
    /**
     * 查询报警配置
     *
     * @param id 报警配置主键
     * @return 报警配置
     */
    public AlarmTriggerConfig selectAlarmTriggerConfigById(Long id);

    /**
     * 查询报警配置列表
     *
     * @param alarmTriggerConfig 报警配置
     * @return 报警配置集合
     */
    public List<AlarmTriggerConfig> selectAlarmTriggerConfigList(AlarmTriggerConfig alarmTriggerConfig);

    /**
     * 新增报警配置
     *
     * @param alarmTriggerConfig 报警配置
     * @return 结果
     */
    public int insertAlarmTriggerConfig(AlarmTriggerConfig alarmTriggerConfig);

    /**
     * 修改报警配置
     *
     * @param alarmTriggerConfig 报警配置
     * @return 结果
     */
    public int updateAlarmTriggerConfig(AlarmTriggerConfig alarmTriggerConfig);

    /**
     * 删除报警配置
     *
     * @param id 报警配置主键
     * @return 结果
     */
    public int deleteAlarmTriggerConfigById(Long id);

    /**
     * 批量删除报警配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlarmTriggerConfigByIds(Long[] ids);
}
