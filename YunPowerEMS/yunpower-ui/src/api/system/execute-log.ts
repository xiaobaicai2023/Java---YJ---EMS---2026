import axios from "axios";
import qs from "qs";


/**
 * 查询执行日志列表
 * @param params 查询参数
 * @returns 结果
 */
export function listExecuteLog(params: any) {
  return axios.get("/system/executelog/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询执行日志详细
 * @param id 执行日志ID
 * @returns 结果
 */
export function getExecuteLog(id: any) {
  return axios.get("/system/executelog/" + id);
}

/**
 * 新增执行日志
 * @param data 执行日志对象
 * @returns 0失败 1成功
 */
export function addExecuteLog(data: any) {
  return axios.post("/system/executelog", data);
}

/**
 * 修改执行日志
 * @param data 执行日志对象
 * @returns 0失败 1成功
 */
export function updateExecuteLog(data: any) {
  return axios.put("/system/executelog", data);
}

/**
 * 删除执行日志
 * @param id 执行日志ID
 * @returns 0失败 1成功
 */
export function delExecuteLog(id: any) {
  return axios.delete("/system/executelog/"+ id);
}

/**
 * 清空执行日志
 * @returns 0失败 1成功
 */
export function cleanExecuteLog() {
  return axios.delete("/system/executelog/clean");
}
