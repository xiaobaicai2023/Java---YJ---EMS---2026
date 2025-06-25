import axios from "axios";
import qs from "qs";

export  interface VariableConfigEntity {
  chartType?: String
  children: VariableConfigEntity[]
  dataType?: String
  isChain?: Number
  isStack?: Number
  storageType?: Number
  unit?: String
  varMapSn?: String
  varName?: String
  varSn?: String
  xAxis?: Number,
  key?:String,
  parentId:String
}

/**
 * 根据【分组ID】获取页面配置详细信息
 * @param groupId 分组ID
 * @returns 结果
 */
export function getPageConfigByGroupId(groupId: any) {
  return axios.get(`/system/page-config/config/${groupId}`);
}

/**
 * 新增页面配置
 * @param data 配置对象
 * @returns 0失败 1成功
 */
export function addPageConfig(data: any) {
  return axios.post("/system/page-config", data);
}

/**
 * 修改页面配置
 * @param data 配置对象
 * @returns 0失败 1成功
 */
export function updatePageConfig(data: any) {
  return axios.put("/system/page-config", data);
}
