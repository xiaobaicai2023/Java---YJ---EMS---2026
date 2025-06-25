import axios from "axios";
import qs from "qs";


/**
 * 查询在线用户列表
 * @param params 查询参数
 * @returns 结果
 */
export function listOnline(params: any) {
  return axios.get("/system/online/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 强退用户
 * @param tokenId token
 * @returns 0失败 1成功
 */
export function forceLogout(tokenId: any) {
  return axios.delete('/system/online/' + tokenId);
}