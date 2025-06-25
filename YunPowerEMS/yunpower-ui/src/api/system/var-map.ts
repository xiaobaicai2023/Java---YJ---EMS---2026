import axios from "axios";
import qs from "qs";

/**
 * 查询监控设备变量索引地图列表
 * @param params 查询参数
 * @returns 结果
 */
export function listVarMap(params: any) {
  return axios.get("/system/var-map/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询监控设备变量索引地图列表-不分页
 * @param params 查询参数
 * @returns 结果
 */
export function listVarMapAll(params: any) {
  return axios.get("/system/var-map/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询监控设备变量索引地图详细
 * @param id 监控设备变量索引地图ID
 * @returns 结果
 */
export function getVarMap(id: any) {
  return axios.get("/system/var-map/" + id);
}

/**
 * 新增监控设备变量索引地图
 * @param data 监控设备变量索引地图对象
 * @returns 0失败 1成功
 */
export function addVarMap(data: any) {
  return axios.post("/system/var-map", data);
}

/**
 * 修改监控设备变量索引地图
 * @param data 监控设备变量索引地图对象
 * @returns 0失败 1成功
 */
export function updateVarMap(data: any) {
  return axios.put("/system/var-map", data);
}

/**
 * 删除监控设备变量索引地图
 * @param id 监控设备变量索引地图ID
 * @returns 0失败 1成功
 */
export function delVarMap(id: any) {
  return axios.delete("/system/var-map/" + id);
}

/**
 * 修改监控设备变量状态
 * @param data 监控设备变量对象
 * @returns 0失败 1成功
 */
export function changeVarStatus(id: any, state: any) {
  return axios.put(`/system/var-map/changeStatus/${id}/${state}`);
}
