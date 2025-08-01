import axios from "axios";
import qs from "qs";

/**
 * 查询通用字典列表
 * @param params 查询参数
 * @returns 结果
 */
export function listDict(params: any) {
  return axios.get("/system/dict/type/list", {
    params,
    paramsSerializer: (obj:any) => {
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
  return axios.get("/system/dict/type/" + id);
}

/**
 * 新增通用字典
 * @param data 通用字典对象
 * @returns 0失败 1成功
 */
export function addDict(data: any) {
  return axios.post("/system/dict/type", data);
}

/**
 * 修改通用字典
 * @param data 通用字典对象
 * @returns 0失败 1成功
 */
export function updateDict(data: any) {
  return axios.put("/system/dict/type", data);
}

/**
 * 删除通用字典
 * @param id 通用 字典ID
 * @returns 0失败 1成功
 */
export function delDict(id: any) {
  return axios.delete("/system/dict/type/" + id);
}
/**
 * 获取字典参数下拉项
 * @returns {}
 */
export function optionselect(){
  return axios.get("/system/dict/type/optionselect")
}

/**
 * 修改字典参数
 * @param id 
 * @param state 
 * @returns 0失败 1成功
 */
export function changeDictStatus(id: any,state:any) {
  return axios.put(`/system/dict/type/changeStatus/${id}/${state}`);
}
