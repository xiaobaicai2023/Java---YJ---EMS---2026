import axios from "axios";
import qs from "qs";

/**
 * 查询消息模板列表
 * @param params 查询参数
 * @returns 结果
 */
export function listMessageTemplate(params: any) {
  return axios.get("/system/message-template/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询消息模板详细
 * @param id 消息模板ID
 * @returns 结果
 */
export function getMessageTemplate(id: any) {
  return axios.get("/system/message-template/" + id);
}

/**
 * 新增消息模板
 * @param data 消息模板对象
 * @returns 0失败 1成功
 */
export function addMessageTemplate(data: any) {
  return axios.post("/system/message-template", data);
}

/**
 * 修改消息模板
 * @param data 消息模板对象
 * @returns 0失败 1成功
 */
export function updateMessageTemplate(data: any) {
  return axios.put("/system/message-template", data);
}


/**
 * 修改消息模板状态
 * @param id id
 * @param state 状态
 * @returns 0失败 1成功
 */
export function changeMessageTemplateStatus(id: any,state:any) {
  return axios.put(`/system/message-template/changeStatus/${id}/${state}`);
}

/**
 * 删除消息模板
 * @param id 消息模板ID
 * @returns 0失败 1成功
 */
export function delMessageTemplate(id: any) {
  return axios.delete("/system/message-template/" + id);
}
