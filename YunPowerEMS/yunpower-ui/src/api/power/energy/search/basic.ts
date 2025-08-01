import axios from "axios";
import qs from "qs";

/**
 * 基础报表对象
 */
export interface SearchBasicEntity {
  device: string,
  parameter: string,
  status:number,
  min: number,
  max: number,
  average: number,
  sum: number
}

/**
 * 用户查询对象
 */
export interface SearchBasicParams extends Partial<SearchBasicEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface SearchBasicListPage {
  list: SearchBasicEntity[];
  total: number;
}

//获取用户列表
export function querySearchBasicvarList(params: SearchBasicParams): any {
  return axios.get<SearchBasicListPage>("/power/energy/search/basic", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

