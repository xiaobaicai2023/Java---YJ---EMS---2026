import axios from "axios";
import qs from "qs";

/**
 * 查询监控设备变量列表
 * @param params 查询参数
 * @returns 结果
 */
export function listDeviceVar(params: any) {
  return axios.get("/system/device-var/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询监控设备变量列表-不分页
 * @param params 查询参数
 * @returns 结果
 */
export function listDeviceVarAll(params: any) {
  return axios.get("/system/device-var/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询监控设备变量详细
 * @param id 监控设备变量ID
 * @returns 结果
 */
export function getDeviceVar(id: any) {
  return axios.get("/system/device-var/" + id);
}

/**
 * 新增监控设备变量
 * @param data 监控设备变量对象
 * @returns 0失败 1成功
 */
export function addDeviceVar(data: any) {
  return axios.post("/system/device-var", data);
}

/**
 * 修改监控设备变量
 * @param data 监控设备变量对象
 * @returns 0失败 1成功
 */
export function updateDeviceVar(data: any) {
  return axios.put("/system/device-var", data);
}

/**
 * 修改监控设备变量状态
 * @param data 监控设备变量对象
 * @returns 0失败 1成功
 */
export function changeDeviceVarStatus(id: any, state: any) {
  return axios.put(`/system/device-var/changeStatus/${id}/${state}`);
}

/**
 * 删除监控设备变量
 * @param id 监控设备变量ID
 * @returns 0失败 1成功
 */
export function delDeviceVar(id: any) {
  return axios.delete("/system/device-var/" + id);
}

/**
 * 修改监控设备变量【监控状态】
 * @param id 监控设备变量ID
 * @returns 0失败 1成功
 */
export function changeMonitorStatus(id: any, state: any) {
  return axios.put(`/system/device-var/changeMonitorStatus/${id}/${state}`);
}

/**
 * 实时数据
 * @param params 查询参数
 * @returns 结果
 */
export function getConfigRunTimeList(deviceSn: string, stationType: any) {
  let params = {
    deviceSn: deviceSn,
    stationType: stationType,
  };
  return axios.get("/system/device-var/configRunTimeList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 实时数据
 * @param params 查询参数
 * @returns 结果
 */
export function getRunTimeList(deviceSn: string) {
  let params = {
    deviceSn: deviceSn,
  };
  return axios.get("/system/device-var/runTimeList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
