import axios from "axios";
import qs from "qs";

/**
 * 查询报警配置列表
 * @param params 查询参数
 * @returns 结果
 */
export function listTriggerConfig(params: any) {
  return axios.get("/system/trigger-config/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询报警配置详细
 * @param id 报警配置ID
 * @returns 结果
 */
export function getTriggerConfig(id: any) {
  return axios.get("/system/trigger-config/" + id);
}

/**
 * 新增报警配置
 * @param data 报警配置对象
 * @returns 0失败 1成功
 */
export function addTriggerConfig(data: any) {
  return axios.post("/system/trigger-config", data);
}

/**
 * 修改报警配置
 * @param data 报警配置对象
 * @returns 0失败 1成功
 */
export function updateTriggerConfig(data: any) {
  return axios.put("/system/trigger-config", data);
}

/**
 * 删除报警配置
 * @param id 报警配置ID
 * @returns 0失败 1成功
 */
export function delTriggerConfig(id: any) {
  return axios.delete("/system/trigger-config/" + id);
}

/**
 * 修改报警配置状态
 * @param data 报警配置对象
 * @returns 0失败 1成功
 */
export function changeTriggerConfigStatus(id:any,state:any) {
  return axios.put(`/system/trigger-config/changeStatus/${id}/${state}`);
}
