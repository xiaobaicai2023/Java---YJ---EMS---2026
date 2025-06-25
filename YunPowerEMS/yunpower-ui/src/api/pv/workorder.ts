import axios from "axios";
import qs from "qs";

/**
 * 查询工单管理列表
 * @param params 查询参数
 * @returns 结果
 */
export function listOrder(params: any) {
  return axios.get("/system/order/list", {
    params,
    paramsSerializer: (obj:any) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询工单管理详细
 * @param id 工单管理ID
 * @returns 结果
 */
export function getOrder(id: any) {
  return axios.get("/system/order/" + id);
}

/**
 * 新增工单管理
 * @param data 工单管理对象
 * @returns 0失败 1成功
 */
export function addOrder(data: any) {
  return axios.post("/system/order", data);
}

/**
 * 修改工单管理
 * @param data 工单管理对象
 * @returns 0失败 1成功
 */
export function updateOrder(data: any) {
  return axios.put("/system/order", data);
}

/**
 * 删除工单管理
 * @param id 工单管理ID
 * @returns 0失败 1成功
 */
export function delOrder(id: any) {
  return axios.delete("/system/order/" + id);
}
/**
 * 获取用户站点列表信息
 * @returns 
 */
export function getStation(params:any) {
  return axios.get("/system/station/list",params);
}
/**
 * 获取报警下拉信息
 * @param params 
 * @returns 
 */
export function getTrigger() {
  return axios.get("/system/trigger/list");
}
/**
 * 获取个人信息
 * @returns 
 */
export function profile() {
  return axios.get("/system/user/profile");
}
