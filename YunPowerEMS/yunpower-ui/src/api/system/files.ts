import axios from "axios";
import qs from "qs";

/**
 * 查询上传文件管理列表
 * @param params 查询参数
 * @returns 结果
 */
export function listFiles(params: any) {
  return axios.get("/system/files/list", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    }
  });
}

/**
 * 查询上传文件管理详细
 * @param id 上传文件管理ID
 * @returns 结果
 */
export function getFiles(id: any) {
  return axios.get("/system/files/" + id);
}

/**
 * 新增上传文件管理
 * @param data 上传文件管理对象
 * @returns 0失败 1成功
 */
export function addFiles(data: any) {
  return axios.post("/system/files", data);
}

/**
 * 修改上传文件管理
 * @param data 上传文件管理对象
 * @returns 0失败 1成功
 */
export function updateFiles(data: any) {
  return axios.put("/system/files", data);
}

/**
 * 删除上传文件管理
 * @param id 上传文件管理ID
 * @returns 0失败 1成功
 */
export function delFiles(id: any) {
  return axios.delete("/system/files/" + id);
}
