package com.yunpower.system.service;

import java.util.List;

import com.yunpower.system.domain.AlarmTriggerCategory;

/**
 * 报警类型Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IAlarmTriggerCategoryService {
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
     * 修改报警类型状态
     *
     * @param id    报警类型主键
     * @param state 状态
     * @return 结果
     */
    public int updateAlarmTriggerCategoryState(AlarmTriggerCategory alarmTriggerCategory, Long id, Integer state);

    /**
     * 批量删除报警类型
     *
     * @param ids 需要删除的报警类型主键集合
     * @return 结果
     */
    public int deleteAlarmTriggerCategoryByIds(AlarmTriggerCategory alarmTriggerCategory, Long[] ids);

    /**
     * 删除报警类型信息
     *
     * @param id 报警类型主键
     * @return 结果
     */
    public int deleteAlarmTriggerCategoryById(AlarmTriggerCategory alarmTriggerCategory, Long id);
}
