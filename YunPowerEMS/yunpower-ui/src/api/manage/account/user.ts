import axios from "axios";
import qs from "qs";

/**
 * 获取用户列表-分页
 * @param params
 * @returns
 */
export function listUser(params: any): any {
  return axios.get("/system/user/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 获取用户列表-不分页
 * @param params
 * @returns
 */
export function listUserAll(params: any): any {
  return axios.get("/system/user/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询用户详细
 * @param userId 用户ID
 * @returns
 */
export function getUser(userId: any) {
  return axios.get("/system/user/" + userId);
}

/**
 * 新增用户
 * @param data 操作日志对象
 * @returns 0失败 1成功
 */
export function addUser(data: any) {
  return axios.post("/system/user", data);
}

/**
 * 修改用户
 * @param data 操作日志对象
 * @returns 0失败 1成功
 */
export function updateUser(data: any) {
  return axios.put("/system/user", data);
}

/**
 * 修改用户密码
 * @param data 操作日志对象
 * @returns 0失败 1成功
 */
export function resetPwd(data: any) {
  return axios.put("/system/user/resetPwd", data);
}

/**
 * 修改用户状态
 * @param id
 * @param state 
 * @returns 0失败 1成功
 */
export function changeUserStatus(id: any, state: any) {
  return axios.put(`/system/user/changeStatus/${id}/${state}`);
}

/**
 * 删除用户
 * @param id 操作日志ID
 * @returns 0失败 1成功
 */
export function delUser(id: any) {
  return axios.delete("/system/user/" + id);
}
