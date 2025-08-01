package com.yunpower.collect.storage.service;


import com.yunpower.collect.storage.domain.AlarmTrigger;

/**
 * 报警管理Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IAlarmTriggerService {

	/**
	 * 新增报警管理
	 *
	 * @param alarmTrigger 报警管理
	 * @return 结果
	 */
	public int insertAlarmTrigger(AlarmTrigger alarmTrigger);

	/**
	 * 批量恢复报警
	 * */
	public int batchRestoreAlarm(AlarmTrigger alarmTrigger);
}
