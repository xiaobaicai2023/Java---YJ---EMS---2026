import axios from "axios";
import qs from "qs";

/**
 * 查询通讯通道列表
 * @param params 查询参数
 * @returns 结果
 */
export function listChannel(params: any) {
  return axios.get("/system/channel/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯通道列表-所有的
 * @param params 查询参数
 * @returns 结果
 */
export function listChannelAll(params: any) {
  return axios.get("/system/channel/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通讯通道详细
 * @param id 通讯通道ID
 * @returns 结果
 */
export function getChannel(id: any) {
  return axios.get("/system/channel/" + id);
}

/**
 * 新增通讯通道
 * @param data 通讯通道对象
 * @returns 0失败 1成功
 */
export function addChannel(data: any) {
  return axios.post("/system/channel", data);
}

/**
 * 修改通讯通道
 * @param data 通讯通道对象
 * @returns 0失败 1成功
 */
export function updateChannel(data: any) {
  return axios.put("/system/channel", data);
}

/**
 * 修改通讯通道状态
 * @param data 通讯通道对象
 * @returns 0失败 1成功
 */
export function changeChannelStatus(id: any,state:any) {
  return axios.put(`/system/channel/changeStatus/${id}/${state}`);
}
/**
 * 删除通讯通道
 * @param id 通讯通道ID
 * @returns 0失败 1成功
 */
export function delChannel(id: any) {
  return axios.delete("/system/channel/" + id);
}
