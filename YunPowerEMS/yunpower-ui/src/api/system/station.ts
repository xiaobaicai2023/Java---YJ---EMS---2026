import axios from "axios";
import qs from "qs";

/**
 * 查询电站列表
 * @param params 查询参数
 * @returns 结果
 */
export function listStation(params: any) {
  return axios.get("/system/station/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询电站列表
 * @param params 查询参数
 * @returns 结果
 */
export function listStationAll(params: any) {
  return axios.get("/system/station/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询电站详细
 * @param id 电站ID
 * @returns 结果
 */
export function getStation(id: any) {
  return axios.get("/system/station/" + id);
}

/**
 * 新增电站
 * @param data 电站对象
 * @returns 0失败 1成功
 */
export function addStation(data: any) {
  return axios.post("/system/station", data);
}

/**
 * 修改电站
 * @param data 电站对象
 * @returns 0失败 1成功
 */
export function updateStation(data: any) {
  return axios.put("/system/station", data);
}

/**
 * 删除电站
 * @param id 电站ID
 * @returns 0失败 1成功
 */
export function delStation(id: any) {
  return axios.delete("/system/station/" + id);
}

/**
 * 修改电站状态
 * @param id  id
 * @param data 状态： 0-启用 1-禁用
 * @returns 0失败 1成功
 */
export function changeStationStatus(id:any,state: any) {
  return axios.put(`/system/station/changeStatus/${id}/${state}`);
}


// 
/**
 * 获取电站报警开关
 * @param id 电站ID
 * @returns 结果
 */
export function getAlarmStatus() {
  return axios.get("/system/station/getAlarmStatus");
}

/**
 * 修改电站报警状态
 * @param data 状态： 0-启用 1-禁用
 * @returns 0失败 1成功
 */
export function changeAlarmStatus(state: any) {
  return axios.put(`/system/station/changeAlarmStatus/${state}`);
}
