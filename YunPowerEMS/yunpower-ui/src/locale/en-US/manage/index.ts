import accountLocal from './account';
import logsLocal from './logs';
import messageLocal from './message';
import noticeLocal from './notice';
import interfaceLocal from './interface';
import companyLocal from './company';
import channelLocal from './channel';
import systemLocal from './system'
import stationLocal from './station';

export default {
	...accountLocal, //账号管理
	...logsLocal, //日志管理
	...messageLocal, //消息管理
	...noticeLocal, //通知公告
	...interfaceLocal, //接口管理
	...companyLocal, //公司管理
	...channelLocal, //通道管理
	...stationLocal, //站点管理
	...systemLocal, //系统管理
	
	// 企业名称
	'manage.system.enterprise': 'Enterprise name',
	// 请输入企业名称，支持模糊查询
	'manage.system.placeholder': 'Please enter the enterprise name, support fuzzy query',
	// 新建企业
	'manage.system.newEnterprise': 'New enterprise',
	// 添加企业信息
	'manage.system.addEnterprise': 'Add enterprise information',
	// 编辑企业信息
	'manage.system.editEnterprise': 'Edit enterprise information',

	// 参数解释
	'manage.system.parameter': 'Parameter interpretation',
	// 请输入KEY
	'manage.system.key': 'Please enter KEY',
	// 新建参数
	'manage.system.newParameter': 'New parameter',

	// 字典名称
	'manage.system.dictionaryName': 'Dictionary name',
	// 新建字典
	'manage.system.newDictionary': 'New dictionary',
	// 值
	'manage.system.value': ' Value',
	//列表
	'manage.system.list': 'List',
	// 字典标签
	'manage.system.dictionaryLabel': 'Dictionary label',

	// 站点
	'manage.system.site': ' Site',
	// 站点名称
	'manage.system.siteName': 'Site name',
	// 请输入
	'manage.system.pleaseInput': 'Please enter',
	// 请选择
	'manage.system.pleaseSelect': 'Please select',
	// 站点类型
	'manage.system.siteType': 'Site type',


}
