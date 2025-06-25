import axios from "axios";
import qs from "qs";

/**
 * 查询公司列表
 * @param params 查询参数
 * @returns 结果
 */
export function listCompany(params: any) {
  return axios.get("/system/company/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询公司列表
 * @param params 查询参数
 * @returns 结果
 */
export function listCompanyAll(params: any) {
  return axios.get("/system/company/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询公司详细
 * @param id 公司ID
 * @returns 结果
 */
export function getCompany(id: any) {
  return axios.get("/system/company/" + id);
}

/**
 * 新增公司
 * @param data 公司对象
 * @returns 0失败 1成功
 */
export function addCompany(data: any) {
  return axios.post("/system/company", data);
}

/**
 * 修改公司
 * @param data 公司对象
 * @returns 0失败 1成功
 */
export function updateCompany(data: any) {
  return axios.put("/system/company", data);
}

/**
 * 删除公司
 * @param id 公司ID
 * @returns 0失败 1成功
 */
export function delCompany(id: any) {
  return axios.delete("/system/company/" + id);
}

/**
 * 修改公司状态
 * @param data 公司对象
 * @returns 0失败 1成功
 */
export function changeCompanyStatus(id: any,state:any) {
  return axios.put(`/system/company/changeStatus/${id}/${state}`);
}
