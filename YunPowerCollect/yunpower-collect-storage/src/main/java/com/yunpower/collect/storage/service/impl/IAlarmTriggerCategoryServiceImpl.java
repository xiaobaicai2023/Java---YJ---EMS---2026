package com.yunpower.collect.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yunpower.collect.storage.domain.AlarmTriggerCategory;
import com.yunpower.collect.storage.mapper.AlarmTriggerCategoryMapper;
import com.yunpower.collect.storage.service.IAlarmTriggerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IAlarmTriggerCategoryServiceImpl implements IAlarmTriggerCategoryService {

	@Autowired
	private AlarmTriggerCategoryMapper alarmTriggerCategoryMapper;

	/**
	 * 查询启用的报警类型
	 * @return 报警类型map
	 */
	@Override
	public Map<Long, AlarmTriggerCategory> selectAlarmTriggerCategoryMap() {
		AlarmTriggerCategory category = new AlarmTriggerCategory();
		category.setStopFlag(0);
		category.setDeleteFlag(0);
		List<AlarmTriggerCategory> list = alarmTriggerCategoryMapper.selectAlarmTriggerCategoryList(category);
		if(ObjectUtil.isNotEmpty(list)){
			return list.stream()
					.collect(Collectors.toMap(AlarmTriggerCategory::getId, item -> item));
        }
		return null;
	}
}
