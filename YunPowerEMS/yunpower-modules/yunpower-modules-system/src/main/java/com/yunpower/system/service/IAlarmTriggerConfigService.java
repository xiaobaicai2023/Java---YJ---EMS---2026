package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.AlarmTriggerConfig;

/**
 * 报警配置Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IAlarmTriggerConfigService {
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
     * 修改报警配置状态
     *
     * @param id    报警配置主键
     * @param state 状态
     * @return 结果
     */
    public int updateAlarmTriggerConfigState(AlarmTriggerConfig alarmTriggerConfig, Long id, Integer state);

    /**
     * 批量删除报警配置
     *
     * @param ids 需要删除的报警配置主键集合
     * @return 结果
     */
    public int deleteAlarmTriggerConfigByIds(AlarmTriggerConfig alarmTriggerConfig, Long[] ids);

    /**
     * 删除报警配置信息
     *
     * @param id 报警配置主键
     * @return 结果
     */
    public int deleteAlarmTriggerConfigById(AlarmTriggerConfig alarmTriggerConfig, Long id);
}
