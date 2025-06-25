import axios from "axios";
import qs from "qs";

/**
 * 查询电价标准列表
 * @param params 查询参数
 * @returns 结果
 */
export function listPriceScheme(params: any) {
  return axios.get("/system/price-scheme/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询电价标准列表
 * @param params 查询参数
 * @returns 结果
 */
export function listPriceSchemeAll(params: any) {
  return axios.get("/system/price-scheme/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询电价标准详细
 * @param id 电价标准ID
 * @returns 结果
 */
export function getPriceScheme(id: any) {
  return axios.get("/system/price-scheme/" + id);
}

/**
 * 新增电价标准
 * @param data 电价标准对象
 * @returns 0失败 1成功
 */
export function addPriceScheme(data: any) {
  return axios.post("/system/price-scheme", data);
}

/**
 * 修改电价标准
 * @param data 电价标准对象
 * @returns 0失败 1成功
 */
export function updatePriceScheme(data: any) {
  return axios.put("/system/price-scheme", data);
}

/**
 * 修改电价标准状态
 * @param id id
 * @param state 状态
 * @returns 0失败 1成功
 */
export function changePriceSchemeStatus(id: any,state:any) {
  return axios.put(`/system/price-scheme/changeStatus/${id}/${state}`);
}

/**
 * 删除电价标准
 * @param id 电价标准ID
 * @returns 0失败 1成功
 */
export function delPriceScheme(id: any) {
  return axios.delete("/system/price-scheme/" + id);
}


/**
 * 通过电价标准ID重新计算所有的分时电价
 * @param id 电价标准ID
 * @returns 结果
 */
export function recalculateSeasonalPriceBySchemeId(id: any) {
  return axios.get("/system/price-scheme-config/recalculateSeasonalPriceBySchemeId/" + id);
}
