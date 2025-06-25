import axios from "axios";
import qs from "qs";

/**
 * 查询企业信息列表
 * @param params 查询参数
 * @returns 结果
 */
export function listEnterprise(params: any) {
  return axios.get("/system/enterprise/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}


/**
 * 查询企业信息列表
 * @param params 查询参数
 * @returns 结果
 */
export function listEnterpriseAll() {
  return axios.get("/system/enterprise/listAll");
}

/**
 * 查询企业信息详细
 * @param id 企业信息ID
 * @returns 结果
 */
export function getEnterprise(id: any) {
  return axios.get("/system/enterprise/" + id);
}

/**
 * 新增企业信息
 * @param data 企业信息对象
 * @returns 0失败 1成功
 */
export function addEnterprise(data: any) {
  return axios.post("/system/enterprise", data);
}

/**
 * 修改企业信息
 * @param data 企业信息对象
 * @returns 0失败 1成功
 */
export function updateEnterprise(data: any) {
  return axios.put("/system/enterprise", data);
}

/**
 * 删除企业信息
 * @param id 企业信息ID
 * @returns 0失败 1成功
 */
export function delEnterprise(id: any) {
  return axios.delete("/system/enterprise/" + id);
}
