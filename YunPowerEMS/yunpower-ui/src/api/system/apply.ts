import axios from "axios";
import qs from "qs";

/**
 * 查询第三方接入申请列表
 * @param params 查询参数
 * @returns 结果
 */
export function listApply(params: any) {
  return axios.get("/system/apply/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询第三方接入申请详细
 * @param id 第三方接入申请ID
 * @returns 结果
 */
export function getApply(id: any) {
  return axios.get("/system/apply/" + id);
}

/**
 * 新增第三方接入申请
 * @param data 第三方接入申请对象
 * @returns 0失败 1成功
 */
export function addApply(data: any) {
  return axios.post("/system/apply", data);
}

/**
 * 修改第三方接入申请
 * @param data 第三方接入申请对象
 * @returns 0失败 1成功
 */
export function updateApply(data: any) {
  return axios.put("/system/apply", data);
}

/**
 * 删除第三方接入申请
 * @param id 第三方接入申请ID
 * @returns 0失败 1成功
 */
export function delApply(id: any) {
  return axios.delete("/system/apply/" + id);
}
