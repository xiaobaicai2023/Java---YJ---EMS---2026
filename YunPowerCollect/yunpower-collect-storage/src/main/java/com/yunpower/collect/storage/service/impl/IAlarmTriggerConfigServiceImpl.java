package com.yunpower.collect.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yunpower.collect.storage.domain.AlarmTriggerCategory;
import com.yunpower.collect.storage.domain.AlarmTriggerConfig;
import com.yunpower.collect.storage.domain.SysCommonDictData;
import com.yunpower.collect.storage.mapper.AlarmTriggerConfigMapper;
import com.yunpower.collect.storage.mapper.SysCommonDictDataMapper;
import com.yunpower.collect.storage.service.IAlarmTriggerCategoryService;
import com.yunpower.collect.storage.service.IAlarmTriggerConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IAlarmTriggerConfigServiceImpl implements IAlarmTriggerConfigService {

	private static final Logger log = LoggerFactory.getLogger(IAlarmTriggerConfigServiceImpl.class);
	@Autowired
	private AlarmTriggerConfigMapper configMapper;

	@Autowired
	private SysCommonDictDataMapper dictDataMapper;

	@Autowired
	private IAlarmTriggerCategoryService categoryService;

	/**
	 * 查询监控设备报警配置 一个变量可能配置多条报警
	 *
	 * @return 报警配置集合
	 */
	@Override
	public Map<String, List<AlarmTriggerConfig>> selectDeviceVarAlarmConfig() {
		try {
			//获取变量报警配置
			AlarmTriggerConfig config = new AlarmTriggerConfig();
			config.setStopFlag(0);
			List<AlarmTriggerConfig> list = configMapper.selectAlarmTriggerConfigList(config);
			if (list == null || list.isEmpty()) {
				log.error("变量报警配置为空");
				return Collections.emptyMap();
			}

			//
			List<SysCommonDictData> dictData = dictDataMapper.selectDictDataByType("sys_trigger_type");
			List<SysCommonDictData> levelDictData = dictDataMapper.selectDictDataByType("sys_alaram_level");
			if (ObjectUtil.isEmpty(dictData)) {
				log.error("字典表没有配置报警表达式：sys_trigger_type");
				return Collections.emptyMap();
			}

			//表达式 存储在 remark 字段
			Map<String, SysCommonDictData> aviatorMap = dictData.stream().collect(Collectors.toMap(
					SysCommonDictData::getDictValue,
					item->item
			));

			//表达式 存储在 remark 字段
			Map<String, String> alarmLevelMap = levelDictData.stream().collect(Collectors.toMap(
					SysCommonDictData::getDictValue,
					SysCommonDictData::getDictLabel
			));

			//获取报警类型
			Map<Long, AlarmTriggerCategory> categoryMap = categoryService.selectAlarmTriggerCategoryMap();

			for (AlarmTriggerConfig item : list) {
				//添加报警类型
				AlarmTriggerCategory category = categoryMap.get(item.getCategoryId());
				if (category == null) {
						item.setCategoryId(0L);
				} else {
					//添加报警类型
					item.setCategory(categoryMap.get(item.getCategoryId()));
					//触发类型 -> 表达式
					SysCommonDictData data =aviatorMap.get(category.getTriggerType());
					if(data != null){
						item.setAviator(data.getRemark());
						item.setTriggerConditionName(data.getDictLabel());
						item.setTriggerConditionSn(data.getDictValue());
					}
					item.getCategory().setTriggerLevelName(alarmLevelMap.get(category.getTriggerLevel().toString()));
				}
			}
			return list.stream().filter(item->item.getCategoryId() > 0 ).collect(Collectors.groupingBy(AlarmTriggerConfig::getVarSn));
		} catch (Exception exception) {
			log.error("查询变量报警配置异常", exception);
			return new HashMap<>();
		}
	}
}
