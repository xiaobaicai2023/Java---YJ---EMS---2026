import axios from "axios";
import qs from "qs";

/**
 * 查询维保记录列表
 * @param params 查询参数
 * @returns 结果
 */
export function listMaintenance(params: any) {
    return axios.get("/system/maintenance/list", {
        params,
        paramsSerializer: (obj) => {
            return qs.stringify(obj);
        }
    });
}

/**
 * 查询维保记录详细
 * @param id 维保记录ID
 * @returns 结果
 */
export function getMaintenance(id: any) {
    return axios.get("/system/maintenance/" + id);
}

/**
 * 新增维保记录
 * @param data 维保记录对象
 * @returns 0失败 1成功
 */
export function addMaintenance(data: any) {
    return axios.post("/system/maintenance", data);
}

/**
 * 修改维保记录
 * @param data 维保记录对象
 * @returns 0失败 1成功
 */
export function updateMaintenance(data: any) {
    return axios.put("/system/maintenance", data);
}

/**
 * 删除维保记录
 * @param id 维保记录ID
 * @returns 0失败 1成功
 */
export function delMaintenance(id: any) {
    return axios.delete("/system/maintenance/" + id);
}
