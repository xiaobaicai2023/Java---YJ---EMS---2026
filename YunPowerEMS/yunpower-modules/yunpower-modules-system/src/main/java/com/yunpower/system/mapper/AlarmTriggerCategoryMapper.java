package com.yunpower.system.mapper;

import java.util.List;

import com.yunpower.system.domain.AlarmTriggerCategory;

/**
 * 报警类型Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface AlarmTriggerCategoryMapper
{
    /**
     * 查询报警类型
     *
     * @param id 报警类型主键
     * @return 报警类型
     */
    public AlarmTriggerCategory selectAlarmTriggerCategoryById(Long id);

    /**
     * 查询报警类型列表
     *
     * @param alarmTriggerCategory 报警类型
     * @return 报警类型集合
     */
    public List<AlarmTriggerCategory> selectAlarmTriggerCategoryList(AlarmTriggerCategory alarmTriggerCategory);

}
