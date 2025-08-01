package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.AlarmTriggerConfig;

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

}
