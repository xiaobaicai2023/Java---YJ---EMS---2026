import axios from "axios";
import qs from "qs";


/**
 * 设备分组对象
 */
export interface EnterpriseEntity {
  id: number;
  name:string,
  socialCreditCode:string,
  enterpriseLegalPerson:string,
  phoneNumber:string,
  establishDate:string,
  status:number,
  operations:string,
}

/**
 * 用户查询对象
 */
export interface EnterpriseVarParams extends Partial<EnterpriseEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface EnterpriseListPage {
  list: EnterpriseEntity[];
  total: number;
}

//获取用户列表
export function queryEnterprisevarList(params: EnterpriseVarParams): any {
  return axios.get<EnterpriseListPage>("/manage/system/enterprise", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
