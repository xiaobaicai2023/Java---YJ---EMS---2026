package com.yunpower.collect.storage.mapper;


import com.yunpower.collect.storage.domain.AlarmTriggerCategory;

import java.util.List;

/**
 * 报警类型Mapper接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface AlarmTriggerCategoryMapper
{
    /**
     * 查询报警类型列表
     *
     * @param alarmTriggerCategory 报警类型
     * @return 报警类型集合
     */
    public List<AlarmTriggerCategory> selectAlarmTriggerCategoryList(AlarmTriggerCategory alarmTriggerCategory);
}
