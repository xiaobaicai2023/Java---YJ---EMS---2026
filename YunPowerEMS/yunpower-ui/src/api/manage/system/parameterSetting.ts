import axios from "axios";
import qs from "qs";

/**
 * 查询参数列表
 * @param params 查询参数
 * @returns 结果
 */
export function listConfig(params: any) {
  return axios.get("/system/config/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询参数详细
 * @param id 参数ID
 * @returns 结果
 */
export function getConfig(id: any) {
  return axios.get("/system/config/" + id);
}

/**
 * 新增参数
 * @param data 参数对象
 * @returns 0失败 1成功
 */
export function addConfig(data: any) {
  return axios.post("/system/config", data);
}

/**
 * 修改参数
 * @param data 参数对象
 * @returns 0失败 1成功
 */
export function updateConfig(data: any) {
  return axios.put("/system/config", data);
}

/**
 * 删除参数
 * @param id 参数ID
 * @returns 0失败 1成功
 */
export function delConfig(id: any) {
  return axios.delete("/system/config/" + id);
}
