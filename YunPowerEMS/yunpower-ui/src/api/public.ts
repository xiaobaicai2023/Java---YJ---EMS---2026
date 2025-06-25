import axios, { AxiosRequestConfig } from "axios";
import qs from "qs";

export interface JsonCommonVo {
  name: string;
  value: string;
}

/**
 * 获取字典
 * @param dictType 字典类型
 * @returns
 */
export function getDict(dictType: string) {
  return axios.get<any>(`/system/dict/data/type/${dictType}`);
}

/**
 * 获取字典-list
 * @param dictType 字典类型
 * @returns
 */
export function getDictList(dictType: string) {
  return axios.get<any>(`/system/dict/data/list?dictSn=${dictType}`);
}

/**
 * 根据字典类型、字典值查询多级信息
 * @param dictType 字典类型
 * @returns
 */
export function dictDataMultiLevel(dictType: string, dictValue: number) {
  return axios.get<any>(
    `/system/dict/data/dictDataMultiLevel?dictType=${dictType}&dictValue=${dictValue}`
  );
}

/**
 * 获取站点列表
 * @param params 搜索参数
 * @returns
 */
export function getStationList(params: any) {
  return axios.get("/system/public/getStationlist", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 获取用户当前企业
 * @returns
 */
export function getEnterprise() {
  return axios.get<any>("/system/public/getEnterprise");
}

/**
 * 获取用户当前站点
 * @returns
 */
export function getStation() {
  return axios.get<any>("/system/public/getStation");
}

/**
 * 保存用户选择的站点
 * @returns
 */
export function setStation(deptId: any) {
  const formData = new FormData();
  formData.append("deptId", deptId);
  return axios.post<any>("/system/public/setStation", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

/**
 * 保存用户选择的站点
 * @returns
 */
export function getWeather(location: String) {
  let params: any = {
    location: location,
    key: "ede0270be04c4adf9834d7b92cb312c2",
  };
  return axios.get<any>("/system/public/getWeather")
  // return axios.get(`https://devapi.qweather.com/v7/weather/now?location=${params.location}&key=${params.key}`);
}

/**
 * 保存用户选择的站点
 * @returns
 */
export function getNetwork(deptId: Number) {
  return axios.get<any>(
      `/system/channel-device/status/all/${deptId}`
  );
}