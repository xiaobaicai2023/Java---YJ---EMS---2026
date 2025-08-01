import axios from "axios";
import qs from "qs";

/**
 * 获取申请列表
 * @param params 搜索参数
 * @returns
 */
export function listApply(params: any) {
  return axios.get("/system/apply/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

// 查询申请详细
export function getApply(id: any) {
  return axios.get('/system/apply/' + id);
}

/**
 * 新建申请
 * @param data
 * @returns
 */
export function addApply(data: any) {
  return axios.post("/system/apply", data);
}

/**
 * 编辑申请
 * @param data 申请数据
 * @returns
 */
export function updateApply(data: any) {
  return axios.put("/system/apply", data);
}

/**
 * 删除申请
 * @param id 申请ID
 * @returns
 */
export function delApply(id: any) {
  return axios.delete('/system/apply/' + id);
}


/**
 * 生成标识
 * @param data 申请数据
 * @returns
 */
export function addToken(params: any) {
  return axios.get("/system/public/genRandomKey?genType=3&len=16", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
/**
 * 生成账号
 * @param data 申请数据
 * @returns
 */
export function addAccount(params: any) {
  return axios.get("/system/public/genRandomKey?genType=3&len=12", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

/**
 * 生成密钥
 * @param data 申请数据
 * @returns
 */
export function addSecret(params: any) {
  return axios.get("/system/public/genRandomKey?genType=3&len=18", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
