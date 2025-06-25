import axios from "axios";
import qs from "qs";

/**
 * 查询全国四级行政区列表
 * @param params 查询参数
 * @returns 结果
 */
export function listAddress(params: any) {
  return axios.get("/system/address/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询全国四级行政区列表
 * @param params 查询参数
 * @returns 结果
 */
export function listAddressAll(params: any) {
  return axios.get("/system/address/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询全国四级行政区详细
 * @param id 全国四级行政区ID
 * @returns 结果
 */
export function getAddress(id: any) {
  return axios.get("/system/address/" + id);
}

/**
 * 查询全国四级行政区详细
 * @param id 全国四级行政区ID
 * @returns 结果
 */
export function getAddressNameByIds(ids: any) {
  return axios.get(`/system/address/getNames/${ids.join(',')}`);
}

/**
 * 新增全国四级行政区
 * @param data 全国四级行政区对象
 * @returns 0失败 1成功
 */
export function addAddress(data: any) {
  return axios.post("/system/address", data);
}

/**
 * 修改全国四级行政区
 * @param data 全国四级行政区对象
 * @returns 0失败 1成功
 */
export function updateAddress(data: any) {
  return axios.put("/system/address", data);
}

/**
 * 删除全国四级行政区
 * @param id 全国四级行政区ID
 * @returns 0失败 1成功
 */
export function delAddress(id: any) {
  return axios.delete("/system/address/" + id);
}
