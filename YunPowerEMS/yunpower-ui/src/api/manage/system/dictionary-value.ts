import axios from "axios";
import qs from "qs";

/**
 * 查询字典标签列表
 * @param params 查询参数
 * @returns 结果
 */
export function listDictData(params: any) {
  return axios.get("/system/dict/data/list", {
    params,
    paramsSerializer: (obj:any) => {
      return qs.stringify(obj);
    }
  });
}
/**
 * 根据字典类型查询字典数据信息
 * @param dictType 
 * @returns 
 */
export function listDictDataForDictType(dictType:string){
  return axios.get(`/system/dict/data/type/${dictType}`)
}
/**
 * 查询字典标签详细
 * @param id 字典标签ID
 * @returns 结果
 */
export function getDictData(id: any) {
  return axios.get("/system/dict/data/" + id);
}

/**
 * 新增字典标签
 * @param data 字典标签对象
 * @returns 0失败 1成功
 */
export function addDictData(data: any) {
  return axios.post("/system/dict/data", data);
}

/**
 * 修改字典标签
 * @param data 字典标签对象
 * @returns 0失败 1成功
 */
export function updateDictData(data: any) {
  return axios.put("/system/dict/data", data);
}

/**
 * 删除字典标签
 * @param id 字典标签ID
 * @returns 0失败 1成功
 */
export function delDictData(id: any) {
  return axios.delete("/system/dict/data/" + id);
}

/**
 * 修改字典参数
 * @param id 
 * @param state 
 * @returns 0失败 1成功
 */
export function changeDictDataStatus(id: any,state:any) {
  return axios.put(`/system/dict/data/changeStatus/${id}/${state}`);
}
