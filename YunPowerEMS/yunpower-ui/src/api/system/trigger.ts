import axios from "axios";
import qs from "qs";

/**
 * 查询报警管理列表
 * @param params 查询参数
 * @returns 结果
 */
export function listTrigger(params: any) {
  return axios.get("/system/trigger/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}


/**
 * 查询报警管理列表-部分也
 * @param params 查询参数
 * @returns 结果
 */
export function listTriggerAll(params: any) {
  return axios.get("/system/trigger/listAll", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询报警管理详细
 * @param id 报警管理ID
 * @returns 结果
 */
export function getTrigger(id: any) {
  return axios.get("/system/trigger/" + id);
}

/**
 * 修改报警管理
 * @param data 报警管理对象
 * @returns 0失败 1成功
 */
export function updateTrigger(data: any) {
  return axios.put("/system/trigger", data);
}

/**
 * 报警事件确认
 * @param data 
 * @returns 0失败 1成功
 */
export function confirmTrigger(data: any) {
  return axios.put("/system/trigger/confirmTrigger", data);
}

/**
 * 删除报警管理
 * @param id 报警管理ID
 * @returns 0失败 1成功
 */
export function delTrigger(id: any) {
  return axios.delete("/system/trigger/" + id);
}




/**
 * 根据【设备名称、报警级别】表格统计报警数据
 * @param params 查询参数
 * @returns 结果
 */
export function getStatisticlist(params: any) {
  return axios.get("/system/trigger/getStatisticlist", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 报警级别统计图
 * @param params 查询参数
 * @returns 结果
 */
export function getLevelStaticList(params: any) {
  return axios.get("/system/trigger/getLevelStaticList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 报警类型统计图
 * @param params 查询参数
 * @returns 结果
 */
export function getCategoryStaticList(params: any) {
  return axios.get("/system/trigger/getCategoryStaticList", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}