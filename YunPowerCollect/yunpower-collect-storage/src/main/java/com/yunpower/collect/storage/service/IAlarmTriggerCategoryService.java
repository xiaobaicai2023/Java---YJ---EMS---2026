package com.yunpower.collect.storage.service;

import com.yunpower.collect.storage.domain.AlarmTriggerCategory;

import java.util.Map;

/**
 * 报警类型Service接口
 *
 * @author JUNFU.WANG
 * @date 2023-10-07
 */
public interface IAlarmTriggerCategoryService {

	/**
	 * 查询启用的报警类型
	 * @return 报警类型map
	 */
	Map<Long,AlarmTriggerCategory> selectAlarmTriggerCategoryMap();
}
