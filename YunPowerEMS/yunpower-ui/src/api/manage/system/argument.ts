import axios from "axios";
import qs from "qs";


/**
 * 设备分组对象
 */
export interface ArgumentEntity {
  id: number;
  name:string,
  valueType:string,
  value:string,
  argumentExplain:string,
  status:number,
  operations:string,
}

/**
 * 用户查询对象
 */
export interface ArgumentVarParams extends Partial<ArgumentEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface ArgumentListPage {
  list: ArgumentEntity[];
  total: number;
}

//获取用户列表
export function queryArgumentvarList(params: ArgumentVarParams): any {
  return axios.get<ArgumentListPage>("/manage/system/argument", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
