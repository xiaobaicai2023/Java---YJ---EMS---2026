import axios from "axios";
import qs from "qs";

/**
 * 查询通讯设备数据区域列表
 * @param params 查询参数
 * @returns 结果
 */
export function listChannelDeviceArea(params: any) {
  return axios.get("/system/channel-device-area/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯设备数据区域详细
 * @param id 通讯设备数据区域ID
 * @returns 结果
 */
export function getChannelDeviceArea(id: any) {
  return axios.get("/system/channel-device-area/" + id);
}

/**
 * 新增通讯设备数据区域
 * @param data 通讯设备数据区域对象
 * @returns 0失败 1成功
 */
export function addChannelDeviceArea(data: any) {
  return axios.post("/system/channel-device-area", data);
}

/**
 * 修改通讯设备数据区域
 * @param data 通讯设备数据区域对象
 * @returns 0失败 1成功
 */
export function updateChannelDeviceArea(data: any) {
  return axios.put("/system/channel-device-area", data);
}

/**
 * 删除通讯设备数据区域
 * @param id 通讯设备数据区域ID
 * @returns 0失败 1成功
 */
export function delChannelDeviceArea(id: any) {
  return axios.delete("/system/channel-device-area/" + id);
}
