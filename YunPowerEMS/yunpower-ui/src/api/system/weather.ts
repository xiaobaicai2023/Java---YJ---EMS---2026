import axios from "axios";
import qs from "qs";

/**
 * 查询通用天气数据列表
 * @param params 查询参数
 * @returns 结果
 */
export function listWeather(params: any) {
  return axios.get("/system/weather/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询通用天气数据详细
 * @param id 通用天气数据ID
 * @returns 结果
 */
export function getWeather(id: any) {
  return axios.get("/system/weather/" + id);
}

/**
 * 新增通用天气数据
 * @param data 通用天气数据对象
 * @returns 0失败 1成功
 */
export function addWeather(data: any) {
  return axios.post("/system/weather", data);
}

/**
 * 修改通用天气数据
 * @param data 通用天气数据对象
 * @returns 0失败 1成功
 */
export function updateWeather(data: any) {
  return axios.put("/system/weather", data);
}

/**
 * 删除通用天气数据
 * @param id 通用天气数据ID
 * @returns 0失败 1成功
 */
export function delWeather(id: any) {
  return axios.delete("/system/weather/" + id);
}
