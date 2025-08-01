import axios from "axios";
import qs from "qs";

/**
 * 设备分组对象
 */
export interface DeviceGroupEntity {
  //编号
  id: number;
  //父编号
  parentId:number;
  //分类名称
  categoryName: string;
  //逻辑代码
  logicCode: string;
  //状态
  status: string;
  //备注
  remark:string;
}

/**
 * 用户查询对象
 */
export interface DeviceGroupParams extends Partial<DeviceGroupEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface DeviceGroupListPage {
  list: DeviceGroupEntity[];
  total: number;
}

//获取用户列表
export function queryDeviceGroupList(params: DeviceGroupParams): any {
  return axios.get<DeviceGroupListPage>("/manage/device/group/", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
