import axios from "axios";
import qs from "qs";

/**
 * 维保记录
 */
export interface MaintenanceEntity {
  //维保名称
  maintenanceName: string;
  //站点
  site: Number;
  //设备
  device: string;
  //维保人
  maintenancePerson: string;
  //维保时间
  maintenanceTime: string;
  //维保内容
  maintenanceContent: string;
  //图片描述
  image: string;
}

/**
 * 用户查询工单
 */
export interface MaintenanceQueryParams extends Partial<MaintenanceEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页工单
 */
export interface MaintenanceListPage {
  list: MaintenanceEntity[];
  total: number;
}

//获取用户列表
export function queryMaintenanceList(params: MaintenanceQueryParams): any {
  return axios.get<MaintenanceListPage>("/manage/maintenance/", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
