import axios from "axios";
import qs from "qs";

/**
 * 获取数据接口列表
 * @param params 搜索参数
 * @returns
 */
export function listInterface(params: any) {
  return axios.get("/system/apply-interface/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

// 查询数据接口详细
export function getInterface(menuId:any) {
  return axios.get('/system/apply-interface/' + menuId);
}

/**
 * 新增数据接口
 * @param data 
 * @returns 
 */
export function addInterface(data:any) {
  return axios.post("/system/apply-interface",data);
}

/**
 * 修改数据接口
 * @param data 数据接口数据
 * @returns 
 */
export function updateInterface(data:any) {
  return axios.put("/system/apply-interface",data);
}

/**
 * 删除数据接口
 * @param menuId 数据接口ID
 * @returns 
 */
export function delInterface(menuId:any) {
  return axios.delete('/system/apply-interface/' + menuId);
}
