import localeMessageBox from '@/components/message-box/locale/zh-CN';
import localeLogin from '@/views/login/locale/zh-CN';

import localeSettings from './zh-CN/settings';
import localePower from './zh-CN/power/index';
import localeDiagram from './zh-CN/diagram';
import localeMonitor from './zh-CN/monitor';
import localePv from './zh-CN/pv'
import localeMaintenance from './zh-CN/maintenance'
import localeManage from './zh-CN/manage';
export default {
	'menu.dashboard': '仪表盘',
	'menu.server.dashboard': '仪表盘-服务端',
	'menu.server.workplace': '工作台-服务端',
	'menu.server.monitor': '实时监控-服务端',
	'menu.list': '列表页',
	'menu.result': '结果页',
	'menu.exception': '异常页',
	'menu.form': '表单页',
	'menu.profile': '详情页',
	'menu.visualization': '数据可视化',
	'menu.user': '个人中心',
	'menu.arcoWebsite': 'Arco Design',
	'menu.faq': '常见问题',
	'navbar.docs': '文档中心',
	'navbar.action.locale': '切换为中文',
	'global.loading': '加载中...',
	'global.save': '保存',
	// 查询
	'global.search': '查询',
	// 重置
	'global.reset': '重置',
	// 确认
	'global.confirm': '确认',
	// 取消
	'global.cancel': '取消',
	// 新建
	'global.new': '新建',
	// 启用状态
	'global.status': '启用状态',
	// 启用
	'global.enable': '启用',
	// 停用
	'global.forbidden': '停用',
	// 编辑
	'global.edit': '编辑',
	// 详情
	'global.detail': '详情',
	// 删除
	'global.delete': '删除',
	// 移除
	'global.remove': '移除',
	// 展开
	'global.expand': '展开',
	// 确认提示
	'global.confirmTip': '确认提示',
	// 折叠
	'global.fold': '折叠',
	//请选择
	'global.pleaseSelect': '请选择',
	//设备列表
	'global.device': '设备列表',
	// 支持模糊查询
	'global.fuzzySearch': '支持模糊查询',
	// 分组名称
	'global.groupName': '分组名称',
	// 上级目录
	'global.parentDirectory': '上级目录',
	// 显示顺序
	'global.displayOrder': '显示顺序',
	// 设计
	'global.design': '设计',
	// 强退
	'global.forceOut': '强退',
	// 批量删除
	'global.batchDeletion': '批量删除',
	// 清空
	'global.empty': '清空',
	// 请输入
	'global.please': '请输入',
	// 显示
	'global.reveal': '显示',
	// 隐藏
	'global.hidden': '隐藏',
	// 正常
	'global.normal': '正常',
	// 停用
	'global.outOfService': '停用',
	// 全选
	'global.checkAll': '全选',
	// 全不选
	'global.checkOutAll': '全不选',
	// 显示顺序
	'global.displaySequence': '显示顺序',
	// 是
	'global.yes': '是',
	// 否
	'global.no': '否',
	// 备注说明
	'global.remarks': '备注说明',
	// 开启
	'global.on': '开启',
	// 关闭
	'global.off': '关闭',
	// 能源类型
	'global.energy': '能源类型',

	...localeSettings,
	...localeMessageBox,
	...localeLogin,
	...localePower,
	...localeDiagram,
	...localeMonitor,
	...localePv,
	...localeMaintenance,
	...localeManage
}
