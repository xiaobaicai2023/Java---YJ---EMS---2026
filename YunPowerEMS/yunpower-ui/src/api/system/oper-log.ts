import axios from "axios";
import qs from "qs";


/**
 * 查询操作日志列表
 * @param params 查询参数
 * @returns 结果
 */
export function listOperLog(params: any) {
  return axios.get("/system/operlog/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询操作日志详细
 * @param id 操作日志ID
 * @returns 结果
 */
export function getOperLog(id: any) {
  return axios.get("/system/operlog/" + id);
}

/**
 * 新增操作日志
 * @param data 操作日志对象
 * @returns 0失败 1成功
 */
export function addOperLog(data: any) {
  return axios.post("/system/operlog", data);
}

/**
 * 修改操作日志
 * @param data 操作日志对象
 * @returns 0失败 1成功
 */
export function updateOperLog(data: any) {
  return axios.put("/system/operlog", data);
}

/**
 * 删除操作日志
 * @param id 操作日志ID
 * @returns 0失败 1成功
 */
export function delOperLog(id: any) {
  return axios.delete("/system/operlog/"+ id);
}

/**
 * 清空操作日志
 * @returns 0失败 1成功
 */
export function cleanOperLog() {
  return axios.delete("/system/operlog/clean");
}
