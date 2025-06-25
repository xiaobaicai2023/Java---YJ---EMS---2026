import axios from "axios";
import qs from "qs";

/**
 * 查询报警管理列表
 * @param params 查询参数
 * @returns 结果
 */
export function listTrigger(params: any) {
  return axios.get("/system/trigger/list", {
    params,
    paramsSerializer: (obj: any) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询报警管理详细
 * @param id 报警管理ID
 * @returns 结果
 */
export function getTrigger(id: any) {
  return axios.get("/system/trigger/" + id);
}

/**
 * 新增报警管理
 * @param data 报警管理对象
 * @returns 0失败 1成功
 */
export function addTrigger(data: any) {
  return axios.post("/system/trigger", data);
}

/**
 * 修改报警管理
 * @param data 报警管理对象
 * @returns 0失败 1成功
 */
export function updateTrigger(data: any) {
  return axios.put("/system/trigger", data);
}

/**
 * 删除报警管理
 * @param id 报警管理ID
 * @returns 0失败 1成功
 */
export function delTrigger(id: any) {
  return axios.delete("/system/trigger/" + id);
}
