import axios from "axios";
import qs from "qs";

/**
 * 获取通知公告列表
 * @param params 查询参数
 * @returns 结果
 */
export function listNotice(params: any) {
  return axios.get("/system/notice/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询公告详细
 * @param id 公告ID
 * @returns 结果
 */
export function getNotice(id: any) {
  return axios.get("/system/notice/" + id);
}

/**
 * 新增公告
 * @param data 公告对象
 * @returns 0失败 1成功
 */
export function addNotice(data: any) {
  return axios.post("/system/notice", data);
}

/**
 * 修改全国四级行政区
 * @param data 公告对象
 * @returns 0失败 1成功
 */
export function updateNotice(data: any) {
  return axios.put("/system/notice", data);
}

/**
 * 删除公告
 * @param id 公告ID
 * @returns 0失败 1成功
 */
export function delNotice(id: any) {
  return axios.delete("/system/notice/" + id);
}

/**
 * 修改公告状态
 * @param id id
 * @param state 状态
 * @returns 0失败 1成功
 */
export function changeNoticeStatus(id: any,state:any) {
  return axios.put(`/system/notice/changeStatus/${id}/${state}`);
}
