import axios from "axios";
import qs from "qs";

/**
 * 站点类型 （1配电 2光伏）
 */
export enum StationTypeEnum {
  /**
   * 配电
   */
  power = 1,
  /**
   * 光伏
   */
  pv = 2,
  /**
   * 用水
   */
  water = 3,
}

/**
 * 报表类型 （1普通 2峰谷）
 */
export enum ReportTypeEnum {
  /**
   * 普通
   */
  basic = 1,
  /**
   * 峰谷
   */
  pv = 2,
}

/**
 * 查询能源监控设备列表
 * @param params 查询参数
 * @returns 结果
 */
export function listDevice(params: any) {
  return axios.get("/system/device/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询能源监控设备列表
 * @param params 查询参数
 * @returns 结果
 */
export function listDeviceAll(params: any) {
  return axios.get("/system/device/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 查询能源监控设备详细
 * @param id 能源监控设备ID
 * @returns 结果
 */
export function getDevice(id: any) {
  return axios.get("/system/device/" + id);
}

/**
 * 新增能源监控设备
 * @param data 能源监控设备对象
 * @returns 0失败 1成功
 */
export function addDevice(data: any) {
  return axios.post("/system/device", data);
}

/**
 * 修改能源监控设备
 * @param data 能源监控设备对象
 * @returns 0失败 1成功
 */
export function updateDevice(data: any) {
  return axios.put("/system/device", data);
}

/**
 * 修改能源监控设备状态
 * @param data 能源监控设备对象
 * @returns 0失败 1成功
 */
export function changeDeviceStatus(id: any, state: any) {
  return axios.put(`/system/device/changeStatus/${id}/${state}`);
}

/**
 * 删除能源监控设备
 * @param id 能源监控设备ID
 * @returns 0失败 1成功
 */
export function delDevice(id: any) {
  return axios.delete("/system/device/" + id);
}

/**
 * 设备树
 * @param params 查询参数
 * @returns 结果
 */
export function listFusionGroup(params: any) {
  return axios.get("/system/device/listFusionGroup", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 光伏设备列表页动态Tab
 * @param params 查询参数
 * @returns 结果
 */
export function getPVTab() {
  return axios.get("/system/device/getPVTab");
}
