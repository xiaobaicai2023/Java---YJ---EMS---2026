import axios from "axios";
import qs from "qs";

/**
 * 设备分组对象
 */
export interface DeviceVarEntity {
  id: number;
  status:number,
  name:string,
  variableCoding:string,
  variableAttribute:string,
  monitoringEquipmentus:string,
  owningChannel:string,
  dataSource:string,
  dataAddress:string,
  dataType:string,
  coefficient:string,
  storageCycle:string,
  operations:string,
}

/**
 * 用户查询对象
 */
export interface DeviceVarParams extends Partial<DeviceVarEntity> {
  current: number;
  pageSize: number;
}
/**
 * 用户分页对象
 */
export interface DeviceGroupListPage {
  list: DeviceVarEntity[];
  total: number;
}

//获取用户列表
export function queryDeviceGroupvarList(params: DeviceVarParams): any {
  return axios.get<DeviceGroupListPage>("/power/energy/device/var", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}

export function queryDeviceGroupvarList2(params: DeviceVarParams): any {
  return axios.get<DeviceGroupListPage>("/power/energy/device/var2", {
    params,
    paramsSerializer: (obj) => {
      return qs.stringify(obj);
    },
  });
}
