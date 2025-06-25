package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.AlarmTriggerConfig;

import java.util.List;
import java.util.Map;

/**
 * 报警配置Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IAlarmTriggerConfigService {

	/**
	 * 查询监控设备报警配置 一个变量可能配置多条报警
	 *
	 * @return 报警配置集合
	 */
	 Map<String,List<AlarmTriggerConfig>> selectDeviceVarAlarmConfig();
}
