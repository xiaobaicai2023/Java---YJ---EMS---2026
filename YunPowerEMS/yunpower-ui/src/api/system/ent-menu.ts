import axios from "axios";
import qs from "qs";

/**
 * 查询企业专属菜单列表
 * @param params 查询参数
 * @returns 结果
 */
export function listEntMenu(params: any) {
  return axios.get("/system/ent-menu/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询企业专属菜单详细
 * @param id 企业专属菜单ID
 * @returns 结果
 */
export function getEntMenu(id: any) {
  return axios.get("/system/ent-menu/" + id);
}

/**
 * 新增企业专属菜单
 * @param data 企业专属菜单对象
 * @returns 0失败 1成功
 */
export function addEntMenu(data: any) {
  return axios.post("/system/ent-menu", data);
}

/**
 * 修改企业专属菜单
 * @param data 企业专属菜单对象
 * @returns 0失败 1成功
 */
export function updateEntMenu(data: any) {
  return axios.put("/system/ent-menu", data);
}

/**
 * 删除企业专属菜单
 * @param id 企业专属菜单ID
 * @returns 0失败 1成功
 */
export function delEntMenu(id: any) {
  return axios.delete("/system/ent-menu/" + id);
}
