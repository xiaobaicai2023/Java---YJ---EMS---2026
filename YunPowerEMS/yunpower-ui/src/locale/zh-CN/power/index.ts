import localDevice from './device'
import localMaintenance from  './maintenance'
import localAlarm from './alarm'
import localVar from './var'
export default {
	// 组件
	'power.energy.preview.day':'按日',
	'power.energy.preview.month':'按月',
	'power.energy.preview.year':'按年',
	// 能耗概览
	'power.energy.preview': '能耗概览',
	'power.energy.preview.active': '有功功率',
	'power.energy.preview.factor': '功率因素',
	'power.energy.preview.load': '负载率',
	'power.energy.preview.today': '今日用电量',
	'power.energy.preview.thisMonth': '本月用电量',
	'power.energy.preview.thisYear': '今年用电量',
	'power.energy.preview.communication': '站点通讯',
	// 用电能耗统计
	'power.energy.preview.peek': '用电能耗统计',
	// 负额和负载率
	'power.energy.preview.loadRate': '负额和负载率',
	// 电流变化
	'power.energy.preview.electricity': '电流变化',
	// 电压变化
	'power.energy.preview.voltage': '电压变化',
	// 有功功率
	'power.energy.preview.activePower': '有功功率',
	// 功率因素
	'power.energy.preview.powerFactor': '功率因素',
	// 峰谷平用电统计
	'power.energy.preview.electricityStatistics': '峰谷平用电统计',
	// 事件警告
	'power.energy.preview.warning': '事件警告',
	// 查看更多
	'power.energy.preview.more': '查看更多',
	// 查询数据
	'power.energy.search.queryData': '查询数据',
	// 默认模板
	'power.energy.search.template':'默认模板',
	// 山东省分时段标准
	'power.energy.analysis.standard': '山东省分时段标准',
	// 用电量环比
	'power.energy.analysis.contrast': '用电量环比',
	// 用电量明细
	'power.energy.analysis.detail': '用电量明细',
	// 添加分组
	'power.energy.device.addGroup': '添加分组',
	// 编辑分组
	'power.energy.device.editGroup': '编辑分组',
	// 设备名称
	'power.energy.device.name': '设备名称',
	// 变量名称
	'power.energy.device.varName': '变量名称',
	// 添加设备
	'power.energy.device.addDevice': '添加设备',
	// 设备导出
	'power.energy.device.export': '设备导出',
	// 设备导入
	'power.energy.device.import': '设备导入',
	// 能源流向
	'power.energy.flow.title': '能源流向图',
	// 分类名称
	'power.energy.categoryName': '分类名称',
	// 逻辑代码
	'power.energy.logicalCode': '逻辑代码',
	// 分类状态
	'power.energy.classificationStatus': '分类状态',

	...localDevice,
	...localMaintenance,
	...localAlarm,
	...localVar
}
