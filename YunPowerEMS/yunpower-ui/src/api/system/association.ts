import axios from "axios";
import qs from "qs";

/**
 * 查询能源监控设备关联列表
 * @param params 查询参数
 * @returns 结果
 */
export function listAssociation(params: any) {
  return axios.get("/system/association/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询能源监控设备关联详细
 * @param id 能源监控设备关联ID
 * @returns 结果
 */
export function getAssociation(id: any) {
  return axios.get("/system/association/" + id);
}

/**
 * 新增能源监控设备关联
 * @param data 能源监控设备关联对象
 * @returns 0失败 1成功
 */
export function addAssociation(data: any) {
  return axios.post("/system/association", data);
}

/**
 * 修改能源监控设备关联
 * @param data 能源监控设备关联对象
 * @returns 0失败 1成功
 */
export function updateAssociation(data: any) {
  return axios.put("/system/association", data);
}

/**
 * 删除能源监控设备关联
 * @param id 能源监控设备关联ID
 * @returns 0失败 1成功
 */
export function delAssociation(id: any) {
  return axios.delete("/system/association/" + id);
}
