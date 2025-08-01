import axios from "axios";
import qs from "qs";


/**
 * 设备分组对象
 */
export interface SiteEntity {
  key: number;
  groupName: string,
  isGroup:string,
  children:{
    key:number,
    enterpriseName:string,
    isDeviceGroup:string,
    children:{
      key:number,
      id:number,
      name:string,
      siteCode:string,
      siteType:string,
      contactPerson:string,
      contactNum:string,
      commissionDate:string,
      status:number,
      operations:string,}[]
  },
}

/**
 * 用户查询对象
 */
export interface SiteVarParams extends Partial<SiteEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface SiteListPage {
  list: SiteEntity[];
  total: number;
}

//获取用户列表
export function querySitevarList(params: SiteVarParams): any {
  return axios.get<SiteListPage>("/manage/system/site", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
