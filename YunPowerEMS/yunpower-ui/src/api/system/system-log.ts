import axios from "axios";
import qs from "qs";


/**
 * 查询系统日志列表
 * @param params 查询参数
 * @returns 结果
 */
export function listSystemLog(params: any) {
  return axios.get("/system/systemlog/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询系统日志详细
 * @param id 系统日志ID
 * @returns 结果
 */
export function getSystemLog(id: any) {
  return axios.get("/system/systemlog/" + id);
}

/**
 * 新增系统日志
 * @param data 系统日志对象
 * @returns 0失败 1成功
 */
export function addSystemLog(data: any) {
  return axios.post("/system/systemlog", data);
}

/**
 * 修改系统日志
 * @param data 系统日志对象
 * @returns 0失败 1成功
 */
export function updateSystemLog(data: any) {
  return axios.put("/system/systemlog", data);
}

/**
 * 删除系统日志
 * @param id 系统日志ID
 * @returns 0失败 1成功
 */
export function delSystemLog(id: any) {
  return axios.delete("/system/systemlog/"+ id);
}

/**
 * 清空系统日志
 * @returns 0失败 1成功
 */
export function cleanSystemLog() {
  return axios.delete("/system/systemlog/clean");
}
