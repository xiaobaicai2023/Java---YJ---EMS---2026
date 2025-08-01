import axios from "axios";
import qs from "qs";

/**
 * 电站概览【电站】基础数据
 * @param params 查询参数
 * @returns 结果
 */
export function getStationInfo(params: any) {
  return axios.get("/datav/pv/chart/getStationInfo", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 电站概览【电站】发电实时
 * @param params 查询参数
 * @returns 结果
 */
export function getRuntimeInfo(params: any) {
  return axios.get("/datav/pv/chart/getRuntimeInfo", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 电站概览【电站】发电历史-头部
 * @param params 查询参数
 * @returns 结果
 */
export function getHistoryHeadInfo(electric:any) {
  return axios.get(`/datav/pv/chart/getHistoryHeadInfo/${electric}`);
}
 
/**
 * 电站概览【电站】发电历史-右侧
 * @param params 查询参数
 * @returns 结果
 */
export function getHistoryRightInfo(electric:any) {
  return axios.get(`/datav/pv/chart/getHistoryRightInfo/${electric}`);
}
