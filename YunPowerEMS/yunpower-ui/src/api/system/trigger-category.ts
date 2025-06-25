import axios from "axios";
import qs from "qs";

/**
 * 查询报警类型列表
 * @param params 查询参数
 * @returns 结果
 */
export function listTriggerCategory(params: any) {
  return axios.get("/system/trigger-category/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询报警类型列表-不分页
 * @param params 查询参数
 * @returns 结果
 */
export function listTriggerCategoryAll(params: any) {
  return axios.get("/system/trigger-category/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}


/**
 * 查询报警类型详细
 * @param id 报警类型ID
 * @returns 结果
 */
export function getTriggerCategory(id: any) {
  return axios.get("/system/trigger-category/" + id);
}

/**
 * 新增报警类型
 * @param data 报警类型对象
 * @returns 0失败 1成功
 */
export function addTriggerCategory(data: any) {
  return axios.post("/system/trigger-category", data);
}

/**
 * 修改报警类型
 * @param data 报警类型对象
 * @returns 0失败 1成功
 */
export function updateTriggerCategory(data: any) {
  return axios.put("/system/trigger-category", data);
}
/**
 * 修改报警类型状态
 * @param data 报警类型对象
 * @returns 0失败 1成功
 */
export function changeTriggerCategoryStatus(id:any,state:any) {
  return axios.put(`/system/trigger-category/changeStatus/${id}/${state}`);
}

/**
 * 删除报警类型
 * @param id 报警类型ID
 * @returns 0失败 1成功
 */
export function delTriggerCategory(id: any) {
  return axios.delete("/system/trigger-category/" + id);
}
