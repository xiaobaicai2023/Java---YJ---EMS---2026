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
    paramsSerializer: (obj:any) => {
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
