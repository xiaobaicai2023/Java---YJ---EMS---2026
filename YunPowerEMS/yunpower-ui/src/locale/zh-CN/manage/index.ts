import accountLocal from './account';
import logsLocal from './logs';
import messageLocal from './message';
import noticeLocal from './notice';
import interfaceLocal from './interface';
import companyLocal from './company';
import channelLocal from './channel';
import stationLocal from './station';
import systemLocal from './system';

export default {
	...accountLocal, //账号管理
	...logsLocal, //日志管理
	...messageLocal, //消息管理
	...noticeLocal, //通知公告
	...interfaceLocal, //接口管理
	...companyLocal, //公司管理
	...channelLocal, //通道管理
	...stationLocal, //站点管理
	...systemLocal, // 系统管理

	// 企业名称
	'manage.system.enterprise': '企业名称',
	// 请输入企业名称，支持模糊查询
	'manage.system.placeholder': '请输入企业名称，支持模糊查询',
	// 新建企业
	'manage.system.newEnterprise': '新建企业',
	// 添加企业信息
	'manage.system.addEnterprise': '添加企业信息',
	// 编辑企业信息
	'manage.system.editEnterprise': '编辑企业信息',

	// 参数解释
	'manage.system.parameter': '参数解释',
	// 请输入KEY
	'manage.system.key': '请输入KEY名称，支持模糊查询',
	// 新建参数
	'manage.system.newParameter': '新建参数',

	// 字典名称
	'manage.system.dictionaryName': '字典名称',
	// 新建字典
	'manage.system.newDictionary': '新建字典',
	// 值
	'manage.system.value': '值',
	//列表
	'manage.system.list': '列表',
	// 字典标签
	'manage.system.dictionaryLabel': '字典标签',

	// 站点
	'manage.system.site': '站点',
	// 站点名称
	'manage.system.siteName': '站点名称',
	// 请输入站点名称，支持模糊查询
	'manage.system.pleaseInput': '请输入站点名称，支持模糊查询',
	// 请选择
	'manage.system.pleaseSelect': '请选择',
	// 站点类型
	'manage.system.siteType': '站点类型',

}
