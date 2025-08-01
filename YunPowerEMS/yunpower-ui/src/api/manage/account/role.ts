import axios from "axios";
import qs from "qs";

/**
 * 获取角色列表
 * @param params 搜索参数
 * @returns
 */
export function listRole(params: any) {
  return axios.get("/system/role/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 获取角色列表
 * @param params 搜索参数
 * @returns
 */
export function listRoleAll(params: any) {
  return axios.get("/system/role/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

// 查询角色详细
export function getRole(roleId: any) {
  return axios.get("/system/role/" + roleId);
}

/**
 * 新增角色
 * @param data
 * @returns
 */
export function addRole(data: any) {
  return axios.post("/system/role", data);
}

/**
 * 修改角色
 * @param data 角色数据
 * @returns
 */
export function updateRole(data: any) {
  return axios.put("/system/role", data);
}

/**
 * 删除角色
 * @param roleId 角色ID
 * @returns
 */
export function delRole(roleId: any) {
  return axios.delete("/system/role/" + roleId);
}

/**
 * 角色数据权限
 * @param data
 * @returns
 */
export function dataScope(data: any) {
  return axios.put("/system/role/dataScope", data);
}

/**
 * 角色状态修改
 * @param roleId 角色ID
 * @param status 状态
 * @returns
 */
export function changeRoleStatus(roleId: any, status: any) {
  const data = {
    roleId,
    status,
  };
  return axios.put(`/system/role/changeStatus/${roleId}/${status}`);
}

/**
 * 查询角色已授权用户列表
 * @param params 查询参数
 * @returns
 */
export function allocatedUserList(params: any) {
  return axios.get("/system/role/authUser/allocatedList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询角色未授权用户列表
 * @param params 参数
 * @returns
 */
export function unallocatedUserList(params: any) {
  return axios.get("/system/role/authUser/unallocatedList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 取消用户授权角色
 * @param data 参数
 * @returns
 */
export function authUserCancel(data: any) {
  return axios.put("/system/role/authUser/cancel", data);
}

/**
 * 批量取消用户授权角色
 * @param data 参数
 * @returns
 */
export function authUserCancelAll(data: any) {
  return axios.put("/system/role/authUser/cancelAll", data);
}

/**
 * 授权用户选择
 * @param data 参数
 * @returns
 */
export function authUserSelectAll(data: any) {
  return axios.put("/system/role/authUser/selectAll", data);
}

/**
 * 根据角色ID查询部门树结构
 * @param roleId 角色ID
 * @returns
 */
export function deptTreeSelect(roleId: any) {
  return axios.get("/system/role/deptTree/" + roleId);
}
