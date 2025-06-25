import axios from "axios";
import qs from "qs";

/**
 * 获取大屏配置详细信息-通过站点id查询
 * @param params 查询参数
 */
export function getBiInfo({stationId,isPre}: {stationId: string,isPre: string}) {
	return axios.get(`/datav/common/special/getBiConfig/${stationId}/${isPre}`);
}
/**
 * 获取大屏配置列表
 * @returns 结果
 * @param params 查询参数
 */
export function getConfigList(params: any) {
	return axios.get(`/system/dashboard/config/list`,{
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		}
	});
}

/**
 * 新建大屏配置
 * @param data 新增配置对象
 * @returns 结果
 */
export function addConfigList(data: any) {
	return axios.post("/system/dashboard/config", data);
}
/**
 * 修改大屏配置
 * @data 新增配置对象
 * @returns 结果
 */
export function editConfigList(data: any) {
	return axios.put("/system/dashboard/config", data);
}
/**
 * 修改配置状态
 * @data 修改配置参数
 * @returns 结果
 */
export function updateConfigStatus(data:any) {
	return axios.put(`/system/dashboard/config/changeStatus/${data.id}/${data.state}`);
}

/**
 * 删除大屏配置
 * @id 删除配置参数
 * @returns 结果
 */
export function deleteConfigList(id: any) {
	return axios.delete("/system/dashboard/config/"+id);
}

/**
 * 获取大屏配置详情
 * @param 查询配置参数
 * @returns 结果
 */
export function getConfigDetail(params: any) {
	return axios.get(`/system/dashboard/config/${params.pageKey}`);
}

/**
 * 获取卡片列表
 * @param 查询参数
 * @returns 结果
 */
export function getCardList(params: any) {
	return axios.get(`/system/dashboard/card/config/listAll`,{
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		}
	});
}


