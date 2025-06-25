import axios from "axios";
import qs from "qs";

/**
 * 查询通用字典-数据列表
 * @param params 查询参数
 * @returns 结果
 */
export function listData(params: any) {
  return axios.get("/system/data/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通用字典-数据详细
 * @param id 通用字典-数据ID
 * @returns 结果
 */
export function getData(id: any) {
  return axios.get("/system/data/" + id);
}

/**
 * 新增通用字典-数据
 * @param data 通用字典-数据对象
 * @returns 0失败 1成功
 */
export function addData(data: any) {
  return axios.post("/system/data", data);
}

/**
 * 修改通用字典-数据
 * @param data 通用字典-数据对象
 * @returns 0失败 1成功
 */
export function updateData(data: any) {
  return axios.put("/system/data", data);
}

/**
 * 删除通用字典-数据
 * @param id 通用字典-数据ID
 * @returns 0失败 1成功
 */
export function delData(id: any) {
  return axios.delete("/system/data/" + id);
}
