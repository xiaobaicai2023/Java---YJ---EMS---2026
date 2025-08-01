import axios from "axios";
import qs from "qs";

/**
 * 查询消息列表
 * @param params 查询参数
 * @returns 结果
 */
export function listMessage(params: any) {
  return axios.get("/system/message/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询消息详细
 * @param id 消息ID
 * @returns 结果
 */
export function getMessage(id: any) {
  return axios.get("/system/message/" + id);
}

/**
 * 新增消息
 * @param data 消息对象
 * @returns 0失败 1成功
 */
export function addMessage(data: any) {
  return axios.post("/system/message", data);
}

/**
 * 修改消息
 * @param data 消息对象
 * @returns 0失败 1成功
 */
export function updateMessage(data: any) {
  return axios.put("/system/message", data);
}

/**
 * 删除消息
 * @param id 消息ID
 * @returns 0失败 1成功
 */
export function delMessage(id: any) {
  return axios.delete("/system/message/" + id);
}

/**
 * 删除消息
 * @param id 消息ID
 * @returns 0失败 1成功
 */
export function delMessageIds(ids: any) {
  return axios.delete("/system/message/"+ids.join(","));
}

/**
 * 清空消息
 * @returns 0失败 1成功
 */
export function cleanMessage() {
  return axios.delete("/system/message/clean");
}
