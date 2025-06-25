import axios from "axios";
import qs from "qs";

/**
 * 查询通讯设备数据区域类型（公共）列表
 * @param params 查询参数
 * @returns 结果
 */
export function listAreaMap(params: any) {
  return axios.get("/system/area-map/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯设备数据区域类型（公共）列表-所有的
 * @param params 查询参数
 * @returns 结果
 */
export function listAreaMapAll(params: any) {
  return axios.get("/system/area-map/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯设备数据区域类型（公共）详细
 * @param id 通讯设备数据区域类型（公共）ID
 * @returns 结果
 */
export function getAreaMap(id: any) {
  return axios.get("/system/area-map/" + id);
}

/**
 * 新增通讯设备数据区域类型（公共）
 * @param data 通讯设备数据区域类型（公共）对象
 * @returns 0失败 1成功
 */
export function addAreaMap(data: any) {
  return axios.post("/system/area-map", data);
}

/**
 * 修改通讯设备数据区域类型（公共）
 * @param data 通讯设备数据区域类型（公共）对象
 * @returns 0失败 1成功
 */
export function updateAreaMap(data: any) {
  return axios.put("/system/area-map", data);
}

/**
 * 删除通讯设备数据区域类型（公共）
 * @param id 通讯设备数据区域类型（公共）ID
 * @returns 0失败 1成功
 */
export function delAreaMap(id: any) {
  return axios.delete("/system/area-map/" + id);
}
