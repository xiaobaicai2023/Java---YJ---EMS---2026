package com.yunpower.collect.storage;

import cn.hutool.core.util.ObjectUtil;
import com.yunpower.collect.storage.algorithm.tool.RefreshActualDataNodesAO;
import com.yunpower.collect.storage.domain.*;
import com.yunpower.collect.storage.domain.jsonvo.SeasonalRangeVo;
import com.yunpower.collect.storage.service.*;
import com.yunpower.common.redis.service.RedisService;
import com.yunpower.mq.publisher.service.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StorageVariables {

	private static final Logger LOGGER = LoggerFactory.getLogger(StorageVariables.class);

	//通讯【通道】服务
	public static ICommunicationChannelService communicationChannelService;

	//用于保存通讯【通道列表】
	public static List<CommunicationChannel> GLOBAL_CHANNEL_LIST = new ArrayList<>();

	//【通讯设备】服务
	public static ICommunicationDeviceService communicationDeviceService;

	//用于保存【通讯设备列表】
	public static List<CommunicationDevice> GLOBAL_CHANNEL_DEVICE_LIST = new ArrayList<>();

	//使用频率较高，用于保存，格式：通道ID-公共地址:通讯设备ID
	public static Map<String, Long> GLOBAL_CHANNEL_GGDZ_DEVICE_MAP = new HashMap<>();
	//通讯设备列表-MAP
	public static Map<Long, CommunicationDevice> GLOBAL_CHANNEL_GGDZ_DEVICE_INFO_MAP = new HashMap<>();
	//【监控设备】服务
	public static IMonitorDeviceService monitorDeviceService;

	//用于保存【监控设备列表】
	public static Map<String, MonitorDevice> GLOBAL_MONITOR_DEVICE_MAP = new HashMap<>();

	//用于保存【变量】，格式==> 通讯设备ID:<信息体地址：变量编码>
	public static Map<Long, Map<Integer, MonitorDeviceVar>> GLOBAL_CHANNEL_DEVICE_STORAGE_VARIABLE = new HashMap<>();
	//用于保存监控设备变量
	public static Map<String, MonitorDeviceVar> GLOBAL_MONITOR_DEVICE_VAR_LIST_MAP = new HashMap<>();
	//【监控设备变量】服务
	public static IMonitorDeviceVarService monitorDeviceVarService;

	//月统计数据服务
	public static ISysCommonCreateService sysCommonCreateService;

	//日数据服务
	public static IDayDataService dayDataService;

	//月统计数据服务
	public static IMonthDataService monthDataService;

	//月累计数据服务
	public static IMonthAccumulateDataService accumulateDataService;

	//电价配置服务
	public static IElectricPriceSchemeConfigService schemeConfigService;

	//电站服务
	public static ISysStationService stationService;

	//存储部门ID和电价标准ID
	public static Map<Long, Long> GLOBAL_DEPTID_SCHEMEID_MAP = new HashMap<>();

	//存储部门ID和报警开关状态
	public static Map<Long, Boolean> GLOBAL_DEPTID_ALARM_SWITCH_MAP = new HashMap<>();

	//存储部门信息
	public static Map<Long, SysStation> GLOBAL_DEPT_MAP = new HashMap<>();

	//存储尖峰平谷时段电价，格式==> 电价标准ID-YearMonth：<时：（seasonalName, price）>
	public static Map<String, Map<Integer, SeasonalRangeVo>> GLOBAL_SEASONAL_RANGE_MAP = new HashMap<>();

	//redis服务
	public static RedisService redisService;

	//保存已注册通道列表
	public static Map<String, CommunicationChannel> REGISTER_LIST = new HashMap<>();

	public static String aliasName;

	//动态刷新分库分表节点配置
	public static RefreshActualDataNodesAO refreshActualDataNodesAO;

	//分库分表服务
	public static IShardingCommonService shardingCommonService;

	//MQ服务
	public static PublisherService publisherService;

	//报警配置服务
	public static IAlarmTriggerConfigService alarmTriggerConfigService;

	//报警事件服务
	public static IAlarmTriggerService alarmTriggerService;

	//存储变量对应的报警配置
	public static Map<String, List<AlarmTriggerConfig>> GLOBAL_DEVICE_VAR_ALARM_CONFIG_MAP = new HashMap<>();

	public static void loadData() {

		//加载通讯【通道】到内存
		GLOBAL_CHANNEL_LIST = communicationChannelService.selectCommunicationChannelList();

		//加载【通讯设备】到内存
		GLOBAL_CHANNEL_DEVICE_LIST = communicationDeviceService.selectCommunicationDeviceList();

		//加载【通道ID-公共地址:通讯设备ID】到内存
		GLOBAL_CHANNEL_GGDZ_DEVICE_MAP = packageChannelGgdz(GLOBAL_CHANNEL_DEVICE_LIST);

		//加载【监控设备】到内存
		packageMonitorDevice();

		//加载【变量】到内存
		GLOBAL_CHANNEL_DEVICE_STORAGE_VARIABLE = packageDeviceVariable();

		//加载【部门ID：电价标准ID】到内存
		packageDeptSchemeWithAlarmSwitch();

		//加载【电价时段配置】到内存
		GLOBAL_SEASONAL_RANGE_MAP = schemeConfigService.selectSeasonalRangeList();

		//加载【变量报警配置】到内存
		GLOBAL_DEVICE_VAR_ALARM_CONFIG_MAP = alarmTriggerConfigService.selectDeviceVarAlarmConfig();
	}


	/**
	 * 组装监控设备列表
	 * */
	private static void packageMonitorDevice() {

		Map<String, MonitorDevice> deviceMap = new HashMap<>();

		List<MonitorDevice> list = monitorDeviceService.selectMonitorDeviceList();
		list.forEach(item -> deviceMap.put(item.getDeviceSn(), item));

		GLOBAL_MONITOR_DEVICE_MAP = deviceMap;
	}

	/**
	 * 将数据封装成【通道ID-公共地址:通讯设备ID】格式
	 *
	 * @param list 通讯设备列表
	 * @return 结果
	 */
	public static Map<String, Long> packageChannelGgdz(List<CommunicationDevice> list) {
		Map<String, Long> result = new HashMap<>();
		Map<Long, CommunicationDevice> deviceMap = new HashMap<>();
		for (CommunicationDevice item : list) {
			result.put(item.getChannelId() + "-" + item.getGgdz(), item.getId());
			deviceMap.put(item.getId(), item);
		}
		GLOBAL_CHANNEL_GGDZ_DEVICE_INFO_MAP = deviceMap;
		return result;
	}

	/**
	 * 组装变量
	 */
	public static Map<Long, Map<Integer, MonitorDeviceVar>> packageDeviceVariable() {
		Map<Long, Map<Integer, MonitorDeviceVar>> result = new HashMap<>();
		Map<String, MonitorDeviceVar> varMap = new HashMap<>();
		Map<Integer, MonitorDeviceVar> temp;

		for (CommunicationDevice item : GLOBAL_CHANNEL_DEVICE_LIST) {
			List<MonitorDeviceVar> deviceVarList = monitorDeviceVarService.selectMonitorDeviceVarListByChannelDevice(item.getId());
			temp = new HashMap<>();
			for (MonitorDeviceVar var : deviceVarList) {
				varMap.put(var.getVarSn(), var);
				if (var.getMessageAddress() == null || var.getMessageAddress() <= 0) {
					continue;
				}
				temp.put(var.getMessageAddress(), var);
			}
			result.put(item.getId(), temp);
		}
		GLOBAL_MONITOR_DEVICE_VAR_LIST_MAP = varMap;
		return result;
	}


	/**
	 * 组装部门电价和报警开关
	 */
	public static void packageDeptSchemeWithAlarmSwitch() {
		List<SysStation> list = stationService.selectStationList();
		Integer openAlarm = 1;
		Map<Long, Long> schemeMap = new HashMap<>();
		Map<Long, Boolean> alarmMap = new HashMap<>();
		Map<Long, SysStation> stationMap = new HashMap<>();
		for (SysStation item : list) {
			if (ObjectUtil.isNotNull(item.getSchemeId()) && item.getSchemeId() > 0L) {
				schemeMap.put(item.getDeptId(), item.getSchemeId());
			}
			alarmMap.put(item.getDeptId(), openAlarm.equals(item.getOpenAlarm()));
			stationMap.put(item.getDeptId(), item);
		}
		//部门电价
		GLOBAL_DEPTID_SCHEMEID_MAP = schemeMap;
		//部门报警开关
		GLOBAL_DEPTID_ALARM_SWITCH_MAP = alarmMap;
		//站点信息
		GLOBAL_DEPT_MAP = stationMap;
	}
}
