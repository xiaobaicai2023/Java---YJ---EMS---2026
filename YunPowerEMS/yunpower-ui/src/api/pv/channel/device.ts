import axios from "axios";
import qs from "qs";

/**
 * 查询通讯设备列表
 * @param params 查询参数
 * @returns 结果
 */
export function listChannelDevice(params: any) {
  return axios.get("/system/channel-device/list", {
    params,
    paramsSerializer: (obj:any) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯设备列表-所有数据
 * @param params 查询参数
 * @returns 结果
 */
export function listChannelDeviceAll(params: any) {
  return axios.get("/system/channel-device/listAll", {
    params,
    paramsSerializer: (obj:any) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯设备详细
 * @param id 通讯设备ID
 * @returns 结果
 */
export function getChannelDevice(id: any) {
  return axios.get("/system/channel-device/" + id);
}
