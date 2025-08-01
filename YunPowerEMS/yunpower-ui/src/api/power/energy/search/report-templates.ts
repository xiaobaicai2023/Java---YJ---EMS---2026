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
 * 获取报表模版详细信息
 * @param id
 */
export function getReportTemplateDetail(id:number): any {
	return axios.get(`/system/report-templates/${id}`)
}
