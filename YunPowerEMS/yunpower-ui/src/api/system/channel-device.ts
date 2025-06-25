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
    paramsSerializer: (obj) => {
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

/**
 * 新增通讯设备
 * @param data 通讯设备对象
 * @returns 0失败 1成功
 */
export function addChannelDevice(data: any) {
  return axios.post("/system/channel-device", data);
}

/**
 * 修改通讯设备
 * @param data 通讯设备对象
 * @returns 0失败 1成功
 */
export function updateChannelDevice(data: any) {
  return axios.put("/system/channel-device", data);
}

/**
 * 删除通讯设备
 * @param id 通讯设备ID
 * @returns 0失败 1成功
 */
export function delChannelDevice(id: any) {
  return axios.delete("/system/channel-device/" + id);
}

/**
 * 修改通讯设备状态
 * @param data 通讯设备对象
 * @returns 0失败 1成功
 */
export function changeChannelDeviceStatus(id: any,state:any) {
  return axios.put(`/system/channel-device/changeStatus/${id}/${state}`);
}
