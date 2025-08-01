package com.yunpower.collect.storage.service.impl;

import com.yunpower.collect.storage.domain.AlarmTrigger;
import com.yunpower.collect.storage.mapper.AlarmTriggerMapper;
import com.yunpower.collect.storage.service.IAlarmTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IAlarmTriggerServiceImpl implements IAlarmTriggerService {

	@Autowired
	private AlarmTriggerMapper alarmTriggerMapper;

	/**
	 * 新增报警管理
	 *
	 * @param alarmTrigger 报警管理
	 * @return 结果
	 */
	@Override
	public int insertAlarmTrigger(AlarmTrigger alarmTrigger) {
		return alarmTriggerMapper.insertAlarmTrigger(alarmTrigger);
	}

	/**
	 * 批量恢复报警
	 * */
	@Override
	public int batchRestoreAlarm(AlarmTrigger alarmTrigger){
		return alarmTriggerMapper.batchRestoreAlarm(alarmTrigger);
	}
}
