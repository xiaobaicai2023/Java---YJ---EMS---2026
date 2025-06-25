import axios from "axios";
import qs from "qs";

/**
 * 基础报表列表
 */
export function getBasicTable(params?:any): any {
	return axios.get("/datav/common/table/getBasicTable",{
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		},
	})
}

/**
 * @desc 查询报表模版列表
 */
export function getTemplateData(params?:any): any {
	return axios.get("/system/report-templates/listAll",{
		params,
		paramsSerializer: (obj) => {
			return qs.stringify(obj);
		},
	})
}
/**
 * 新增报表模版
 * @param data
 */
export function addReportTemplates(data:any): any {
	return axios.post("/system/report-templates", data)
}

/**
 * @param data
 */
export function editReportTemplates(data:any): any {
	return axios.put('/system/report-templates',data)
}
/**
 * 修改报表模版状态
 * @param id
 * @param state
 */
export function editReportTemplatesStatus({id,state}:any): any {
	return axios.put(`/system/report-templates/changeStatus/${id}/${state}`)
}
/**
 * 获取报表模版详细信息
 * @param id
 */
export function getReportTemplateDetail(id:number): any {
	return axios.get(`/system/report-templates/${id}`)
}
/**
 * 删除
 * @param ids
 */
export function deleteReportTemplates(ids:number|string): any {
	return axios.delete(`/system/report-templates/${ids}`)
}
