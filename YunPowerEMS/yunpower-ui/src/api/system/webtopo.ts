import axios from "axios";
import qs from "qs";

/**
 * 查询接线图列表
 * @param params 查询参数
 * @returns 结果
 */
export function listWebtopoProject(params: any) {
  return axios.get("/system/webtopo/project/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询接线图详细
 * @param id 接线图ID
 * @returns 结果
 */
export function getWebtopoProject(id: any) {
  return axios.get("/system/webtopo/project/" + id);
}

/**
 * 新增接线图
 * @param data 接线图对象
 * @returns 0失败 1成功
 */
export function addWebtopoProject(data: any) {
  return axios.post("/system/webtopo/project", data);
}

/**
 * 修改接线图
 * @param data 接线图对象
 * @returns 0失败 1成功
 */
export function updateWebtopoProject(data: any) {
  return axios.put("/system/webtopo/project", data);
}

/**
 * 删除接线图
 * @param id 接线图ID
 * @returns 0失败 1成功
 */
export function delWebtopoProject(id: any) {
  return axios.delete("/system/webtopo/project/" + id);
}
