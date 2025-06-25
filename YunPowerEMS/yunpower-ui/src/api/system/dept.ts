import axios from "axios";

/**
 * 根据部门编号获取详细信息
 * @param id 部门ID
 * @returns 结果
 */
export function getDept(id: any) {
  return axios.get("/system/dept/" + id);
}