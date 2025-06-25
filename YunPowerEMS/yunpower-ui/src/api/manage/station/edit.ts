import axios from "axios";
import qs from "qs";

/**
 * 获取卡片列表
 * @param 查询参数
 * @returns 结果
 */
export function getCardList(params: any) {
	return axios.get(`/system/dashboard/card/config/list`,{
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		}
	});
}

/**
 * 保存卡片信息
 * @param data 查询参数
 * @returns 结果
 */
export function updateConfigCard(data: any) {
	return axios.put("/system/dashboard/card/config", data);
}

/**
 * 获取卡片信息
 * @param data 查询参数
 * @returns 结果
 */
export function getConfigCard(data) {
	return axios.get(`/system/dashboard/card/config/${data.dashboardConfigId}/${data.cardKey}`);
}

/**
 * 删除卡片
 * @id 删除配置参数
 * @returns 结果
 */
export function deleteConfigList(id: any) {
	return axios.delete("/system/dashboard/card/config/"+id);
}

/**
 * 修改配置状态
 * @data 修改配置参数
 * @returns 结果
 */
export function updateConfigStatus(data:any) {
	return axios.put(`/system/dashboard/card/config/changeStatus/${data.id}/${data.state}`);
}
