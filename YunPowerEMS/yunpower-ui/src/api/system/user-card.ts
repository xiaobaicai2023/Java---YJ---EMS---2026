import axios from "axios";
import qs from "qs";

/**
 * 查询用户充值卡列表
 * @param params 查询参数
 * @returns 结果
 */
export function listUserCard(params: any) {
  return axios.get("/system/user-card/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询用户充值卡详细
 * @param id 用户充值卡ID
 * @returns 结果
 */
export function getUserCard(id: any) {
  return axios.get("/system/user-card/" + id);
}

/**
 * 新增用户充值卡
 * @param data 用户充值卡对象
 * @returns 0失败 1成功
 */
export function addUserCard(data: any) {
  return axios.post("/system/user-card", data);
}

/**
 * 修改用户充值卡
 * @param data 用户充值卡对象
 * @returns 0失败 1成功
 */
export function updateUserCard(data: any) {
  return axios.put("/system/user-card", data);
}

/**
 * 删除用户充值卡
 * @param id 用户充值卡ID
 * @returns 0失败 1成功
 */
export function delUserCard(id: any) {
  return axios.delete("/system/user-card/" + id);
}
