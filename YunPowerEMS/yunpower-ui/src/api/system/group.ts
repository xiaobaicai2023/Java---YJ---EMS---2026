import axios from "axios";
import qs from "qs";

/**
 * 查询常用分组列表
 * @param params 查询参数
 * @returns 结果
 */
export function listGroup(params: any) {
  return axios.get("/system/group/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询常用分组列表-不分页
 * @param params 查询参数
 * @returns 结果
 */
export function listGroupAll(params: any) {
  return axios.get("/system/group/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}


/**
 * 查询常用分组【下拉】列表（不分页）
 * @param params 查询参数
 * @returns 结果
 */
export function listSelect(params: any) {
  return axios.get("/system/group/listSelect", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询常用分组详细
 * @param id 常用分组ID
 * @returns 结果
 */
export function getGroup(id: any) {
  return axios.get("/system/group/" + id);
}

/**
 * 新增常用分组
 * @param data 常用分组对象
 * @returns 0失败 1成功
 */
export function addGroup(data: any) {
  return axios.post("/system/group", data);
}

/**
 * 修改常用分组
 * @param data 常用分组对象
 * @returns 0失败 1成功
 */
export function updateGroup(data: any) {
  return axios.put("/system/group", data);
}

/**
 * 删除常用分组
 * @param id 常用分组ID
 * @returns 0失败 1成功
 */
export function delGroup(id: any) {
  return axios.delete("/system/group/" + id);
}
