import axios from "axios";
import qs from "qs";

/**
 * 查询通用字典列表
 * @param params 查询参数
 * @returns 结果
 */
export function listDict(params: any) {
  return axios.get("/system/dict/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通用字典详细
 * @param id 通用字典ID
 * @returns 结果
 */
export function getDict(id: any) {
  return axios.get("/system/dict/" + id);
}

/**
 * 新增通用字典
 * @param data 通用字典对象
 * @returns 0失败 1成功
 */
export function addDict(data: any) {
  return axios.post("/system/dict", data);
}

/**
 * 修改通用字典
 * @param data 通用字典对象
 * @returns 0失败 1成功
 */
export function updateDict(data: any) {
  return axios.put("/system/dict", data);
}

/**
 * 删除通用字典
 * @param id 通用字典ID
 * @returns 0失败 1成功
 */
export function delDict(id: any) {
  return axios.delete("/system/dict/" + id);
}
