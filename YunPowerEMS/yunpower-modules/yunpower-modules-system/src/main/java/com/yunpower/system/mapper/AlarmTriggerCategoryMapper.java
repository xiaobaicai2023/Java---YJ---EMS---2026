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

    /**
     * 新增报警类型
     *
     * @param alarmTriggerCategory 报警类型
     * @return 结果
     */
    public int insertAlarmTriggerCategory(AlarmTriggerCategory alarmTriggerCategory);

    /**
     * 修改报警类型
     *
     * @param alarmTriggerCategory 报警类型
     * @return 结果
     */
    public int updateAlarmTriggerCategory(AlarmTriggerCategory alarmTriggerCategory);

    /**
     * 删除报警类型
     *
     * @param id 报警类型主键
     * @return 结果
     */
    public int deleteAlarmTriggerCategoryById(Long id);

    /**
     * 批量删除报警类型
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlarmTriggerCategoryByIds(Long[] ids);
}
