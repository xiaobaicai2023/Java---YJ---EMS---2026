import axios from "axios";
import qs from "qs";

/**
 * 查询留言回复管理列表
 * @param params 查询参数
 * @returns 结果
 */
export function listLeaveMessage(params?:any) {
  return axios.get("/system/leave-message/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询留言回复管理详细
 * @param id 留言回复管理ID
 * @returns 结果
 */
export function getLeaveMessage(id: any) {
  return axios.get("/system/leave-message/" + id);
}

/**
 * 新增留言回复管理
 * @param data 留言回复管理对象
 * @returns 0失败 1成功
 */
export function addLeaveMessage(data: any) {
  return axios.post("/system/leave-message", data);
}

/**
 * 修改留言回复管理
 * @param data 留言回复管理对象
 * @returns 0失败 1成功
 */
export function updateLeaveMessage(data: any) {
  return axios.put("/system/leave-message", data);
}

/**
 * 删除留言回复管理
 * @param id 留言回复管理ID
 * @returns 0失败 1成功
 */
export function delLeaveMessage(id: any) {
  return axios.delete("/system/leave-message/" + id);
}
