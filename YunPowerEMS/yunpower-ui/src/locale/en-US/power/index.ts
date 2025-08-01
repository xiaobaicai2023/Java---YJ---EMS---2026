import localDevice from './device'
import localMaintenance from  './maintenance'
import localAlarm from './alarm'
import localVar from './var'
export default {
	// 组件
	'power.energy.preview.day':'day',
	'power.energy.preview.month':'month',
	'power.energy.preview.year':'years',
	// 能耗概览
	'power.energy.preview': 'Energy consumption generalization',
	'power.energy.preview.active': 'Active power',
	'power.energy.preview.factor': 'Power factor',
	'power.energy.preview.load': 'Load rate',
	'power.energy.preview.today': 'Electricity used today',
	'power.energy.preview.thisMonth': 'This months power usage',
	'power.energy.preview.thisYear': 'This years electricity use',
	'power.energy.preview.communication': 'Site communication',
	// 用电能耗统计
	'power.energy.preview.peek': 'Electricity consumption statistics',
	// 负额和负载率
	'power.energy.preview.loadRate': 'Load and load rate',
	// 电流变化
	'power.energy.preview.electricity': 'Current change',
	// 电压变化
	'power.energy.preview.voltage': 'Voltage variation',
	// 有功功率
	'power.energy.preview.activePower': 'Active power',
	// 功率因素
	'power.energy.preview.powerFactor': 'Power factor',
	// 峰谷平用电统计
	'power.energy.preview.electricityStatistics': 'Peak valley flat electricity consumption statistics',

	// 事件警告
	'power.energy.preview.warning': 'Event warning',
	// 查看更多
	'power.energy.preview.more': 'See more',
	// 查询数据
	'power.energy.search.queryData': 'Query data',
	// 默认模板
	'power.energy.search.template':'Default template',
	// 山东省分时段标准
	'power.energy.analysis.standard': 'Shandong province time division standard',
	// 用电量环比
	'power.energy.analysis.contrast': 'Sequential electricity consumption',
	// 用电量明细
	'power.energy.analysis.detail': 'Detail of electricity consumption',
	// 添加分组
	'power.energy.device.addGroup': 'Add group',
	// 编辑分组
	'power.energy.device.editGroup': 'Edit group',
	// 设备名称
	'power.energy.device.name': 'Device name',
	// 变量名称
	'power.energy.device.varName': 'Variable name',
	// 添加设备
	'power.energy.device.addDevice': 'Add device',
	// 设备导出
	'power.energy.device.export': 'Device export',
	// 设备导入
	'power.energy.device.import': 'Device import',
	// 能源流向
	'power.energy.flow.title': 'Energy flow diagram',
	// 分类名称
	'power.energy.categoryName': 'Category Name',
	// 逻辑代码
	'power.energy.logicalCode': 'Logical code',
	// 分类状态
	'power.energy.classificationStatus': 'Classification status',

	...localDevice,
	...localMaintenance,
	...localAlarm,
	...localVar
}
