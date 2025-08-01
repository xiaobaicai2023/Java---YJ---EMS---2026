import axios from "axios";
import qs from "qs";

/**
 * 查询数据接口列表
 * @param params 查询参数
 * @returns 结果
 */
export function listApplyInterface(params: any) {
  return axios.get("/system/apply-interface/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询数据接口详细
 * @param id 数据接口ID
 * @returns 结果
 */
export function getApplyInterface(id: any) {
  return axios.get("/system/apply-interface/" + id);
}

/**
 * 新增数据接口
 * @param data 数据接口对象
 * @returns 0失败 1成功
 */
export function addApplyInterface(data: any) {
  return axios.post("/system/apply-interface", data);
}

/**
 * 修改数据接口
 * @param data 数据接口对象
 * @returns 0失败 1成功
 */
export function updateApplyInterface(data: any) {
  return axios.put("/system/apply-interface", data);
}

/**
 * 删除数据接口
 * @param id 数据接口ID
 * @returns 0失败 1成功
 */
export function delApplyInterface(id: any) {
  return axios.delete("/system/apply-interface/" + id);
}
