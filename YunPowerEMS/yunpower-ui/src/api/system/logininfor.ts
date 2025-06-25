import axios from "axios";
import qs from "qs";

/**
 * 查询登录日志列表
 * @param params 查询参数
 * @returns 结果
 */
export function listLogininfor(params: any) {
  return axios.get("/system/logininfor/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询登录日志详细
 * @param id 登录日志ID
 * @returns 结果
 */
export function getLogininfor(id: any) {
  return axios.get("/system/logininfor/" + id);
}

/**
 * 新增登录日志
 * @param data 登录日志对象
 * @returns 0失败 1成功
 */
export function addLogininfor(data: any) {
  return axios.post("/system/logininfor", data);
}

/**
 * 修改登录日志
 * @param data 登录日志对象
 * @returns 0失败 1成功
 */
export function updateLogininfor(data: any) {
  return axios.put("/system/logininfor", data);
}

/**
 * 删除登录日志
 * @param id 登录日志ID
 * @returns 0失败 1成功
 */
export function delLogininfor(ids: any) {
  return axios.delete("/system/logininfor/"+ids.join(","));
}

/**
 * 清空登录日志
 * @returns 0失败 1成功
 */
export function cleanLogininfor() {
  return axios.delete("/system/logininfor/clean");
}
