import axios from "axios";
import qs from "qs";
import {LocationQueryValue} from "vue-router";

/**
 * 获取卡片配置布局
 * @param pageKey 查询参数
 * @returns 结果
 */
export function getCardConfig(pageKey: string) {
	return axios.get(`/system/dashboard/config/${pageKey}`, {
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		}
	});
}

/**
 * 新增卡片配置布局
 * @param data 查询参数
 * @returns 结果
 */
export function updateDashboardConfig(data: any) {
	return axios.put("/system/dashboard/config", data);
}

/**
 * 获取卡片信息
 * @param data 查询参数
 * @returns 结果
 */
export function getConfigCard(data: any) {
	// let {dashboardConfigId, cardKey} =data
	// console.log(dashboardConfigId, cardKey)
	return axios.get(`/system/dashboard/card/config/${data.dashboardConfigId}/${data.cardKey}`);
}

/**
 * 获取仪表盘配置详细信息
 * @param id
 */
export function getConfigInfo(id: string | null) {
	return axios.get(`/system/dashboard/config/getInfo/${id}`);
}

/**
 * 获取图表详情
 */
interface ChartInfo {
	configId: number, //配置ID
	cardKey: number | string, //卡片key
	deviceSn?: string, //设备编号
	dayTime?: string, //天日期
	monthTime?: string, //月日期
	yearTime?: string //年日期
}

export function getChartInfo(params: ChartInfo) {
	return axios.get('/datav/common/chart/getChart', {
		params,
		paramsSerializer: (params) => {
			return qs.stringify(params);
		}
	});
}
